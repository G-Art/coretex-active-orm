package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static org.apache.commons.lang3.StringUtils.isBlank

class TargetRelationIsBinded implements BidirectionalResolverStrategy {

    @Override
    boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        return isBlank(source.target) && isNotEmpty(targets) && targets.any { it.target == source.name }
    }

    @Override
    void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        Attribute<ClassItem> target = targets.find { it.target == source.name }
        bindRelations(target, source)
    }

}
