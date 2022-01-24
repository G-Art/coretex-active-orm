package com.coretex.orm.core.activeorm.query.select.rules;

import com.coretex.orm.core.activeorm.query.select.CoretexBasicSqlType;
import com.coretex.orm.core.activeorm.query.select.CoretexRelDataTypeFieldImpl;
import com.coretex.orm.core.activeorm.query.select.QueryOptimizerConfigFactory;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.orm.meta.AbstractGenericItem;
import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelRule;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.hint.Hintable;
import org.apache.calcite.rel.hint.RelHint;
import org.apache.calcite.rel.metadata.DelegatingMetadataRel;
import org.apache.calcite.rel.rules.TransformationRule;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.tools.RelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrap param or column to BIN_TO_UUID
 */
public class CoretexMysqlBinToUuidRule extends RelRule<CoretexMysqlBinToUuidRule.Config>
		implements TransformationRule {

	private final Logger LOG = LoggerFactory.getLogger(CoretexMysqlBinToUuidRule.class);

	public final static RelHint hint = RelHint.builder("BIN_TO_UUID_WRAPPER").build();

	protected CoretexMysqlBinToUuidRule(Config config) {
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

	@Override
	public boolean matches(RelOptRuleCall call) {
		RelNode root = call.getPlanner().getRoot();
		if (root instanceof DelegatingMetadataRel) {
			root = ((DelegatingMetadataRel) root).getMetadataDelegateRel();
		}
		RelNode rel = call.rel(0);

		boolean equals = rel.equals(root);

		if(rel instanceof Hintable){
			return equals && !((Hintable) rel).getHints().contains(hint);
		}
		return equals;
	}

	@Override
	public void onMatch(RelOptRuleCall call) {
		RelNode rel = call.rel(0);
		LOG.debug("Perform [CoretexMysqlBinToUuidRule] rule (mysql only) for ["+rel+"]");
		RelNode newRel = process(rel, call.builder());
		if (newRel != rel) {
			call.transformTo(newRel);
			call.getPlanner().prune(rel);
		}
	}

	private RelNode process(RelNode rel, RelBuilder builder) {

		ImmutableList<RexNode> fields = builder.push(rel)
				.fields();
		RexBuilder rexBuilder = builder.getRexBuilder();
		final boolean[] adjusted = new boolean[]{false};
		List<RexNode> rexNodes = fields.stream()
				.map(rexNode -> {
					switch (rexNode.getKind()) {
						case INPUT_REF: {
							RelDataType type = rexNode.getType();
							if (type instanceof CoretexBasicSqlType) {
								RelDataTypeField fieldFor = ((CoretexBasicSqlType) type).getFieldFor();
								if (fieldFor instanceof CoretexRelDataTypeFieldImpl
										&& isUuidPersistenceType(((CoretexRelDataTypeFieldImpl) fieldFor)
										.getMetaAttributeTypeItem())) {
									String name = rel.getRowType().getFieldNames().get(((RexInputRef) rexNode).getIndex());
									RexNode node = rexBuilder.makeCall(type,
											QueryOptimizerConfigFactory.MYSQL_BIN_TO_UUID_FUNCTION,
											List.of(rexNode)
									);
									adjusted[0] = true;
									return builder.alias(node, name);
								}
							}
						}
						default:
							return rexNode;
					}
				})
				.collect(Collectors.toList());

		if(adjusted[0]){
			builder.project(rexNodes).hints(hint);
			return builder.build();
		}
		return rel;
	}


	/**
	 * Rule configuration.
	 */
	public interface Config extends RelRule.Config {

		Config DEFAULT = EMPTY
				.withOperandSupplier(op -> op.operand(RelNode.class)
						.anyInputs())
				.as(Config.class);

		@Override
		default CoretexMysqlBinToUuidRule toRule() {
			return new CoretexMysqlBinToUuidRule(this);
		}
	}
}
