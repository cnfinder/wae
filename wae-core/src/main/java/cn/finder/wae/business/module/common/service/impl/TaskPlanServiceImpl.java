package cn.finder.wae.business.module.common.service.impl;

import java.util.List;

import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.module.common.dao.TaskPlanDao;
import cn.finder.wae.business.module.common.service.TaskPlanService;

public class TaskPlanServiceImpl implements TaskPlanService{

	private TaskPlanDao taskPlanDao;
	
	public void setTaskPlanDao(TaskPlanDao taskPlanDao){
		this.taskPlanDao = taskPlanDao;
	}
	@Override
	public List<TaskPlan> list() {
		// TODO Auto-generated method stub
		return taskPlanDao.list();
	}
	@Override
	public int updateResetStatus(long taskPlanId) {
		// TODO Auto-generated method stub
		return taskPlanDao.resetStatus(taskPlanId);
	}
	@Override
	public void updateTaskPlanUpdate(List<TaskPlan> taskPlans) {
		// TODO Auto-generated method stub
		taskPlanDao.updateTaskPlanUpdate(taskPlans);
	}
	@Override
	public void updateStatusToRunForStartWithServer() {
		taskPlanDao.updateStatusToRunForStartWithServer();
		
	}
	@Override
	public int updateParamValue(long taskPlanId, String param_name, String param_value) {
		// TODO Auto-generated method stub
		return taskPlanDao.updateParamValue(taskPlanId, param_name, param_value);
	}
	@Override
	public TaskPlan findTaskPlan(long taskPlanId) {
		// TODO Auto-generated method stub
		return taskPlanDao.findTaskPlan(taskPlanId);
	}
	
	

}
