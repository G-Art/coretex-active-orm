package com.coretex.build.data.db.diff.dataholders

import com.coretex.build.converters.impl.AbstractConverter
import com.coretex.build.data.db.Table
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.traits.DbEntity
import com.coretex.build.data.items.traits.JavaClass

abstract class AbstractClassItemDiffDataHolder<I extends AbstractItem & DbEntity & JavaClass> extends ItemDiffDataHolder<I> {

    public static final String C_TABLEOWNER = "c_tableowner"
    public static final String C_TABLENAME = "c_tablename"
    public static final String C_CODE = "c_typecode"
    public static final String PARENT_UUID = "parentuuid"

    Map<UUID, AttributeItemDiffDataHolder> dataHolderMap = [:]

    private Table table

    Table convertToTable() {
        if (table == null) {
            table = converter().doConverting(this.item)
        }
        return table
    }

    String code(){
        return  item != null ? item.code : dbData.get(C_CODE)
    }

    boolean tableOwner(){
        return  item != null ? item.table : dbData.get(C_TABLEOWNER)
    }

    String tableName(){
        return  item != null ? item.tableName : dbData.get(C_TABLENAME)
    }

    boolean parentChanged(){
        def ip = item != null ? item.parentItem != null ? item.parentItem.uuid : null : null
        def dbIp = dbData != null ? dbData.get(PARENT_UUID) : null

        return ip != dbIp
    }

    abstract AbstractConverter<I, Table> converter()


    @Override
    String toString() {
        return """${this.getClass().getSimpleName()}{ uuid = ${itemUUID}, code = ${code()}, table= ${tableOwner()? '!' : ''}${tableName()}, itemClass = ${getItemClass()}"""
    }
}
