package com.coretex.orm.core.services.items.context.provider.strategies.impl;

import com.coretex.orm.core.activeorm.factories.RowMapperFactory;
import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.orm.core.services.items.context.provider.strategies.LoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GeneralRegularAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(GeneralRegularAttributeLoadValueStrategy.class);

	private LoadAttributeValueStrategy localizedRegularAttributeLoadValueStrategy;
	private LoadAttributeValueStrategy regularAttributeLoadValueStrategy;

	public GeneralRegularAttributeLoadValueStrategy(CoretexContext coretexContext, DbDialectService dbDialectService) {
		super(coretexContext, dbDialectService);
	}

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		if (attribute.getLocalized()) {
			if(LOG.isDebugEnabled()){
				LOG.debug("Load localized attribute [{}]", attribute.getAttributeName());
			}
			return localizedRegularAttributeLoadValueStrategy.load(ctx, attribute);
		} else {
			if(LOG.isDebugEnabled()){
				LOG.debug("Load attribute [{}]", attribute.getAttributeName());
			}
			return regularAttributeLoadValueStrategy.load(ctx, attribute);
		}
	}

	public void setLocalizedRegularAttributeLoadValueStrategy(LoadAttributeValueStrategy localizedRegularAttributeLoadValueStrategy) {
		this.localizedRegularAttributeLoadValueStrategy = localizedRegularAttributeLoadValueStrategy;
	}

	public void setRegularAttributeLoadValueStrategy(LoadAttributeValueStrategy regularAttributeLoadValueStrategy) {
		this.regularAttributeLoadValueStrategy = regularAttributeLoadValueStrategy;
	}

	@Override
	public ActiveOrmOperationExecutor getOperationExecutor() {
		return null;
	}
}
