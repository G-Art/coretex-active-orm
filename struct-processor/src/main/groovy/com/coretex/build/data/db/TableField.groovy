package com.coretex.build.data.db

import com.coretex.build.data.items.attributes.Attribute
import com.google.common.base.MoreObjects

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class TableField {

    private String name
    private String javaClassType
    private String sqlType
    private Boolean pk = false
    private Attribute attribute
    private Set<String> indexName = []  as Set

    Set<String> getIndexName() {
        return indexName
    }

    void addIndexName(String idxName){
        indexName.add(idxName)
    }

    Attribute getAttribute() {
        return attribute
    }

    void setAttribute(Attribute attribute) {
        this.attribute = attribute
    }

    String getJavaClassType() {
        javaClassType
    }

    void setJavaClassType(String javaClassType) {
        this.javaClassType = javaClassType
    }

    String getName() {
        name
    }

    void setName(String name) {
        this.name = name
    }

    String getSqlType() {
        sqlType
    }

    void setSqlType(String sqlType) {
        this.sqlType = sqlType
    }

    Boolean getPk() {
        pk
    }

    void setPk(Boolean pk) {
        this.pk = pk
    }

    boolean presentInTable(){
        return !attribute.localized
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(this)
                .add('name', name)
                .add('javaClassType', javaClassType)
                .add('sqlType', sqlType)
                .add('pk', pk)
                .add('attribute', attribute)
                .toString()
    }
}
