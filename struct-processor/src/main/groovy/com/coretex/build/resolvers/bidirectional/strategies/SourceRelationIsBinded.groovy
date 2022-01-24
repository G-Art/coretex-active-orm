package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.lang3.StringUtils.isNoneBlank

class SourceRelationIsBinded implements BidirectionalResolverStrategy {

    @Override
    boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        return isNoneBlank(source.target)
    }

    @Override
    void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        Attribute<ClassItem> target = targets?.find { it.name == source.target }
        if (target == null) {
            createImplicitRelation(source, source.type)
        } else {
            bindRelations(source, target)
        }
    }
}
