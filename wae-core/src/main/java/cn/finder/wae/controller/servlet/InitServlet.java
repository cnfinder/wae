package cn.finder.wae.controller.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/***
 * 服务器启动 初始化或者重置一些数据
 * @author Administrator
 *
 */
@Deprecated
public class InitServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(InitServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;


	public void init(ServletConfig servletConfig) throws ServletException {
		
		logger.debug("====InitServlet init..");
		
		ServletContext ctxt=servletConfig.getServletContext();
		
		
		new InitThread(ctxt).start();

		
	}
	
	private void doInit(ServletContext ctxt){
		logger.debug("Iron arch CacheServlet.init  start....  ");
		//SysService sysService =WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("sysService",SysService.class);
		
		String sql ="{call proc_data_import_status_reset()}";
		
		
		
		
	}
	
	class InitThread extends Thread
	{
		private ServletContext ctxt;
		
		
		

		public InitThread(ServletContext ctxt) {
			super();
			this.ctxt = ctxt;
		}


		@Override
		public void run() {
			doInit(ctxt);
			
		}
		
	}

}
