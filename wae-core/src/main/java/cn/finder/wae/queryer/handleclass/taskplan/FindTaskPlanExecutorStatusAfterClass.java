package cn.finder.wae.queryer.handleclass.taskplan;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.controller.listener.CustomedContextLoaderListener;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: finder
 * @function:获取 调度器 线程状态 待处理
 */
public class FindTaskPlanExecutorStatusAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
		 
		  ScheduledExecutorService executorService=CustomedContextLoaderListener.getExecutorService();
		 
		  
		  
	       
	       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
	       tableQueryResult.setCount(1l);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       tableQueryResult.setResultList(list);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
