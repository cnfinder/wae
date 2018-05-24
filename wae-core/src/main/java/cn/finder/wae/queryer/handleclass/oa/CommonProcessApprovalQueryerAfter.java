package cn.finder.wae.queryer.handleclass.oa;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.springframework.web.context.support.WebApplicationContextUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.oaFlow.OAFlowTaskRecord;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * 
 * @author lizhi
 * @function:处理OA流程
 */
public class CommonProcessApprovalQueryerAfter extends QueryerDBAfterClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);
	ActivitiServiceManager activitiServiceManager = AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();

		String taskId = data.get("taskId").toString();
		String business_key = data.get("business_key").toString();
		int status = Integer.parseInt(data.get("status").toString());
		String user_id = data.get("user_id").toString();
		String content = data.get("content").toString();
		String handler_id = data.get("handler").toString();

		OAFlowTaskRecord taskRecord = new OAFlowTaskRecord();
		taskRecord.setBusiness_key(business_key);
		taskRecord.setTask_id(taskId);
		taskRecord.setUser_id(user_id);
		taskRecord.setContent(content);

		String handler_result = "";

		TaskService taskService = activitiServiceManager.getTaskService();
		Map<String, Object> map = new HashMap<String, Object>();
		String pre_handler_name = commonService.findUser(user_id).getName();
		map.put("pre_handler_id", user_id);
		map.put("pre_handler_name", pre_handler_name);
		map.put("pre_content", content);
		tableQueryResult.getMessage().setMsg("处理成功");
		if (status == 1) {
			handler_result = "同意";
			map.put("handle_result", "同意");
			map.put("complete", false);
			map.put("handler_id", handler_id);
			map.put("handler_name", commonService.findUser(handler_id).getName());
			taskService.complete(taskId, map);
		} else if (status == 2) {
			handler_result = "驳回";
			map.put("handle_result", "驳回");
			map.put("complete", false);
			try {
				String ph_id = taskService.getVariable(taskId, "pre_handler_id").toString();
				map.put("handler_id", ph_id);
				map.put("handler_name", commonService.findUser(ph_id).getName());
				taskService.complete(taskId, map);
			} catch (Exception e) {
				map.put("complete", true);
				handler_result = "结束流程";
				map.put("handle_result", "结束流程");
				taskService.complete(taskId, map);
			}
		} else if (status == 3) {
			handler_result = "结束流程";
			map.put("complete", true);
			map.put("handle_result", "结束流程");
			taskService.complete(taskId, map);
		} else if (status == 4) {// 会审确认
			handler_result = data.get("joinhear_result").toString();
			taskService.complete(taskId);
		}
		taskRecord.setTask_result(handler_result);
		commonService.addOAFlowTaskRecord(taskRecord);
		return tableQueryResult;

	}

}
