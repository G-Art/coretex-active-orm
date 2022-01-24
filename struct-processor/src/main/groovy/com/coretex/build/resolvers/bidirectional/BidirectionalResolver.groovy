package com.coretex.build.resolvers.bidirectional

import com.coretex.build.data.items.AbstractItem
import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.attributes.Attribute
import com.coretex.build.resolvers.Resolver
import com.coretex.build.resolvers.bidirectional.strategies.BidirectionalResolverStrategy

import javax.naming.OperationNotSupportedException

class BidirectionalResolver implements Resolver {

    private List<BidirectionalResolverStrategy> resolverStrategies

    BidirectionalResolver() {
        resolverStrategies = [] as Queue
    }

    @Override
    void resolve(AbstractItem item) {
        if (item.class == ClassItem) {
            item.relations.each this.&resolveRelation
        }
    }

    private void resolveRelation(Attribute<ClassItem> sourceRelation) {
        ClassItem targetType = sourceRelation.type
        Set<Attribute<ClassItem>> targetRelations =
                targetType.relations.findAll { rel -> rel.type.code == sourceRelation.owner.code }

        boolean notResolved = true
        for (BidirectionalResolverStrategy resolverStrategy in resolverStrategies) {
            if (resolverStrategy.canResolve(sourceRelation, targetRelations)) {
                resolverStrategy.resolve(sourceRelation, targetRelations)
                notResolved = false
            }
        }

        if (notResolved) {
            throw new OperationNotSupportedException("Can't resolve '$sourceRelation.name' relation of '$sourceRelation.owner.code' type")
        }
    }

    @Override
    void resolve(List<AbstractItem> items) {
        items.each this.&resolve
    }
}
