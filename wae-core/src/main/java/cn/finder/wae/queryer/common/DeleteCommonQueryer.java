package cn.finder.wae.queryer.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;


/***
 * 看做DAO层  因为这样处理数据源容易
 * 
 * 删除也做配置 是为了 适应多数据源和不同的业务处理。 因为删除操作可能涉及到其他业务表
 * 此处理类 只是删除当前一个表， 没有其他业务。
 * @author wu hualong
 *
 */
public class DeleteCommonQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(DeleteCommonQueryer.class);
	@FinderLogger(archType=ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into DeleteCommonQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		final DeleteQueryConditionDto dto= (DeleteQueryConditionDto)condition;
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		
		
		
		ShowDataConfig showDataConfig =ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);
		
		
		
		
		String sql ="delete from "+showDataConfig.getFieldTableName() +" where "+showDataConfig.getFieldName()+"=?";
		
		final int size = dto.getIds().length;
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setObject(1, dto.getIds()[i]);
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return size;
			}
		};
				
			
		 getJdbcTemplate().batchUpdate(sql, batchSetter);
		 qr.setPageIndex(1);
		 qr.setPageSize(1);
		 qr.setCount(0l);
		
		return qr;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

}
