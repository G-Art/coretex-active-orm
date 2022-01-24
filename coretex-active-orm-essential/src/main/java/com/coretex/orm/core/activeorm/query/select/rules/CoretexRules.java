package com.coretex.orm.core.activeorm.query.select.rules;

import org.apache.calcite.plan.RelOptRule;

public class CoretexRules {

	public static final RelOptRule CORETEX_ITEM_TO_TABLE_RULE =
			CoretexExtendItemTableRule.Config.DEFAULT.toRule();

	public static final RelOptRule CORETEX_MYSQL_UUID_TO_BIN_RULE =
			CoretexMysqlUuidToBinRule.Config.DEFAULT.toRule();

	public static final RelOptRule CORETEX_MYSQL_BIN_TO_UUID_RULE =
			CoretexMysqlBinToUuidRule.Config.DEFAULT.toRule();
}
