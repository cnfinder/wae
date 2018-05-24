package cn.finder.wae.queryer.handleclass.flow;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TaskPlan;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.queryer.handleclass.QueryerAfterClass;

import common.Logger;

/**
 * @author: wuhualong
 * @data:2014-9-17
 * @function: 配药流程处理
 */
public class TaskPlanProcessStartAfterQueryer  implements QueryerAfterClass{
	
	private Logger logger = Logger.getLogger(TaskPlanProcessStartAfterQueryer.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		
		
		
		
		logger.debug("===启动流程...:");
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		@SuppressWarnings("unchecked")
		MapParaQueryConditionDto<String, Object> mapPara =(MapParaQueryConditionDto<String, Object>)condition;
		TaskPlan taskPlan =  (TaskPlan)mapPara.get("taskplan");
		
		
		//流程 key
		String flowKey = taskPlan.getString("flow_key");  //获取 配置KEY
		
		Map<String,Object> variables = new  HashMap<String, Object>();
	   ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
       ProcessInstance processInstance = activitiServiceManager.getRuntimeService().startProcessInstanceByKey(flowKey, "TASKPLAN_"+Common.formatDate(new Date()), variables);
		
       	
       
		return tableQueryResult;
	}
	
	
	

}
