package com.coretex.build.data.items.attributes

import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RegularClassItem

import static com.coretex.Constants.COLUMN_PREFIX

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class Attribute<T extends Item> extends Item {

    private String description
    private String typeCode
    private AbstractItem owner
    private Class<Object> containerType
    private Object defaultValue
    private String[] index
    private T type

    private boolean localized = false
    private boolean optional = true
    private boolean unique = false
    private boolean dynamic = false
    private boolean associated = false

    Attribute(String name, String typeCode) {
        super(name)
        this.typeCode = typeCode
    }

    Attribute(String name, String typeCode, T type) {
        this(name, typeCode)
        this.type = type
    }

    String getName() {
        return code
    }

    String getColumnName() {
        return "${COLUMN_PREFIX}${code}"
    }

    AbstractItem getOwner() {
        return owner
    }

    void setOwner(AbstractItem owner) {
        this.owner = owner
    }

    String getTypeCode() {
        return typeCode
    }

    void setTypeCode(String typeCode) {
        this.typeCode = typeCode
    }

    T getType() {
        return type
    }

    void setType(T type) {
        this.type = type
    }

    Object getDefaultValue() {
        return this.defaultValue
    }

    void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue
    }

    boolean getOptional() {
        return optional
    }

    void setOptional(boolean optional) {
        this.optional = optional
    }

    boolean getUnique() {
        return unique
    }

    void setUnique(boolean unique) {
        this.unique = unique
    }

    boolean getDynamic() {
        return dynamic
    }

    void setDynamic(boolean dynamic) {
        this.dynamic = dynamic
    }

    Class<Object> getContainerType() {
        return this.containerType
    }

    void setContainerType(Class<Object> containerType) {
        this.containerType = containerType
    }

    boolean getLocalized() {
        return localized
    }

    void setLocalized(boolean localized) {
        this.localized = localized
    }

    boolean isRelation(){
        return !(type.class in RegularClassItem)
    }

    boolean getAssociated() {
        return associated
    }

    void setAssociated(boolean associated) {
        this.associated = associated
    }

    String[] getIndex() {
        return index
    }

    void setIndex(String[] index) {
        this.index = index
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Attribute attribute = (Attribute) o

        return name == attribute.name && uuid == attribute.uuid
    }

    int hashCode() {
        int result
        result = (uuid != null ? uuid.hashCode() : 0)
        result = 31 * result + (name != null ? name.hashCode() : 0)
        return result
    }

    @Override
    String toString() {
        return """\
            |Attribute{
            |   uuid=$uuid, 
            |   name='$name', 
            |   description='$description', 
            |   typeCode='$typeCode', 
            |   containerType=$containerType, 
            |   owner=${this.owner?.code}, 
            |   type=${type?.code}, 
            |   defaultValue=$defaultValue, 
            |   optional=$optional, 
            |   unique=$unique, 
            |   dynamic=$dynamic,
            |   localized=$localized
            |}""".stripMargin('|')
    }
}
