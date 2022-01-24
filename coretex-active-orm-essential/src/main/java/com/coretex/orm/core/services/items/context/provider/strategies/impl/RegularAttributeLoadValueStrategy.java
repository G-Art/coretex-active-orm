package com.coretex.orm.core.services.items.context.provider.strategies.impl;

import com.coretex.orm.core.activeorm.exceptions.SearchException;
import com.coretex.orm.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.orm.core.activeorm.factories.RowMapperFactory;
import com.coretex.orm.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.orm.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.orm.core.activeorm.services.ReactiveSearchResult;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.orm.core.services.items.context.ItemContext;
import com.coretex.orm.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RegularAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(RegularAttributeLoadValueStrategy.class);

	public RegularAttributeLoadValueStrategy(CoretexContext coretexContext, DbDialectService dbDialectService) {
		super(coretexContext, dbDialectService);
	}

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		var loadedAttributes = ctx.loadedAttributes();
		var unloadedAttributes = getCortexContext().getAllAttributes(ctx.getTypeCode())
				.values()
				.stream()
				.filter(AttributeTypeUtils::isRegularTypeAttribute)
				.filter(attr -> !attr.getLocalized())
				.filter(attr -> !loadedAttributes.contains(attr.getAttributeName()))
				.collect(Collectors.toList());
		CoretexReactiveResultSetExtractor<List<Object>> extractor = new CoretexReactiveResultSetExtractor<>(getCortexContext());
		extractor.setMapperFactorySupplier(() -> creteMapperFactory(unloadedAttributes));
		ReactiveSearchResult<List<Object>> searchResult = getOperationExecutor().execute(new SelectItemAttributeOperationSpec(attribute, ctx, getDbDialectService(), extractor).createOperationContext());

		return processResult(searchResult, unloadedAttributes, attribute, ctx);
	}

	private RowMapperFactory creteMapperFactory(List<MetaAttributeTypeItem> unloadedAttributes) {
		return new RowMapperFactory() {
			@Override
			public <T> RowMapper<T> createMapper(Class<T> ignore) {
				return (rs, rowNum) -> {

					List<Object> resultRow = Lists.newArrayList();

					for (int column = 1; column <= unloadedAttributes.size(); column++) {
						var metaAttr = unloadedAttributes.get(column - 1);
						try {
							var result = getCortexContext().getTypeTranslator(((RegularTypeItem) metaAttr.getAttributeType())
									.getRegularClass())
									.read(rs, column);
							resultRow.add(result);
						} catch (Exception e) {
							if (LOG.isDebugEnabled()) {
								LOG.error(String.format("Can't read column [%s] number %s", metaAttr.getAttributeName(), rowNum), e);
							}
							try {
								resultRow.add(JdbcUtils.getResultSetValue(rs, rowNum));
							} catch (SQLException e1) {
								throw new RuntimeException(e1);
							}
						}
					}

					return (T) resultRow;

				};
			}
		};
	}

	private Object processResult(ReactiveSearchResult<List<Object>> searchResultStream, List<MetaAttributeTypeItem> unloadedAttributes, MetaAttributeTypeItem attribute, ItemContext ctx) {
		List<Object> resultRow = null;
		List<List<Object>> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());
		if (CollectionUtils.isEmpty(searchResult)) {
			return null;
		} else {
			if (searchResult.size() > 1) {
				throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
			}
			resultRow = searchResult.iterator().next();

		}

		return enrichContextAndGetResult(ctx, unloadedAttributes, attribute,
				resultRow
		);

	}

	private Object enrichContextAndGetResult(ItemContext ctx, List<MetaAttributeTypeItem> metaAttributeTypeItems, MetaAttributeTypeItem attribute, List<Object> resultRow) {
		Object result = null;
		for (int i = 0; i < metaAttributeTypeItems.size(); i++) {
			var metaAttributeTypeItem = metaAttributeTypeItems.get(i);
			var columnResult = resultRow.get(i);
			if (attribute.getUuid().equals(metaAttributeTypeItem.getUuid())) {
				result = columnResult;
			} else {
				ctx.initValue(metaAttributeTypeItem.getAttributeName(), columnResult);
			}
		}
		return result;
	}

	@Override
	public ActiveOrmOperationExecutor getOperationExecutor() {
		return null;
	}
}
