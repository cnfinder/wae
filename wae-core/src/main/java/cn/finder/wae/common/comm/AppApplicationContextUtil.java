package cn.finder.wae.common.comm;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppApplicationContextUtil implements ApplicationContextAware {

	// 声明一个静态变量保存

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext context) throws BeansException {

		AppApplicationContextUtil.context=context;
	}
	public static ApplicationContext getContext(){
		
		return context;
	}
}
