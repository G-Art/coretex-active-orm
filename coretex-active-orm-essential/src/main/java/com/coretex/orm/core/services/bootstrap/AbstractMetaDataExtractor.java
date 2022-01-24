package com.coretex.orm.core.services.bootstrap;

import com.coretex.orm.core.services.bootstrap.meta.MetaDataExtractor;
import com.coretex.orm.core.utils.JDBCUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractMetaDataExtractor implements MetaDataExtractor {

	private final DataSource dataSource;

	public AbstractMetaDataExtractor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected List<Map<String, Object>> execute(String sql) {
		try (var con = dataSource.getConnection()) {
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet resultSet = statement
					.executeQuery();
			return JDBCUtils.result(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
