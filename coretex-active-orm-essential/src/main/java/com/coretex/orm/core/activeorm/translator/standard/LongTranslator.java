
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class LongTranslator implements TypeTranslator<Long>
{
    public Long read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Long l = resultSet.getLong(columnIndex);
        if (resultSet.wasNull()) l = null;
        return l;
    }
}
