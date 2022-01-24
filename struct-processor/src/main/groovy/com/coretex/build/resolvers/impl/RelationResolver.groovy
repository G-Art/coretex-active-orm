package com.coretex.build.resolvers.impl


import com.coretex.build.context.CoretexPluginContext
import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.Item
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.resolvers.Resolver
import com.google.common.collect.Sets

import static java.util.Optional.ofNullable
import static org.apache.commons.lang3.StringUtils.isBlank

/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */

class RelationResolver implements Resolver {

    private static final String META_RELATION_TYPE = 'MetaRelationType'

    private CoretexPluginContext buildContext = CoretexPluginContext.instance

    @Override
    void resolve(AbstractItem item) {
        if (item.class in ClassItem) {
            ClassItem classItem = item as ClassItem
            classItem.attributes.each { attribute ->
                bindOwner(attribute, classItem)
                attribute.type = findRegularType(attribute.typeCode)
                        .orElseGet { findRelatedType(attribute.typeCode) }
            }
            classItem.parentItem = findRelatedType(classItem.extend)
        }
        if (item.class in RelationItem) {
            RelationItem classItem = item as RelationItem
            classItem.implicitAttributes.each { attribute ->
                bindOwner(attribute, classItem)
                attribute.type = findRegularType(attribute.typeCode)
                        .orElseGet { findRelatedType(attribute.typeCode) }
            }
        }
    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.findAll { it.class in RelationItem }
                .each {
                    adjustRelations(it)
                    resolve(it)
                }
        items.findAll { it.class in ClassItem }
                .each { resolve(it) }
    }

    private void adjustRelations(RelationItem relationItem) {
        addToOwners(relationItem)
        relationItem.metaTypeClass = findRelatedType(META_RELATION_TYPE)
        relationItem.parentItem = findRelatedType(relationItem.extend)
        generateImplicitAttributes(relationItem)
    }

    void generateImplicitAttributes(RelationItem relationItem) {
        relationItem.implicitAttributes.addAll(implicitAttributes(relationItem))
    }

    Set<Attribute> implicitAttributes(RelationItem relationItem) {
        def sourceAttribute = new Attribute('source', relationItem.source)
        sourceAttribute.optional = false
        sourceAttribute.description = 'Specify source insistence'

        def targetAttribute = new Attribute('target', relationItem.target)
        targetAttribute.optional = false
        targetAttribute.description = 'Specify target insistence'

        def sourceTypeAttribute = new Attribute('sourceType', 'MetaType')
        sourceTypeAttribute.optional = false
        sourceTypeAttribute.description = 'Specify attribute of source relation owner'

        def targetTypeAttribute = new Attribute('targetType', 'MetaType')
        targetTypeAttribute.optional = false
        targetTypeAttribute.description = 'Specify attribute of target relation owner'

        return Sets.newHashSet(sourceAttribute, sourceTypeAttribute, targetAttribute, targetTypeAttribute)
    }

    private void addToOwners(RelationItem relationItem) {
        Attribute<ClassItem> source = relationItem.sourceAttribute
        Attribute<ClassItem> target = relationItem.targetAttribute

        AbstractItem sourceItem = findRelatedType(relationItem.source)
        sourceItem.relations.put(source.name, relationItem)
        sourceItem.addAttribute(source)

        AbstractItem targetItem = findRelatedType(relationItem.target)
        targetItem.relations.put(target.name, relationItem)
        targetItem.addAttribute(target)
    }

    private void bindOwner(Attribute attribute, AbstractItem owner) {
        attribute.owner = owner
    }

    private AbstractItem findRelatedType(String type) {
        if (isBlank(type)) {
            return null
        }
        return ofNullable(buildContext.items.find { it.code == type })
                .orElseThrow { throw new ClassNotFoundException("Type [ ${type} ] not defined") }
    }

    private Optional<Item> findRegularType(String type) {
        return ofNullable(buildContext.regularItems.get(type))
    }
}
