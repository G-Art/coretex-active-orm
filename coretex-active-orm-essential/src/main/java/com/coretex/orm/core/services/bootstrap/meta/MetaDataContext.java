package com.coretex.orm.core.services.bootstrap.meta;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class MetaDataContext {

    private Table<UUID, String, Object> typeContext;

    private Table<UUID, String, Object> attributeContext;

    private Table<UUID, String, Object> regularContext;

    private Table<UUID, String, Object> enumContext;

    private Table<UUID, String, Object> enumValueContext;


    public MetaDataContext() {
        typeContext = HashBasedTable.create();
        attributeContext = HashBasedTable.create();
        regularContext = HashBasedTable.create();
        enumContext = HashBasedTable.create();
        enumValueContext = HashBasedTable.create();
    }

    public Map<String, Object> getItemData(UUID uuid) {
        Map<String, Object> result = getTypeData(uuid);
        if (MapUtils.isEmpty(result)) {
            result = getAttributeData(uuid);
        }
        if (MapUtils.isEmpty(result)) {
            result = getRegularData(uuid);
        }
        if (MapUtils.isEmpty(result)) {
            result = getEnumData(uuid);
        }
        if (MapUtils.isEmpty(result)) {
            result = getEnumValueData(uuid);
        }
        return result;
    }

    public Map<String, Object> getTypeData(UUID typeUUID) {
        return typeContext.row(typeUUID);
    }

    public List<Map<String, Object>> getAllMetaTypes() {
        return new ArrayList<>(typeContext.rowMap().values());
    }

    public Map<String, Object> getAttributeData(UUID attributeUUID) {
        return attributeContext.row(attributeUUID);
    }

    public List<Map<String, Object>> getAllAttributes() {
        return new ArrayList<>(attributeContext.rowMap().values());
    }

    public Map<String, Object> getRegularData(UUID regularUUID) {
        return regularContext.row(regularUUID);
    }

    public List<Map<String, Object>> getAllRegularTypes() {
        return new ArrayList<>(regularContext.rowMap().values());
    }

    public Map<String, Object> getEnumData(UUID typeUUID) {
        return enumContext.row(typeUUID);
    }

    public List<Map<String, Object>> getAllEnumDatas() {
        return new ArrayList<>(enumContext.rowMap().values());
    }

    public Map<String, Object> getEnumValueData(UUID typeUUID) {
        return enumValueContext.row(typeUUID);
    }

    public List<Map<String, Object>> getAllEnumValueDatas() {
        return new ArrayList<>(enumValueContext.rowMap().values());
    }


    public void addTypeData(UUID typeUUID, Map<String, Object> typeItemData) {
        addItemData(typeUUID, typeItemData, this.typeContext);
    }

    public void addAttributeData(UUID attributeUUID, Map<String, Object> attributeItemData) {
        addItemData(attributeUUID, attributeItemData, this.attributeContext);
    }

    public void addRegularData(UUID regularItemUUID, Map<String, Object> regularItemData) {
        addItemData(regularItemUUID, regularItemData, this.regularContext);
    }

    public void addEnumData(UUID enumUuid, Map<String, Object> enumItemData) {
        addItemData(enumUuid, enumItemData, this.enumContext);
    }

    public void addEnumValueData(UUID enumValueUuid, Map<String, Object> enumValueItemData) {
        addItemData(enumValueUuid, enumValueItemData, this.enumValueContext);
    }

    private void addItemData(UUID itemUuid, Map<String, Object> item, Table<UUID, String, Object> itemDataCtx) {
        if (MapUtils.isNotEmpty(item) && nonNull(itemUuid)) {
            item.entrySet().stream()
                    .filter(entry -> nonNull(entry.getValue()))
                    .forEach(entry -> itemDataCtx.put(itemUuid, entry.getKey(), entry.getValue()));

        }
    }
}
