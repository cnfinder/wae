package cn.finder.wae.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * @author wu hualong
 * {@value} 此拦截器主要是 记录用户访问 的信息 ,如果用户登录 ，那么 就要保存用户 ID
 *
 */
public class VisitLoggerInterceptor  extends  MethodFilterInterceptor{

	private static final long serialVersionUID = 1145413605605474293L;

	@Override
	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		
		
		
		new VisitLoggerThread(new VisitLog()).start();
		
		
		
		return actionInvocation.invoke();
	}
	
	
	/***
	 * 异步写入用户操作习惯
	 * 
	 *
	 */
	private  class VisitLoggerThread extends Thread
	{
		private VisitLog visitLog;

		public VisitLoggerThread(VisitLog visitLog) {
			this.visitLog = visitLog;
		}

		@Override
		public void run() {
			
			//具体实现
		}
		
		
		
		
	}

	class VisitLog
	{
		
	}
}
