package cn.finder.wae.api.sdk.service;

import java.util.List;
import java.util.Map;

import cn.finder.httpcommons.IClient;
import cn.finder.httpcommons.response.ApiResponse;
import cn.finder.wae.api.sdk.domain.SoftVersion;
import cn.finder.wae.api.sdk.domain.User;
import cn.finder.wae.api.sdk.request.FindAppMenuListRequest;
import cn.finder.wae.api.sdk.request.FindFlowTaskListRequest;
import cn.finder.wae.api.sdk.request.FindFlowTaskVariablesRequest;
import cn.finder.wae.api.sdk.request.FindHistoryTaskRequest;
import cn.finder.wae.api.sdk.request.FindMenuIconRequest;
import cn.finder.wae.api.sdk.request.FindSoftVersionListRequest;
import cn.finder.wae.api.sdk.request.FindUserImgRequest;
import cn.finder.wae.api.sdk.request.FindUserInfoRequest;
import cn.finder.wae.api.sdk.request.FlowOrderworkerOrTransferRequest;
import cn.finder.wae.api.sdk.request.FlowOrderworkerRequest;
import cn.finder.wae.api.sdk.request.FlowTaskClaimRequest;
import cn.finder.wae.api.sdk.request.FlowTaskCompleteRequest;
import cn.finder.wae.api.sdk.request.ProcessInstStatusUpdate;
import cn.finder.wae.api.sdk.request.UpdateUserPwdRequest;
import cn.finder.wae.api.sdk.request.UserLoginRequest;
import cn.finder.wae.api.sdk.request.UserSearchRequest;
import cn.finder.wae.api.sdk.response.FindAppMenuListResponse;
import cn.finder.wae.api.sdk.response.FindFlowTaskListResponse;
import cn.finder.wae.api.sdk.response.FindFlowTaskVariablesResponse;
import cn.finder.wae.api.sdk.response.FindHistoryTaskResponse;
import cn.finder.wae.api.sdk.response.FindSoftVersionListResponse;
import cn.finder.wae.api.sdk.response.FindUserInfoResponse;
import cn.finder.wae.api.sdk.response.FlowOrderworkerOrTransferResponse;
import cn.finder.wae.api.sdk.response.FlowOrderworkerResponse;
import cn.finder.wae.api.sdk.response.FlowTaskClaimResponse;
import cn.finder.wae.api.sdk.response.FlowTaskCompleteResponse;
import cn.finder.wae.api.sdk.response.UpdateUserPwdResponse;
import cn.finder.wae.api.sdk.response.UserLoginResponse;
import cn.finder.wae.api.sdk.response.UserSearchResponse;
import cn.finder.wae.httpcommons.ApiConfig.ServiceInterfaceConfig;
import cn.finder.wae.httpcommons.HttpPostClient;

public class ApiCommonService {

	/***
	 * 用户登录认证
	 * 
	 * @param req
	 * @return
	 */
	public User userLogin(String userName, String password) {

		IClient client = new HttpPostClient(ServiceInterfaceConfig.getAuthInterfaceUrl(), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		UserLoginRequest req = new UserLoginRequest();
		req.setAccount(userName);
		req.setPassword(password);

		UserLoginResponse resp = client.execute(req);
		User user = null;
		if (resp.isSuccess()) {
			user = resp.getUser();
		}
		return user;

	}

	/***
	 * 用户信息查询
	 * 
	 * @param req
	 * @return
	 */
	public UserSearchResponse userSearch(UserSearchRequest req) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		UserSearchResponse resp = client.execute(req);

		return resp;

	}

	/***
	 * 获取用户头像
	 * 
	 * @param req
	 * @return
	 */
	public String findUserImg(String account) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceStreamUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		FindUserImgRequest req = new FindUserImgRequest();
		req.setAccount(account);

		ApiResponse resp = client.execute(req);

		return resp.getBody();

	}

	/***
	 * 获取用户具体信息
	 * 
	 * @param account
	 * @param session
	 * @return
	 */
	public User findUserInfo(String account, String session) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		FindUserInfoRequest req = new FindUserInfoRequest();
		req.setAccount(account);

		FindUserInfoResponse resp = client.execute(req, session);
		User user = null;
		if (resp.isSuccess()) {
			user = resp.getUser();
		}
		return user;
	}

	/***
	 * 获取流程用户任务
	 * 
	 * @param userId
	 *        用户ID
	 * @return
	 */
	public FindFlowTaskListResponse findFlowTasks(String userId, int pageSize, int pageIndex) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FindFlowTaskListRequest req = new FindFlowTaskListRequest();
		req.setP_user_id(userId);
		req.setPageSize(pageSize);
		req.setPageIndex(pageIndex);
		FindFlowTaskListResponse resp = client.execute(req);

		return resp;
	}

	/***
	 * 流程任务签收
	 * 
	 * @param userId
	 * @param taskId
	 * @return if true -签收成功 else 签收失败
	 */
	public boolean flowTaskClaim(String userId, String taskId) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		FlowTaskClaimRequest req = new FlowTaskClaimRequest();
		req.setUserId(userId);
		req.setTaskId(taskId);

		FlowTaskClaimResponse resp = client.execute(req);

		if (resp.isSuccess()) {
			if ((Integer) resp.getTag() == 1) {
				return true;
			}
		}
		return false;

	}

	/***
	 * 流程任务完成提交
	 * 
	 * @param taskId
	 * @param variables
	 * @return if true 任务提交成功 ， else 任务提交失败
	 */
	@Deprecated
	public boolean flowTaskComplete(String taskId, List<Map<String, Object>> variables) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FlowTaskCompleteRequest req = new FlowTaskCompleteRequest();
		req.setTaskId(taskId);
		req.addVariable(variables);
		FlowTaskCompleteResponse resp = client.execute(req);
		if (resp.isSuccess()) {
			if ((Integer) resp.getTag() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 流程任务完成提交或撤销
	 * @param taskId 任务id
	 * @param rr_remark 备注
	 * @param business_key 业务主键（任务列表可以获取到）
	 * @param user_id 当前登录者用户名
	 * @param variables 主要存储变量condition
	 * @return
	 */
	public boolean flowTaskCompleteOrRevocation(String taskId, String rr_remark, String business_key, String user_id, List<Map<String, Object>> variables) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FlowTaskCompleteRequest req = new FlowTaskCompleteRequest();
		req.setTaskId(taskId);
		req.addVariable(variables);
		req.setRr_remark(rr_remark);
		req.setBusiness_key(business_key);
		req.setUser_id(user_id);
		FlowTaskCompleteResponse resp = client.execute(req);
		if (resp.isSuccess()) {
			if ((Integer) resp.getTag() == 1) {
				return true;
			}
		}
		return false;
	}

	/***
	 * 获取流程任务 变量
	 * 
	 * @param taskId
	 *        任务ID
	 * @return
	 */
	public Map<String, Object> findFlowTaskVariable(String taskId) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		FindFlowTaskVariablesRequest req = new FindFlowTaskVariablesRequest();
		req.setTaskId(taskId);
		req.setPageIndex(1);
		req.setPageSize(1000000);

		FindFlowTaskVariablesResponse resp = client.execute(req);

		if (resp.isSuccess()) {

			return resp.getVariables();
		}
		return null;
	}

	/**
	 * 获取软件版本
	 * 
	 * @param file_group_key
	 *        : 软件分组标记
	 * @return
	 */
	public SoftVersion findSoftVersion(String file_group_key) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FindSoftVersionListRequest req = new FindSoftVersionListRequest();
		req.setFile_group_key(file_group_key);
		FindSoftVersionListResponse resp = client.execute(req);
		SoftVersion apkVersion = null;
		if (resp.isSuccess()) {
			if (resp.getEntities() != null && resp.getEntities().size() > 0) {
				apkVersion = resp.getEntities().get(0);
			}
		}
		return apkVersion;

	}

	/***
	 * 获取动态工作流图
	 * 
	 * @param processInstanceId
	 *        流程实例ID
	 * @return 图片地址
	 */
	public String findDynamicWorkflowGrapic(String processInstanceId) {
		String path = ServiceInterfaceConfig.getContextRootUrl() + "" + "/activiti/workflow/dynamic_grapic.png?processInstanceId=" + processInstanceId;
		return path;
	}

	/***
	 * 获取完成的工作流图
	 * 
	 * @param processInstanceId
	 *        流程实例ID
	 * @return 图片地址
	 */
	public String findCompleteWorkflowGrapic(String processInstanceId) {
		String path = ServiceInterfaceConfig.getContextRootUrl() + "" + "/activiti/workflow/dynamic_grapic_complete.png?processInstanceId=" + processInstanceId;

		return path;
	}

	/***
	 * 激活或者挂起流程实例
	 * 
	 * @param processInstanceId
	 *        流程实例id
	 * @param activeOrSuspend
	 *        激活或者挂起标记
	 * @return
	 */
	public boolean activeOrSuspendProcessInst(String processInstanceId, String status) {

		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		ProcessInstStatusUpdate req = new ProcessInstStatusUpdate();
		req.setProcessInstanceId(processInstanceId);
		req.setStatus(status);
		ApiResponse resp = client.execute(req);
		if (resp.isSuccess()) {
			return true;
		}

		return false;
	}

	/**
	 * 工程主管指派员工
	 * 
	 * @param business_key
	 *        业务主键，即报修单号
	 * @param worker_account
	 *        指派人员账号
	 * @return
	 */
	@Deprecated
	public boolean flowOrderworker(String business_key, String worker_account) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FlowOrderworkerRequest req = new FlowOrderworkerRequest();
		req.setBusiness_key(business_key);
		req.setWorker_account(worker_account);
		FlowOrderworkerResponse resp = client.execute(req);
		if (resp.isSuccess()) {
			if ((Integer) resp.getTag() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 工程主管指派员工或者转发
	 * 
	 * @param request
	 * @return
	 */
	public boolean flowOrderworkerOrTransfer(FlowOrderworkerOrTransferRequest request) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FlowOrderworkerOrTransferResponse resp = client.execute(request);
		if (resp.isSuccess()) {
			if ((Integer) resp.getTag() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询历史任务
	 * 
	 * @param worker_account账号
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public FindHistoryTaskResponse flowHistoryTask(String worker_account, int pageIndex, int pageSize) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FindHistoryTaskRequest req = new FindHistoryTaskRequest();
		req.setPageSize(pageSize);
		req.setPageIndex(pageIndex);
		req.setAssignee(worker_account);
		FindHistoryTaskResponse resp = client.execute(req);
		return resp;
	}

	/**
	 * 用户密码修改
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public UpdateUserPwdResponse userPwdUpdate(String account, String oldPwd, String newPwd) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrl("fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		UpdateUserPwdRequest req = new UpdateUserPwdRequest();
		req.setAccount(account);
		req.setOldPwd(oldPwd);
		req.setNewPwd(newPwd);
		UpdateUserPwdResponse resp = client.execute(req);
		return resp;
	}
	
	
	
	/**
	 * 根据菜单图片
	 * 
	 * @param menu_id
	 * @return
	 */
	public String findMenuIcon(String url,int menu_id) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceStreamUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FindMenuIconRequest req = new FindMenuIconRequest();
		req.setId(menu_id);
		ApiResponse resp = client.execute(req);
		return resp.getBody();
	}
	
	
	
	/**
	 * 获取App菜单列表
	 * 
	 * @param code
	 *        APP的代码,智爱物业:ZAWYAPP
	 * @param user_id
	 * @return
	 */
	public FindAppMenuListResponse findAppMenuList(String url,String code, String user_id) {
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		FindAppMenuListRequest req = new FindAppMenuListRequest();
		req.setCode(code);
		req.setUser_id(user_id);
		return client.execute(req);
	}

}
