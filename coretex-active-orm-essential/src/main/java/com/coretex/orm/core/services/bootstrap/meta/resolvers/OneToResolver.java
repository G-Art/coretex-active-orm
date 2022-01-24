package com.coretex.orm.core.services.bootstrap.meta.resolvers;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.orm.core.services.bootstrap.meta.MetaItemContext;
import com.coretex.items.core.GenericItem;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.Validate.isTrue;

public class OneToResolver extends ReferenceResolver {

    private boolean failWhenNotFound;
    private String source;
    private String target;
    private Function<Object, ? extends Collection<Object>> targetCollectionConverter;

    public OneToResolver(MetaDataContext dataContext, MetaItemContext itemContext, String source) {
        this(dataContext, itemContext, source, false);
    }

    public OneToResolver(MetaDataContext dataContext, MetaItemContext itemContext, String source, boolean failWhenNotFound) {
        this(dataContext, itemContext, source, null, failWhenNotFound);
    }

    public OneToResolver(MetaDataContext dataContext,
                         MetaItemContext itemContext,
                         String source,
                         String target,
                         boolean failWhenNotFound) {
        this(dataContext, itemContext, source, target, null, failWhenNotFound);
    }

    public OneToResolver(MetaDataContext dataContext,
                         MetaItemContext itemContext,
                         String source,
                         String target,
                         Function<Object, ? extends Collection<Object>> targetCollectionConverter,
                         boolean failWhenNotFound) {
        this(dataContext, itemContext, source, target, targetCollectionConverter);
        this.failWhenNotFound = failWhenNotFound;
    }

    public OneToResolver(MetaDataContext dataContext,
                         MetaItemContext itemContext,
                         String source,
                         String target,
                         Function<Object, ? extends Collection<Object>> targetCollectionConverter) {
        super(dataContext, itemContext);
        this.source = source;
        this.target = target;
        this.targetCollectionConverter = targetCollectionConverter;
    }

    @Override
    public void resolve(UUID itemUUID) {
        Map<String, Object> itemData = dataContext.getItemData(itemUUID);
        isTrue(isNotEmpty(itemData), "Can't find item data by %s uuid", Objects.toString(itemData));
        if (nonNull(itemData.get(source))) {
            populateSourceValue(itemUUID, itemData);
        }
    }

    private void populateSourceValue(UUID itemUUID, Map<String, Object> itemData) {
        UUID targetUUID = toUUID(itemData.get(source));
        GenericItem targetItem = itemContext.getGenericItem(targetUUID);
        if (isNull(targetItem)) {
            checkThrowException("Can't find target item by uuid: '" + targetUUID + "'");
            itemData.remove(source);
        } else {
            itemData.put(source, targetItem);
            populateTargetValue(itemUUID, targetUUID);
        }
    }

    private void populateTargetValue(UUID itemUUID, UUID targetUUID) {
        if (nonNull(target)) {
            Map<String, Object> targetData = dataContext.getItemData(targetUUID);
            targetData.put(target, getValue(itemUUID, targetData));
        }
    }

    private Object getValue(UUID itemUUID, Map<String, Object> targetData) {
        GenericItem item = itemContext.getGenericItem(itemUUID);

        if (isNull(item)) {
            checkThrowException("Can't find generic item by '" + itemUUID + "' uuid");
            return null;
        }

        if (isNull(targetCollectionConverter)) {
            return item;
        }

        Collection<Object> itemCollection = targetCollectionConverter.apply(targetData.get(target));
        itemCollection.add(item);
        return itemCollection;
    }

    private void checkThrowException(String message) {
        if (failWhenNotFound) {
            throw new IllegalStateException(message);
        }
    }

}
