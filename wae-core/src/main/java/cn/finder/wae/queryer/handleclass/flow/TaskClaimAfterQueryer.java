package cn.finder.wae.queryer.handleclass.flow;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.IdentityLink;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:
 * @function: 签收操作处理
 */
public class TaskClaimAfterQueryer extends QueryerDBAfterClass {

	/*
	 * (non-Javadoc)
	 * @see
	 * cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.
	 * TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult, long showTableConfigId, QueryCondition<Object[]> condition) {
		super.handle(tableQueryResult, showTableConfigId, condition);

		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>) condition).getMapParas();

		String taskId = data.get("taskId").toString();
		String userId = "";
		try {
			userId = data.get("userId").toString();
		} catch (Exception e) {

			User user = (User) data.get("session_user");
			if (user != null)
				userId = user.getAccount();
		}

		if (!userId.equals("")) {
			ActivitiServiceManager activitiServiceManager = AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
			List<IdentityLink> links = activitiServiceManager.getTaskService().getIdentityLinksForTask(taskId);
			int flag = -1;
			for (IdentityLink link : links) {
				String user_temp = link.getUserId();
				if (user_temp.equals(userId)) {
					flag = 1;
					activitiServiceManager.getTaskService().claim(taskId, userId);
					tableQueryResult.getMessage().setMsg("签收成功");
					break;
				}
			}
			if(flag == -1){
				tableQueryResult.getMessage().setMsg("您没有签收权限，请重新登录");
			}
			
		} else {
			tableQueryResult.getMessage().setMsg("签收失败，请重新登录");
		}

		return tableQueryResult;
	}
}
