package com.coretex.build.data.db.inserts.columns.regulartype

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.inserts.RegularTypeInsert
import com.coretex.build.data.db.inserts.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = REGULAR_TYPE, columnName = 'persistenceType')
class RegularTypePersistenceTypeColumn extends Column<RegularTypeInsert.RegularTypeWrapper> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    @Override
    Object getRawValue() {
        return "'${retrieveDbType()}'"
    }

    private String retrieveDbType() {
        return buildContext.dbDialect.typeAppropriateService.getBasicMappingType(data.item)
    }
}
