package cn.finder.wae.queryer.handleclass.flow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.User;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.iron.activiti.ActivitiServiceManager;

/**
 * @author: wuhualong
 * @data: 2015-08-07
 * @function:启动测试流程-只用于测试
 *  必要参数:
 *  process_key: 流程键
 *  business_key:业务键
 *  initiator_user:可选
 */
public class ProcessStartTestAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
			
		//接口流程键
	 	String processKey =data.get("process_key").toString();
	 	
	 	//业务KEY
	 	String businessKey="";
	 	
	 	//变量
	 	Map<String, Object> variables =new HashMap<String, Object>();
	 	

		ExpressionParser parser =new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext(data);  
		context.setVariable("currentTime", System.currentTimeMillis());
		
		businessKey = parser.parseExpression(data.get("business_key").toString()).getValue(context, String.class);
	 	
		logger.debug("====businessKey:"+businessKey);
		variables = data;
		
		
		logger.info("====输入参数:");
		if(variables!=null){
			  Set<Entry<String,Object>> es =	variables.entrySet();
	    	   
	    	     Iterator<Entry<String,Object>>  ie =  es.iterator();
	    	     while(ie.hasNext()){
	    	    	 
	    	    	Entry<String,Object> entry = ie.next();
	    	    	
	    		   logger.info("===variable: " + entry.getKey() + " = " + entry.getValue());
	    	   }
		}
		logger.info("==================================================:");
		
		
			
		   ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);
		   
		   
		   
		   User user=  (User)data.get("session_user");
		   String initiatorUser="config_admin";
		   if(user!=null){
			   initiatorUser=user.getAccount();
		   }
		   
		   if(data.containsKey("initiator_user")){
			   initiatorUser=data.get("initiator_user").toString();
		   }
		   
		   activitiServiceManager.getIdentityService().setAuthenticatedUserId(initiatorUser);
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
	       
	      
	       logger.info("====返回变量");
	       if(vs!=null && vs.size()>0){
	    	   //返回变量输出
	    	   
	    	  
	    	     Set<Entry<String,Object>> es =	vs.entrySet();
	    	   
	    	     Iterator<Entry<String,Object>>  ie =  es.iterator();
	    	     while(ie.hasNext()){
	    	    	 
	    	    	Entry<String,Object> entry = ie.next();
	    	    	
	    		   logger.info("===variable: " + entry.getKey() + " = " + entry.getValue());
	    	   }
	       }
	       logger.info("============================================");
	       
	       
	       List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
	       list.add(vs);
	       tableQueryResult.setCount(1l);
	       tableQueryResult.setPageSize(1);
	       tableQueryResult.setPageIndex(1);
	       tableQueryResult.setResultList(list);
	       
	       String msg  = String.format("{start process of {key=%1$s, bkey=%2$s, pid=%3$s}", processKey,businessKey, processInstanceId);
	       logger.debug(msg);
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
