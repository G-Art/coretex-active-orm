package com.coretex.orm.core.services.items.context.provider.impl;


import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.AttributeProvider;
import com.coretex.orm.core.services.items.context.provider.strategies.LoadAttributeValueStrategyProvider;
import com.coretex.orm.core.services.items.exceptions.NotFoundAttributeException;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Objects;
import java.util.Set;

import static com.coretex.orm.core.general.utils.AttributeTypeUtils.isCollection;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultAttributeProvider implements AttributeProvider {

	private final Logger LOG = LoggerFactory.getLogger(DefaultAttributeProvider.class);

	private static final String MISSED_ATTRIBUTE_TYPE_ERROR_MSG =
			"To retrieve attribute value attribute definition must be not null. For item: %s with Type %s";
	private static final String MISSED_CTX_ERROR_MSG = "To retrieve attribute value item context must be not null";

	private LoadAttributeValueStrategyProvider loadAttributeValueStrategyProvider;

	private MetaTypeProvider metaTypeProvider;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String attributeName, ItemContext ctx) {
		checkArgument(nonNull(ctx), MISSED_CTX_ERROR_MSG);
		checkArgument(nonNull(attributeName), MISSED_ATTRIBUTE_TYPE_ERROR_MSG, ctx.getUuid(), ctx.getTypeCode());

		if (GenericItem.META_TYPE.equals(attributeName)) {
			return (T) metaTypeProvider.findMetaType(ctx.getTypeCode());
		}

		MetaAttributeTypeItem attribute = metaTypeProvider.findAttribute(ctx.getTypeCode(), attributeName);

		if(Objects.isNull(attribute)){
			LOG.warn("Attribute [{}::{}] is not exist. Please update data base.", ctx.getTypeCode(), attributeName);
			return null;
		}

		if (!ctx.isNew() && nonNull(ctx.getUuid())) {
			return (T) retrieveValue(attribute, attributeName, ctx);
		}

		if (isCollection(attribute)) {
			Class containerType = attribute.getContainerType();
			if (Set.class.isAssignableFrom(containerType)) {
				return (T) Sets.newLinkedHashSet();
			}
			return (T) Lists.newArrayList();
		}
		return null;
	}

	private Object retrieveValue(MetaAttributeTypeItem attribute, String attributeName, ItemContext ctx) {

		if (isNull(attribute)) {
			throw new NotFoundAttributeException(String.format("Attribute '%s' is absent in '%s' type",
					attributeName, ctx.getTypeCode()));
		}
		return loadValue(ctx, attribute);
	}

	private Object loadValue(ItemContext ctx, MetaAttributeTypeItem attribute) {
		return loadAttributeValueStrategyProvider.strategyForAttribute(attribute).load(ctx, attribute);
	}

	public void setMetaTypeProvider(MetaTypeProvider metaTypeProvider) {
		this.metaTypeProvider = metaTypeProvider;
	}

	public void setLoadAttributeValueStrategyProvider(LoadAttributeValueStrategyProvider loadAttributeValueStrategyProvider) {
		this.loadAttributeValueStrategyProvider = loadAttributeValueStrategyProvider;
	}

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}
}
