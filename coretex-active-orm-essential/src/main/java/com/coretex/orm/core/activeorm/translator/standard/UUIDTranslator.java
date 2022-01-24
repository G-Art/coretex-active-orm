
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;
import java.util.UUID;

public class UUIDTranslator implements TypeTranslator<UUID>
{
    public UUID read(ResultSet resultSet, int columnIndex) throws Exception
    {
        String uuid = resultSet.getString(columnIndex);
        if (uuid == null) return null;
        return UUID.fromString(uuid);
    }
}
