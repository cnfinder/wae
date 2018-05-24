package cn.finder.wae.queryer;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

/***
 * 统一 业务执行接口
 * @author Administrator
 *
 */
public interface Queryer {

	/***
	 * 设置数据源，以便使用spring jdbc
	 * @param dataSource
	 */
	public void setJDBCDataSource(DataSource dataSource);
	
	
	
	
	/**
	 * 查询接口定义
	 * @param showTableConfigId
	 * @param condition
	 * @return
	 */
	public TableQueryResult queryTableQueryResultManager(long showTableConfigId,QueryCondition<Object[]> condition);
}
