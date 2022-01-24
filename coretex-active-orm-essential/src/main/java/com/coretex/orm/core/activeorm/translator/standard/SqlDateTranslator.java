
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class SqlDateTranslator implements TypeTranslator<java.sql.Date>
{
    public java.sql.Date read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getDate(columnIndex);
    }
}
