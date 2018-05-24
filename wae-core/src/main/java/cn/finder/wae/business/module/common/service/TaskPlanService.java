package cn.finder.wae.business.module.common.service;

import java.util.List;

import cn.finder.wae.business.domain.TaskPlan;

public interface TaskPlanService {

	
	public List<TaskPlan> list();
	
	
	public int updateResetStatus(long taskPlanId);
	
	
	public void updateTaskPlanUpdate(List<TaskPlan> taskPlans);
	
	/***
	 *  设置 随服务器启动的记录状态到运行
	 */
	public void updateStatusToRunForStartWithServer();
	
	/***
	 * 更新某参数的值
	 * @param taskPlanId
	 * @param param_name
	 * @param param_value
	 * @return
	 */
	public int updateParamValue(long taskPlanId,String param_name,String param_value);
	
	/***
	 * 获取一条任务计划
	 * @param taskPlanId
	 * @return
	 */
	public TaskPlan findTaskPlan(long taskPlanId);
}
