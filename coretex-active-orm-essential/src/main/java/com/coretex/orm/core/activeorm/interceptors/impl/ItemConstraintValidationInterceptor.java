package com.coretex.orm.core.activeorm.interceptors.impl;

import com.coretex.orm.core.activeorm.constraints.ItemConstraintsValidator;
import com.coretex.orm.core.activeorm.interceptors.Interceptor;
import com.coretex.orm.core.activeorm.interceptors.OnSavePrepareInterceptor;
import com.coretex.items.core.GenericItem;


@Interceptor(
		items = GenericItem.class
)
public class ItemConstraintValidationInterceptor<I extends GenericItem>
		implements OnSavePrepareInterceptor<I> {

	private ItemConstraintsValidator itemConstraintsValidator;

	public ItemConstraintValidationInterceptor(ItemConstraintsValidator itemConstraintsValidator) {
		this.itemConstraintsValidator = itemConstraintsValidator;
	}

	@Override
	public void onSavePrepareAction(I item) {
		itemConstraintsValidator.validate(item);
	}

}
