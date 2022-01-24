package com.coretex.orm.core.activeorm.query;

import com.coretex.orm.core.activeorm.query.operations.dataholders.QueryInfoHolder;


public interface QueryTransformationProcessor <S extends QueryInfoHolder> {


	S execute(String query);
}
