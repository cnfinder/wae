package cn.finder.wae.queryer.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;


/***
 * 看做DAO层  因为这样处理数据源容易
 * 
 * API 接口多条件删除   需要配置2个主键字段 
 * 
 * 
 * @author wu hualong
 *
 */
public class ApiMutilKeyDeleteCommonQueryer extends  BaseCommonDBQueryer{

	String[] ids=null;
	private final static Logger logger = Logger.getLogger(ApiMutilKeyDeleteCommonQueryer.class);
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into DeleteCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String idsStr = String.valueOf(data.get("ids"));
		
		
		if(!StringUtils.isEmpty(idsStr)){
			ids = idsStr.split(",");
		}
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		
		ShowDataConfig showDataConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		
		
		
		
		String sql ="delete from "+showDataConfig.getFieldTableName() +" where "+showDataConfig.getFieldName()+"=?";
		
		final int size = ids.length;
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setObject(1, ids[i]);
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return size;
			}
		};
				
			
		 getJdbcTemplate().batchUpdate(sql, batchSetter);
		
		
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
