
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class DateTranslator implements TypeTranslator<Date>
{
    public Date read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Object object = resultSet.getObject(columnIndex);
        if (Objects.nonNull(object)) {
            if (object instanceof Timestamp) {
                return new Date(((Timestamp) object).getTime());
            }
            if (object instanceof LocalDateTime) {
                return Date.from(((LocalDateTime) object).atZone(ZoneId.systemDefault()).toInstant());
            }
        }
        return null;
    }
}
