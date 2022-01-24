package com.coretex.build.data.db

class Index {
	Set<TableField> tableFields = [] as Set

	Set<TableField> getTableFields() {
		return tableFields
	}

	String getFieldNames(){
		return tableFields.stream().map({it.name}).toArray().join(",")
	}

	void addTableField(TableField tableField) {
		this.tableFields.add(tableField)
	}
}
