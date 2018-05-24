package cn.finder.wae.queryer.common;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;


/***
 * 任务计划 不同数据源表 视图 或者存储过程 的数据导入
 * @author wu hualong
 *
 */
@Deprecated
public class TaskPlanDataImportQueryer extends  BaseCommonDBQueryer{

	
	private final static Logger logger = Logger.getLogger(TaskPlanDataImportQueryer.class);
	
	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into TableDataImportQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		
		
		
		@SuppressWarnings("unchecked")
		MapParaQueryConditionDto<String, Object> mapPara =(MapParaQueryConditionDto<String, Object>)condition;
		TaskPlan taskPlan =  (TaskPlan)mapPara.get("taskplan");
		
		
		//1.数据来源 表配置
		long sourceShowtableConfigId = taskPlan.getInt("sourceShowtableConfigId");
		
		//2. 插入目标数据  表配置
		long destShowtableConfigId = taskPlan.getInt("destShowtableConfigId");
		
		QueryService pharmacyService=AppApplicationContextUtil.getContext().getBean("queryService", QueryService.class);
		
		
		
		TableQueryResult queryResult = pharmacyService.queryTableQueryResult(sourceShowtableConfigId, condition);
		
		
		//这里一定要设置count的值 ，否则struts序列化会有错误，返回不到前台
		queryResult.setCount(0L);
		return queryResult;
	}

	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}
	

}
