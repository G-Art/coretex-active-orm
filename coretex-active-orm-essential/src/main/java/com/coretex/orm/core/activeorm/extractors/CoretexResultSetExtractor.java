package com.coretex.orm.core.activeorm.extractors;

import com.coretex.orm.core.activeorm.factories.RowMapperFactory;
import com.coretex.orm.core.activeorm.query.operations.SelectOperation;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CoretexResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

	private Logger LOG = LoggerFactory.getLogger(RowMapperFactory.class);

	private SqlRowSetResultSetExtractor sqlRowSetResultSetExtractor;
	private SelectOperation selectOperation;
	private MetaTypeProvider metaTypeProvider;
	private Supplier<RowMapperFactory> mapperFactorySupplier;

	public CoretexResultSetExtractor(SelectOperation selectOperation, MetaTypeProvider context) {
		this.sqlRowSetResultSetExtractor = new SqlRowSetResultSetExtractor();
		this.metaTypeProvider = context;
		this.selectOperation = selectOperation;
	}

	@Override
	public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
		ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet) sqlRowSetResultSetExtractor.extractData(rs);
		try {
			if (hasMetaTypeColumn(rowSet)) {
				return extractData(rowSet, getRowMapper(GenericItem.class));
			}
			return extractData(rowSet, getRowMapper(Map.class));
		} catch (Exception e) {
			return extractData(rowSet, getRowMapper(Map.class));
		}
	}

	private boolean hasMetaTypeColumn(ResultSetWrappingSqlRowSet rowSet) {
		return ArrayUtils.contains(rowSet.getMetaData().getColumnNames(), metaTypeProvider.findAttribute(GenericItem.ITEM_TYPE, MetaTypeItem.META_TYPE).getColumnName().toLowerCase());
	}

	public void setMapperFactorySupplier(Supplier<RowMapperFactory> mapperFactorySupplier) {
		this.mapperFactorySupplier = mapperFactorySupplier;
	}

	@SuppressWarnings("unchecked")
	private RowMapper<T> getRowMapper(Class tClass) {
		return mapperFactorySupplier.get().createMapper(tClass);
	}

	private List<T> extractData(ResultSetWrappingSqlRowSet rs, RowMapper<T> rowMapper) throws SQLException {
		List<T> results = new ArrayList<>();
		while (rs.next()) {
			results.add(rowMapper.mapRow(rs.getResultSet(), rs.getRow()));
		}
//		if(results.size() == 1){
//			T singleRow = results.get(0);
//			if(singleRow instanceof Map && ((Map) singleRow).size() == 1){
//				var value = ((Map) singleRow).values().iterator().next();
//				return Objects.isNull(value) ? null : (List<T>) List.of(value);
//			}
//		}
		return results;
	}
}
