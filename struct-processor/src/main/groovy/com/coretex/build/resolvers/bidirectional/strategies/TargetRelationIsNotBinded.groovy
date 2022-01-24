package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static org.apache.commons.lang3.StringUtils.isBlank

class TargetRelationIsNotBinded implements BidirectionalResolverStrategy {

    @Override
    boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        return isBlank(source.target) && isNotEmpty(targets) && targets.count { isBlank(it.target) } == 1
    }

    @Override
    void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        Attribute<ClassItem> target = targets.find { isBlank(it.target) }
        bindRelations(source, target)
    }
}
