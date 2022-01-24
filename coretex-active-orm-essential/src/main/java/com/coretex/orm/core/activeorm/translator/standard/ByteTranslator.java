
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;


public class ByteTranslator implements TypeTranslator<Byte>
{
    public Byte read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Byte b = resultSet.getByte(columnIndex);
        if (resultSet.wasNull()) b = null;
        return b;
    }
}
