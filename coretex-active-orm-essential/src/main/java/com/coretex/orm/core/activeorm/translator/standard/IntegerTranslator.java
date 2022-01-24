
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class IntegerTranslator implements TypeTranslator<Integer>
{
    public Integer read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Integer i = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) i = null;
        return i;
    }
}
