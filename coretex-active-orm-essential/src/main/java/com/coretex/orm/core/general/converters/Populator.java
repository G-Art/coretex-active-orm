package com.coretex.orm.core.general.converters;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface Populator<S, T> extends BiConsumer<S, T> {

    void populate(S source, T target);

    @Override
    default void accept(S source, T target) {
        populate(source, target);
    }

    default boolean isSuitable(S source, T target) {
        return true;
    }
}
