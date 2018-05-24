package cn.finder.wae.api.sdk.service;

import cn.finder.httpcommons.IClient;
import cn.finder.wae.api.sdk.DNLocation;
import cn.finder.wae.api.sdk.domain.User;
import cn.finder.wae.api.sdk.request.CheckSessionRequest;
import cn.finder.wae.api.sdk.request.UserLoginRequest;
import cn.finder.wae.api.sdk.response.CheckSessionResponse;
import cn.finder.wae.api.sdk.response.UserLoginResponse;
import cn.finder.wae.httpcommons.ApiConfig.ServiceInterfaceConfig;
import cn.finder.wae.httpcommons.HttpPostClient;

public class AuthService {

	/***
	 * 用户登录
	 * 登录后 获取到的session 有效期为7200秒,需要用户自己维护 session
	 * @param userName  user_name or phone
	 * @param password
	 * @return
	 */
	public User userLogin(String userName, String password) {

		IClient client = new HttpPostClient(ServiceInterfaceConfig.getAuthInterfaceUrl(DNLocation.DN_API), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

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
	 * 获取 session 是否有效,如果无效 需要重新登录获取 最新 session 
	 * @param session
	 * @return  true->有效   else 无效
	 */
	public boolean checkSession(String session) {

		IClient client = new HttpPostClient(ServiceInterfaceConfig.getCheckSessionUrl(DNLocation.DN_API), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		CheckSessionRequest req = new CheckSessionRequest();

		CheckSessionResponse resp = client.execute(req,session);

		return resp.isValid();

	}
	/***
	 * 刷新 session 
	 * @param session
	 * @return  如果刷新成功 返回 User 对象 ，最新的session 也在其中
	 */
	public User refreshSession(String session) {

		IClient client = new HttpPostClient(ServiceInterfaceConfig.getRefreshSessionUrl(DNLocation.DN_API), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);

		CheckSessionRequest req = new CheckSessionRequest();

		CheckSessionResponse resp = client.execute(req,session);

		return resp.getUser();

	}
	

}
