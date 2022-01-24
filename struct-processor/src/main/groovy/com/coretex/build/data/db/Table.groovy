package com.coretex.build.data.db

import com.coretex.build.data.items.AbstractItem
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
class Table {

    private String name
    private Set<TableField> fields
    private AbstractItem item
    private boolean localeSupportTableRequired = false
    private Map<String, Index> indexes = [:]

    AbstractItem getItem() {
        item
    }

    void setItem(AbstractItem item) {
        this.item = item
    }

    Set<TableField> getFields() {
        fields
    }

    Map<String, Index> getIndexes() {
        return indexes
    }

    void setFields(Set<TableField> fields) {
        addFields(fields)
    }

    void addField(TableField field) {
        if (this.fields == null) {
            this.fields = [] as Set
        }
        this.fields.add(field)

        field.indexName.forEach{
            indexes.computeIfAbsent(it, { key ->
                Index idx = new Index()
                idx.addTableField(field)
                return idx
            })

            indexes.computeIfPresent(it, { key, idx ->
                idx.addTableField(field)
                return idx
            })
        }
    }

    void addFields(Collection<TableField> field) {
        field.forEach{
            addField(it)
        }
    }

    String getName() {
        name
    }

    void setName(String name) {
        this.name = name
    }

    boolean getLocaleSupportTableRequired() {
        return localeSupportTableRequired
    }

    void setLocaleSupportTableRequired(boolean localeSupportTableRequired) {
        this.localeSupportTableRequired = localeSupportTableRequired
    }

    @Override
    String toString() {
        return 'Table{' +
                'name=\'' + name + '\'' +
                '\', fields=\'' + fields +
                '\', item=\'' + item +
                '\'}'
    }
}
