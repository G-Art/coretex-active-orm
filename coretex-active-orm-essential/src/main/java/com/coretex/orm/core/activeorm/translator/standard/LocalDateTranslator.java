
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

public class LocalDateTranslator implements TypeTranslator<LocalDate>
{
    public LocalDate read(ResultSet resultSet, int columnIndex) throws Exception
    {
    	Date date = resultSet.getDate(columnIndex); 
        if (date == null) return null;
        else              return date.toLocalDate();
    }
}
