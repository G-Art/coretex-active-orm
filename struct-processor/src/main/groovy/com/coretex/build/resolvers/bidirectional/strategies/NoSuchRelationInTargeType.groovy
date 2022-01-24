package com.coretex.build.resolvers.bidirectional.strategies

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute

import static org.apache.commons.collections4.CollectionUtils.isEmpty
import static org.apache.commons.lang3.StringUtils.isBlank

class NoSuchRelationInTargeType implements BidirectionalResolverStrategy {

    @Override
    boolean canResolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        return isBlank(source.target) && isEmpty(targets)
    }

    @Override
    void resolve(Attribute<ClassItem> source, Set<Attribute<ClassItem>> targets) {
        createImplicitRelation(source, source.type)
    }
}
