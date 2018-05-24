package cn.finder.wae.common.db.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public  class DefaultRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		return DefaultContextHolder.getDataSourceType();
	}

/*	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}*/

}
