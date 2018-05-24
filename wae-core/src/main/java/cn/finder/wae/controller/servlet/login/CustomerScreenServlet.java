package cn.finder.wae.controller.servlet.login;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/****
 * 大屏默认登录用户 登录入口
 * @author wuhualong
 *
 */
public class CustomerScreenServlet extends HttpServlet{

	
	private String userName;
	
	private String password;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 736157191599096559L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	//	resp.setContentType("text/html;charset=UTF-8");
		req.setAttribute("user.account", userName);
		req.setAttribute("user.password", password);
		//String target = "/auth/loginBg.action?user.account="+userName+"&user.password="+password;
		
		
		
		String target =req.getContextPath()+"/auth/loginBg.action?user.account="+userName+"&user.password="+password;
		//req.getRequestDispatcher("/auth/loginBg.action").forward(req, resp);
		
		resp.sendRedirect(target);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		userName = config.getInitParameter("userName");
		password= config.getInitParameter("password");
		
	}

}
