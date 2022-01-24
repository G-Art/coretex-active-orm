
package com.coretex.orm.core.activeorm.translator.standard;

import com.coretex.orm.core.activeorm.translator.TypeTranslator;

import java.math.BigDecimal;
import java.sql.ResultSet;


public class BigDecimalTranslator implements TypeTranslator<BigDecimal>
{
    public BigDecimal read(ResultSet resultSet, int columnIndex) throws Exception
    {
        return resultSet.getBigDecimal(columnIndex);
    }
}
