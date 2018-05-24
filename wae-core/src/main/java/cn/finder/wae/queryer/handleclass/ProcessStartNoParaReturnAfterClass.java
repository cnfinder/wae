package cn.finder.wae.queryer.handleclass;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowtableConfigProcess;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

/**
 * @author: wuhualong
 * @data:2014-7-16上午11:25:21
 * @function:启动流程,从表配置来启动流程的  在后置处理类中需要配置此类
 */
public class ProcessStartNoParaReturnAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 CommonService commonService=AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		ShowtableConfigProcess showtableConfigProcess = commonService.findShowtableConfigProcess(showTableConfigId);
		 
			
		//接口流程键
	 	String processKey =showtableConfigProcess.getProcessKey();
	 	
	 	//业务KEY
	 	String businessKey="";
	 	
	 	//变量
	 	Map<String, Object> variables =new HashMap<String, Object>();
	 	

		ExpressionParser parser =new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext(data);  
		context.setVariable("currentTime", System.currentTimeMillis());
		
		businessKey = parser.parseExpression(showtableConfigProcess.getBusinessKey()).getValue(context, String.class);
	 	
		logger.debug("====businessKey:"+businessKey);
		variables = data;
		
		
		logger.debug("====输入参数:");
		if(variables!=null){
			  Set<Entry<String,Object>> es =	variables.entrySet();
	    	   
	    	     Iterator<Entry<String,Object>>  ie =  es.iterator();
	    	     while(ie.hasNext()){
	    	    	 
	    	    	Entry<String,Object> entry = ie.next();
	    	    	
	    		   logger.debug("===variable: " + entry.getKey() + " = " + entry.getValue());
	    	   }
		}
		logger.debug("==================================================:");
		
		
			
		   ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
			// activitiServiceManager.getIdentityService().setAuthenticatedUserId("yaoshi01");
	       ProcessInstance processInstance = activitiServiceManager.getRuntimeService().startProcessInstanceByKey(processKey, businessKey, variables);
	       String processInstanceId = processInstance.getId();

	       
	       
	       
	       
	       
	       Map<String,Object> vs =new HashMap<String, Object>();
	       
	       
	       
	       //List<HistoricDetail> variable_list = activitiServiceManager.getHistoryService().createHistoricDetailQuery().processInstanceId(processInstance.getId()).orderByTime().desc().list();
	      
	       // 变量 加入 vs中 
	       
	      /* for (HistoricDetail historicDetail : variable_list) {
	           HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
	           if(!vs.containsKey(variable.getVariableName())){
	        	   vs.put(variable.getVariableName(), variable.getValue());
	           }
	           
	       }*/
	       
	       List<HistoricVariableInstance> historicVariableInstances= activitiServiceManager.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
	       for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
	        	   vs.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
	           
	       }
	       
	       
	       
	       
	      //List<Execution> executions =  activitiServiceManager.getRuntimeService().createExecutionQuery().processInstanceId(processInstanceId).list();
	       
	       
	      // String executionId ="";
	       //vs = activitiServiceManager.getRuntimeService().getVariables(executionId);
	       
	      
	       logger.debug("====返回变量");
	       if(vs!=null && vs.size()>0){
	    	   //返回变量输出
	    	   
	    	  
	    	     Set<Entry<String,Object>> es =	vs.entrySet();
	    	   
	    	     Iterator<Entry<String,Object>>  ie =  es.iterator();
	    	     while(ie.hasNext()){
	    	    	 
	    	    	Entry<String,Object> entry = ie.next();
	    	    	
	    		   logger.debug("===variable: " + entry.getKey() + " = " + entry.getValue());
	    	   }
	       }
	       logger.debug("============================================");
	       
	       
	       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
	       list.add(vs);
	       tableQueryResult.setCount(1l);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       
	       String msg  = String.format("{start process of {key=%1$s, bkey=%2$s, pid=%3$s}", processKey,businessKey, processInstanceId);
	       logger.debug(msg);
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
