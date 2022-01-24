package com.coretex.orm.core.services.bootstrap.meta.resolvers;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataContext;
import com.coretex.items.core.GenericItem;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.coretex.orm.core.general.constants.GeneralConstants.SYSTEM_TIME_ZONE;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.ObjectUtils.NULL;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.isTrue;

public class GenericItemDataResolver extends DataResolver {

    public GenericItemDataResolver(MetaDataContext dataContext) {
        super(dataContext);
    }

    @Override
    public void resolve(UUID itemUUID) {
        Map<String, Object> itemData = dataContext.getItemData(itemUUID);
        isTrue(isNotEmpty(itemData), "Can't find item date by %s uuid", Objects.toString(itemData));

        convertDate(GenericItem.CREATE_DATE, itemData);
        convertDate(GenericItem.UPDATE_DATE, itemData);
    }

    private void convertDate(String dateField, Map<String, Object> itemData) {
        Object date = itemData.get(dateField);
        if(date instanceof Date){
            itemData.put(dateField, ((Date) date).toInstant().atZone(SYSTEM_TIME_ZONE).toLocalDateTime());
        }else{
            if(date instanceof LocalDateTime){
                itemData.put(dateField, ((LocalDateTime) date).atZone(SYSTEM_TIME_ZONE));
            } else {
                throw new IllegalStateException(String.format("Date field [%s] has illegal type [%s]", dateField, defaultIfNull(date, NULL).getClass().getName()) );
            }
        }

    }
}
