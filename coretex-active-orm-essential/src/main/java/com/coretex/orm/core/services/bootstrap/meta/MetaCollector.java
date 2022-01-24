package com.coretex.orm.core.services.bootstrap.meta;

import com.coretex.items.core.*;
import com.coretex.orm.meta.AbstractGenericItem;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MetaCollector {

    private final Logger LOG = LoggerFactory.getLogger(MetaCollector.class);

    private final MetaDataExtractor extractor;

    private MetaItemContext itemCtx;

    public MetaCollector(MetaDataExtractor extractor) {
        this.extractor = extractor;
    }

    public void load() {
        MetaDataContext dataCtx = createTypeDataContext();
        itemCtx = createItemContext(dataCtx);
        MetaItemResolver resolver = new MetaItemResolver(itemCtx, dataCtx);
        LOG.info("Meta data resolving ...");
        resolver.resolve();
    }

    public List<MetaTypeItem> collectSystemMetaTypes() {
        return itemCtx.getAllMetaTypes();
    }

    public List<RegularTypeItem> collectSystemRegularTypeTypes() {
        return itemCtx.getAllRegularTypes();
    }

    public List<MetaEnumValueTypeItem> collectMetaEnumValueTypeItems(){
        return itemCtx.getAllMetaEnumValueTypeItems();
    }

    public MetaDataExtractor getExtractor() {
        return extractor;
    }

    private MetaDataContext createTypeDataContext() {
        MetaDataContext ctx = new MetaDataContext();
        LOG.info("Collect meta data [TypeItems] ...");
        populateItemData(extractor.selectTypeItems(), ctx::addTypeData);
        LOG.info("Collect meta data [AttributeItems] ...");
        populateItemData(extractor.selectAttributeItems(), ctx::addAttributeData);
        LOG.info("Collect meta data [RegularItems] ...");
        populateItemData(extractor.selectRegularItems(), ctx::addRegularData);
        LOG.info("Collect meta data [EnumTypes] ...");
        populateItemData(extractor.selectEnumTypes(), ctx::addEnumData);
        LOG.info("Collect meta data [EnumValues] ...");
        populateItemData(extractor.selectEnumValues(), ctx::addEnumValueData);
        return ctx;
    }

    private void populateItemData(List<Map<String, Object>> items, BiConsumer<UUID, Map<String, Object>> populator) {
        for (Map<String, Object> item : items) {
            UUID itemUuid = getItemUuid(item);
            item.put(AbstractGenericItem.UUID, itemUuid);
            populator.accept(itemUuid, item);
        }
    }

    private MetaItemContext createItemContext(MetaDataContext dataContext) {
        MetaItemContext itemContext = new MetaItemContext();
        ItemCreator creator = new ItemCreator(dataContext, itemContext);
        populateMetaItemContext(dataContext.getAllMetaTypes(), creator, item -> itemContext.addType((MetaTypeItem) item));
        populateMetaItemContext(dataContext.getAllAttributes(), creator, item -> itemContext.addAttribute((MetaAttributeTypeItem) item));
        populateMetaItemContext(dataContext.getAllRegularTypes(), creator, item -> itemContext.addRegularType((RegularTypeItem) item));
        populateMetaItemContext(dataContext.getAllEnumDatas(), creator, item -> itemContext.addEnumType((MetaEnumTypeItem) item));
        populateMetaItemContext(dataContext.getAllEnumValueDatas(), creator, item -> itemContext.addEnumValue((MetaEnumValueTypeItem) item));
        return itemContext;
    }

    private void populateMetaItemContext(List<Map<String, Object>> metaDatas, ItemCreator itemCreator, Consumer<GenericItem> populator) {
        for (Map<String, Object> metaData : metaDatas) {
            UUID uuid = toUuid(metaData.get(AbstractGenericItem.UUID));
            UUID metaTypeUuid = toUuid(metaData.get(GenericItem.META_TYPE));
            populator.accept(itemCreator.convert(uuid, extractItemClass(metaTypeUuid, itemCreator.getDataContext())));
        }
    }

    private UUID toUuid(Object attributeValue) {
        return Optional.ofNullable(attributeValue)
                .map(Object::toString)
                .map(UUID::fromString)
                .orElseThrow(() -> new IllegalStateException("Can't convert to uuid attribute value: '" + attributeValue + '\''));
    }


    private Class<? extends GenericItem> extractItemClass(UUID metaTypeUuid, MetaDataContext dataContext) {
        return Optional.ofNullable(metaTypeUuid)
                .map(dataContext::getTypeData)
                .map(typeData -> typeData.get(MetaTypeItem.ITEM_CLASS))
                .map(Object::toString)
                .map(this::getItemClass)
                .orElseThrow(() -> new IllegalStateException("Can't find type or denominate class of type"));

    }

    @SuppressWarnings("unchecked")
    private Class<? extends GenericItem> getItemClass(String className) {
        try {
            return (Class<? extends GenericItem>) ClassUtils.getClass(className);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private UUID getItemUuid(Map<String, Object> item) {
        return Optional.ofNullable(item.get(AbstractGenericItem.UUID))
                .map(Object::toString)
                .map(UUID::fromString)
                .orElseThrow(() -> new IllegalStateException("Can't convert value from db to uuid"));
    }

}
