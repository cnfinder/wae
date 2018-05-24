package cn.finder.wae.queryer.common.nosql.handleclass;

import org.springframework.data.mongodb.core.MongoTemplate;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.db.ds.DSManager;
import cn.finder.wae.queryer.common.nosql.MongoDBQueryer;
import cn.finder.wae.queryer.handleclass.QueryerBeforeClass;


/*
 * 有对数据库操作的 那么轻继承此类,重写 handler方法  并且调用 基类的 handle
 * 就可以直接操作数据库,
 * 
 * 
 */
public abstract class MongoQueryerDBBeforeClass extends MongoDBQueryer implements QueryerBeforeClass
{

	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		
	    MongoTemplate mongoTemplate=DSManager.getMongoDataSource(showTableConfig.getTargetDs());
		setDataSource(mongoTemplate);
		
	}


	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
