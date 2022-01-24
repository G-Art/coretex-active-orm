package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static org.apache.commons.lang3.StringUtils.isBlank

class TargetRelationsAreAlreadyBinded implements BidirectionalResolverStrategy {

    @Override
    boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        return isBlank(source.target) && isNotEmpty(targets) && !targets.any { isBlank(it.target) }
    }

    @Override
    void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        createImplicitRelation(source, source.type)
    }
}
