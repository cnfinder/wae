package cn.finder.wae.queryer.common;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache.DataType;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;

/***
 * 更新调用存储过程， 带输出参数   输出参数标记通过 ShowDataType  CODE="OUT"  标记,注意字段配置顺序和存储过程参数顺序一致
 * @author wuhualong
 *
 */
public class ProcedureUpdateParamCommonQueryer extends  BaseCommonDBQueryer{
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into ProcedureUpdateParamCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		TableQueryResult qr =new TableQueryResult();
		
		
		@SuppressWarnings("unchecked")
		final Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		final List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		
		
		
		String procedureName =showTableConfig.getShowTableName();
		
		StringBuffer  sb_sql = new StringBuffer();
		
		StringBuffer  sb_paramFlags = new StringBuffer();
		
		//String sql ="{call "+procedureName +"("+paramsFlags +")}";

		List<Object> values = new ArrayList<Object>();

		
		for(int i=0;i<showDataConfigs.size();i++)
		{
			ShowDataConfig itemDC=showDataConfigs.get(i);
		
			Object v = data.get(itemDC.getFieldNameAlias());
			values.add(v);
			
			sb_paramFlags.append(",").append("?");
		}
		
		if(sb_paramFlags.toString().length()>0){
			sb_paramFlags.deleteCharAt(0);
		}
		
		sb_sql.append("{call ").append(procedureName).append("(").append(sb_paramFlags.toString()).append(")}");
		
		logger.debug("== call procedure :"+sb_sql.toString());
		
		final List<Object> para_values= values;
			
		//int cnt = getJdbcTemplate().update(sb_sql.toString(), values.toArray());
		
		
		
		Map<String,Object>  out_value = this.getJdbcTemplate().execute(sb_sql.toString(),new CallableStatementCallback<Map<String,Object>>(){
		      public Map<String,Object>  doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
		    	 
			    	for(int j=0;j<showDataConfigs.size();j++)
					{
						ShowDataConfig itemDC=showDataConfigs.get(j);
					
						Object v = data.get(itemDC.getFieldNameAlias());
						
						if("OUT".equalsIgnoreCase(itemDC.getShowDataType().getCode())){
							cs.registerOutParameter(j+1,Types.VARCHAR);//注册返回参数类型
						}else{
							cs.setObject(j+1, para_values.get(j));
						}
						
					}
			    	
			        int cnt = cs.executeUpdate();
			        Map<String,Object> out_item =new HashMap<String,Object>();
			        for(int j=0;j<showDataConfigs.size();j++)
					{
						ShowDataConfig itemDC=showDataConfigs.get(j);
						
						if("OUT".equalsIgnoreCase(itemDC.getShowDataType().getCode())){
							Object value = null;
									
							try{
								value = cs.getObject(j+1); // 如果字段不存在  抛出 SQLException
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
							out_item.put(itemDC.getFieldNameAlias(), value);
						}
					}
			        
			       
			        return out_item;
			        
		      }
		   });
		 
		List<Map<String,Object>> result = new  ArrayList<Map<String,Object>>();
		/*Map<String,Object> entity_item =new HashMap<String, Object>();
		entity_item.put(showDataConfigs.get(showDataConfigs.size()-1).getFieldNameAlias(), out_value);*/
		 data.putAll(out_value);
		result.add(data);
		
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount((long)1);
		
		
		qr.setResultList(result);
		
		return qr;
	
	}
	
	
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
}
