package com.coretex.orm.core.general.converters;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanNameAware;

import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class GeneralConverter<S, T> implements Converter<S, T>, Populator<S, T>, BeanNameAware {

    private Supplier<T> supplier;
    private List<Populator<S, T>> populators;
    private String convertBeanName;

    @Override
    public void populate(S source, T target) {
        if (CollectionUtils.isNotEmpty(populators)) {
            populators.forEach(populator -> {
                if (populator.isSuitable(source, target)) {
                    populator.populate(source, target);
                }
            });
        }
    }

    @Override
    public T convert(S source) {
        T target = createTarget(source);
        populate(source, target);
        return target;
    }

    protected T createTarget(S source) {
        return nonNull(supplier) ? supplier.get() : getTargetClass().map(BeanUtils::instantiateClass).orElseThrow(() ->
                new BeanCreationException("Cant determine target class for " + convertBeanName));
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public List<Populator<S, T>> getPopulators() {
        return populators;
    }

    public void setPopulators(List<Populator<S, T>> populators) {
        this.populators = populators;
    }

    @Override
    public void setBeanName(String name) {
        convertBeanName = name;
    }
}
