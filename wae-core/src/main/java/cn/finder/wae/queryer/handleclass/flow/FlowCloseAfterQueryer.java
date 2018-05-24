package cn.finder.wae.queryer.handleclass.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * 结束流程操作
 * 
 * @author lizhi
 */
public class FlowCloseAfterQueryer extends QueryerDBAfterClass {
	ActivitiServiceManager activitiServiceManager = AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
	TaskService taskService = activitiServiceManager.getTaskService();
	RepositoryService repositoryService = activitiServiceManager.getRepositoryService();

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();

		String businessKey = data.get("businessKey").toString();
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		String taskId = task.getId();
		TaskEntity taskNow = findTaskById(taskId);
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
		ActivityImpl currentActivity = ((ProcessDefinitionImpl) processDefinition).findActivity(taskNow.getTaskDefinitionKey());
		ActivityImpl endActivity = ((ProcessDefinitionImpl) processDefinition).findActivity("closeService");
		List<PvmTransition> oriPvmTransitionList = clearTransition(currentActivity);
		TransitionImpl newTransition = currentActivity.createOutgoingTransition();
		newTransition.setDestination(endActivity);
		taskService.complete(taskId);
		endActivity.getIncomingTransitions().remove(newTransition);
		// 还原以前流向
		restoreTransition(currentActivity, oriPvmTransitionList);
		return tableQueryResult;
	}

	private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());
		return processDefinition;
	}

	private TaskEntity findTaskById(String taskId) {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();
		return oriPvmTransitionList;
	}

	private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}
}
