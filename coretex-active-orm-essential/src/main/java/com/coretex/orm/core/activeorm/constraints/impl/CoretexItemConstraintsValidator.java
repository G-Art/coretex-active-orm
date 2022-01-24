package com.coretex.orm.core.activeorm.constraints.impl;

import com.coretex.orm.core.activeorm.constraints.ItemConstraint;
import com.coretex.orm.core.activeorm.constraints.ItemConstraintsValidator;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class CoretexItemConstraintsValidator implements ItemConstraintsValidator {

	private List<ItemConstraint> constraints;

	public CoretexItemConstraintsValidator(List<ItemConstraint> constraints) {
		this.constraints = constraints;
	}

	@Override
	public void validate(GenericItem item) {
		if (CollectionUtils.isNotEmpty(constraints)) {
			constraints.forEach(itemConstraint -> itemConstraint.check(item));
		}
	}
}
