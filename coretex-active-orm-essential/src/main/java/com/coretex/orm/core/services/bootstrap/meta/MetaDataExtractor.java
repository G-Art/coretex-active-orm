package com.coretex.orm.core.services.bootstrap.meta;

import java.util.List;
import java.util.Map;

public interface MetaDataExtractor {

    List<Map<String, Object>> selectTypeItems();

    List<Map<String, Object>> selectAttributeItems();

    List<Map<String, Object>> selectRegularItems();

    List<Map<String, Object>> selectEnumTypes();

    List<Map<String, Object>> selectEnumValues();


}
