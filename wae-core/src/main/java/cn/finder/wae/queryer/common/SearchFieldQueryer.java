package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.sys.dao.SysDao;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * @author wu hualong
 *
 */
public class SearchFieldQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(SearchFieldQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into SearchFieldQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> forwardParams = ((MapParaQueryConditionDto<String, Object>)condition).getForwardParams();
		Map<String,Object> data = null;
		
		String parastr = ((String[])forwardParams.get("data"))[0];
		
		data=JsonUtil.getMap4Json(parastr);
		
		
		//数据源
		long target_ds = Long.valueOf(String.valueOf(data.get("target_ds")));
		
		//表类型
		int show_type = Integer.valueOf(String.valueOf(data.get("show_type")));
		
		//表名、视图名等  存储过程  函数     
		// mongo集合名称  
		String showtable_name =String.valueOf(data.get("showtable_name"));
		
		
		
		
		
		
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		//List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getShowDataConfigs(showtable_name);
		
		//ServletContext ctx = (ServletContext)forwardParams.get("servletContext");
		SysDao sysDao=AppApplicationContextUtil.getContext().getBean("sysDao", SysDao.class);
		
		
		
		List<ShowDataConfig> showDataConfigs = sysDao.findShowDataByShowTableName(showtable_name);
		
		final TableQueryResult qr =new TableQueryResult();
		
		List<Map<String, Object>> resultList =new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> db_columns=new ArrayList<Map<String,Object>>();
		//暂时处理表和视图
		if(show_type == ConstantsCache.TableType.TABLETYPE_TABLE || show_type == ConstantsCache.TableType.TABLETYPE_VIEW)
		{
			
			DataSource ds = DSManager.getDataSource(target_ds);
			BasicDataSource basicDataSource = (BasicDataSource)ds;
			
			this.setJDBCDataSource(basicDataSource);
			
			// 获取 表或视图中的列
			//数据库 中所有的字段
			List<String> dbField =new ArrayList<String>();
			
			
			String sql_columns = "select * from v_find_all_column where table_name = ?";
			
			db_columns = getJdbcTemplate().queryForList(sql_columns, new Object[]{showtable_name});
			
			
			
			
		}else if(show_type == ConstantsCache.TableType.TABLETYPE_MONGO_COLLECTION){
			// mongodb 没有预定义的列 , 但是可以 搜索最长的列 作为 列
			MongoTemplate mongoTemplate=DSManager.getMongoDataSourceMaps().get(target_ds);
			
			
			
			Document item=null;
			
			try{
				item=mongoTemplate.getCollection(showtable_name).find().limit(1).first();
			}
			catch(Throwable e){
				//没有列
				item=null;
			}
			
			
			Set<Entry<String,Object>> entrySet= item.entrySet();
			
			Iterator<Entry<String,Object>> itr= entrySet.iterator();
			while(itr.hasNext()){
				Entry<String,Object> entry= itr.next();
				String field_name=entry.getKey();
				Object v=entry.getValue();
				String data_type="varchar";
				if(v instanceof Long){
					data_type="Number";
				}
				else if(v instanceof Integer){
					data_type="int";
				}
				else if(v instanceof Float){
					data_type="float";
				}
				else if(v instanceof String){
					data_type="varchar";
				}
				
				Map<String,Object> item_field=new HashMap<String,Object>();
				item_field.put("table_schema", mongoTemplate.getDb().getName());
				item_field.put("table_name", showtable_name);
				item_field.put("column_name", field_name);
				item_field.put("data_type", data_type);
				item_field.put("is_user_defined", 0);
				db_columns.add(item_field);
			}
			
			
			
		}
		
		
		for(Map<String,Object> colItem:db_columns){
			
			String colName = colItem.get("column_name").toString();
			
			colItem.put("chk_value", "0");
			colItem.put("is_exists","0");
			for(ShowDataConfig sdc:showDataConfigs){
				
				colItem.put("showdata_config", new ShowDataConfig());
				
				if(colName.equalsIgnoreCase(sdc.getFieldName())){
					if(colItem.containsKey("chk_value")){
						colItem.remove("chk_value");
						colItem.put("chk_value", "1");
					}
					colItem.remove("showdata_config");
					colItem.put("showdata_config", sdc);
					
					colItem.remove("is_exists");
					colItem.put("is_exists","1");
					break;
				}
				
				
			}
			
		}
		resultList = db_columns;
	
	    
	    long cnt = resultList.size();
	    
	    
	    logger.debug("== 总记录数:"+cnt);
	  
	   
	  
	   
	   
	    qr.setResultList(resultList);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(cnt);
		
		
		
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
