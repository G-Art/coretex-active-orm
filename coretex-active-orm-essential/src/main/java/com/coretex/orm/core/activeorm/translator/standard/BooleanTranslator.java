
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class BooleanTranslator implements TypeTranslator<Boolean>
{
    public Boolean read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Boolean b = resultSet.getBoolean(columnIndex);
        if (resultSet.wasNull()) b = null;
        return b;
    }
}
