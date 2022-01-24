package com.coretex.orm.meta;

import com.coretex.orm.core.AbstractCoretexContextProvider;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.factory.ItemContextFactory;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 12-02-2016
 */
public abstract class AbstractGenericItem implements Serializable, Cloneable {

    public static final String UUID = "uuid";

    private static final String ITEM_COX_FACTORY_UNAVAILABLE_ERROR_MSG =
            "Could't find item context factory to generate item context";

    private ItemContext ctx;

    private transient ItemContextFactory itemContextFactory;
    private final transient AbstractCoretexContextProvider abstractCoretexContextProvider = AbstractCoretexContextProvider.getInstance();

    public AbstractGenericItem() {
        itemContextFactory = abstractCoretexContextProvider.getItemContextFactory();
        Preconditions.checkNotNull(this.itemContextFactory, ITEM_COX_FACTORY_UNAVAILABLE_ERROR_MSG);
        create(getFactory().create(this.getClass()));
    }

    public AbstractGenericItem(ItemContext ctx) {
        create(ctx);
    }

    private void create(ItemContext ctx) {
        this.ctx = ctx;
        if (!ctx.isSystemType()) {
            var operationInterceptorService = abstractCoretexContextProvider.getItemOperationInterceptorService();
            if (ctx.isNew()) {
                operationInterceptorService.onCreate(this);
            } else {
                operationInterceptorService.onLoad(this);
            }
        }

    }

    public UUID getUuid() {
        return getItemContext().getUuid();
    }

    public void setUuid(UUID uuid) {
        getItemContext().setUuid(uuid);
    }

    public ItemContext getItemContext() {
        return ctx;
    }

    public final ItemContextFactory getFactory() {
        return this.itemContextFactory;
    }

    public void setAttributeValue(String name, Object value) {
        getItemContext().setValue(name, value);
    }

    public Object getAttributeValue(String name) {
        return getItemContext().getValue(name);
    }

    public void setAttributeValue(String name, Object value, Locale locale) {
        getItemContext().setLocalizedValue(name, value, locale);
    }

    public Object getAttributeValue(String name, Locale locale) {
        return getItemContext().getLocalizedValue(name, locale);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (Objects.nonNull(getUuid()) && Objects.nonNull(((AbstractGenericItem) o).getUuid()))
            return Objects.equals(getUuid(), ((AbstractGenericItem) o).getUuid());

        return hashCode() == o.hashCode();

    }

    @Override
    public int hashCode() {
        return Objects.nonNull(getUuid()) ? Objects.hashCode(getUuid()) : super.hashCode();
    }

    @Override
    public AbstractGenericItem clone() {
        try {
            var clone = (AbstractGenericItem)super.clone();
            clone.ctx = this.ctx.clone();
            clone.itemContextFactory = this.itemContextFactory;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
