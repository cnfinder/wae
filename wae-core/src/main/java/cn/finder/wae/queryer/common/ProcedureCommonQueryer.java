package cn.finder.wae.queryer.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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
import cn.finder.wae.cache.ConstantsCache.DataType;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 存储过程查询功能  (不受事务管理)
 * 客户端请求参数的顺序 需要和存储过程 输入参数顺序一样， 并且输入的参数必须在返回的列表中
 * @author wu hualong
 *
 */
public class ProcedureCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(ProcedureCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into ProcedureCommonQueryer======");
		
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
	    
	    List<Object> params =new ArrayList<Object>();
	    
	    if(condition.getWherepParameterValues()!=null){
	    	params.addAll(Arrays.asList(condition.getWherepParameterValues()));
	    }
	    params.add(condition.getPageIndex());
	    params.add(condition.getPageSize());
	   
	    for(int i=0;i<params.size();i++){
	    	
	    	logger.debug("存储过程参数 "+i+": "+params.get(i).toString());
	    	
	    }
	    
	    
	   
	    final long targetDs = showTableConfig.getTargetDs();
	    
	    
	    sql_sb =new StringBuffer();
	    sql_sb.append("{call ").append(showTableConfig.getShowTableName());
	    
	    StringBuffer  sbParamsFlag =new StringBuffer();
	    if(params!=null){
		    for(int i=0;i<params.size();i++){
		    	sbParamsFlag.append(",").append("?");
		    }
		    if(sbParamsFlag.length()>0){
		    	sbParamsFlag.deleteCharAt(0);
		    	
		    }
	    }
	    sql_sb.append("(").append(sbParamsFlag).append(")").append("}");
	    
	    logger.debug("== 查询数据SQL："+sql_sb.toString());
	    
	    
	    List<Map<String, Object>>  dataList=getJdbcTemplate().query(sql_sb.toString(),params.toArray(),new RowMapper<Map<String,Object>>(){

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
					
					Object value;
				
					
					try{
						value = rs.getObject(itemDC.getFieldName());// 如果字段不存在  抛出 SQLException
					}
					catch(SQLException e){
						logger.warn(e.toString());
						 int  dataType = itemDC.getDataType();
						 if(dataType == DataType.DATATYPE_STRING){
							 value =null;
						 }
						 else{
							 value ='0';
						 }
						
					}
					
					if(StringUtils.isEmpty(itemDC.getParentTableName()))
					{
						//如果父表 没有填写，那么直接把值放入
						//item.put(itemDC.getFieldName(), rs.getObject(itemDC.getFieldNameAlias()));
						
						/*if(rs.getObject(itemDC.getFieldNameAlias()) instanceof java.util.Date){
							item.put(itemDC.getFieldNameAlias(), Common.formatDate((Timestamp)rs.getObject(itemDC.getFieldNameAlias())));
						}
						else*/
							//item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldName()));
						item.put(itemDC.getFieldNameAlias(), value);
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
	    
	    
	    
	    sql_cnt_sb =new StringBuffer();
	    
	    
	    StringBuffer  sbcntParamsFlag =new StringBuffer();
	    if(condition.getWherepParameterValues()!=null){
		    for(int i=0;i<condition.getWherepParameterValues().length;i++){
		    	sbcntParamsFlag.append(",").append("?");
		    }
		    if(sbParamsFlag.length()>0){
		    	sbcntParamsFlag.deleteCharAt(0);
		    	
		    }
	    }
	    
	    
	    
	    sql_cnt_sb.append("{call ").append(showTableConfig.getShowTableName()).append("_TotalRecord");
	    
	    sql_cnt_sb.append("(").append(sbcntParamsFlag).append(")").append("}");
	    
	    logger.debug("== 查询记录数SQL："+sql_cnt_sb.toString());
	    
	    
	    
	   long cnt =0;
	   try
	   {
		   cnt = queryForLong(sql_cnt_sb.toString(), condition.getWherepParameterValues());
	   // long cnt = 0;
	   }
	   catch(Exception  e){
		   logger.debug(e.toString());
	   }
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
