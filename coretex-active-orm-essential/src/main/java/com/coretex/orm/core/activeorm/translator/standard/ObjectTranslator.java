
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;


public class ObjectTranslator implements TypeTranslator<Object>
{
    public Object read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getObject(columnIndex);
    }
}
