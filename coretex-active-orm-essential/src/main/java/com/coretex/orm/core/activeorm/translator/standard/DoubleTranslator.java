
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class DoubleTranslator implements TypeTranslator<Double>
{
    public Double read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Double d = resultSet.getDouble(columnIndex);
        if (resultSet.wasNull()) d = null;
        return d;
    }
}
