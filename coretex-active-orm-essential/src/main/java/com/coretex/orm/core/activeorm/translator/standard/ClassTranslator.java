
package com.coretex.orm.core.activeorm.translator.standard;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;
import java.util.Objects;


public class ClassTranslator implements TypeTranslator<Class>
{
    public Class read(ResultSet resultSet, int columnIndex) throws Exception
    {
        String value = resultSet.getString(columnIndex);
        return Objects.nonNull(value) ? Class.forName(resultSet.getString(columnIndex)) : null;
    }
}
