package com.coretex.orm.core.services.bootstrap.meta.resolvers;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.Validate.isTrue;

public class ClassDataResolver extends DataResolver {

    private static final Logger logger = LoggerFactory.getLogger(ClassDataResolver.class);
    private final String attributeName;

    public ClassDataResolver(MetaDataContext dataContext, String attributeName) {
        super(dataContext);
        this.attributeName = attributeName;
    }

    @Override
    public void resolve(UUID itemUUID) {
        Map<String, Object> itemData = dataContext.getItemData(itemUUID);
        isTrue(isNotEmpty(itemData), "Can't find item by %s", Objects.toString(itemData));
        Object containerClass = itemData.get(attributeName);
        if (nonNull(containerClass)) {
            containerClass = containerClass.getClass() == Class.class ? containerClass : fromName(containerClass.toString());
            itemData.put(attributeName, containerClass);
        }
    }

    private Class<?> fromName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(format("Can't find class for meta attribute '%s'.", attributeName), e);
        }
    }
}
