package com.coretex.orm.core.activeorm.services;

import com.coretex.orm.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.orm.core.activeorm.query.specs.select.PageableSelectOperationSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PageableSearchResult<T> extends SearchResult<T> {

	private Logger LOG = LoggerFactory.getLogger(PageableSearchResult.class);

	private transient SelectOperationConfigContext selectOperationConfigContext;
	private transient PageableSelectOperationSpec operationSpec;

	private Long totalCount;
	private Integer totalPages;

	public PageableSearchResult(SelectOperationConfigContext selectOperationConfigContext, Supplier<Stream<T>> resultSupplier) {
		super(resultSupplier);
		this.selectOperationConfigContext = selectOperationConfigContext;
		if(selectOperationConfigContext.isPageable()){
			operationSpec = (PageableSelectOperationSpec) selectOperationConfigContext.getOperationSpec();
			this.totalCount = selectOperationConfigContext.getTotalCount();
			this.totalPages = (int) Math.ceil(((double) totalCount)
					/ operationSpec.getCount());
		}

	}

	public PageableSelectOperationSpec nextPage(){
		if (Objects.nonNull(operationSpec.getCount()) && Objects.nonNull(operationSpec.getPage())){
			operationSpec.setPage(operationSpec.getPage()+1);
			return operationSpec;
		}else {
			throw new IllegalStateException("Query [" + operationSpec.getQuery() +"] is not pageable");
		}
	}

	public Long getPage(){
		return operationSpec.getPage();
	}

	public String getTransformedQuery() {
		return operationSpec.getTransformedQuery();
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public Integer getTotalPages() {
		return totalPages;
	}
}
