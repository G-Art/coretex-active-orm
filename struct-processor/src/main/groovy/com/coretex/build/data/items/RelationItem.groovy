package com.coretex.build.data.items

import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.data.items.traits.DbEntity
import com.coretex.build.data.items.traits.JavaClass

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class RelationItem extends AbstractItem implements JavaClass, DbEntity {

    private ClassItem metaTypeClass

    private Attribute<ClassItem> sourceAttribute
    private Attribute<ClassItem> targetAttribute

    private String source
    private String target

    private Set<Attribute> implicitAttributes

    RelationItem(String code) {
        super(code)
        implicitAttributes = [] as LinkedHashSet
    }

    @Override
    String getTypeName() {
        return "${code}"
    }

    String getSource() {
        return source
    }

    void setSource(String source) {
        this.source = source
    }

    String getTarget() {
        return target
    }

    void setTarget(String target) {
        this.target = target
    }

    Attribute<ClassItem> getSourceAttribute() {
        return sourceAttribute
    }

    void setSourceAttribute(Attribute<ClassItem> sourceAttribute) {
        this.sourceAttribute = sourceAttribute
    }

    Attribute<ClassItem> getTargetAttribute() {
        return targetAttribute
    }

    void setTargetAttribute(Attribute<ClassItem> targetAttribute) {
        this.targetAttribute = targetAttribute
    }

    ClassItem getMetaTypeClass() {
        return metaTypeClass
    }

    void setMetaTypeClass(ClassItem metaTypeClass) {
        this.metaTypeClass = metaTypeClass
    }

    Set<Attribute> getImplicitAttributes() {
        return implicitAttributes
    }

    void setImplicitAttributes(Set<Attribute> implicitAttributes) {
        this.implicitAttributes = implicitAttributes
    }

    @Override
    String getItemTypeFolder() {
        return 'relations'
    }
}
