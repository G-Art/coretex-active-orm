package com.coretex.orm.core.activeorm.query.select.rules;

import com.coretex.orm.core.activeorm.query.select.CoretexBasicSqlType;
import com.coretex.orm.core.activeorm.query.select.CoretexRelDataTypeFieldImpl;
import com.coretex.orm.core.activeorm.query.select.QueryOptimizerConfigFactory;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import com.google.common.collect.Lists;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.rules.TransformationRule;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.*;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.util.NlsString;
import org.apache.calcite.util.Sarg;
import org.apache.calcite.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrap param or column to UUID_TO_BIN
 */
public class CoretexMysqlUuidToBinRule extends RelRule<CoretexMysqlUuidToBinRule.Config>
		implements TransformationRule {

	private final Logger LOG = LoggerFactory.getLogger(CoretexMysqlUuidToBinRule.class);

	/**
	 * Creates a CoretexExtendItemTableRule.
	 */
	protected CoretexMysqlUuidToBinRule(Config config) {
		super(config);
	}

	//~ Methods ----------------------------------------------------------------

	private boolean isUuidPersistenceType(MetaAttributeTypeItem attribute) {
		return AttributeTypeUtils.isEnumTypeAttribute(attribute) ||
				AttributeTypeUtils.isItemAttribute(attribute) ||
				(AttributeTypeUtils.isRegularTypeAttribute(attribute) &&
						((RegularTypeItem) attribute.getAttributeType()).getPersistenceType()
								.equalsIgnoreCase(AbstractGenericItem.UUID));
	}

	private boolean needWrap(RexNode n1, RexNode n2) {
		switch (n1.getKind()) {
			case INPUT_REF: {
				RelDataType type = n1.getType();
				if (type instanceof CoretexBasicSqlType) {
					RelDataTypeField fieldFor = ((CoretexBasicSqlType) type).getFieldFor();
					if (fieldFor instanceof CoretexRelDataTypeFieldImpl && isUuidPersistenceType(((CoretexRelDataTypeFieldImpl) fieldFor).getMetaAttributeTypeItem())) {
						return n2.getKind() == SqlKind.LITERAL || n2.getKind() == SqlKind.DYNAMIC_PARAM;
					}
				}
				return false;

			}
			case LITERAL:
			case DYNAMIC_PARAM: {
				if (n2.getKind() == SqlKind.INPUT_REF) {
					RelDataType type = n2.getType();
					if (type instanceof CoretexBasicSqlType) {
						RelDataTypeField fieldFor = ((CoretexBasicSqlType) type).getFieldFor();
						return fieldFor instanceof CoretexRelDataTypeFieldImpl
								&& isUuidPersistenceType(((CoretexRelDataTypeFieldImpl) fieldFor).getMetaAttributeTypeItem());
					}
				}
				return false;
			}
			default:
				return false;
		}
	}

	@Override
	public boolean matches(RelOptRuleCall call) {
		Filter filter = call.rel(0);
		try {
			filter.getCondition().accept(new RexVisitorImpl<Void>(true) {

				@Override
				public Void visitCall(RexCall call) {
					switch (call.getKind()) {
						case EQUALS:
						case NOT_EQUALS:
						case GREATER_THAN:
						case GREATER_THAN_OR_EQUAL:
						case LESS_THAN:
						case LESS_THAN_OR_EQUAL:
						case SEARCH:
							RexNode op0 = call.operands.get(0);
							RexNode op1 = call.operands.get(1);
							if (needWrap(op0, op1)) {
								throw Util.FoundOne.NULL;
							}
						default:
							return super.visitCall(call);
					}

				}


			});
			return false;
		} catch (Util.FoundOne e) {
			return true;
		}
	}

	@Override
	public void onMatch(RelOptRuleCall call) {
		Filter filter = call.rel(0);
		LOG.debug("Perform [CoretexMysqlUuidToBinRule] rule (mysql only) for ["+filter+"]");
		RelBuilder builder = call.builder();
		builder.push(filter.getInput());
		RexNode newCondition = filter.getCondition().accept(new RexShuttle() {
			public RexNode visitCall(RexCall call) {
				boolean[] update = new boolean[]{false};

				switch (call.getKind()) {
					case EQUALS:
					case NOT_EQUALS:
					case GREATER_THAN:
					case GREATER_THAN_OR_EQUAL:
					case LESS_THAN:
					case LESS_THAN_OR_EQUAL:
						RexNode op0 = call.operands.get(0);
						RexNode op1 = call.operands.get(1);
						if (needWrap(op0, op1)) {
							RexNode wrap0 = wrap(op0, builder.getRexBuilder());
							RexNode wrap1 = wrap(op1, builder.getRexBuilder());
							if (wrap0 != op0 || wrap1 != op1) {
								return call.clone(call.getType(), List.of(wrap0, wrap1));
							}
						}
					case SEARCH:
						return processSearch(call, builder.getRexBuilder());
					case IS_DISTINCT_FROM:
					case IS_NOT_DISTINCT_FROM:
					case AND:
					case OR:
					default:
						List<RexNode> clonedOperands = this.visitList(call.operands, update);
						return update[0] ? call.clone(call.getType(), clonedOperands) : call;
				}
			}

		});
		builder.filter(newCondition);
		call.transformTo(builder.build());
	}

	private RexNode processSearch(RexCall call, RexBuilder rexBuilder) {
		RexNode op0 = call.operands.get(0);
		RexNode op1 = call.operands.get(1);
		if (needWrap(op0, op1)) {
			RexLiteral rexLiteral = (RexLiteral) op1;
			Sarg<?> value = (Sarg<?>) rexLiteral.getValue();
			List<String> literals = Lists.newArrayList();
			value.rangeSet.asRanges().forEach(range -> {
				Comparable<?> o = range.lowerEndpoint();
				if (o instanceof NlsString) {
					literals.add(((NlsString) o).getValue());
				} else {
					literals.add(o.toString());
				}
			});
			return rexBuilder.makeIn(op0,
					literals.stream()
							.map(l -> rexBuilder.makeCast(
											call.getType(),
											rexBuilder.makeCall(
													QueryOptimizerConfigFactory.MYSQL_UUID_TO_BIN_FUNCTION,
													rexBuilder.makeLiteral(l)
											)
									)
							)
							.collect(Collectors.toList())
			);

		}
		return call;
	}

	private RexNode wrap(RexNode call, RexBuilder rexBuilder) {
		switch (call.getKind()) {
			case LITERAL:
			case DYNAMIC_PARAM:
				return rexBuilder.makeCast(
						call.getType(),
						rexBuilder.makeCall(QueryOptimizerConfigFactory.MYSQL_UUID_TO_BIN_FUNCTION, call)
				);

			default:
				return call;
		}
	}

	/**
	 * Rule configuration.
	 */
	public interface Config extends RelRule.Config {

		Config DEFAULT = EMPTY
				.withOperandSupplier(op -> op.operand(Filter.class)
						.predicate(filter -> filter.getCondition() != null)
						.anyInputs())
				.as(Config.class);

		@Override
		default CoretexMysqlUuidToBinRule toRule() {
			return new CoretexMysqlUuidToBinRule(this);
		}
	}
}
