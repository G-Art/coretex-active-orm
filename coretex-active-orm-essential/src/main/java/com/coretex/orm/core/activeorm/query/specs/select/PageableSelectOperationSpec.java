package com.coretex.orm.core.activeorm.query.specs.select;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;

import java.util.Map;
import java.util.Objects;

public class PageableSelectOperationSpec extends SelectOperationSpec {

	private Long count = 100L;
	private Long page = 0L;


	public PageableSelectOperationSpec(String query) {
		super(query);
	}

	public PageableSelectOperationSpec(String query, Map parameters) {
		super(query, parameters);
	}


	public String getOriginalQuery(){
		return super.getQuery();
	}

	@Override
	public String getQuery() {
		if(Objects.nonNull(count) || Objects.nonNull(page)){
			StringBuilder queryBuilder = new StringBuilder(super.getQuery());
			if(Objects.nonNull(count)){
				queryBuilder.append(" LIMIT ").append(count).append(" ");
			}

			if(Objects.nonNull(page)){
				queryBuilder.append(" OFFSET ").append(count * page).append(" ");
			}

			return queryBuilder.toString();
		}
		return super.getQuery();
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	@Override
	public SelectOperationConfigContext createOperationContext() {
		return new SelectOperationConfigContext(this, true);
	}
}
