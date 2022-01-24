
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantTranslator implements TypeTranslator<Instant>
{
    public Instant read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Timestamp timestamp = resultSet.getTimestamp(columnIndex);
        if (timestamp == null) return null;
        else                   return timestamp.toInstant();
    }
}
