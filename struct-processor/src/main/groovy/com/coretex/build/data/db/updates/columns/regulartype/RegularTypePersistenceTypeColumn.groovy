package com.coretex.build.data.db.updates.columns.regulartype

import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.db.diff.dataholders.RegularClassItemDiffDataHolder
import com.coretex.build.data.db.updates.RegularClassTypeUpdate
import com.coretex.build.data.db.updates.columns.Column
import com.coretex.common.annotation.EssentialDataItemColumn

import static com.coretex.common.annotation.EssentialDataItem.EssentialItem.REGULAR_TYPE
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
@EssentialDataItemColumn(itemCode = REGULAR_TYPE, columnName = 'persistenceType')
class RegularTypePersistenceTypeColumn extends Column<RegularClassItemDiffDataHolder, RegularClassTypeUpdate> {

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    @Override
    Object getValue() {
        return "'${retrieveDbType()}'"
    }

    private String retrieveDbType() {
        return buildContext.dbDialect.typeAppropriateService.getBasicMappingType(data.item)
    }
}
