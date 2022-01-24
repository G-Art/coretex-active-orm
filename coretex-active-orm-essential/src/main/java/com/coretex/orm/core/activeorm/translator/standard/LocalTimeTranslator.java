
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;


public class LocalTimeTranslator implements TypeTranslator<LocalTime>
{
    public LocalTime read(ResultSet resultSet, int columnIndex) throws Exception
    {
        Time time = resultSet.getTime(columnIndex);
        if (time == null) return null;
        else              return time.toLocalTime();
    }
}
