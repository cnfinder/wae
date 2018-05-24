package cn.finder.wae.queryer.common.nosql;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.DeleteQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.aspect.logger.ArchType;
import cn.finder.wae.common.aspect.logger.FinderLogger;
import cn.finder.wae.common.db.ds.DSManager;

/**
 * MongoDB 删除
 * 
 * @author lizhi
 * 
 */
public class MongoDeleteCommonQueryer extends MongoDBQueryer {

	private final static Logger logger = Logger.getLogger(MongoDeleteCommonQueryer.class);

	@FinderLogger(archType = ArchType.FINDER_ARCH)
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId, QueryCondition<Object[]> condition) {

		logger.debug("=====go into MongoDeleteCommonQueryer======");

		logger.debug("==showTableConfigId:" + showTableConfigId);

		final DeleteQueryConditionDto dto = (DeleteQueryConditionDto) condition;

		final TableQueryResult qr = new TableQueryResult();

		ShowDataConfig showDataConfig = ArchCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showTableConfigId);

		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		MongoTemplate ds = DSManager.getMongoDataSource(showTableConfig.getTargetDs());
		setDataSource(ds);// mongo

		Document deleteObj = new Document(showDataConfig.getFieldName(), dto.getIds()[0]);

		
		getMongoTemplate().remove(deleteObj, showDataConfig.getFieldTableName());
		/*
		 * String sql = "delete from " + showDataConfig.getFieldTableName() +
		 * " where " + showDataConfig.getFieldName() + "=?";
		 * 
		 * final int size = dto.getIds().length;
		 * 
		 * BatchPreparedStatementSetter batchSetter = new
		 * BatchPreparedStatementSetter() {
		 * 
		 * @Override public void setValues(PreparedStatement ps, int i) throws
		 * SQLException { // TODO Auto-generated method stub ps.setObject(1,
		 * dto.getIds()[i]); }
		 * 
		 * @Override public int getBatchSize() { // TODO Auto-generated method
		 * stub return size; } };
		 * 
		 * getJdbcTemplate().batchUpdate(sql, batchSetter);
		 */

		return qr;
	}

}
