package com.coretex.build.data.db.updates

import com.coretex.build.data.db.DbCommands
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder

class AttributeOwnerRelationUpdate extends Update<AttributeItemDiffDataHolder> {

    List<DbCommands> delegates = []

    @Override
    Collection<String> commands() {
        return delegates.collect({it.commands()}).flatten() as Collection<String>
    }
}
