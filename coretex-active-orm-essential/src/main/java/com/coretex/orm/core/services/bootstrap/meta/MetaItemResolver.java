package com.coretex.orm.core.services.bootstrap.meta;

import com.coretex.orm.core.services.bootstrap.meta.resolvers.*;
import com.coretex.items.core.*;
import com.coretex.orm.meta.AbstractGenericItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetaItemResolver {

    private List<DataResolver> resolvers;
    private MetaItemContext itemCtx;
    private MetaDataContext dataCtx;

    public MetaItemResolver(MetaItemContext itemCtx, MetaDataContext dataCtx) {
        this.itemCtx = itemCtx;
        this.dataCtx = dataCtx;
        this.resolvers = new ArrayList<>();
        this.resolvers.add(new GenericItemDataResolver(dataCtx));
        this.resolvers.add(new ClassDataResolver(dataCtx, MetaAttributeTypeItem.CONTAINER_TYPE));
        this.resolvers.add(new ClassDataResolver(dataCtx, RegularTypeItem.REGULAR_CLASS));
        this.resolvers.add(new ClassDataResolver(dataCtx, MetaTypeItem.ITEM_CLASS));
        this.resolvers.add(new ClassDataResolver(dataCtx, MetaEnumTypeItem.ENUM_CLASS));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, GenericItem.META_TYPE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaTypeItem.PARENT, MetaTypeItem.SUBTYPES, ReferenceResolver::toSet));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaAttributeTypeItem.OWNER, MetaTypeItem.ITEM_ATTRIBUTES, ReferenceResolver::toSet, true));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaAttributeTypeItem.ATTRIBUTE_TYPE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaRelationTypeItem.SOURCE_ATTRIBUTE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaRelationTypeItem.TARGET_ATTRIBUTE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaRelationTypeItem.SOURCE_TYPE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaRelationTypeItem.TARGET_TYPE));
        this.resolvers.add(new OneToResolver(dataCtx, itemCtx, MetaEnumValueTypeItem.OWNER, MetaEnumTypeItem.VALUES, ReferenceResolver::toSet, true));
    }

    public void resolve() {
        List<GenericItem> allItems = itemCtx.getAllItems();
        for (DataResolver resolver : resolvers) {
            allItems.stream()
                    .map(AbstractGenericItem::getUuid)
                    .forEach(resolver::resolve);
        }
        for (GenericItem item : allItems) {
            Map<String, Object> itemData = dataCtx.getItemData(item.getUuid());
            ((MetaAttributeValueProvider) item.getItemContext().getProvider()).init(itemData);
        }
    }

}
