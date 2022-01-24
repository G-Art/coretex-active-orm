
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;

public class StringTranslator implements TypeTranslator<String>
{
    public String read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getString(columnIndex);
    }
}
