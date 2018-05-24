package cn.finder.wae.queryer.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * @author wu hualong
 *
 */
public class SearchCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(SearchCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into SearchCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		MapParaQueryConditionDto<String, Object> queryCondition=(MapParaQueryConditionDto<String, Object>)condition;
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		
	   // final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
		 final List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showTableConfigId);
		
		 List<Map<String, Object>>  dataList =new ArrayList<Map<String,Object>>();
		 
		  final long targetDs = showTableConfig.getTargetDs();
		 
		 if(showDataConfigs!=null && showDataConfigs.size()>0){
			 Map<String,Object> item = new HashMap<String, Object>();
			 for(ShowDataConfig itemDC:showDataConfigs){
				
				 
				 //if(itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_COMBOX){
				 if(ConstantsCache.isLoadComboxData(itemDC.getShowType())){
					//普通下拉框 加载 父表对应数据
					 ShowTableConfig p_ShowTableConfig=ArchCache.getInstance().getShowTableConfigCache().getShowTableConfig(itemDC.getParentTableName());
					 MapParaQueryConditionDto<String, Object> p_condition = new MapParaQueryConditionDto<String, Object>();
					p_condition.setPageIndex(1);
					p_condition.setPageSize(1000);
					p_condition.setSearchObject(new Object[]{false});
					
					
					
					p_condition.put("session_user", queryCondition.get("session_user"));
					
					p_condition.put("WebRoot_path", queryCondition.get("WebRoot_path"));
					
					setJDBCDataSource(DSManager.getDataSource(p_ShowTableConfig.getTargetDs()));
					// item.put(itemDC.getFieldNameAlias(), queryParentTableQueryResult(p_ShowTableConfig.getId(),p_condition));
					
					QueryService queryService=AppApplicationContextUtil.getContext().getBean("queryService", QueryService.class);
					
					item.put(itemDC.getFieldNameAlias(), queryService.queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
						setJDBCDataSource(DSManager.getDataSource(targetDs));
					 
					 
					 
				 }
				 else
				{
					//非下拉框，直接设置""
					item.put(itemDC.getFieldNameAlias(), "");
				}
				 
				 
				 
			 }
			 dataList.add(item);
		 }
			 
		qr.setResultList(dataList);
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount(0l);
			
		 return qr;
		 
		
	}
	
	
	public TableQueryResult queryParentTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into SearchCommonQueryer.queryParentTableQueryResult======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		if(condition!=null)
		{
			logger.debug("==Sql:"+condition.getSql());
			logger.debug("==WhereCluster:"+condition.getWhereCluster());
			logger.debug("==WherepParameterValues:"+condition.getWherepParameterValues());
			logger.debug("==pageIndex:"+condition.getPageIndex());
			logger.debug("==pageSize:"+condition.getPageSize());
		}
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		//从缓存中获取 ShowTableConfig 配置
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
		
	   // final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
		 final List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showTableConfigId);
	   
	    
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
	    	 if(condition.getSearchObject()!=null &&condition.getSearchObject().length==1 && condition.getSearchObject()[0] ==Boolean.FALSE)
	    	 {
	    		 
	    	 }
	    	 else{
	    		 where_sb.append(showTableConfig.getSqlConfig());
	    	 }
	    	
	    }
	    else{
	    	if(condition.getSearchObject()!=null &&condition.getSearchObject().length==1 && condition.getSearchObject()[0] ==Boolean.FALSE)
	    	 {
	    		
	    	 }
	    	
	    	else{
	    		where_sb.append(" "+showTableConfig.getSqlConfig());
	    	}
	    }
	    
	    sql_sb.append("select ").append(fields_sb+" ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
	    
	    sql_cnt_sb.append("select ").append(" count(1) cnt ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
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
	    		int order_idx=cnt_where.toLowerCase().indexOf("order");
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
	  //mysql 
	   // sql_sb.append(" limit ").append((condition.getPageIndex()-1)*condition.getPageSize()).append(",").append(condition.getPageSize());
	    
	    sql_sb =new StringBuffer(wrapperPagerSql(sql_sb.toString(), condition.getPageIndex(), condition.getPageSize()));
	    
	    final Object[] params = condition.getWherepParameterValues();
	   
	    logger.debug("== 查询数据SQL："+sql_sb.toString());
	    
	    
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
						item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
					}
					else
					{
						
						//加载 父表对应数据
						
						//这里就先做一级 数据显示
					//	if(itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_COMBOX)
						if(ConstantsCache.isLoadComboxData(itemDC.getShowType()))
						{
							//处理组合框
							//查找父所有数据
							/*Map<String,Object> p_value_map=getJdbcTemplate().queryForMap(itemDC.getParentSelectSql());
							
							item.put(itemDC.getFieldName(), p_value_map);
						}
						else{*/
							
							//根据 表名称获取  showtableConfigId
							ShowTableConfig p_ShowTableConfig=ArchCache.getInstance().getShowTableConfigCache().getShowTableConfig(itemDC.getParentTableName());
							QueryCondition<Object[]> p_condition = new QueryCondition<Object[]>();
							p_condition.setPageIndex(1);
							p_condition.setPageSize(1000);
							
							//p_condition.setWherepParameterValues(new Object[]{rs.getObject(itemDC.getFieldNameAlias())});
							//如果 where SearchObject[0] = false .表示参数 搜索父项所有
							p_condition.setSearchObject(new Object[]{false});
							
							
							item.put(itemDC.getFieldNameAlias(), queryTableQueryResult(p_ShowTableConfig.getId(),p_condition));
						}
						
						
					}
					
					
				}
				
				return item;
			}
	    	
	    	
	    });
	    
	    
	    logger.debug("== 查询记录数SQL："+sql_cnt_sb.toString());
	    
	   long cnt =  queryForLong(sql_cnt_sb.toString(), params);
	    
	    
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
		//qr.setShowTableConfig(showTableConfig);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
