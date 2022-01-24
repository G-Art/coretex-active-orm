package com.coretex.orm.core.activeorm.translator;

import java.sql.ResultSet;



public interface TypeTranslator<T>
{
    T read(ResultSet resultSet, int columnIndex) throws Exception;
}
