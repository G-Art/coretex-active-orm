package com.coretex.orm.core.services.bootstrap.meta;

import com.coretex.items.core.*;

import java.util.*;

import static java.util.Objects.isNull;

public class MetaItemContext {

    private Map<UUID, MetaTypeItem> types;

    private Map<UUID, MetaAttributeTypeItem> attributes;

    private Map<UUID, RegularTypeItem> regularTypes;

    private Map<UUID, MetaEnumTypeItem> enumTypes;

    private Map<UUID, MetaEnumValueTypeItem> enumValues;

    public MetaItemContext() {
        types = new HashMap<>();
        attributes = new HashMap<>();
        regularTypes = new HashMap<>();
        enumTypes = new HashMap<>();
        enumValues = new HashMap<>();
    }

    public GenericItem getGenericItem(UUID uuid) {
        GenericItem result = getMetaType(uuid);
        if (isNull(result)) {
            result = getAttributeType(uuid);
        }
        if (isNull(result)) {
            result = getRegularType(uuid);
        }
        if (isNull(result)) {
            result = getEnumType(uuid);
        }
        if (isNull(result)) {
            result = getEnumValue(uuid);
        }
        return result;
    }

    public void addType(MetaTypeItem type) {
        types.put(type.getUuid(), type);
    }

    public void addAttribute(MetaAttributeTypeItem attribute) {
        attributes.put(attribute.getUuid(), attribute);
    }

    public void addRegularType(RegularTypeItem regularType) {
        regularTypes.put(regularType.getUuid(), regularType);
    }

    public void addEnumType(MetaEnumTypeItem enumType) {
        enumTypes.put(enumType.getUuid(), enumType);
    }

    public void addEnumValue(MetaEnumValueTypeItem enumValue) {
        enumValues.put(enumValue.getUuid(), enumValue);
    }


    public MetaTypeItem getMetaType(UUID uuid) {
        return types.get(uuid);
    }

    public List<MetaTypeItem> getAllMetaTypes() {
        return new ArrayList<>(types.values());
    }

    public List<RegularTypeItem> getAllRegularTypes() {
        return new ArrayList<>(regularTypes.values());
    }

    public List<MetaEnumValueTypeItem> getAllMetaEnumValueTypeItems(){
        return new ArrayList<>(enumValues.values());
    }

    public List<GenericItem> getAllItems() {
        ArrayList<GenericItem> items = new ArrayList<>();
        items.addAll(types.values());
        items.addAll(attributes.values());
        items.addAll(regularTypes.values());
        items.addAll(enumTypes.values());
        items.addAll(enumValues.values());
        return items;
    }

    public MetaAttributeTypeItem getAttributeType(UUID uuid) {
        return attributes.get(uuid);
    }

    public RegularTypeItem getRegularType(UUID uuid) {
        return regularTypes.get(uuid);
    }

    public MetaEnumTypeItem getEnumType(UUID uuid) {
        return enumTypes.get(uuid);
    }

    public MetaEnumValueTypeItem getEnumValue(UUID uuid) {
        return enumValues.get(uuid);
    }
}
