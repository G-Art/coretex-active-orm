package com.coretex.orm.core.activeorm.query.select.rules;

import com.coretex.orm.core.activeorm.query.select.CoretexAbstractItemTable;
import com.coretex.orm.core.activeorm.query.select.CoretexLocalizedItemTable;
import com.coretex.orm.core.activeorm.query.select.CoretexStrictItemTable;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.hint.RelHint;
import org.apache.calcite.rel.rules.TransformationRule;
import org.apache.calcite.tools.RelBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class CoretexExtendItemTableRule extends RelRule<CoretexExtendItemTableRule.Config>
		implements TransformationRule {

	private final Logger LOG = LoggerFactory.getLogger(CoretexExtendItemTableRule.class);
	public final static RelHint hint = RelHint.builder("EXTENDED_ITEM").build();

	/**
	 * Creates a CoretexExtendItemTableRule.
	 */
	protected CoretexExtendItemTableRule(Config config) {
		super(config);
	}

	//~ Methods ----------------------------------------------------------------

	@Override
	public void onMatch(RelOptRuleCall call) {
		RelBuilder builder = call.builder();
		TableScan scan = call.rel(0);
		LOG.debug("Perform [CoretexExtendItemTableRule] rule for ["+scan+"]");
		RelOptTable table = scan.getTable();
		CoretexAbstractItemTable itemTable = table.unwrap(CoretexAbstractItemTable.class);

		RelBuilder projectBuilder = RelFactories.LOGICAL_BUILDER.create(builder.getCluster(), table.getRelOptSchema());

		List<RelNode> collect;
		if (itemTable instanceof CoretexStrictItemTable) {
			collect = buildStrictRelNodes(projectBuilder, scan, itemTable.getMetaType());
		} else {
			collect = buildRelNodes(projectBuilder, scan, itemTable.getMetaType());
		}


		builder.pushAll(collect);
		if (collect.size() > 1) {
			builder.union(true, collect.size());
		}

		call.transformTo(builder.build());
	}

	private List<RelNode> buildStrictRelNodes(RelBuilder projectBuilder, TableScan scan, MetaTypeItem itable) {
		projectBuilder.scan(itable.getTypeCode())
				.hints(hint)
				.project(scan.getRowType()
								.getFieldNames()
								.stream()
								.map(projectBuilder::field)
								.collect(Collectors.toList()),
						scan.getRowType()
								.getFieldNames(),
						false
				);
		projectBuilder.filter(projectBuilder.equals(
				projectBuilder.field(GenericItem.META_TYPE),
				projectBuilder.literal(itable.getUuid().toString()))
		);
		return Collections.singletonList(projectBuilder.build());
	}

	private List<RelNode> buildRelNodes(RelBuilder projectBuilder, TableScan scan, MetaTypeItem itable) {
		List<RelNode> relNodes = Lists.newArrayList();

		Set<MetaTypeItem> types = Sets.newHashSet(itable);
		if (CollectionUtils.isNotEmpty(itable.getSubtypes())) {
			relNodes.addAll(itable.getSubtypes()
					.stream()
					.filter(MetaTypeItem::getTableOwner)
					.flatMap(itableO -> buildRelNodes(projectBuilder, scan, itableO).stream())
					.collect(Collectors.toList()));

			types.addAll(itable.getSubtypes()
					.stream()
					.filter(Predicate.not(MetaTypeItem::getTableOwner))
					.collect(Collectors.toSet()));
		}
		projectBuilder.scan(itable.getTypeCode())
				.hints(hint)
				.project(scan.getRowType()
								.getFieldNames()
								.stream()
								.map(projectBuilder::field)
								.collect(Collectors.toList()),
						scan.getRowType()
								.getFieldNames(),
						false
				);

		if (types.size() > 1) {
			final RelBuilder finalProjectBuilder = projectBuilder;
			projectBuilder.filter(projectBuilder.in(
					projectBuilder.field(GenericItem.META_TYPE),
					types.stream()
							.map(i -> finalProjectBuilder.literal(i.getUuid().toString()))
							.collect(Collectors.toList()))
			);
		}

		if (types.size() == 1) {
			projectBuilder.filter(projectBuilder.equals(
					projectBuilder.field(GenericItem.META_TYPE),
					projectBuilder.literal(itable.getUuid().toString()))
			);
		}
		relNodes.add(projectBuilder.build());

		return relNodes;
	}

	/**
	 * Rule configuration.
	 */
	public interface Config extends RelRule.Config {

		Config DEFAULT = EMPTY
				.withOperandSupplier(
						b1 -> b1.operand(TableScan.class)
								.predicate(tableScan -> {
									RelOptTable table = tableScan.getTable();
									CoretexAbstractItemTable itemTable = table.unwrap(CoretexAbstractItemTable.class);
									return !tableScan.getHints().contains(hint) && !(itemTable instanceof CoretexLocalizedItemTable);
								})
								.anyInputs()
				)
				.as(Config.class);

		@Override
		default CoretexExtendItemTableRule toRule() {
			return new CoretexExtendItemTableRule(this);
		}
	}
}
