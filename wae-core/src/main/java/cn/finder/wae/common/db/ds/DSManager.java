package cn.finder.wae.common.db.ds;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.mongodb.core.MongoTemplate;

public class DSManager {
	
	//===============jdbc数据源===================//
	private  static Map<Long,DataSource> dataSourceMap=null;
	
	static{
		dataSourceMap =new HashMap<Long, DataSource>();
	}

	
	public  Map<Long, DataSource> getDataSourceMap() {
		return dataSourceMap;
	}
	
	public  static Map<Long, DataSource> getDataSourceMaps() {
		return dataSourceMap;
	}
	

	public static void setDataSourceMap(Map<Long, DataSource> pdataSourceMap) {
		dataSourceMap = pdataSourceMap;
	}
	
	public static DataSource getDataSource(Long sourceId)
	{
		return dataSourceMap.get(sourceId);
	}
	
	
	
	
	//==============mongodb数据源===================//
	
	private static Map<Long,MongoTemplate> mongoDataSourceMap=null;// mongo数据库
	
	static{
		mongoDataSourceMap=new HashMap<Long, MongoTemplate>();
	}
	
	
	public  Map<Long, MongoTemplate> getMongoDataSourceMap() {
		return mongoDataSourceMap;
	}
	
	public  static Map<Long, MongoTemplate> getMongoDataSourceMaps() {
		return mongoDataSourceMap;
	}
	

	public static void setMongoDataSourceMap(Map<Long, MongoTemplate> pdataSourceMap) {
		mongoDataSourceMap = pdataSourceMap;
	}
	
	public static MongoTemplate getMongoDataSource(Long sourceId)
	{
		return mongoDataSourceMap.get(sourceId);
	}
	
	/*
	
	
	public static void setTransactionManagerMap(Map<Long, TransactionManager> pTransactionManagerMap) {
		transactionManagerMap = pTransactionManagerMap;
	}
	
	public static TransactionManager getTransactionManager(Long sourceId)
	{
		return transactionManagerMap.get(sourceId);
	}*/
	
	
}
