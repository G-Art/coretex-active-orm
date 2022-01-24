package com.coretex.build.data.items


import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.data.items.traits.Adjustable
import com.coretex.build.data.items.traits.DbEntity
import com.coretex.build.data.items.traits.JavaClass
import com.coretex.struct.StructConstants
import com.google.common.base.MoreObjects
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class ClassItem extends AbstractItem implements JavaClass, DbEntity, Adjustable{

    private Set<Attribute> attributes
    private boolean hasLocalizedAttributes = false

    private Map<String, RelationItem> relations

    private List<ClassItem> childItems

    ClassItem(String code) {
        super(code)
        attributes = [] as LinkedHashSet
        relations = [] as HashMap
    }

    String getTypeName() {
        return "${code}${StructConstants.Item.CLASS_SUFFIX}"
    }

    Set<Attribute> getAttributes() {
        return attributes
    }

    void addAttribute(Attribute attribute) {
        if(attribute.localized && !hasLocalizedAttributes){
            hasLocalizedAttributes = true
        }
        attributes.add(attribute)
    }

    List<ClassItem> getChildItems() {
        return childItems
    }

    void setChildItems(List<ClassItem> childItems) {
        this.childItems = childItems
    }

    Map<String, RelationItem> getRelations() {
        return relations
    }

    boolean getHasLocalizedAttributes() {
        return hasLocalizedAttributes
    }

    @Override
    String getItemTypeFolder() {
        return 'items'
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
                .add('code', code)
                .add('table', table)
                .add('imports', imports)
                .add('attributes', attributes)
                .add('relations', relations)
                .add('parentItem', parentItem?.typeName)
                .add('extend', extend)
                .add("hasLocalizedAttributes", hasLocalizedAttributes)
                .toString()
    }
}
