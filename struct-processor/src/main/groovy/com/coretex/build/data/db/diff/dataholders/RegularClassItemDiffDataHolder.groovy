package com.coretex.build.data.db.diff.dataholders


import com.coretex.build.data.items.RegularClassItem

class RegularClassItemDiffDataHolder extends ItemDiffDataHolder<RegularClassItem> {


    public static final String C_CODE = "c_regularitemcode"

    String code(){
        return  item != null ? item.code : dbData.get(C_CODE)
    }


    @Override
    String toString() {
        return "RegularClassItemDiffDataHolder{uuid = ${itemUUID}, code = ${code()}"
    }
}
