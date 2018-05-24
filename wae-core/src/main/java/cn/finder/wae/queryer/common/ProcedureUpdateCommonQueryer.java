package cn.finder.wae.queryer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;

/***
 * 普通存储过程更新操作处理  受事务管理
 * @author wuhualong 
 *
 */
public class ProcedureUpdateCommonQueryer extends  BaseCommonDBQueryer{
	
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition){
		
		logger.debug("=====go into ProcedureUpdateCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		TableQueryResult qr =new TableQueryResult();
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
		
		
		
		
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
		
			
		int cnt = getJdbcTemplate().update(sb_sql.toString(), values.toArray());
		 
		
		qr.setFields(showDataConfigs);
		qr.setPageIndex(condition.getPageIndex());
		qr.setPageSize(condition.getPageSize());
		qr.setCount((long)cnt);
		return qr;
	
	}
	
	
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	
}
