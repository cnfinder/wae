package cn.finder.wae.queryer.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 多数据源表格视图同步到主数据源中
 * @author wu hualong
 *
 */
public class SyncDataSourceQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(SyncDataSourceQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into SyncDataSourceQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		final TableQueryResult qr =new TableQueryResult();
		
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		ShowDataConfig showDataConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		
		//主数据源
		long mainDsKey= showTableConfig.getTargetDs();
		
		//主数据源就是当前数据源
		//String sql_selectds = "select * from v_constants_target_ds";
		DataSource mainDs = DSManager.getDataSource(mainDsKey);
		
		Map<Long,DataSource> dsMap = DSManager.getDataSourceMaps();
		
		//删除原表数据
		deleteOldTablesViews(mainDs);
		
		
		if(dsMap!=null && dsMap.size()>0){
			
			
			
			Set<Entry<Long, DataSource>> setEntry = dsMap.entrySet();
			
			Iterator<Entry<Long, DataSource>> ite = setEntry.iterator();
			while(ite.hasNext()){
				
				Entry<Long, DataSource>  entry =ite.next();
				
				long dsKey = entry.getKey();
				DataSource ds = entry.getValue();
				
				//if(dsKey==100)
				try{
				syncTablesViews(mainDs,dsKey,ds);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
				}
			}
			
			
		}
		
		
		//处理mongodb数据源
		Map<Long,MongoTemplate> mongoDsMap=DSManager.getMongoDataSourceMaps();
		if(mongoDsMap!=null && mongoDsMap.size()>0){
			
			
			
			Set<Entry<Long, MongoTemplate>> setEntry = mongoDsMap.entrySet();
			
			Iterator<Entry<Long, MongoTemplate>> ite = setEntry.iterator();
			while(ite.hasNext()){
				
				Entry<Long, MongoTemplate>  entry =ite.next();
				
				long dsKey = entry.getKey();
				MongoTemplate ds = entry.getValue();
				
				//if(dsKey==100)
				try{
				syncMongoTablesViews(mainDs,dsKey,ds);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
				}
			}
			
			
		}
		
		
		//这里一定要设置count的值 ，否则struts序列化会有错误，返回不到前台
		qr.setCount(0L);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
	
	/***
	 * 删除旧的表和视图
	 * @param mainDs
	 */
	private void deleteOldTablesViews(DataSource mainDs){
		String sql ="{call proc_tables_views_delete()}";
		
		getJdbcTemplate().update(sql);
	}
	
	
	/***
	 * 做数据同步操作
	 * 从目标数据库 
	 * @param mainDs
	 * @param sourceDsKey
	 * @param sourceDataSource
	 */
	private void syncTablesViews(DataSource mainDs,final long sourceDsKey,DataSource sourceDataSource){
		
		
		BasicDataSource basicDataSource = (BasicDataSource)sourceDataSource;
		String driverClassName = basicDataSource.getDriverClassName();
		//判断数据库类型  获取表视图数据   
		//但是这可以不用判断 数据库脚本  因为 名称空间和表名 由 v_constants_taget_ds的 value 决定了
		/*
		if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
			
			
		}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
			
		}else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
			
		}*/
		
		///获取数据
		String value = ArchCache.getInstance().getConstantsCache().get(sourceDsKey).getValue();
		
		String[] k_v = value.split(";");
		
		Map<String,String> kvmap = new HashMap<String, String>();
		for(String kv:k_v){
			String[] kv_value = kv.split("=");
			kvmap.put(kv_value[0], kv_value[1]);
		}
		
		
		String tableName = kvmap.get("tableviews");
		
		
		FindTableView findTableView = new FindTableView(tableName);
		findTableView.setDataSource(sourceDataSource);
		final List<Map<String,Object>> tableViewDatas =findTableView.queryTableView();
		
		if(tableViewDatas==null || tableViewDatas.size()<=0){
			return;
		}
		
		//插入到主数据源
		String insertMainDs_sql = "{call proc_tables_views_insert(?,?,?,?,?,?)}";
		
		
		
		
		final int size = tableViewDatas.size();
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setObject(1, sourceDsKey);
				ps.setObject(2, tableViewDatas.get(i).get("table_schema"));
				ps.setObject(3, tableViewDatas.get(i).get("show_table_name"));
				ps.setObject(4, tableViewDatas.get(i).get("table_name"));
				ps.setObject(5, tableViewDatas.get(i).get("table_type"));
				ps.setObject(6, tableViewDatas.get(i).get("long_table_name"));
				
			}
			
			@Override
			public int getBatchSize() {
				return size;
			}
		};
				
			
		 getJdbcTemplate().batchUpdate(insertMainDs_sql, batchSetter);
		 
		
	}
	
	
	/***
	 * 做数据同步操作-Mongo
	 * 从目标数据库 
	 * @param mainDs
	 * @param sourceDsKey
	 * @param sourceDataSource
	 */
	private void syncMongoTablesViews(DataSource mainDs,final long sourceDsKey,MongoTemplate sourceDataSource){
		
		
		
		final String dbname=sourceDataSource.getDb().getName();
		Set<String> collectionNames= sourceDataSource.getCollectionNames();
		
		final Iterator<String> iter=collectionNames.iterator();
		
		if(collectionNames==null || collectionNames.size()<=0){
			return;
		}
		
		//插入到主数据源
		String insertMainDs_sql = "{call proc_tables_views_insert(?,?,?,?,?,?)}";
		
		
		final int size = collectionNames.size();
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				String collection=iter.next();
				
				ps.setObject(1, sourceDsKey);
				
				ps.setObject(2, dbname);
				ps.setObject(3, collection);
				ps.setObject(4, collection);
				ps.setObject(5, "COLLECTION");
				ps.setObject(6, dbname+"("+collection+")");
				
			}
			
			@Override
			public int getBatchSize() {
				return size;
			}
		};
				
			
		 getJdbcTemplate().batchUpdate(insertMainDs_sql, batchSetter);
		 
		
	}
	
	
	
	
	
	class FindTableView extends BaseJdbcDaoSupport{
		
		
		
		private String tableName;
		public FindTableView(String tableName){
			this.tableName = tableName;
			
		}
		
		
		public List<Map<String,Object>> queryTableView(){
			if(!StringUtils.isEmpty(tableName)){
				String sql ="select table_schema,show_table_name,table_name,table_type,long_table_name from  "+tableName;
				
				return getJdbcTemplate().queryForList(sql);
			}
			return null;
		}
		
	}

}
