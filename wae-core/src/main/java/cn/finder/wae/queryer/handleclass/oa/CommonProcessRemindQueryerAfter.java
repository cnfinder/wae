package cn.finder.wae.queryer.handleclass.oa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.web.context.support.WebApplicationContextUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.TemplateMsg;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.templatemsg.WXCorpTemplateMsgSender;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * 
 * @author lizhi
 * @function:发送消息
 */
public class CommonProcessRemindQueryerAfter extends QueryerDBAfterClass {

	CommonService commonService = WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("commonService", CommonService.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();

		String business_key = data.get("business_key").toString();

		ActivitiServiceManager activitiServiceManager = AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
		TaskService taskService = activitiServiceManager.getTaskService();
		try {
			Task task = taskService.createTaskQuery().processInstanceBusinessKey(business_key).singleResult();
			Map<String, Object> variables = taskService.getVariables(task.getId());
			TemplateMsg tm = commonService.findTemplateMsg("TM_WXCORP_OA_REMIND");
			WXCorpTemplateMsgSender wxCorpTemplateMsgSender = new WXCorpTemplateMsgSender(tm);
			wxCorpTemplateMsgSender.send(variables);
		} catch (Exception e) {
			Map<String,Object> rest = new HashMap<String, Object>();
			rest.put("result", "流程已经结束");
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			resultList.add(rest);
			tableQueryResult.setResultList(resultList);
		}

		return tableQueryResult;

	}

}
