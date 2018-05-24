package cn.finder.wae.common.aspect.logger;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.OperationLog;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.OperationLogService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.SysConfigCache;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.constant.Constant;
import cn.finder.wae.common.thread.AppContent;

/**
 * log to t_operation_log
 * 
 * field: 
 * user_id(userId)  可以为空
 * user_name(account) 可能为空,后台操作DAO的时候，默认用户名为: system_thread
 * log_level, 日志级别， 操作日志为 1， 警告日志为2， 错误日志为3
 * process_class  处理的类完全名称
 * method_signature  方法签名(包括参数以及返回值)
 * arguments_value JSON或者XML序列化格式,字段名称可以从 showdataconfig中获取（用于ArchType）
 * ref_id(showtableConfigId)  业务ID ，这里使用 showtableConfigId  以便获取操作名称,一般为第一个参数
 * operation_name(showtableConfig_Name) 操作名称， 用户定义或者 showtableConfig中获取
 * operation_date 操作时间
 * create_date 创建日期	
 * update_date 更新日期
 * 
 * 
 * @author wuhualong
 *
 */
public class OperationLoggerAspect {
	private final static Logger logger=Logger.getLogger(OperationLoggerAspect.class);
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		
		
		
		
		Object returnVal = joinPoint.proceed();//1
		
		Signature signature = joinPoint.getSignature();
		Object target=joinPoint.getTarget();//cn.iron.rms.business.dao.impl.EquipmentDaoImpl@95a8c3

		Object[] argValues = joinPoint.getArgs();
		
		HttpSession session  = AppContent.getSession();
		OperationLogService operationLogService = null;
		String userId=null;
		String userName=null;
		operationLogService=AppApplicationContextUtil.getContext().getBean("operationLogService",OperationLogService.class);
		if(session != null){
			//operationLogService =WebApplicationContextUtils.getWebApplicationContext(AppContent.getSession().getServletContext()).getBean("operationLogService",OperationLogService.class);
			
			
			User user = (User)session.getAttribute(Constant.KEY_SESSION_USER);
			//可以读取到
			if(user!=null){
				logger.debug("=======user id:"+user.getAccount());
				userId = user.getId()+"";
				userName= user.getAccount();
				
			}
		}else{
			userName="unknown";
		}
		
		int opeLogEnable = ArchCache.getInstance().getSysConfigCache().get(SysConfigCache.KEY_CONFIG_OPERATIONLOG_ENABLE).getIntValue();
		
		if(opeLogEnable==1){
			
			new LoggerProcess(target, operationLogService, signature, argValues,userId,userName).log();
		}
		return returnVal;
		
	}
	
	
	
	
	class LoggerProcess 
	{
		private Object target;
		
		private OperationLogService operationLogService;
		
		private Signature signature;
		

		private Object[] argValues;
		
		private String userId;
		
		private String userName;
		
		public LoggerProcess(Object target,OperationLogService operationLogService,Signature signature,Object[] argValues,
				String userId,String userName) {
			this.target =target;
			this.operationLogService = operationLogService;
			this.signature = signature;
			this.argValues = argValues;
			this.userId = userId;
			this.userName = userName;
		}
		
		public void log()
		{
			try
			{
			
				boolean isAnnoFlag = false;
				
				
				MethodSignature methodSignature = (MethodSignature) signature;  
				Method method = methodSignature.getMethod(); 
				
				
				FinderLogger finderLogger = method.getAnnotation(FinderLogger.class);
				
				if(finderLogger ==null){
					
					//实现类判断
					
					method=target.getClass().getMethod(method.getName(), method.getParameterTypes());
					
					finderLogger = method.getAnnotation(FinderLogger.class);
					if(finderLogger !=null){
						isAnnoFlag = true;
					}else{
						isAnnoFlag = false;
					}
				
				}
				else{
					isAnnoFlag = true;
				}
				
				if(isAnnoFlag){
					
					
					
					
					//参数值对象
					Object arguments_value="{}";
					OperationLog log =new OperationLog();
					
					log.setUserId(userId);
					log.setUserName(userName);
					
					
					ArchType archType = finderLogger.archType();
					if(archType == ArchType.NORMAL){
						
						log.setProcessClass(target.getClass().getName());
						log.setOperationName(finderLogger.name());
						
						String[] argNames =finderLogger.argNames();
						if(argValues!=null && argValues.length>0){
							Map<String,Object> arg_values =new HashMap<String, Object>();
							for(int i=0;i<argValues.length;i++){
								String argName =argNames[i]==null?(i+""):argNames[i];
								arg_values.put(argName, argValues[i]);
							}
							
							arguments_value=arg_values;
						}
						
						
					}else if(archType==ArchType.FINDER_ARCH){
						//基础架构
						long showtableConfigId = (Long)(argValues[0]);
						
						log.setRefId(showtableConfigId+"");
						
						@SuppressWarnings("unchecked")
						QueryCondition<Object[]> condition = (QueryCondition<Object[]>)argValues[1];
						
						ShowTableConfig showTableConfig=ArchCache.getInstance().getShowTableConfigCache().get(showtableConfigId);
						String queryClass=showTableConfig.getProcessCommand();
						
						
						method=Class.forName(queryClass).getMethod(method.getName(), method.getParameterTypes());
						
						finderLogger = method.getAnnotation(FinderLogger.class);
						
						if(finderLogger ==null){
							isAnnoFlag = false;
						}else{
							log.setProcessClass(queryClass);
							log.setOperationName(showTableConfig.getName());
							//arguments_value = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
							if(condition instanceof MapParaQueryConditionDto)
							{
								@SuppressWarnings("unchecked")
								MapParaQueryConditionDto<String, Object> cond =((MapParaQueryConditionDto<String, Object>)condition);
								Map<String,Object> args_value =cond.getMapParas();
								args_value.remove("session_user");
								//arguments_value.put("queryCondition", condition);
								
								List<ShowDataConfig> showDataConfigs = showTableConfig.getShowDataConfigs();
								
								
								Set<Entry<String, Object>> sets  = args_value.entrySet();
								
								Iterator<Entry<String, Object>> ite = sets.iterator();
								
								Map<String,Object> repMap =new HashMap<String,Object>();
								while(ite.hasNext())
								{
									Entry<String,Object> entry = ite.next();
									
									String key = entry.getKey();
									Object val = entry.getValue();
									
									//替换字段名称为中文名称
									for(ShowDataConfig showDataConfig:showDataConfigs){
										String fieldName = showDataConfig.getFieldName();
										if(fieldName.equalsIgnoreCase(key)){
											ite.remove();
											repMap.put(showDataConfig.getFieldNameAlias(), val);
										}
									}
									
								}
								if(repMap.size()>0){
									args_value.putAll(repMap);
								}
								
								arguments_value = cond;
							}
							else{
								arguments_value = condition;
	
							//	ShowDataConfig showDataConfig =PSISCache.getInstance().getShowTableConfigCache().getPKShowDataConfig(showtableConfigId);
								
								//showDataConfig.getFieldNameAlias();
							}
							
						}
						
						
					}
					
					if(isAnnoFlag){
						JsonConfig jsonConfig = new JsonConfig();
						
						jsonConfig.setIgnoreDefaultExcludes(false);//DEFAULT_EXCLUDES    "class", "declaringClass",  "metaClass" 
					    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); //去掉自包含
					    
					    jsonConfig.registerJsonBeanProcessor(Date.class,  
					            new JsDateJsonBeanProcessor()); // 当输出时间格式时，采用和JS兼容的格式输出  
					    
					    Object o  =  JSONSerializer.toJSON(arguments_value, jsonConfig);
					    
					    log.setArgumentsValue(o.toString());
					    
					    
						
						
						log.setLogLevel(1);
						log.setMethodSignature(signature.toLongString());
						
						
						log.setOperationDate(new Date());
						log.setCreateDate(new Date());
						log.setUpdateDate(new Date());
						
						
						
						
						operationLogService.addOperationLog(log);
					}
					
					
					
				}
			}
			
			catch(Exception e)
			{
				logger.error(e);
			}
			
		}
		
		public void logInThread()
		{
			new  LoggerThread(target, operationLogService, signature, argValues, userId, userName).start();
		}
	}
	
	
	class LoggerThread extends Thread
	{
		
		private Object target;
		
		private OperationLogService operationLogService;
		
		private Signature signature;
		

		private Object[] argValues;
		
		private String userId;
		
		private String userName;
		
		public LoggerThread(Object target,OperationLogService operationLogService,Signature signature,Object[] argValues,
				String userId,String userName) {
			this.target =target;
			this.operationLogService = operationLogService;
			this.signature = signature;
			this.argValues = argValues;
			this.userId = userId;
			this.userName = userName;
		}
 
		@Override
		public void run() {
			
			new LoggerProcess(target, operationLogService, signature, argValues, userId, userName).log();
			
		}
		
		
		
	}
	
	
}
