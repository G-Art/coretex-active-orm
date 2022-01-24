package com.coretex.orm.core.activeorm.query.select.optimizer;

import com.coretex.orm.core.query.parser.CoretexSqlParserImpl;
import com.coretex.orm.core.activeorm.query.SqlDynamicNamedParam;
import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.orm.core.activeorm.query.rex.RexDynamicNamedParam;
import com.coretex.orm.core.activeorm.query.select.CoretexAbstractItemTable;
import com.coretex.orm.core.activeorm.query.select.CoretexAbstractRelDataTypeFieldImpl;
import com.coretex.orm.core.activeorm.query.select.CoretexRelDataTypeFieldImpl;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.core.*;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexProgram;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.dialect.PostgresqlSqlDialect;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.Program;
import org.apache.calcite.tools.Programs;
import org.apache.calcite.tools.RuleSets;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.apache.calcite.sql.SqlKind.DYNAMIC_PARAM;

public abstract class AbstrctQueryOptimizer extends RelToSqlConverter {


	private Logger LOG = LoggerFactory.getLogger(AbstrctQueryOptimizer.class);

	protected OptimizingSteps DEFAULT = new OptimizingSteps();

	private SqlValidator validator;
	private SqlToRelConverter converter;
	private Supplier<RelOptPlanner> planner;

	public AbstrctQueryOptimizer(SqlValidator validator,
	                             Supplier<RelOptPlanner> planner,
	                             SqlToRelConverter converter,
	                             CoretexContext coretexContext) {
		super(dialectConfig(coretexContext.getDbDialectService()));
		this.validator = validator;
		this.planner = planner;
		this.converter = converter;
	}

	private static SqlDialect dialectConfig(DbDialectService dbDialectService) {
		switch (dbDialectService.getDatabaseProduct()) {
			case MYSQL -> {
				return new MysqlSqlDialect(dialectContextConfig(MysqlSqlDialect.DEFAULT_CONTEXT));
			}
			case POSTGRESQL -> {
				return new PostgresqlSqlDialect(dialectContextConfig(PostgresqlSqlDialect.DEFAULT_CONTEXT));
			}
			default -> throw new IllegalStateException("Db type [" + dbDialectService.getDatabaseProduct().name() + "] is not supported");
		}
	}

	private static SqlDialect.Context dialectContextConfig(SqlDialect.Context dialectContext) {
		assert dialectContext != null : "Dialect context required";
		return dialectContext
				.withUnquotedCasing(Casing.UNCHANGED)
				.withQuotedCasing(Casing.UNCHANGED)
				.withCaseSensitive(true);
	}

	protected SqlNode parse(String sql) {

		final SqlParser.Config config =
				SqlParser.config()
						.withCaseSensitive(false)
						.withQuotedCasing(Casing.UNCHANGED)
						.withUnquotedCasing(Casing.UNCHANGED)
						.withParserFactory(CoretexSqlParserImpl.FACTORY)
						.withConformance(SqlConformanceEnum.DEFAULT);

		SqlParser parser = SqlParser.create(sql, config);

		try {
			return parser.parseStmt();
		} catch (SqlParseException e) {
			throw new RuntimeException(e);
		}
	}

	protected SqlNode validate(SqlNode sqlNode) {
		return validator.validate(sqlNode);
	}

	protected RelNode convert(SqlNode validatedSqlNode) {
		RelRoot root = converter.convertQuery(validatedSqlNode, false, true);
		return root.rel;
	}

	protected RelNode optimize(RelNode node) {
		Program program = Programs.of(RuleSets.ofList());
		var p = planner.get();
		p.setRoot(node);
		return program.run(
				p,
				node,
				node.getTraitSet().plus(Convention.NONE),
				Collections.emptyList(),
				Collections.emptyList()
		);
	}

	protected String toSql(RelNode node) {
		Result result = visitRoot(node);
		final SqlNode sqlNode = result.asStatement();
		return sqlNode.toSqlString(dialect).getSql();
	}

	public Result visit(TableScan e) {
		final SqlIdentifier identifier = getSqlTargetTable(e);
		return result(identifier, ImmutableList.of(Clause.FROM), e, Map.of(identifier.toString(), e.getRowType()));
	}

	public Result visit(Filter e) {
		final RelNode input = e.getInput();
		if (input instanceof Aggregate) {
			final Aggregate aggregate = (Aggregate) input;
			final boolean ignoreClauses = aggregate.getInput() instanceof Project;
			final Result x = visitInput(e, 0, isAnon(), ignoreClauses,
					ImmutableSet.of(Clause.HAVING));
			parseCorrelTable(e, x);
			final Builder builder = x.builder(e);
			x.asSelect().setHaving(
					SqlUtil.andExpressions(x.asSelect().getHaving(),
							builder.context.toSql(null, e.getCondition())));
			return builder.result();
		} else {
			Result x;
			Builder builder;
			if (input instanceof TableScan) {
				x = visitInput(e, 0, Clause.WHERE, Clause.SELECT);
				parseCorrelTable(e, x);
				builder = x.builder(e);
				final List<SqlNode> selectList = new ArrayList<>();
				for (RelDataTypeField field : input.getRowType().getFieldList()) {
					SqlNode sqlExpr = new SqlIdentifier(ImmutableList.of(((CoretexAbstractRelDataTypeFieldImpl) field).getColumnName()),
							POS);
					addSelect(selectList, sqlExpr, input.getRowType());
				}
				builder.setSelect(new SqlNodeList(selectList, POS));
			} else {
				x = visitInput(e, 0, Clause.WHERE);
				parseCorrelTable(e, x);
				builder = x.builder(e);
			}
			builder.setWhere(builder.context.toSql(null, e.getCondition()));
			return builder.result();
		}
	}

	private static SqlIdentifier getSqlTargetTable(RelNode e) {
		final RelOptTable table = requireNonNull(e.getTable());
		return table.maybeUnwrap(CoretexAbstractItemTable.class)
				.map(coretexItemTable -> new SqlIdentifier(coretexItemTable.getTableName(), SqlParserPos.ZERO))
				.orElseGet(() ->
						new SqlIdentifier(table.getQualifiedName(), SqlParserPos.ZERO));
	}

	@Override
	public void addSelect(List<SqlNode> selectList, SqlNode node,
	                      RelDataType rowType) {
		String name;
		RelDataTypeField relDataTypeField = rowType.getFieldList().get(selectList.size());
		if (relDataTypeField instanceof CoretexRelDataTypeFieldImpl) {
			name = ((CoretexRelDataTypeFieldImpl) relDataTypeField).getColumnName();
		} else {
			name = rowType.getFieldNames().get(selectList.size());
		}
		String alias = SqlValidatorUtil.getAlias(node, -1);
		if (alias == null || !alias.equals(name)) {
			node = as(node, name);
		}
		selectList.add(node);
	}

	public Result visit(Project e) {
		final Result x = visitInput(e, 0, Clause.SELECT);
		parseCorrelTable(e, x);
		final Builder builder = x.builder(e);
		final List<SqlNode> selectList = new ArrayList<>();
		for (RexNode ref : e.getProjects()) {
			SqlNode sqlExpr = builder.context.toSql(null, ref);
			if (SqlUtil.isNullLiteral(sqlExpr, false)) {
				final RelDataTypeField field =
						e.getRowType().getFieldList().get(selectList.size());
				sqlExpr = castNullType(sqlExpr, field.getType());
			}
			addSelect(selectList, sqlExpr, e.getRowType());
		}
		builder.setSelect(new SqlNodeList(selectList, POS));
		return builder.result();
	}

	private SqlNode castNullType(SqlNode nullLiteral, RelDataType type) {
		final SqlNode typeNode = dialect.getCastSpec(type);
		if (typeNode == null) {
			return nullLiteral;
		}
		return SqlStdOperatorTable.CAST.createCall(POS, nullLiteral, typeNode);
	}

	private void parseCorrelTable(RelNode relNode, Result x) {
		for (CorrelationId id : relNode.getVariablesSet()) {
			correlTableMap.put(id, x.qualifiedContext());
		}
	}

	public Context aliasContext(Map<String, RelDataType> aliases,
	                            boolean qualified) {
		return new AliasContext(dialect, aliases, qualified) {
			@Override
			public SqlNode toSql(@Nullable RexProgram program, RexNode rex) {
				if (rex.getKind() == DYNAMIC_PARAM && rex instanceof RexDynamicNamedParam) {
					final RexDynamicNamedParam caseParam = (RexDynamicNamedParam) rex;
					return new SqlDynamicNamedParam(caseParam.getName(), caseParam.getIndex(), POS);
				}
				return super.toSql(program, rex);
			}

			@Override
			public SqlNode field(int ordinal) {
				for (Map.Entry<String, RelDataType> alias : aliases.entrySet()) {
					final List<RelDataTypeField> fields = alias.getValue().getFieldList();
					if (ordinal < fields.size()) {
						RelDataTypeField field = fields.get(ordinal);
						if (field instanceof CoretexRelDataTypeFieldImpl) {
							return new SqlIdentifier(!qualified
									? ImmutableList.of(((CoretexRelDataTypeFieldImpl) field).getColumnName())
									: ImmutableList.of(alias.getKey(), ((CoretexRelDataTypeFieldImpl) field).getColumnName()),
									POS);
						} else {
							return new SqlIdentifier(!qualified
									? ImmutableList.of(field.getName())
									: ImmutableList.of(alias.getKey(), field.getName()),
									POS);
						}

					}
					ordinal -= fields.size();
				}
				throw new AssertionError(
						"field ordinal " + ordinal + " out of range " + aliases);
			}
		};
	}

	public String process(String query) {
		return process(query, DEFAULT);
	}

	public void process(QueryInfoHolder queryInfoHolder) {
		process(queryInfoHolder, DEFAULT);
	}

	public String process(String query, OptimizingSteps optimizingSteps) {
		return optimizingSteps.apply(query);
	}

	public void process(QueryInfoHolder queryInfoHolder, OptimizingSteps optimizingSteps) {
		try {
			queryInfoHolder.setResultQuery(process(queryInfoHolder.getQuery(), optimizingSteps));
			queryInfoHolder.setValid(true);
		} catch (Throwable e) {
			queryInfoHolder.setValid(false);
			queryInfoHolder.setValidationThrowable(e);
		}
	}

	public class OptimizingSteps {
		private final OptimizingStep<String, SqlNode> parsingStep;
		private final OptimizingStep<SqlNode, SqlNode> validationStep;
		private final OptimizingStep<SqlNode, RelNode> convertingStep;
		private final OptimizingStep<RelNode, RelNode> optimizationStep;
		private final OptimizingStep<RelNode, String> stringifyStep;


		private OptimizingSteps(OptimizingStep<String, SqlNode> parsingStep,
		                        OptimizingStep<SqlNode, SqlNode> validationStep,
		                        OptimizingStep<SqlNode, RelNode> convertingStep,
		                        OptimizingStep<RelNode, RelNode> optimizationStep,
		                        OptimizingStep<RelNode, String> stringifyStep) {
			this.parsingStep = parsingStep;
			this.validationStep = validationStep;
			this.convertingStep = convertingStep;
			this.optimizationStep = optimizationStep;
			this.stringifyStep = stringifyStep;
		}

		private OptimizingSteps() {
			this(
					new OptimizingStep<>() {
						@Override
						public SqlNode perform(String s) {
							LOG.debug("Perform :: parse [param :: " + s + "]");
							return parse(s);
						}
					},
					new OptimizingStep<>() {
						@Override
						public SqlNode perform(SqlNode s) {
							LOG.debug("Perform :: validate [param :: " + s + "]");
							return validate(s);
						}
					},
					new OptimizingStep<>() {
						@Override
						public RelNode perform(SqlNode s) {
							LOG.debug("Perform :: convert [param :: " + s + "]");
							return convert(s);
						}
					},
					new OptimizingStep<>() {
						@Override
						public RelNode perform(RelNode s) {
							LOG.debug("Perform :: optimize [param :: " + s + "]");
							return optimize(s);
						}
					},
					new OptimizingStep<>() {
						@Override
						public String perform(RelNode s) {
							LOG.debug("Perform :: toSql [param :: " + s + "]");
							return toSql(s);
						}
					}
			);
		}

		public OptimizingSteps withParsingStep(OptimizingStep<String, SqlNode> parsingStep) {
			return new OptimizingSteps(
					parsingStep,
					this.validationStep,
					this.convertingStep,
					this.optimizationStep,
					this.stringifyStep
			);
		}

		public OptimizingSteps withValidationStep(OptimizingStep<SqlNode, SqlNode> validationStep) {
			return new OptimizingSteps(
					this.parsingStep,
					validationStep,
					this.convertingStep,
					this.optimizationStep,
					this.stringifyStep
			);
		}

		public OptimizingSteps withConvertingStep(OptimizingStep<SqlNode, RelNode> convertingStep) {
			return new OptimizingSteps(
					this.parsingStep,
					this.validationStep,
					convertingStep,
					this.optimizationStep,
					this.stringifyStep
			);
		}

		public OptimizingSteps withOptimizationStep(OptimizingStep<RelNode, RelNode> optimizationStep) {
			return new OptimizingSteps(
					this.parsingStep,
					this.validationStep,
					this.convertingStep,
					optimizationStep,
					this.stringifyStep
			);
		}

		public OptimizingSteps withStringifyStep(OptimizingStep<RelNode, String> stringifyStep) {
			return new OptimizingSteps(
					this.parsingStep,
					this.validationStep,
					this.convertingStep,
					this.optimizationStep,
					stringifyStep
			);
		}

		public String apply(String query) {
			return stringifyStep
					.apply(
							optimizationStep
									.apply(
											convertingStep
													.apply(
															validationStep
																	.apply(
																			parsingStep.apply(query)
																	)
													)
									)
					);
		}
	}


	protected abstract static class OptimizingStep<T, R> implements Function<T, R> {
		private Function<T, T> before = Function.identity();
		private Function<R, R> after = Function.identity();

		public abstract R perform(T t);

		public OptimizingStep() {
		}

		public OptimizingStep(Function<T, T> before, Function<R, R> after) {
			this.before = before;
			this.after = after;
		}

		@Override
		public R apply(T t) {
			return after.apply(perform(before.apply(t)));
		}
	}
}
