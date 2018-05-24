package cn.finder.wae.queryer.handleclass;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

/**
 * @author: wuhualong
 * @data:2015-03-07 上午11cha:25:21
 * @function:获取流程中指定任务变量 处理
 */
public class ProcessTaskVariableAfterClass extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
	 	//流程实例ID
	 	String taskId =data.get("taskId").toString();
	 	
			
		   ActivitiServiceManager activitiServiceManager=AppApplicationContextUtil.getContext().getBean("activitiServiceManager", ActivitiServiceManager.class);

	       
	       Map<String,Object> vs = activitiServiceManager.getTaskService().getVariables(taskId);
	      
	       logger.debug("====返回  任务 变量");
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
	       tableQueryResult.setResultList(list);
	       
		 
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
