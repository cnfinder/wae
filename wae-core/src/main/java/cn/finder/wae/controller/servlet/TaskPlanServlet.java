package cn.finder.wae.controller.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.finder.wae.business.domain.TaskPlan;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.QueryService;
import cn.finder.wae.business.module.common.service.TaskPlanService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.DateUtils;
import cn.finder.wae.common.comm.Reflection;
import cn.finder.wae.common.exception.WaeRuntimeException;
import cn.finder.wae.controller.listener.CustomedContextLoaderListener;
import cn.finder.wae.queryer.Queryer;

public class TaskPlanServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(TaskPlanServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847503911589250340L;

	
	TaskPlanService  taskPlanService=null;
	
	final static String THREAD_NAME_PREFIX="taskplan_";
	//private LinkedList<TaskPlanExecutor> taskPlanExecutorPool;
	private List<TaskPlanExecutor> taskPlanExecutorPool;
	private ServletContext ctxt_p;
	
	
	//定时调度器
	//Timer taskTimer = new Timer(); 
	//public  ScheduledExecutorService executorService=Executors.newScheduledThreadPool(10);
	
	public  ScheduledExecutorService executorService=null;
	private List<String> mustExecuteTaskIds=new ArrayList<String>();//必须执行的 taskId 
	public void init(ServletConfig servletConfig) throws ServletException {
		
		executorService=CustomedContextLoaderListener.getExecutorService();
		
		ServletContext ctxt=servletConfig.getServletContext();
		ctxt_p=ctxt;
		String enabled = servletConfig.getInitParameter("enabled");
		
	
		try{
			String executeIds=servletConfig.getInitParameter("executeIds");
			if(!StringUtils.isEmpty(executeIds)){
				String[] arr=executeIds.split(",");
				mustExecuteTaskIds= Arrays.asList(arr);
			}
		}catch(Exception e){
			
		}
		
		if("true".equalsIgnoreCase(enabled)){
			logger.debug("WAE arch TaskPlanServlet.init  start....  ");
			
			taskPlanService =WebApplicationContextUtils.getWebApplicationContext(ctxt).getBean("taskPlanService",TaskPlanService.class);
			
			try {
				//休眠  以便让缓存先加载好 
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new TaskPlanController(taskPlanService).start();
			
			
		}
		
		
		
	}
	
	
	@Override
	public void destroy() {
		
		
		if(!executorService.isShutdown()){
			try{
				executorService.shutdownNow();//不能使用shutdown()=>还是会把任务执行完毕
			}
			catch(Exception e){
				
			}
		}
			
		super.destroy();
	}


	/*
	 * 任务计划管理器
	 * 
	 */
	class TaskPlanController extends Thread
	{
		
		
		private TaskPlanService taskPlanService;
		
		
		
		
		public TaskPlanController(TaskPlanService taskPlanService)
		{
			this.taskPlanService = taskPlanService;
			//taskPlanExecutorPool =new LinkedList<TaskPlanServlet.TaskPlanExecutor>();
		//	taskPlanExecutorPool =new LinkedList<TaskPlanServlet.TaskPlanExecutor>();
			//taskPlanExecutorPool = (LinkedList<TaskPlanExecutor>) Collections.synchronizedCollection(new LinkedList<TaskPlanServlet.TaskPlanExecutor>());
			taskPlanExecutorPool = (List<TaskPlanExecutor>) Collections.synchronizedList(new ArrayList<TaskPlanServlet.TaskPlanExecutor>());
			
		}

		@SuppressWarnings("unused")
		@Override
		public void run() {
			
			//设置随服务器启动的任务 状态为运行
			taskPlanService.updateStatusToRunForStartWithServer();
			
			while(true)
			{
				
				 try{
					 if(!CustomedContextLoaderListener.getRunState()){
						 return;
					 }
				 }catch(Throwable e){
					 return;
				 }
				
				try
				{
	
					
					
					
					List<TaskPlan> taskPlans = taskPlanService.list();
					
					if(mustExecuteTaskIds!=null && mustExecuteTaskIds.size()>0){
						//如果 此 集合存在 那么只运行指定的任务 其他的任务不执行
						
						List<TaskPlan> executeTask=new ArrayList<TaskPlan>();
						
						for(TaskPlan tp_item:taskPlans){
							
							for(String e_id:mustExecuteTaskIds){
								if(e_id.equalsIgnoreCase(tp_item.getId()+"")){
									tp_item.setStatus(1);//设置启动状态
									executeTask.add(tp_item);
									break;
								}
							}
						}
						if(taskPlans==null){
							taskPlans=new ArrayList<TaskPlan>();
									
						}
						
						if(taskPlans!=null)
							taskPlans.clear();
						
						
						taskPlans.addAll(executeTask);
					}
					
					
					
					//使用线程去重置update字段
					new TaskPlanResetUpdateThread(taskPlans).start();
					
					
					
					
					if(taskPlans !=null && taskPlans.size()>0)
					{
						
						
						//处理线程池  taskPlanExecutorPool  动态remove 这里是否哪边需要 安全操作
						for(TaskPlanExecutor tp:taskPlanExecutorPool)
						{
							
							/*if(tp ==null || tp.isCompleted == true)
							{
								taskPlanExecutorPool.remove(tp);
							}
							else{*/
								
								//1. 判断是否有删除任务计划，如果任务管理器中有就停止线程池中的线程
								boolean isRemoved = true;
								for(TaskPlan tplan:taskPlans)
								{
									if((THREAD_NAME_PREFIX + tplan.getId()).equalsIgnoreCase(tp.getName()))
									{
										isRemoved = false;
										break;
									}
								}
								if(isRemoved){
									tp.stopped();
									tp.remove();
								}
						}
						
						
						
						
						for(TaskPlan item:taskPlans){
							
							String threadName = THREAD_NAME_PREFIX+item.getId();
							
							//如果 后台已经设置任务停止，那么如果线程池中有的需要停止、删除
							if(item.getStatus()==0)
							{
								for(TaskPlanExecutor tp:taskPlanExecutorPool)
								{
									if(tp.getName().equalsIgnoreCase(threadName))
									{
										tp.stopped();
										
									}
								}
							}
							else{
								//状态
								//查找线程池中是否有
								//处理线程池
								boolean isInPool=false;
									for(TaskPlanExecutor tp:taskPlanExecutorPool)
									{
										if(tp.getName().equalsIgnoreCase(threadName))
										{
											
											
											
											//如果在任务管理器中，那么判断参数修改，如果参数修改，那么停止线程 重新启动线程
											
											if(item.getShowtableConfigId()!=tp.getItem().getShowtableConfigId()
													|| !item.getProcessClass().equals(tp.getItem().getProcessClass())
													|| !item.getParams().equals(tp.getItem().getParams())
													|| item.getDuration() != tp.getItem().getDuration()
													|| item.getTimes() != tp.getItem().getTimes())
											{
												//
												
												tp.stopped();
											}
											else{
												isInPool= true;
											}
											
											break;
										}
									}
								
								if(!isInPool)
								{
									TaskPlanExecutor taskPlanExecutor =new TaskPlanExecutor(threadName,item);
									
									if(item.getStartTime()==null || item.getStartTime().getTime() <=new Date().getTime()){
										
										item.setStartTime(new Date());
										
									}
									
									//taskTimer.scheduleAtFixedRate(taskPlanExecutor, item.getStartTime(), item.getDuration());
									
									long initDealy=System.currentTimeMillis()-item.getStartTime().getTime();
									
									if(initDealy<0){
										initDealy=0;
									}
									/*
									 * 
									 * 
									 * ScheduledExecutorService 中两种最常用的调度方法 ScheduleAtFixedRate 和 ScheduleWithFixedDelay。
									 * ScheduleAtFixedRate 每次执行时间为上一次任务开始起向后推一个时间间隔，
									 * 即每次执行时间为 :initialDelay, initialDelay+period, initialDelay+2*period, …；ScheduleWithFixedDelay 
									 * 每次执行时间为上一次任务结束起向后推一个时间间隔，即每次执行时间为：initialDelay, initialDelay+executeTime+delay, initialDelay+2*executeTime+2*delay。
									 * 由此可见，ScheduleAtFixedRate 
									 * 是基于固定时间间隔进行任务调度，ScheduleWithFixedDelay 取决于每次任务执行的时间长短，是基于不固定时间间隔进行任务调度
									 */
								    ScheduledFuture<?>	scheduledFuture=executorService.scheduleAtFixedRate(taskPlanExecutor, initDealy, item.getDuration(), TimeUnit.MILLISECONDS);
									
									//scheduleAtFixedRate 情况下 如果时间 是现在时间之前的时间，那么之前的那段时间 也执行
									//schedule 之前一段时间不会执行 而是从当前的时间开始执行
									
									//taskTimer.scheduleAtFixedRate(taskPlanExecutor, item.getStartTime(), item.getDuration());
									synchronized(taskPlanExecutorPool){
										taskPlanExecutorPool.add(taskPlanExecutor);
									}
								}
								
							}
							
						}
					}
					
					
				}
				catch(Exception e){
					
					logger.error(e);
				}
				
				finally
				{
					//间隔时间
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
			
		}
		
	}
	
	
	
	/*
	 * 
	 * 任务执行
	 */
	class TaskPlanExecutor implements Runnable
	{
		public boolean isStoped = false;
		
		public boolean isCompleted= false;
		
		
		private TaskPlan item;
		
		
		private String threadName;
		

		public TaskPlanExecutor(String threadName,final TaskPlan item) {
			super();
			
			this.threadName = threadName;
			
			this.item = item;
			
		}

		@Override
		public void run() {
			
		
			/*if(item.getIsThreadQueue() == 1){
				// 判断当期那上下文中是否此线程 运行着，如果运行着
				for(TaskPlanExecutor tp:taskPlanExecutorPool)
				{
					if(tp.getName().equalsIgnoreCase(threadName))
					{
						return;
					}
				}
			}
			*/
			
			
			
			Queryer queryer=null;
			
			Reflection reflect = new Reflection();
			if(!StringUtils.isEmpty(item.getProcessClass())){
				String queryClass=item.getProcessClass();
				
				if (queryClass == null || queryClass.equals("")) {
					queryClass = "cn.finder.wae.queryer.common.CommonQuery";
				}
			
				try {
				/*	try {
						//动态创建 不会有数据源并发问题
						queryer = (Queryer) reflect.newInstance(queryClass,
								new Object[] {});
					} catch (Exception e) {
						throw new PSISRuntimeException(e.toString());
					}
					
					//设置数据源
					//DefaultContextHolder.setDataSourceType(showTableConfig.getTargetDs());
					
					BaseJdbcDaoSupport baseJdbcDaoSupport = (BaseJdbcDaoSupport)queryer;
					
					DataSource ds = DSManager.getDataSource(showTableConfig.getTargetDs());
					BasicDataSource basicDataSource = (BasicDataSource)ds;
					String driverClassName = basicDataSource.getDriverClassName();
					//判断数据库类型
					if(driverClassName.toLowerCase().indexOf("mysql")!=-1){
						baseJdbcDaoSupport.setDbType(DBType.MySql);
					}else if(driverClassName.toLowerCase().indexOf("sqlserver")!=-1){
						
						baseJdbcDaoSupport.setDbType(DBType.SqlServer);
					}
					else if(driverClassName.toLowerCase().indexOf("oracle")!=-1){
						
						baseJdbcDaoSupport.setDbType(DBType.Oracle);
					}
					
					queryer.setJDBCDataSource(ds);
					
					
					MapParaQueryConditionDto<String, Object> condition =new MapParaQueryConditionDto<String, Object>();
					condition.put("taskplan", item);
					condition.put("servletContext", ctxt_p);
					queryer.queryTableQueryResultManager(item.getShowtableConfigId(), condition);
					*/
					
				} catch (Exception e) {
					throw new WaeRuntimeException(e.toString());
				}
			}
			
			
			else if(item.getShowtableConfigId()>0)
			{
				//在事务中执行
				QueryService queryService =AppApplicationContextUtil.getContext().getBean("queryService", QueryService.class);
				
				
				try
				{
				
					logger.debug(String.format("任务:【%1$s】 在 %2$s 开始执行...",item.getName(),DateUtils.formatDate(new Date())));
					

					//1.先判断任务处理类
					//如果配置处理类，那么showtableConfigId忽略
					
					
					//if(item.getShowtableConfigId()>0)
					//{
						//2. 使用showtable配置来执行
						//主要使用的 处理类(Queryer)  和数据源
						
						MapParaQueryConditionDto<String, Object> condition =new MapParaQueryConditionDto<String, Object>();
						condition.put("taskplan", item);
						//condition.put("servletContext", ctxt_p);
						
						queryService.queryTableQueryResultNoTransaction(item.getShowtableConfigId(), condition);
						
						
					//}
					
					
					
					logger.debug(String.format("任务:【%1$s】 在 %2$s 执行完成...",item.getName(),DateUtils.formatDate(new Date())));
					
				
					
					
					
					
				}
				catch(Throwable e)
				{
					logger.error(String.format("任务:【%1$s】 在 %2$s 执行异常:"+e.toString(),item.getName(),DateUtils.formatDate(new Date())));
				}
				finally
				{	item.setHasCompleteTimes(item.getHasCompleteTimes()+1);
					//time设置为 0 表示一直运行  这里不管是执行成功还是执行失败  都算是执行了一次了
					if(item.getTimes() !=0)
					{
						
						if(item.getHasCompleteTimes() == item.getTimes()){
							//任务停止
							this.stopped();
							
							
							//修改 数据库记录状态
							resetStatus();
						}
					}
					
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
			
			
			
			
			
		}
		
		
		public synchronized  void stopped()
		{
			logger.info(String.format("任务:【%1$s】 已从任务队列移除  正在 停止中...", item.getName()));
			isStoped = true;
			taskPlanExecutorPool.remove(this);
			item.setForceStoped(true);
			
			
			
			//this.cancel();
			//停止线程
			
			//logger.info(String.format("任务:【%1$s】 在 %2$s 停止:",item.getName(),DateUtils.formatDate(new Date())));
		}
		
		public synchronized void remove()
		{
			taskPlanExecutorPool.remove(this);
		}

		public TaskPlan getItem() {
			return item;
		}
		
		public String getName(){
			
			return this.threadName;
		}
		
		
		public void resetStatus(){
			taskPlanService.updateResetStatus(item.getId());
		}
		
	}
	
	
	/***
	 * 最新数据读取之后 ，  重置记录更新状态
	 * @author: wuhualong
	 * @data:2014-6-4下午3:01:02
	 * @function:
	 */
	class TaskPlanResetUpdateThread extends Thread
	{

		private List<TaskPlan> taskPlans;
		
		
		
		public TaskPlanResetUpdateThread(List<TaskPlan> taskPlans) {
			super();
			this.taskPlans = taskPlans;
		}



		@Override
		public void run() {
			
			if(taskPlans!=null && taskPlans.size()>0)
			{
				List<TaskPlan> tasks =new ArrayList<TaskPlan>();
				for (TaskPlan tp:taskPlans)
				{
					if(tp.getIsUpdated()==1){
						tasks.add(tp);
					}
				}
				
				if(tasks.size()>0)
					taskPlanService.updateTaskPlanUpdate(tasks);
				
				
			}
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
}
