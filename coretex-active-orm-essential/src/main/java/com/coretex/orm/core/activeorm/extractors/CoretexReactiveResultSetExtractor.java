package com.coretex.orm.core.activeorm.extractors;

import com.coretex.orm.core.activeorm.factories.RowMapperFactory;
import com.coretex.orm.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CoretexReactiveResultSetExtractor<T> implements ResultSetExtractor<Stream<T>> {

	private Logger LOG = LoggerFactory.getLogger(RowMapperFactory.class);

	private SqlRowSetResultSetExtractor sqlRowSetResultSetExtractor;
	private MetaTypeProvider metaTypeProvider;
	private Supplier<RowMapperFactory> mapperFactorySupplier;

	public CoretexReactiveResultSetExtractor(MetaTypeProvider context) {
		this.sqlRowSetResultSetExtractor = new SqlRowSetResultSetExtractor();
		this.metaTypeProvider = context;
	}

	public CoretexReactiveResultSetExtractor(MetaTypeProvider context, RowMapperFactory defaultRowMapperFactory) {
		this(context);
		mapperFactorySupplier = () -> defaultRowMapperFactory;
	}

	@Override
	public Stream<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
		ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet) sqlRowSetResultSetExtractor.extractData(rs);
		final boolean parallel = false;
		if (Objects.isNull(rowSet) || rowSet.getMetaData().getColumnCount()==0) {
			return Stream.empty();
		}
		try {
			if (hasMetaTypeColumn(rowSet)) {
				return extractData(rowSet, getRowMapper(GenericItem.class), parallel);
			}
			return extractData(rowSet, getRowMapper(Map.class), parallel);
		} catch (Exception e) {
			return extractData(rowSet, getRowMapper(Map.class), parallel);
		}
	}

	private boolean hasMetaTypeColumn(ResultSetWrappingSqlRowSet rowSet) {
		String mTypeC = metaTypeProvider.findAttribute(GenericItem.ITEM_TYPE, MetaTypeItem.META_TYPE).getColumnName();
		return Arrays.stream(rowSet.getMetaData().getColumnNames()).anyMatch(s -> s.equalsIgnoreCase(mTypeC));
	}

	public void setMapperFactorySupplier(Supplier<RowMapperFactory> mapperFactorySupplier) {
		this.mapperFactorySupplier = mapperFactorySupplier;
	}

	@SuppressWarnings("unchecked")
	private RowMapper<T> getRowMapper(Class tClass) {
		return mapperFactorySupplier.get().createMapper(tClass);
	}

	private Stream<T> extractData(ResultSetWrappingSqlRowSet rs, RowMapper<T> rowMapper, boolean parallel)  {

		Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(new Iterator<>() {

			@Override
			public boolean hasNext() {
				return !rs.isLast();
			}

			@Override
			public T next() {
				if(!rs.next()) {
					throw new NoSuchElementException();
				}
				try {
					return rowMapper.mapRow(rs.getResultSet(), rs.getRow());
				} catch (SQLException e) {
					throw new RuntimeException("Row mapping exception", e);
				}
			}
		}, Spliterator.IMMUTABLE);

		return StreamSupport.stream(spliterator, parallel);
	}
}
