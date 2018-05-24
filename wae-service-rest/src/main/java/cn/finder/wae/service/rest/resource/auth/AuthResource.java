package cn.finder.wae.service.rest.resource.auth;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

public interface AuthResource {
	/**
	 * 根据用户名和密码登录获取user_id  如果user_id != 0 表示登录成功
	 * @return User
	 * 
	 * @throws ResourceException
	 */
	//  /auth/authority.json?username=xx&pwd=xxx
	@Post
	@Path("/auth/authority")
	@Produces({"application/json"})
	public Representation authority(Representation representation) throws ResourceException;
}
