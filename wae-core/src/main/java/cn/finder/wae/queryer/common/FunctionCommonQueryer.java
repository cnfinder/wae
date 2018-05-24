package cn.finder.wae.queryer.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 通用函数查询处理类
 * @author wu hualong
 *
 */
public class FunctionCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(FunctionCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into FunctionCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
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
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
	
		
	    final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
	    
	    StringBuffer sql_sb =new StringBuffer();
	    
	    StringBuffer sql_cnt_sb = new StringBuffer();
	    
	    
	    // "id 编号,name 名称"
	    StringBuffer fields_sb =new StringBuffer();
	    
	    // where 后面的条件
	    StringBuffer where_sb =new StringBuffer();
	    
	    
	   final  List<String> filedNames=new ArrayList<String>();
	   
	    
	    for(ShowDataConfig df:showDataConfigs)
	    {
	    	filedNames.add(df.getFieldName()+" "+df.getFieldNameAlias());
	    }
	    
	    fields_sb.append(StringUtils.join(filedNames, ','));
	    
	    
	    if(!StringUtils.isEmpty(condition.getWhereCluster()))
	    {
	    	 where_sb.append(condition.getWhereCluster());
	    }
	    
	    if(StringUtils.isEmpty(condition.getWhereCluster()))
	    {
	    	 where_sb.append(showTableConfig.getSqlConfig());
	    }
	    else{
	    	if(!StringUtils.isEmpty(showTableConfig.getSqlConfig())){
	    		
	    		if(showTableConfig.getSqlConfig().toLowerCase().startsWith("order")){
	    			where_sb.append("  "+showTableConfig.getSqlConfig());
	    		}
	    		else
	    			where_sb.append(" AND "+showTableConfig.getSqlConfig());
	    	}
	    		
	    }
	    
	    sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(wrapperTableName(showTableConfig.getShowTableName(),showTableConfig.getShowType())).append(" where 1=1 ");
	    
	    sql_cnt_sb.append("select ").append(" count(*) cnt ").append(" from ").append(wrapperTableName(showTableConfig.getShowTableName(),showTableConfig.getShowType())).append(" where 1=1 ");
	    //oracle
	    //sql_sb.append(" and ").append(" (rownum >").append((condition.getPageIndex()-1)*condition.getPageSize()).append(" and ").append(" rownum <").append(condition.getPageSize()+") ");
	    
	    
	  
	    
	    if(!StringUtils.isEmpty(where_sb.toString())){
	    	if(where_sb.toString().trim().toLowerCase().startsWith("order by"))
	    	{
	    		sql_sb.append(" ").append(where_sb.toString());
	    		//sql_cnt_sb.append(" ").append(where_sb.toString()); //统计数量不需要排序
	    		
	    		
	    	 
	    	}else{
	    		sql_sb.append("and ").append(where_sb.toString());
	    		
	    		String cnt_where =where_sb.toString();
	    	
	    		//需要去掉order by
	    		int order_idx=cnt_where.toLowerCase().indexOf("order ");
	    		if(order_idx!=-1)
	    		{
		    		cnt_where= cnt_where.substring(0, order_idx);
		    		
		    		sql_cnt_sb.append("and ").append(cnt_where);
	    		}
	    		else{
	    			sql_cnt_sb.append("and ").append(cnt_where);
	    		}
	    	}
	    }
	    
	    
	    //处理前台传过来的排序
	    if(condition!=null && condition.getOrderBy()!=null)
	    {
		    Map<String,String> sortMap =condition.getOrderBy();
			if(sortMap.size()>0)
			{
				int pos = sql_sb.toString().trim().indexOf("order by");
					//如果有order by 应该去除之前的排序
				if(pos!=-1){
					
					
					String newWhereSql = sql_sb.substring(0, pos);
					sql_sb.delete(0, sql_sb.length());
					sql_sb.append(newWhereSql);
				}
					//
				Set<Entry<String, String>> sets = sortMap.entrySet();
				
				Iterator<Entry<String, String>>  ite = sets.iterator();
				
				sql_sb.append(" ").append(" order by ");
				StringBuffer sb_order =new StringBuffer();
				
				while(ite.hasNext())
				{
					Entry<String, String> entry = ite.next();
					String k=entry.getKey();
					String v =entry.getValue();
					
					if(!StringUtils.isEmpty(k) && !StringUtils.isEmpty(v))
					{
						sb_order.append(" ,");
						sb_order.append(k).append(" ").append(v);
					}
				}
				sb_order.delete(0, 2);
				
				sql_sb.append(sb_order);
			}
	    }
		
		
		
	    
	    
	  //mysql 
	    //sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
	    
	    sql_sb =new StringBuffer(wrapperPagerSql(sql_sb.toString(), condition.getPageIndex(), condition.getPageSize()));
	    
	    final Object[] params = condition.getWherepParameterValues();
	   
	    logger.debug("== 查询数据SQL："+sql_sb.toString());
	   
	    final long targetDs = showTableConfig.getTargetDs();
	    List<Map<String, Object>>  dataList=getJdbcTemplate().query(sql_sb.toString(),params,new RowMapper<Map<String,Object>>(){

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int index)
					throws SQLException {
				
				Map<String,Object> item = new HashMap<String, Object>();
				
				/*
				for(String field:filedNames)
				{
					item.put(field, rs.getObject(field));
				}*/
				
				
				
				for(int i=0;i<showDataConfigs.size();i++)
				{
					ShowDataConfig itemDC=showDataConfigs.get(i);
					if(StringUtils.isEmpty(itemDC.getParentTableName()))
					{
						//如果父表 没有填写，那么直接把值放入
						//item.put(itemDC.getFieldName(), rs.getObject(itemDC.getFieldNameAlias()));
						
						/*if(rs.getObject(itemDC.getFieldNameAlias()) instanceof java.util.Date){
							item.put(itemDC.getFieldNameAlias(), Common.formatDate((Timestamp)rs.getObject(itemDC.getFieldNameAlias())));
						}
						else*/
							item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
					}
					else
					{
						
						//加载 父表对应数据
						
						//这里就先做一级 数据显示
						//if(itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_COMBOX)
						if(ConstantsCache.isLoadComboxData(itemDC.getShowType()))
						{
							//处理组合框
							//查找父所有数据
							Map<String,Object> p_value_map=getJdbcTemplate().queryForMap(itemDC.getParentSelectSql());
							
							//item.put(itemDC.getFieldName(), p_value_map);
							item.put(itemDC.getFieldNameAlias(), p_value_map);
						}
						else{
							/*StringBuffer sql_parent = new StringBuffer();
							sql_parent.append("select "+itemDC.getParentShowField() +" from "+itemDC.getParentTableName())
								.append(" where ").append(itemDC.getParentTableKey()).append("=").append(rs.getObject(itemDC.getFieldName()));
							
							String parent_value=getJdbcTemplate().queryForObject(sql_parent.toString(), String.class);
							*/
							/*
							Map<String,Object> p_value_map=getJdbcTemplate().queryFor(itemDC.getParentSelectSql(), new Object[]{rs.getObject(itemDC.getFieldName())}, new RowMapper<Map<String,Object>>(){

								@Override
								public Map<String, Object> mapRow(
										ResultSet rs, int arg1)
										throws SQLException {
									
									Map<String,Object> item = new HashMap<String, Object>();
									
										ResultSetMetaData metaData = rs.getMetaData();
										int colCount = metaData.getColumnCount();
										int i=0;
										while(rs.next()){
											String columnName=metaData.getColumnName(i);
											item.put(columnName, rs.getObject(columnName));
											
											i++;
									}
									
									return item;
								}
								
							});*/
							
							//Map<String,Object> p_value_map=getJdbcTemplate().queryForMap(itemDC.getParentSelectSql(), new Object[]{rs.getObject(itemDC.getFieldNameAlias())});
							
							//根据 表名称获取  showtableConfigId
							ShowTableConfig p_ShowTableConfig=ArchCache.getInstance().getShowTableConfigCache().getShowTableConfig(itemDC.getParentTableName());
							QueryCondition<Object[]> p_condition = new QueryCondition<Object[]>();
							p_condition.setPageIndex(1);
							p_condition.setPageSize(1);
							p_condition.setWherepParameterValues(new Object[]{rs.getObject(itemDC.getFieldNameAlias())});
						//	item.put(itemDC.getFieldName(), queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
							
							setJDBCDataSource(DSManager.getDataSource(p_ShowTableConfig.getTargetDs()));
							
							item.put(itemDC.getFieldNameAlias(), queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
							
							setJDBCDataSource(DSManager.getDataSource(targetDs));
							
							
						}
						
						
					}
					
					
					 //处理 列数字统计
					   //statistics
					
					if(!StringUtils.isEmpty(itemDC.getStatistics()))
					{
						
						StringBuffer statistics_sb = new StringBuffer();
						
						//包含where条件
						statistics_sb.append(itemDC.getStatistics());
						
						
						String staticsString=queryForSingle(statistics_sb.toString(),null,new RowMapper<String>() {

							@Override
							public String mapRow(ResultSet rs, int rowNum)
									throws SQLException {
								// TODO Auto-generated method stub
								return rs.getString(1);
							}
						});
						
						qr.getStatistics().put(itemDC.getFieldNameAlias(), staticsString);
					}
				}
				
				return item;
			}
	    	
	    	
	    });
	    
	    
	    logger.debug("== 查询记录数SQL："+sql_cnt_sb.toString());
	    
	   long cnt = queryForLong(sql_cnt_sb.toString(), params);
	    
	    
	    logger.debug("== 总记录数:"+cnt);
	  
	   
	  
	   
	   
	    
	    qr.setResultList(dataList);
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(cnt);
		
		
		
		//设置grid menus
		//qr.setToolbarGridMenus(ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId).getToolBarMenus());
		//qr.setToolbarGridMenus(ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId).getRowFrontMenus());
		//qr.setToolbarGridMenus(ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId).getRowBackMenus());
		//为了方便 就先直接设置ShowTableConfig到前台，但是数据量有点大
		qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
