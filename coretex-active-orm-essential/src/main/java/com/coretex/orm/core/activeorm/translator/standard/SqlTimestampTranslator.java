
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class SqlTimestampTranslator implements TypeTranslator<java.sql.Timestamp>
{
    public java.sql.Timestamp read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getTimestamp(columnIndex);
    }
}
