
package com.coretex.orm.core.activeorm.translator.standard;


import com.coretex.orm.core.activeorm.translator.TypeTranslator;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;

public class CharacterTranslator implements TypeTranslator<Character>
{
    public Character read(ResultSet resultSet, int columnIndex) throws Exception
    {
        String charS = resultSet.getString(columnIndex);
        return StringUtils.isNotBlank(charS) && charS.length() == 1 ? charS.charAt(0) : null;
    }
}
