package cn.finder.wae.queryer.handleclass.flow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.activiti.utils.WorkflowUtils;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * @author: wuhualong
 * @data:
 * @function: 获取流程实例当前活动图
 */
public class FlowDynamicGrapicAfterQueryer  extends QueryerDBAfterClass{
	
	
	ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
	
	ProcessEngineFactoryBean processEngine;
	
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		//ProcessEngineFactoryBean processEngine=AppApplicationContextUtil.getContext().getBeansOfType(ProcessEngineFactoryBean.class).get(0);
		//processEngine=AppApplicationContextUtil.getContext().getBean("processEngine", ProcessEngineFactoryBean.class);
		//Object bean =AppApplicationContextUtil.getContext().getBean("processEngine");
		List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
		Map<String,Object> item=new HashMap<String, Object>();
		list.add(item);
		
		String processInstanceId =data.get("processInstanceId").toString();
		
		try {
			  
			  ProcessInstance processInstance=	 activitiServiceManager.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			  if(processInstance==null){
				  item.put("binary_data", "获取流程图异常!".getBytes());
			  }else{
				 BpmnModel bpmnModel= activitiServiceManager.getRepositoryService().getBpmnModel(processInstance.getProcessDefinitionId());
				 List<HistoricActivityInstance> activityInstances=activitiServiceManager.getHistoryService().createHistoricActivityInstanceQuery()
							.processInstanceId(processInstanceId)
							.orderByHistoricActivityInstanceStartTime().asc().list();
				 List<String> activitiIds=new ArrayList<String>();
				 List<String> flowIds=new ArrayList<String>();
				 ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) activitiServiceManager.getRepositoryService())
							.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
				 flowIds=WorkflowUtils.getHighLightedFlows(processDefinition, activityInstances);//获取流程走过的线
				
				 for(HistoricActivityInstance hai:activityInstances){
					 activitiIds.add(hai.getActivityId());//获取流程走过的节点
				 }
				
				 
				 Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());
				
				 
				 InputStream imageStream =  processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				    .generateDiagram(bpmnModel, "png", activitiIds,flowIds,
				                     processEngine.getProcessEngineConfiguration().getActivityFontName(),
				                     processEngine.getProcessEngineConfiguration().getLabelFontName(), 
				                     processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);
				 
				byte[] buffer=new byte[imageStream.available()];
				imageStream.read(buffer);
				 
				
				item.put("binary_data", buffer);
				
			  }
		} catch (Exception e) {
			logger.error("获取流程图异常!", e);
			item.put("binary_data", "获取流程图异常!".getBytes());
		}
		
	   
	   
     
       tableQueryResult.setCount(1l);
       tableQueryResult.setPageSize(1);
       tableQueryResult.setPageIndex(1);
       tableQueryResult.setResultList(list);
       
       
	      
		return tableQueryResult;
	}
	
	

}
