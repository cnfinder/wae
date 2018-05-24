package cn.finder.wae.queryer.handleclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParseHandlers;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
import org.activiti.engine.impl.bpmn.parser.handler.BoundaryEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.BusinessRuleParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CallActivityParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CancelEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CompensateEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EndEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ErrorEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventBasedGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventSubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ExclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.InclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateCatchEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateThrowEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ManualTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.MessageEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ParallelGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ReceiveTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ScriptTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SendTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SignalEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.StartEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TimerEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TransactionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.cfg.BpmnParseFactory;
import org.activiti.engine.impl.cfg.DefaultBpmnParseFactory;
import org.activiti.engine.impl.cfg.SpringBeanFactoryProxyMap;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.parse.BpmnParseHandler;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import cn.finder.ui.webtool.QueryCondition;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

/**
 * @author: wuhualong
 * @data:2014-4-23下午2:15:59
 * @function:更新设备 对药房中设备编码进行验证
 */
public class FlowsAddQueryerBeforeClass extends QueryerDBBeforeClass {

	
	
	@Override
	public void handle(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		super.handle(showTableConfigId, condition);

		logger.debug("====================MachineEditQueryerBeforeClass.handle ");
		logger.debug("====================showTableConfigId: "+showTableConfigId);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String rootPath = data.get("WebRoot_path").toString();
		String url = data.get("file_data").toString();
		File file = new File(rootPath+url);
		BpmnParseFactory bpmnParseFactory = new DefaultBpmnParseFactory();
		InputStream fileInputStream = null;
		try {
			 fileInputStream = new FileInputStream(file);
			 
			 
	         ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
	         Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
	         BpmnParser bpmnParser = new BpmnParser();
	         
	         
	         List<BpmnParseHandler> parseHandlers = new ArrayList<BpmnParseHandler>();
		      parseHandlers.addAll(getDefaultBpmnParseHandlers());
		      
		      BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
		      bpmnParseHandlers.addHandlers(parseHandlers);
		      bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);
		      
		      //设置 Deployment
		      DeploymentEntity deployment=new DeploymentEntity();
		      deployment.setId("111");
		     
		      //设置Expression
		      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		      SpringBeanFactoryProxyMap beans = new SpringBeanFactoryProxyMap(beanFactory);
		      ExpressionManager expressionManager = new ExpressionManager(beans);
		      bpmnParser.setExpressionManager(expressionManager);
		      
		      DefaultActivityBehaviorFactory defaultActivityBehaviorFactory = new DefaultActivityBehaviorFactory();
		      defaultActivityBehaviorFactory.setExpressionManager(expressionManager);
		      bpmnParser.setActivityBehaviorFactory(defaultActivityBehaviorFactory);
		      
		      
		      
		      DefaultListenerFactory defaultListenerFactory = new DefaultListenerFactory();
		      defaultListenerFactory.setExpressionManager(expressionManager);
		      bpmnParser.setListenerFactory(defaultListenerFactory);
		      
		      bpmnParser.setBpmnParseFactory(bpmnParseFactory);
		      
		      BpmnParse bpmnParse =bpmnParseFactory.createBpmnParse(bpmnParser).sourceInputStream(fileInputStream).deployment(deployment);
		      
		      
		    
		      
		     // Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());
		      bpmnParse.execute();
		      List<ProcessDefinitionEntity> list = bpmnParse.getProcessDefinitions();
		      
		      ProcessDefinitionEntity pde = list.get(0);
		      String key = pde.getKey();
		      String name  = pde.getName();
		      
		      data.put("flow_key", key);
		      data.put("flow_name_cn", name);
		      data.put("flow_url", url);
		      data.put("flow_type", '0');
		      
		      ((MapParaQueryConditionDto<String, Object>)condition).setMapParas(data);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	
		
	}
	
	
	protected List<BpmnParseHandler> getDefaultBpmnParseHandlers() {
	    
	    // Alpabetic list of default parse handler classes
	    List<BpmnParseHandler> bpmnParserHandlers = new ArrayList<BpmnParseHandler>();
	    bpmnParserHandlers.add(new BoundaryEventParseHandler());
	    bpmnParserHandlers.add(new BusinessRuleParseHandler());
	    bpmnParserHandlers.add(new CallActivityParseHandler());
	    bpmnParserHandlers.add(new CancelEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new CompensateEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new EndEventParseHandler());
	    bpmnParserHandlers.add(new ErrorEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new EventBasedGatewayParseHandler());
	    bpmnParserHandlers.add(new ExclusiveGatewayParseHandler());
	    bpmnParserHandlers.add(new InclusiveGatewayParseHandler());
	    bpmnParserHandlers.add(new IntermediateCatchEventParseHandler());
	    bpmnParserHandlers.add(new IntermediateThrowEventParseHandler());
	    bpmnParserHandlers.add(new ManualTaskParseHandler());
	    bpmnParserHandlers.add(new MessageEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new ParallelGatewayParseHandler());
	    bpmnParserHandlers.add(new ProcessParseHandler());
	    bpmnParserHandlers.add(new ReceiveTaskParseHandler());
	    bpmnParserHandlers.add(new ScriptTaskParseHandler());
	    bpmnParserHandlers.add(new SendTaskParseHandler());
	    bpmnParserHandlers.add(new SequenceFlowParseHandler());
	    bpmnParserHandlers.add(new ServiceTaskParseHandler());
	    bpmnParserHandlers.add(new SignalEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new StartEventParseHandler());
	    bpmnParserHandlers.add(new SubProcessParseHandler());
	    bpmnParserHandlers.add(new EventSubProcessParseHandler());
	    bpmnParserHandlers.add(new TaskParseHandler());
	    bpmnParserHandlers.add(new TimerEventDefinitionParseHandler());
	    bpmnParserHandlers.add(new TransactionParseHandler());
	    bpmnParserHandlers.add(new UserTaskParseHandler());
	    
	   
	    
	    return bpmnParserHandlers;
	  }
	

}
