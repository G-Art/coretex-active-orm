package com.coretex.orm.core.services.bootstrap.meta.resolvers;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.orm.core.services.bootstrap.meta.MetaItemContext;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public abstract class ReferenceResolver extends DataResolver {

    @SuppressWarnings("unchecked")
    public static  <T> List<T> toList(Object list) {
        if (!(list instanceof List)) {
            return new ArrayList<>();
        }
        return (List<T>) list;
    }

    @SuppressWarnings("unchecked")
    public static  <T> Set<T> toSet(Object set) {
        if (!(set instanceof Set)) {
            return new HashSet<>();
        }
        return (Set<T>) set;
    }

    protected MetaItemContext itemContext;

    public ReferenceResolver(MetaDataContext dataContext, MetaItemContext itemContext) {
        super(dataContext);
        this.itemContext = itemContext;
    }

    protected UUID toUUID(Object link) {
        checkArgument(nonNull(link), "Link must be not null");
        if (UUID.class == link.getClass()) {
            return (UUID) link;
        }
        if (link instanceof AbstractGenericItem) {
            return ((AbstractGenericItem) link).getUuid();
        }
        return UUID.fromString(link.toString());
    }

}
