package com.coretex.orm.core.activeorm.query.select.optimizer;

import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.orm.core.activeorm.query.select.CoretexItemTable;
import com.coretex.orm.core.activeorm.query.select.CoretexLocalizedItemTable;
import com.coretex.orm.core.activeorm.query.select.CoretexStrictItemTable;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttleImpl;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class SelectQueryOptimizer extends AbstrctQueryOptimizer {

	private Logger LOG = LoggerFactory.getLogger(SelectQueryOptimizer.class);

	public SelectQueryOptimizer(SqlValidator validator, Supplier<RelOptPlanner> planner, SqlToRelConverter converter, CoretexContext coretexContext) {
		super(validator, planner, converter, coretexContext);
	}

	@Override
	public void process(QueryInfoHolder queryInfoHolder) {
		OptimizingSteps steps = DEFAULT.withConvertingStep(
				new OptimizingStep<>(
						Function.identity(),
						relNode -> {
							LOG.debug("Perform :: after convert [param :: " + relNode + "]");
							relNode.accept(
									new RelShuttleImpl() {
										@Override
										public RelNode visit(TableScan scan) {
											RelOptTable table = scan.getTable();
											Table itemTable = table.unwrap(Table.class);
											if(itemTable instanceof CoretexItemTable){
												queryInfoHolder.addItemUsed(((CoretexItemTable) itemTable).getMetaType());
												((CoretexItemTable) itemTable).getAllSubtypes().forEach(queryInfoHolder::addItemUsed);
											}
											if(itemTable instanceof CoretexStrictItemTable){
												queryInfoHolder.addItemUsed(((CoretexStrictItemTable) itemTable).getMetaType());
											}
											if(itemTable instanceof CoretexLocalizedItemTable){
												queryInfoHolder.addItemUsed(((CoretexLocalizedItemTable) itemTable).getMetaType());
												queryInfoHolder.setLocalizedTable(true);
											}

											return super.visit(scan);
										}
									}
							);
							return relNode;
						}
				) {
					@Override
					public RelNode perform(SqlNode sqlNode) {
						LOG.debug("Perform :: convert [param :: " + sqlNode + "]");
						return convert(sqlNode);
					}
				}
		);
		super.process(queryInfoHolder, steps);
	}
}
