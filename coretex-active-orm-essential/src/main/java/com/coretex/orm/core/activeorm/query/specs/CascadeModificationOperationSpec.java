package com.coretex.orm.core.activeorm.query.specs;

import com.coretex.orm.meta.AbstractGenericItem;

public interface CascadeModificationOperationSpec {
	boolean existInCascade(AbstractGenericItem item);
}
