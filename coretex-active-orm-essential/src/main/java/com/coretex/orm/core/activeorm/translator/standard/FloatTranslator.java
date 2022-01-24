
package com.coretex.orm.core.activeorm.translator.standard;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class FloatTranslator implements TypeTranslator<Float>
{
    public Float read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Float f = resultSet.getFloat(columnIndex);
        if (resultSet.wasNull()) f = null;
        return f;
    }
}
