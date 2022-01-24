package com.coretex.orm.core.general.converters;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface Converter<S, T> extends Function<S, T>, org.springframework.core.convert.converter.Converter<S, T>{

    default Optional<Class<S>> getSourceClass() {
        return Optional.empty();
    }

    default Optional<Class<T>> getTargetClass() {
        return Optional.empty();
    }

    default Object convertRaw(Object value) {
        return getSourceClass()
                .filter(sClass -> sClass.isInstance(value))
                .map(sClass -> sClass.cast(value))
                .map(this::convert)
                .orElse((T) value);
    }

    @Override
    default T apply(S source) {
        return convert(source);
    }

    T convert(S source);
}
