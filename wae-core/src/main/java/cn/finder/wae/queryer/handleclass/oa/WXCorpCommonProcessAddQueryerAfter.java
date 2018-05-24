package cn.finder.wae.queryer.handleclass.oa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.context.support.WebApplicationContextUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.Common;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * 
 * @author lizhi
 * @function:微信企业号通知OA上报
 */
public class WXCorpCommonProcessAddQueryerAfter extends QueryerDBAfterClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);
	ActivitiServiceManager activitiServiceManager = AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		logger.info("========WXCorpCommonProcessAddQueryerAfter");
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();

		String proc_key = data.get("proc_key").toString();
		String wx_appid = data.get("wx_appid").toString();
		String business_key = data.get("business_key").toString();
		String user_id = data.get("user_id").toString();
		String handler = data.get("handler").toString();
		String request_content = data.get("remark").toString();
		String title = data.get("title").toString();
		String proc_id = data.get("proc_id").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business_key", business_key);
		map.put("nowDate", Common.formatDate(new Date(), "MM-dd"));
		map.put("proc_key", proc_key);
		map.put("proc_id", proc_id);
		map.put("wx_appid", wx_appid);
		map.put("title", title);// 流程实例名
		map.put("host", ArchCache.getInstance().getUserConfigCache().get("user_config_iv_app_host").getValue());

		map.put("request_id", user_id);// 请求人id
		map.put("request_name", commonService.findUser(user_id).getName());// 请求人
		map.put("request_content", request_content);// 请求内容
		map.put("pre_handler_id", "");// 前一位处理人id
		map.put("pre_handler_name", "");// 前一位处理人,初始为空
		map.put("pre_content", "");// 前一位处理人审批内容,初始为空
		map.put("handler_id", handler);// 处理人id

		if (proc_key.equals("OAProcess")) {
			map.put("handler_name", commonService.findUser(handler).getName());// 处理人
		} else if (proc_key.equals("OAProcessJoinHear") || proc_key.equals("OAProcessRead")) {
			List<String> users = new ArrayList<String>();
			for (String user : handler.split(",")) {
				users.add(user);
			}
			map.put("users", users);
		}
		map.put("handle_result", "");// 处理结果

		RuntimeService runtimeService = activitiServiceManager.getRuntimeService();
		TaskService taskService = activitiServiceManager.getTaskService();
		activitiServiceManager.getIdentityService().setAuthenticatedUserId(user_id);
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(proc_key, business_key, map);
		runtimeService.setProcessInstanceName(instance.getId(), title);

		if (proc_key.equals("OAProcessRead")) {
			// 审阅流程自动完成
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
			for (Task task : tasks) {
				taskService.complete(task.getId());
			}
		}

		return tableQueryResult;

	}

}
