package com.coretex.orm.core.activeorm.constraints.impl;

import com.coretex.orm.core.activeorm.constraints.ItemConstraint;
import com.coretex.orm.core.activeorm.constraints.exceptions.ItemUniqueFieldConstraintViolationException;
import com.coretex.orm.core.activeorm.services.SearchService;
import com.coretex.orm.core.general.utils.AttributeTypeUtils;
import com.coretex.orm.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class ItemUniqueFieldConstraint implements ItemConstraint {

	private final String SEARCH_QUERY_STUB = "select item.* from \"#%s\" as item where %s";
	private final String SEARCH_QUERY_CONDITION = "item.%s = %s";

	private CoretexContext coretexContext;

	private SearchService searchService;

	@Override
	public void check(GenericItem item) throws ItemUniqueFieldConstraintViolationException {
		var uniqueAttributes = coretexContext.getAllAttributes(item.getMetaType())
				.values()
				.stream()
				.filter(MetaAttributeTypeItem::getUnique)
				.filter(not(MetaAttributeTypeItem::getLocalized))
				.filter(not(AttributeTypeUtils::isRelationAttribute))
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(uniqueAttributes)) {
			List<GenericItem> genericItems = findByUniqueAttributes(uniqueAttributes, item);
			if ((genericItems.size() == 1
					&& (Objects.isNull(item.getUuid()) || !genericItems.get(0).getUuid().equals(item.getUuid())))
					|| genericItems.size() > 1) {
				throw new ItemUniqueFieldConstraintViolationException("Unique field value constraint exception");
			}
		}
	}

	private List<GenericItem> findByUniqueAttributes(List<MetaAttributeTypeItem> uniqueAttributes, GenericItem item) {

		var condition = new StringBuilder();
		for (int i = 0, size = uniqueAttributes.size(); i < size; i++) {
			MetaAttributeTypeItem uniqueAttribute = uniqueAttributes.get(i);
			condition.append(
					String.format(
							SEARCH_QUERY_CONDITION,
							uniqueAttribute.getAttributeName(), ":" + uniqueAttribute.getAttributeName()
					)
			);
			if (i < size - 1) {
				condition.append(" OR ");
			}
		}
		var result = searchService.
				<GenericItem>search(
						String.format(SEARCH_QUERY_STUB, item.getMetaType().getTypeCode(), condition),
						uniqueAttributes
								.stream()
								.collect(
										Collectors.toMap(
												MetaAttributeTypeItem::getAttributeName,
												metaAttributeTypeItem -> item.getAttributeValue(metaAttributeTypeItem.getAttributeName())
										)
								)
				);
		return result.getResult();
	}
}
