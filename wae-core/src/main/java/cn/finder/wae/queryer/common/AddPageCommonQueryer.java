package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.constant.Constant;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.DBType;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 看做DAO层  因为这样处理数据源容易
 * @author wu hualong
 * 加载添加页面控件数据
 *
 */
public class AddPageCommonQueryer extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(AddPageCommonQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into AddPageCommonQueryer.queryTableQueryResult======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		MapParaQueryConditionDto<String, Object> mapCondition=(MapParaQueryConditionDto<String, Object>)condition;
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
		
		if(condition.getPageSize()==0){
	    	//默认为数据库设置大小
	    	condition.setPageSize(showTableConfig.getPageSize()==0?10:showTableConfig.getPageSize());
	    }
	   // final List<ShowDataConfig> showDataConfigs =showTableConfig.getShowDataConfigs();
		 final List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showTableConfigId);
		
		 List<Map<String, Object>>  dataList =new ArrayList<Map<String,Object>>();
		 
		 if(showDataConfigs!=null && showDataConfigs.size()>0){
			 Map<String,Object> item = new HashMap<String, Object>();
			 for(ShowDataConfig itemDC:showDataConfigs){
				
				 
				 //if(itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_COMBOX || itemDC.getShowType()==ConstantsCache.ControlType.CONTROLTYPE_CASCADE_COMBOX){
				if(ConstantsCache.isLoadComboxData(itemDC.getShowType()) && ConstantsCache.ControlType.CONTROLTYPE_POPUP_TABLESELECT!=itemDC.getShowType()){
					//普通下拉框 加载 父表对应数据
					 ShowTableConfig p_ShowTableConfig=ArchCache.getInstance().getShowTableConfigCache().getShowTableConfig(itemDC.getParentTableName());
					 
					 MapParaQueryConditionDto<String, Object> p_condition=new MapParaQueryConditionDto<String, Object>();
					p_condition.setPageIndex(1);
					p_condition.setPageSize(1000000000);
					p_condition.setSearchObject(new Object[]{false});
					
					Map<String,Object> dataMap = new HashMap<String,Object>();
					//dataMap.put("session_user",  cn.finder.wae.common.base.BaseActionSupport.findSessionUser());
					dataMap.put("session_user",  mapCondition.getMapParas().get("session_user"));
					p_condition.setMapParas(dataMap);
					
					p_condition.setWhereCluster(p_ShowTableConfig.getSqlConfig());
					
					
					
					AddPageParentCommonQueryer addPageParentCommonQueryer=new AddPageParentCommonQueryer();
					addPageParentCommonQueryer.setDataSource(DSManager.getDataSource(p_ShowTableConfig.getTargetDs()));
					// item.put(itemDC.getFieldNameAlias(), queryParentTableQueryResult(p_ShowTableConfig.getId(),p_condition));
					BaseJdbcDaoSupport baseJdbcDaoSupport = (BaseJdbcDaoSupport)addPageParentCommonQueryer;
					DataSource ds = DSManager.getDataSource(p_ShowTableConfig.getTargetDs());
					BasicDataSource basicDataSource = (BasicDataSource)ds;
					String driverClassName = basicDataSource.getDriverClassName();
					//判断数据库类型
					if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
						baseJdbcDaoSupport.setDbType(DBType.MySql);
					}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
						
						baseJdbcDaoSupport.setDbType(DBType.SqlServer);
					}
					else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
						
						baseJdbcDaoSupport.setDbType(DBType.Oracle);
					}
					
					 item.put(itemDC.getFieldNameAlias(), addPageParentCommonQueryer.queryTableQueryResultManager(p_ShowTableConfig.getId(),p_condition));
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
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
