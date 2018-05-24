package cn.finder.wae.common.db.ds;

import javax.sql.DataSource;

public interface MultiDataSourceDao {

	public void setJdbcDataSource(DataSource dataSource);
}
