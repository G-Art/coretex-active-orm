package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.lang3.StringUtils.isNoneBlank
import static org.apache.commons.lang3.StringUtils.uncapitalize

trait BidirectionalResolverStrategy {

    abstract boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets)

    abstract void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets)

    void createImplicitRelation(Attribute<ClassItem> source, ClassItem targetType) {
        String implicitRelationName = source.target ?: uncapitalize(source.owner.typeName)

        Attribute<ClassItem> target = new Attribute<ClassItem>(implicitRelationName, source.owner.typeName)
        target.aggregated = source.aggregated
        target.owner = targetType
        target.target = source.name
        target.type = source.owner
        targetType.addRelation(target)

        if (targetType.package != source.owner.package) {
            targetType.addImport(source.owner.fullTypeName)
        }
        bindRelations(source, target)
    }

    void bindRelations(Attribute<ClassItem> source, Attribute<ClassItem> target) {
        validate(source, target)
        validate(target, source)
        target.target = source.name
        source.target = target.name
    }

    void validate(Attribute<ClassItem> source, Attribute<ClassItem> target) {
        if (isNoneBlank(source.target) && source.target != target.name) {
            throw new IllegalStateException("Incorrect binding param: '$source.target' of '$source.name' relation. " +
                    "Target Relation name: '$target.name'")
        }

//        if ((isBlank(source.collectionType) && isBlank(target.collectionType) && (source.owner != target.owner))) {
//            if ((source.relationOwner && target.relationOwner) || !(source.relationOwner && target.relationOwner)) {
//                throw new IllegalStateException("Can't get relation owner in one to one relation between " +
//                        "'$source.name':owner:$source.relationOwner and '$target.name':owner:$target.relationOwner")
//            }
//        }
    }
}