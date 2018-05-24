package cn.finder.wae.queryer.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.BaseJdbcDaoSupport;
import cn.finder.wae.common.db.ds.DSManager;


/***
 * 不同数据源表数据导入
 * @author wu hualong
 *
 */
public class TableDataImportExQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(TableDataImportExQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into TableDataImportExQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		final TableQueryResult qr =new TableQueryResult();
		
		MapParaQueryConditionDto<String,Long> paraCondition = (MapParaQueryConditionDto<String,Long>)condition;
		
		//1.获取源
		long sourceShowtableConfigId = paraCondition.get("sourceShowtableConfigId");
		long destShowtableConfigId = paraCondition.get("destShowtableConfigId");
		
		
		
		ImportDestViewData importDestViewData=new ImportDestViewData(sourceShowtableConfigId,paraCondition);
		DataSource  destDS = DSManager.getDataSource(ArchCache.getInstance().getShowTableConfigCache().get(destShowtableConfigId).getTargetDs());
		importDestViewData.setDataSource(destDS);
		
		importDestViewData.queryTableQueryResult(destShowtableConfigId,new QueryCondition<Object[]>());
		
		
		
		
		//这里一定要设置count的值 ，否则struts序列化会有错误，返回不到前台
		qr.setCount(0L);
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
	
	
	
	class FindSourceViewData extends BaseCommonDBQueryer{
		
		

		@Override
		public void setJDBCDataSource(DataSource dataSource) {
			this.setJdbcDataSource(dataSource);
		}


		@Override
		public TableQueryResult queryTableQueryResult(long showTableConfigId,
				QueryCondition<Object[]> condition) {
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
		    
		    sql_cnt_sb.append("select ").append(" count(*) cnt ").append(" from ").append(showTableConfig.getShowTableName()).append(" where 1=1 ");
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
					
					for(int i=0;i<showDataConfigs.size();i++)
					{
						ShowDataConfig itemDC=showDataConfigs.get(i);
						item.put(itemDC.getFieldNameAlias(), rs.getObject(itemDC.getFieldNameAlias()));
					}
					
					return item;
					
				}
		    });
		    
		    TableQueryResult data=new TableQueryResult();
		    data.setResultList(dataList);
		    return data;
		}
		
	}
	
	
	/***
	 * 导入目标数据
	 * @author Administrator
	 *
	 */
	class ImportDestViewData extends BaseCommonDBQueryer{
		
		
		private long sourceShowtableConfigId;
		
		private QueryCondition<Object[]> condition;
		
		public ImportDestViewData(long sourceShowtableConfigId,QueryCondition<Object[]> condition){
			this.sourceShowtableConfigId = sourceShowtableConfigId;
			this.condition = condition;
			
		}


		@Override
		public void setJDBCDataSource(DataSource dataSource) {
			super.setDataSource(dataSource);
			
		}


		@Override
		public TableQueryResult queryTableQueryResult(long destShowTableConfigId,
				QueryCondition<Object[]> queryCondition) {
			TableQueryResult queryResult = new TableQueryResult();
			
			FindSourceViewData findSourceViewData = new FindSourceViewData();
			
			DataSource  sourceDS = DSManager.getDataSource(ArchCache.getInstance().getShowTableConfigCache().get(sourceShowtableConfigId).getTargetDs());
			findSourceViewData.setJdbcDataSource(sourceDS);
			
			final List<Map<String,Object>> sourceDataList =findSourceViewData.queryTableQueryResult(sourceShowtableConfigId,condition).getResultList();
			
			
			
			//source field
			ShowTableConfig sourceShowTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(sourceShowtableConfigId);
			 final List<ShowDataConfig> sourceShowDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(sourceShowtableConfigId);
		   
			
			 
			 
			 
			//dest field
			ShowTableConfig destShowTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(destShowTableConfigId);
			final List<ShowDataConfig> destShowDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(destShowTableConfigId);
			
			
			  String sql_deletedata ="delete from "+destShowTableConfig.getShowTableName();
			  
			  getJdbcTemplate().update(sql_deletedata);
				 
				 
			  StringBuffer sql_sb =new StringBuffer();
			    
			    
			    StringBuffer fields_sb =new StringBuffer();
			    
			    
			   final  List<String> filedNames=new ArrayList<String>();
			   
			   final  List<String> filedNamesFlag=new ArrayList<String>();
			   
			    for(ShowDataConfig df:destShowDataConfigs)
			    {
			    	filedNames.add(df.getFieldName());
			    	filedNamesFlag.add("?");
			    }
			    
			    fields_sb.append(org.apache.commons.lang.StringUtils.join(filedNames,","));
			    String flagString = org.apache.commons.lang.StringUtils.join(filedNamesFlag,",");;
			    
			    
			    sql_sb.append("insert into "+destShowTableConfig.getShowTableName()+"  (");
			    
			    sql_sb.append(fields_sb.toString());
			    
			    sql_sb.append(") values (");
			    
			    sql_sb.append(flagString);
			    
			    sql_sb.append(")");
			    
			    logger.debug("==insert to dest sql:"+sql_sb.toString());
			    
				final int size = sourceDataList.size();
				
				BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						
						for(int j=0;j<sourceShowDataConfigs.size();j++){
							
							Map<String,Object> itemData = sourceDataList.get(i);
							ShowDataConfig sdc = sourceShowDataConfigs.get(j);
							ps.setObject(j+1, itemData.get(sdc.getFieldNameAlias()));
							
						}
						
					}
					
					@Override
					public int getBatchSize() {
						return size;
					}
				};
						
					
				 getJdbcTemplate().batchUpdate(sql_sb.toString(), batchSetter);
				 
				 queryResult.setCount(0l);
				 
				 return queryResult;
		}
		
	}

}
