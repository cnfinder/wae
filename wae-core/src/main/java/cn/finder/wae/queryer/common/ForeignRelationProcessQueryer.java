package cn.finder.wae.queryer.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.mapping.Array;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.ForeignRelationChksQueryConditionDto;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;


/***
 * 处理表关联数据
 * @author wu hualong
 * 插入表的顺序为该出来配置字段顺序。  约定第一个为主表id值，第二个为辅表主键值
 * 一般关系字段为 2个  。如 role_id - menu_id
 * 主表ID值从  queryCondition.wherepParamterValues取得
 * 辅表ID值从  当前行主键值中获取
 *
 */
public class ForeignRelationProcessQueryer extends  BaseCommonDBQueryer{

	
private final static Logger logger = Logger.getLogger(ForeignRelationProcessQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into ForeignReleationProcessQueryer======");
		
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
		
		final ForeignRelationChksQueryConditionDto params=(ForeignRelationChksQueryConditionDto)condition;
		
		
		final  List<String> filedNames=new ArrayList<String>();
		   
		final List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showTableConfigId);
	    for(ShowDataConfig df:showDataConfigs)
	    {
	    	filedNames.add(df.getFieldName());
	    }
	    
	    
	    //需要添加的数据
	    List<String> need_inserted_vallue=new ArrayList<String>();
	    
	    List<String> exists_value=new ArrayList<String>();//数据库已经存在
	    
	    if(params.getSubValues()!=null && params.getSubValues().size()>0){
	    	//查询已经存在的不需要更新的项
	    	StringBuffer sql_check = new StringBuffer();
	    	sql_check.append("select count(1) cnt from ").append(showDataConfigs.get(0).getFieldTableName());
	    	sql_check.append(" where ").append(showDataConfigs.get(0).getFieldName()).append("=? ");
	    	sql_check.append(" and ").append(showDataConfigs.get(1).getFieldName()).append("=? ");
	    	
	    	logger.debug("====sql check==="+sql_check);
	    	for(int i=0;i<params.getSubValues().size();i++){
	    		String item_v=params.getSubValues().get(i);
	    		int cnt=queryForInt(sql_check.toString(), new Object[]{params.getMainValue(),item_v});
	    		if(cnt==0){
	    			need_inserted_vallue.add(item_v);
	    		}else{
	    			exists_value.add(item_v);
	    		}
	    	}
	    }
	    
	    StringBuffer sb_exists_valuesstr =new StringBuffer();
		for(String v:exists_value){
			sb_exists_valuesstr.append(",");
			sb_exists_valuesstr.append("'").append(v).append("'");
		}
		if(sb_exists_valuesstr.length()>0)
			sb_exists_valuesstr.deleteCharAt(0);
	    
	    
	    
	   /* StringBuffer sql_delete = new StringBuffer();
	    
	    sql_delete.append("delete from ");
	    sql_delete.append(showDataConfigs.get(0).getFieldTableName());
	    sql_delete.append(" where "+showDataConfigs.get(0).getFieldName()+"=?");
		
	    logger.debug("delete  sql:"+sql_delete.toString());
	    
	    getJdbcTemplate().update(sql_delete.toString(), params.getMainValue());*/
	    
	    //需要删除的数据  为 当前表中不在 update_values 里面的所有的只
	    
	    StringBuffer sql_delete = new StringBuffer();
	    
	    sql_delete.append("delete from ");
	    sql_delete.append(showDataConfigs.get(0).getFieldTableName());
	    sql_delete.append(" where "+showDataConfigs.get(0).getFieldName()+"=?");
	    
	    if(sb_exists_valuesstr.length()>0)
	    {
	    	sql_delete.append(" and ").append(showDataConfigs.get(1).getFieldName()).append(" not in ").append("(").append(sb_exists_valuesstr).append(")");
	    }
	    
	    logger.debug("delete  sql:"+sql_delete.toString());
	    getJdbcTemplate().update(sql_delete.toString(), params.getMainValue());
	    
	    
	    
	    
	    
	    if(need_inserted_vallue!=null && need_inserted_vallue.size()>0){
	  
			
			StringBuffer sql =new StringBuffer();
			//sql.append("insert into ").append(showTableConfig.getShowTableName());
			sql.append("insert into ").append(showDataConfigs.get(0).getFieldTableName());
			sql.append("(");
			sql.append(StringUtils.join(filedNames,","));
			sql.append(")");
			sql.append(" values(");
			sql.append("?,?");
			sql.append(")");
			
			logger.debug("insert  sql:"+sql.toString());
			
			
			final int size = need_inserted_vallue.size();
			
			final List<String> need_inserted_vallue_tmp=need_inserted_vallue;
			
			BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					
					ps.setObject(1,params.getMainValue()); //queryCondition.值
					ps.setObject(2,need_inserted_vallue_tmp.get(i)); //当前行主键值
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return size;
				}
			};
					
				
			 getJdbcTemplate().batchUpdate(sql.toString(), batchSetter);
			
			
		
	    }
		qr.setCount(0l);
		qr.getMessage().setMsg("操作成功");
		 return qr;
		 
		
	}
	
	

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
