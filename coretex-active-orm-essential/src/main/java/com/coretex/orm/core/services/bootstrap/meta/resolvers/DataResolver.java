package com.coretex.orm.core.services.bootstrap.meta.resolvers;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataContext;

import java.util.UUID;

public abstract class DataResolver {

    protected MetaDataContext dataContext;

    public DataResolver(MetaDataContext dataContext) {
        this.dataContext = dataContext;
    }

    public abstract void resolve(UUID itemUUID);

}
