package com.coretex.build.converters.impl

import com.coretex.build.converters.Converter
import com.coretex.build.services.TypeAppropriateService
import com.coretex.common.DbDialect

import java.lang.reflect.ParameterizedType
/**
 ***********************************************************************
 * Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 ***********************************************************************
 */
abstract class AbstractConverter<SOURCE, TARGET> implements Converter<SOURCE, TARGET> {

    private TypeAppropriateService appropriateService

    AbstractConverter(DbDialect dialect) {
        this.appropriateService = dialect.typeAppropriateService
    }

    TARGET getTarget() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.class.genericSuperclass
        @SuppressWarnings('unchecked')
        Class<TARGET> clazz = genericSuperclass.actualTypeArguments[1] as Class<TARGET>
        return clazz.newInstance()
    }

    TypeAppropriateService getAppropriateService() {
        return appropriateService
    }

    void setAppropriateService(TypeAppropriateService appropriateService) {
        this.appropriateService = appropriateService
    }

    abstract TARGET doConverting(SOURCE source)

    @Override
    TARGET convert(SOURCE source) {
        return doConverting(source)
    }
}
