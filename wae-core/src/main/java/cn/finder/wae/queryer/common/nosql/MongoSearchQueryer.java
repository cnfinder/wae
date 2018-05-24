package cn.finder.wae.queryer.common.nosql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import cn.finder.httpcommons.utils.JsonUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;


/***
 * 
 * 表视图 标识==》集合collection name
 * 查找的条件按照sql的写法 填写  id=5 and c=6   空格分隔 然后一个个遍历 
 * 
 * @author wu hualong
 *
 */
public class MongoSearchQueryer extends  MongoDBQueryer{

	
	private final static Logger logger = Logger.getLogger(MongoSearchQueryer.class);
	private Document fields;
	private Document wheres;
	private Document sort;
	
	private int index = 0;
	private Object[] params;
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId, QueryCondition<Object[]> condition) {

		logger.debug("=====go into MongoSearchQueryer======");

		logger.debug("==showTableConfigId:" + showTableConfigId);
		// 从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		if (condition.getPageSize() == 0) {
			// 默认为数据库设置大小
			condition.setPageSize(showTableConfig.getPageSize() == 0 ? 10 : showTableConfig.getPageSize());
		}
		if (condition != null) {
			logger.debug("==Sql:" + condition.getSql());
			logger.debug("==WhereCluster:" + condition.getWhereCluster());
			logger.debug("==WherepParameterValues:" + condition.getWherepParameterValues());
			logger.debug("==pageIndex:" + condition.getPageIndex());
			logger.debug("==pageSize:" + condition.getPageSize());
		}

		final TableQueryResult qr = new TableQueryResult();
		String collectionName = showTableConfig.getShowTableName();// 集合名称

		fields = new Document();
		wheres = new Document();
		sort = new Document();

		final List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		fields.put("_id",false);
		for (ShowDataConfig df : showDataConfigs) {
			fields.put(df.getFieldName(), df.getFieldNameAlias());
			//fields.put(df.getFieldNameAlias(),"$"+df.getFieldName());
		}

		if (!StringUtils.isEmpty(condition.getWhereCluster())) {
			// 遍历后设置查询条件
			setWhereAndSort(condition.getWhereCluster());
		}
		if (!StringUtils.isEmpty(showTableConfig.getSqlConfig())) {
			// 遍历后设置查询条件
			setWhereAndSort(showTableConfig.getSqlConfig());
		}
		int skip = (condition.getPageIndex() - 1) * condition.getPageSize();
		int limit = condition.getPageSize();
		 List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		 /* DBCursor cursor =null;
		 DBCursor cursor = getMongoTemplate().getCollection(collectionName).find(wheres, fields).sort(sort).skip(skip).limit(limit);
		
		for (DBObject db : cursor.toArray()) {
			dataList.add(db.toMap());
		}*/
		 
		 
		 
		 FindIterable<Document> itr = getMongoTemplate().getCollection(collectionName).find(wheres,Document.class).projection(fields).sort(sort).skip(skip).limit(limit);
		
		 MongoCursor<Document> cursor = itr.iterator();
         while (cursor.hasNext()) {
        	 Document item = cursor.next();
             dataList.add(item);
         }
		 
		 
		long cnt = getMongoTemplate().getCollection(collectionName).count(wheres);

		logger.debug("== 总记录数:" + cnt);

		qr.setResultList(dataList);
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(cnt);

		qr.setShowTableConfig(showTableConfig);
		return qr;
	}
	
	private Object setValue(String value) {
		if (value.equals("?")) {
			Object obj;
			try {
				obj = new Integer(Integer.parseInt(params[index++].toString()));
			} catch (Exception e) {
				obj = params[index++];
			}
			return obj;
		} else {
			if (value.indexOf("'") != -1) {
				return value.split("'")[1];
			} else {
				return Integer.parseInt(value);
			}
		}
	}

	private void setWhereAndSort(String whereCluster) {
		String[] strs = whereCluster.trim().split("\\s+");
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].equals("=")) {
				wheres.put(strs[i - 1], setValue(strs[i + 1]));
			} else if (strs[i].equals(">")) {
				wheres.put(strs[i - 1], new Document("&gt", setValue(strs[i + 1])));
			} else if (strs[i].equals("<")) {
				wheres.put(strs[i - 1], new Document("&lt", setValue(strs[i + 1])));
			} else if (strs[i].equals(">=")) {
				wheres.put(strs[i - 1], new Document("&gte", setValue(strs[i + 1])));
			} else if (strs[i].equals("<=")) {
				wheres.put(strs[i - 1], new Document("&lte", setValue(strs[i + 1])));
			} else if (strs[i].equals("!=")) {
				wheres.put(strs[i - 1], new Document("&ne", setValue(strs[i + 1])));
			} else if (strs[i].equals("is")) {
				if (strs[i + 1].equals("null")) {
					wheres.put(strs[i - 1], null);
				} else if (strs[i + 1].equals("not") && strs[i + 2].equals("null")) {
					wheres.put(strs[i - 1], new Document("$ne", null));
				}
			} else if (strs[i].equals("order") && strs[i + 1].equals("by")) {
				// order by a,b,c desc
				String[] orders = strs[i + 2].split(",");// a,b,c
				int aod = 1;
				try {
					aod = strs[i + 3].equalsIgnoreCase("desc") ? -1 : 1;// desc/asc/null
				} catch (Exception e) {
					// 数组越界
				}
				for (int j = 0; i < orders.length; i++) {
					sort.put(setValue(strs[j]).toString(), aod);
				}
			}
		}
	}
	
//	@Override
//	public TableQueryResult queryTableQueryResult(long showTableConfigId,
//			QueryCondition<Object[]> condition) {
//		
//		logger.debug("=====go into MongoDBCommonQueryer======");
//		
//		logger.debug("==showTableConfigId:"+showTableConfigId);
//		//从缓存中获取 ShowTableConfig 配置
//		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
//		if(condition.getPageSize()==0){
//	    	//默认为数据库设置大小
//	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
//	    }
//		if(condition!=null)
//		{
//			logger.debug("==Sql:"+condition.getSql());
//			logger.debug("==WhereCluster:"+condition.getWhereCluster());
//			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
//			logger.debug("==pageIndex:"+condition.getPageIndex());
//			logger.debug("==pageSize:"+condition.getPageSize());
//		}
//		
//		
//		final TableQueryResult qr =new TableQueryResult();
//		
//		String collectionName=showTableConfig.getShowTableName();//集合名称
//		Query query=new Query();
//		
//		//设置分页
//		query.skip((condition.getPageIndex()-1)*condition.getPageSize());
//		query.limit(condition.getPageSize());
//		
//		
//		Criteria c=new Criteria("name").in("dragon");
//		
//		query.addCriteria(c);
//		
//		
//		//执行查询
//		List<BasicDBObject> dataList=getMongoTemplate().find(query, BasicDBObject.class,collectionName);
//		
//	    CommandResult cr=getMongoTemplate().executeCommand("{}");
//
//	    
//	    
//	    
//	    List<DBObject> person_results = getMongoTemplate().execute("", new CollectionCallback<List<DBObject>>() {
//
//			@Override
//			public List<DBObject> doInCollection(DBCollection collection)
//					throws MongoException, DataAccessException {
//				DBCursor dbCursor=collection.find(); 
//		        return dbCursor.toArray(); 
//			}
//		});
//	    
//	    
//	    
//	    
//		for(int i=0;i<dataList.size();i++){
//			BasicDBObject bdbo=dataList.get(i);
//			
//			Set<Entry<String,Object>> entrySet= bdbo.entrySet();
//			
//			
//			Iterator<Entry<String,Object>> itr= entrySet.iterator();
//			
//			
//			
//		}
//		
//		
//		
//	    final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
//	    
//	    StringBuffer sql_sb =new StringBuffer();
//	    
//	    StringBuffer sql_cnt_sb = new StringBuffer();
//	    
//	    
//	    // "id 编号,name 名称"
//	    StringBuffer fields_sb =new StringBuffer();
//	    
//	    // where 后面的条件
//	    StringBuffer where_sb =new StringBuffer();
//	    
//	    
//	   final  List<String> filedNames=new ArrayList<String>();
//	   
//	    
//	    for(ShowDataConfig df:showDataConfigs)
//	    {
//	    	filedNames.add(df.getFieldName()+" '"+df.getFieldNameAlias()+"'");
//	    }
//	    
//	    fields_sb.append(StringUtils.join(filedNames, ','));
//	    
//	    
//	    if(!StringUtils.isEmpty(condition.getWhereCluster()))
//	    {
//	    	 where_sb.append(condition.getWhereCluster());
//	    }
//	    
//	    if(StringUtils.isEmpty(condition.getWhereCluster()))
//	    {
//	    	 where_sb.append(showTableConfig.getSqlConfig());
//	    }
//	    else{
//	    	if(!StringUtils.isEmpty(showTableConfig.getSqlConfig())){
//	    		
//	    		if(showTableConfig.getSqlConfig().toLowerCase().startsWith("order")){
//	    			where_sb.append("  "+showTableConfig.getSqlConfig());
//	    		}
//	    		else
//	    			where_sb.append(" AND "+showTableConfig.getSqlConfig());
//	    	}
//	    		
//	    }
//	    
//	    //sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(showTableConfig.getShowTableName()).append(" alias ").append(" with (nolock) ").append(" where 1=1 ");
//	    
//	    sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(showTableConfig.getShowTableName()).append(" alias ").append("  ").append(" where 1=1 ");
//	    
//	    sql_cnt_sb.append("select ").append(" count(*) cnt ").append(" from ").append(showTableConfig.getShowTableName()).append(" alias ").append("  ").append(" where 1=1 ");
//	    //oracle
//	    //sql_sb.append(" and ").append(" (rownum >").append((condition.getPageIndex()-1)*condition.getPageSize()).append(" and ").append(" rownum <").append(condition.getPageSize()+") ");
//	    
//	    
//	  
//	    
//	    if(!StringUtils.isEmpty(where_sb.toString())){
//	    	if(where_sb.toString().trim().toLowerCase().startsWith("order by"))
//	    	{
//	    		sql_sb.append(" ").append(where_sb.toString());
//	    		//sql_cnt_sb.append(" ").append(where_sb.toString()); //统计数量不需要排序
//	    		
//	    		
//	    	 
//	    	}else{
//	    		sql_sb.append("and ").append(where_sb.toString());
//	    		
//	    		String cnt_where =where_sb.toString();
//	    	
//	    		//需要去掉order by
//	    		int order_idx=cnt_where.toLowerCase().indexOf("order ");
//	    		if(order_idx!=-1)
//	    		{
//		    		cnt_where= cnt_where.substring(0, order_idx);
//		    		
//		    		sql_cnt_sb.append("and ").append(cnt_where);
//	    		}
//	    		else{
//	    			sql_cnt_sb.append("and ").append(cnt_where);
//	    		}
//	    	}
//	    }
//	    
//	    
//	    //处理前台传过来的排序
//	    if(condition!=null && condition.getOrderBy()!=null)
//	    {
//		    Map<String,String> sortMap =condition.getOrderBy();
//			if(sortMap.size()>0)
//			{
//				int pos = sql_sb.toString().trim().indexOf("order by");
//					//如果有order by 应该去除之前的排序
//				if(pos!=-1){
//					
//					
//					String newWhereSql = sql_sb.substring(0, pos);
//					sql_sb.delete(0, sql_sb.length());
//					sql_sb.append(newWhereSql);
//				}
//					//
//				Set<Entry<String, String>> sets = sortMap.entrySet();
//				
//				Iterator<Entry<String, String>>  ite = sets.iterator();
//				
//				sql_sb.append(" ").append(" order by ");
//				StringBuffer sb_order =new StringBuffer();
//				
//				while(ite.hasNext())
//				{
//					Entry<String, String> entry = ite.next();
//					String k=entry.getKey();
//					String v =entry.getValue();
//					
//					if(!StringUtils.isEmpty(k) && !StringUtils.isEmpty(v))
//					{
//						sb_order.append(" ,");
//						sb_order.append(k).append(" ").append(v);
//					}
//				}
//				sb_order.delete(0, 2);
//				
//				sql_sb.append(sb_order);
//			}
//	    }
//		
//		
//		
//	    
//	    
//	  //mysql 
//	    //sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
//	    
//	    sql_sb =new StringBuffer(wrapperPagerSql(sql_sb.toString(), condition.getPageIndex(), condition.getPageSize()));
//	    
//	    final Object[] params = condition.getWherepParameterValues();
//	   
//	    logger.info("== 查询数据SQL："+sql_sb.toString());
//	   
//	    final long targetDs = showTableConfig.getTargetDs();
//	    List<Map<String, Object>>  dataList=getJdbcTemplate().query(sql_sb.toString(),params,new RowMapper<Map<String,Object>>(){
//
//			@Override
//			public Map<String, Object> mapRow(ResultSet rs, int index)
//					throws SQLException {
//				
//				Map<String,Object> item = new HashMap<String, Object>();
//				
//				/*
//				for(String field:filedNames)
//				{
//					item.put(field, rs.getObject(field));
//				}*/
//				
//				
//				
//				for(int i=0;i<showDataConfigs.size();i++)
//				{
//					ShowDataConfig itemDC=showDataConfigs.get(i);
//					
//					if(ConstantsCache.ControlType.CONTROLTYPE_POSITION_FLAG ==itemDC.getShowType()){
//						//占位符控件，不加载数据
//						item.put(itemDC.getFieldNameAlias(),"");
//						
//					}
//						
//					
//					else if(StringUtils.isEmpty(itemDC.getParentTableName()))
//					{
//						//如果父表 没有填写，那么直接把值放入
//						//item.put(itemDC.getFieldName(), rs.getObject(itemDC.getFieldNameAlias()));
//						
//						/*if(rs.getObject(itemDC.getFieldNameAlias()) instanceof java.util.Date){
//							item.put(itemDC.getFieldNameAlias(), Common.formatDate((Timestamp)rs.getObject(itemDC.getFieldNameAlias())));
//						}
//						else*/
//							item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
//					}
//					else
//					{
//						
//						//加载 父表对应数据
//						
//						//这里就先做一级 数据显示
//						//if(itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_COMBOX)
//						if(ConstantsCache.isLoadComboxData(itemDC.getShowType()))
//						{
//							//处理组合框
//							//查找父所有数据
//							Map<String,Object> p_value_map=getJdbcTemplate().queryForMap(itemDC.getParentSelectSql());
//							
//							//item.put(itemDC.getFieldName(), p_value_map);
//							item.put(itemDC.getFieldNameAlias(), p_value_map);
//						}
//						else{
//							/*StringBuffer sql_parent = new StringBuffer();
//							sql_parent.append("select "+itemDC.getParentShowField() +" from "+itemDC.getParentTableName())
//								.append(" where ").append(itemDC.getParentTableKey()).append("=").append(rs.getObject(itemDC.getFieldName()));
//							
//							String parent_value=getJdbcTemplate().queryForObject(sql_parent.toString(), String.class);
//							*/
//							/*
//							Map<String,Object> p_value_map=getJdbcTemplate().queryFor(itemDC.getParentSelectSql(), new Object[]{rs.getObject(itemDC.getFieldName())}, new RowMapper<Map<String,Object>>(){
//
//								@Override
//								public Map<String, Object> mapRow(
//										ResultSet rs, int arg1)
//										throws SQLException {
//									
//									Map<String,Object> item = new HashMap<String, Object>();
//									
//										ResultSetMetaData metaData = rs.getMetaData();
//										int colCount = metaData.getColumnCount();
//										int i=0;
//										while(rs.next()){
//											String columnName=metaData.getColumnName(i);
//											item.put(columnName, rs.getObject(columnName));
//											
//											i++;
//									}
//									
//									return item;
//								}
//								
//							});*/
//							
//							//Map<String,Object> p_value_map=getJdbcTemplate().queryForMap(itemDC.getParentSelectSql(), new Object[]{rs.getObject(itemDC.getFieldNameAlias())});
//							
//							//根据 表名称获取  showtableConfigId
//							ShowTableConfig p_ShowTableConfig=ArchCache.getInstance().getShowTableConfigCache().getShowTableConfig(itemDC.getParentTableName());
//							QueryCondition<Object[]> p_condition = new QueryCondition<Object[]>();
//							p_condition.setPageIndex(1);
//							p_condition.setPageSize(1);
//							p_condition.setWherepParameterValues(new Object[]{rs.getObject(itemDC.getFieldNameAlias())});
//						//	item.put(itemDC.getFieldName(), queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
//							
//							setJDBCDataSource(DSManager.getDataSource(p_ShowTableConfig.getTargetDs()));
//							
//							item.put(itemDC.getFieldNameAlias(), queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
//							
//							setJDBCDataSource(DSManager.getDataSource(targetDs));
//							
//							
//						}
//						
//						
//					}
//					
//					
//					 //处理 列数字统计
//					   //statistics
//					
//					if(!StringUtils.isEmpty(itemDC.getStatistics()))
//					{
//						
//						StringBuffer statistics_sb = new StringBuffer();
//						
//						//包含where条件
//						statistics_sb.append(itemDC.getStatistics());
//						
//						
//						String staticsString=queryForSingle(statistics_sb.toString(),null,new RowMapper<String>() {
//
//							@Override
//							public String mapRow(ResultSet rs, int rowNum)
//									throws SQLException {
//								// TODO Auto-generated method stub
//								return rs.getString(1);
//							}
//						});
//						
//						qr.getStatistics().put(itemDC.getFieldNameAlias(), staticsString);
//					}
//				}
//				
//				return item;
//			}
//	    	
//	    	
//	    });
//	    
//	    
//	    logger.debug("== 查询记录数SQL："+sql_cnt_sb.toString());
//	    
//	    long cnt=getMongoTemplate().count(query, BasicDBObject.class,collectionName);
//	    
//	    
//	    logger.debug("== 总记录数:"+cnt);
//	  
//	   
//	  
//	   
//	   
//	    
//	    qr.setResultList(dataList);
//		qr.setFields(showDataConfigs);
//		qr.setPageIndex(condition.getPageIndex());
//		qr.setPageSize(condition.getPageSize());
//		qr.setCount(cnt);
//		
//		
//		
//		qr.setShowTableConfig(showTableConfig);
//		return qr;
//	}


}
