package com.coretex.build.data.db.updates

import com.coretex.build.data.db.DbCommands
import com.coretex.build.data.db.diff.dataholders.AbstractClassItemDiffDataHolder
import com.coretex.build.data.db.diff.dataholders.AttributeItemDiffDataHolder
import com.coretex.build.data.items.AbstractItem

class InheritanceRelationUpdate extends Update<AbstractClassItemDiffDataHolder> {

    List<DbCommands> delegates = []

    @Override
    Collection<String> commands() {
        return delegates.collect({it.commands()}).flatten() as Collection<String>
    }
}
