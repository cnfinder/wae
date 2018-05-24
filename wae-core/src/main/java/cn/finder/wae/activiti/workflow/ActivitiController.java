package cn.finder.wae.activiti.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.finder.wae.business.domain.Response;
import cn.iron.activiti.ActivitiServiceManager;
import cn.finder.wae.activiti.service.activiti.WorkflowProcessDefinitionService;
import cn.finder.wae.activiti.service.activiti.WorkflowTraceService;
import cn.finder.wae.activiti.utils.RestTemplateExecuter;
import cn.finder.wae.activiti.utils.WorkflowUtils;
import cn.finder.wae.common.comm.JsonUtil;
import cn.finder.wae.common.constant.Constant;

/**
 * 流程管理控制器
 *
 * @author HenryYan
 */
@Controller
@RequestMapping(value = "/activiti/workflow")
public class ActivitiController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected WorkflowProcessDefinitionService workflowProcessDefinitionService;

  protected RepositoryService repositoryService;

  protected RuntimeService runtimeService;

  protected TaskService taskService;

  protected WorkflowTraceService traceService;
  
  protected HistoryService historyService;
  

  @Autowired
  protected ActivitiServiceManager activitiServiceManager;
  
  protected static Map<String, ProcessDefinition> PROCESS_DEFINITION_CACHE = new HashMap<String, ProcessDefinition>();

  @Autowired
  ProcessEngineFactoryBean processEngine;

  
  
  
  @RequestMapping(value = "/process_deploy_page")
  public ModelAndView processDeployPage(HttpServletRequest request) {
    ModelAndView mav = new ModelAndView("activiti/workflow/process_deploy_page");
    return mav;
    
  }
  
  
  /**
   * 流程定义列表
   *
   * @return
   */
  @RequestMapping(value = "/process-list")
  public ModelAndView processList(HttpServletRequest request) {
    ModelAndView mav = new ModelAndView("workflow/process-list");

    /*
     * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
     */
    /*List<Object[]> objects = new ArrayList<Object[]>();

    Page<Object[]> page = new Page<Object[]>(PageUtil.PAGE_SIZE);
    int[] pageParams = PageUtil.init(page, request);

    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
    for (ProcessDefinition processDefinition : processDefinitionList) {
      String deploymentId = processDefinition.getDeploymentId();
      Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
      objects.add(new Object[]{processDefinition, deployment});
    }

    page.setTotalCount(processDefinitionQuery.count());
    page.setResult(objects);
    mav.addObject("page", page);*/

    
    ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
    
    List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(1,10);
    
    
    return mav;
  }

  /**
   * 部署全部流程
   *
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/redeploy/all")
  public String redeployAll(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir) throws Exception {
    workflowProcessDefinitionService.deployAllFromClasspath(exportDir);
    return "redirect:/workflow/process-list";
  }

  /**
   * 读取资源，通过部署ID
   *
   * @param processDefinitionId 流程定义
   * @param resourceType 资源类型(xml|image)
   * @throws Exception
   */
  @RequestMapping(value = "/resource/read")
  public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                               HttpServletResponse response) throws Exception {
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    String resourceName = "";
    if (resourceType.equals("image")) {
      resourceName = processDefinition.getDiagramResourceName();
    } else if (resourceType.equals("xml")) {
      resourceName = processDefinition.getResourceName();
    }
    InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    byte[] b = new byte[1024];
    int len = -1;
    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
      response.getOutputStream().write(b, 0, len);
    }
  }
  
  
  
  @RequestMapping(value = "/dynamic_grapic.png")
  public void loadDynamicWorkflowGrapic(@RequestParam("processInstanceId") String processInstanceId,@RequestParam("business_key") String businessKey,
                               HttpServletResponse response) throws Exception {
	  
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("UTF-8");
		try {
			if(businessKey != null && !businessKey.equals("") && !businessKey.equals("null")){
				processInstanceId = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult().getProcessInstanceId();
			}
			  HistoricProcessInstance processInstance=	 historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			  if(processInstance==null){
				  throw new RuntimeException("获取流程图异常!"); 
			  }else{
				 BpmnModel bpmnModel= repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
				 List<HistoricActivityInstance> activityInstances=historyService.createHistoricActivityInstanceQuery()
							.processInstanceId(processInstanceId)
							.orderByHistoricActivityInstanceStartTime().asc().list();
				 List<String> activitiIds=new ArrayList<String>();
				 List<String> flowIds=new ArrayList<String>();
				 ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
							.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
				 flowIds=getHighLightedFlows(processDefinition, activityInstances);//获取流程走过的线
				
				 for(HistoricActivityInstance hai:activityInstances){
					 activitiIds.add(hai.getActivityId());//获取流程走过的节点
				 }
				//ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
				
				 
				 Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());
				
				 
				 
				// InputStream imageStream =ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activitiIds,flowIds);
				 
				 
				 //5.16.4
			/*	 InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				    .generateDiagram(bpmnModel, "png", activitiIds,flowIds);*/
				 
				 
				 InputStream imageStream =  processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				    .generateDiagram(bpmnModel, "png", activitiIds,flowIds,
				                     processEngine.getProcessEngineConfiguration().getActivityFontName(),
				                     processEngine.getProcessEngineConfiguration().getLabelFontName(), 
				                     processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);
				 
				
				 
				 response.setContentType("image/png");
					OutputStream os = response.getOutputStream();
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					os.close();
					imageStream.close();
			  }
		} catch (Exception e) {
			logger.error("获取流程图异常!", e);
			throw new RuntimeException("获取流程图异常!");
		}
	  
  }
  
  
  @RequestMapping(value = "/dynamic_grapic_complete.png")
  public void loadCompleteDynamicWorkflowGrapic(@RequestParam("processInstanceId") String processInstanceId,
                               HttpServletResponse response) throws Exception {
	  
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setCharacterEncoding("UTF-8");
		try {
			  
			HistoricProcessInstance processInstance=	 historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			  if(processInstance==null){
				  throw new RuntimeException("获取流程图异常!"); 
			  }else{
				 BpmnModel bpmnModel= repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
				 List<HistoricActivityInstance> activityInstances=historyService .createHistoricActivityInstanceQuery()
							.processInstanceId(processInstanceId)
							.orderByHistoricActivityInstanceStartTime().asc().list();
				 List<String> activitiIds=new ArrayList<String>();
				 List<String> flowIds=new ArrayList<String>();
				 ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
							.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
				 flowIds=getHighLightedFlows(processDefinition, activityInstances);//获取流程走过的线
				
				 for(HistoricActivityInstance hai:activityInstances){
					 activitiIds.add(hai.getActivityId());//获取流程走过的节点
				 }
				//ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
				
				 
				 Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());

				 //InputStream imageStream =ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activitiIds,flowIds);
				 
				 
				 //5.16.4
				/* InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				    .generateDiagram(bpmnModel, "png", 
				                     processEngine.getProcessEngineConfiguration().getActivityFontName(),
				                     processEngine.getProcessEngineConfiguration().getLabelFontName(), 
				                     processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);*/
				 InputStream imageStream =  processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
						    .generateDiagram(bpmnModel, "png", activitiIds,flowIds,
						                     processEngine.getProcessEngineConfiguration().getActivityFontName(),
						                     processEngine.getProcessEngineConfiguration().getLabelFontName(), 
						                     processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);
				 
				 
				 response.setContentType("image/png");
					OutputStream os = response.getOutputStream();
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					os.close();
					imageStream.close();
			  }
		} catch (Exception e) {
			logger.error("获取流程图异常!", e);
			throw new RuntimeException("获取流程图异常!");
		}
	  
  }
 
  
  
  public List<String> getHighLightedFlows(
			ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i)
							.getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1)
							.getActivityId());// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances
						.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances
						.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(
						activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl
					.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
						.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;

	}


  
  
  
  
  
  
  

  /**
   * 读取资源，通过流程ID
   *
   * @param resourceType      资源类型(xml|image)
   * @param processInstanceId 流程实例ID
   * @param response
   * @throws Exception
   */
  @RequestMapping(value = "/resource/process-instance")
  public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
          throws Exception {
    InputStream resourceAsStream = null;
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
            .singleResult();

    String resourceName = "";
    if (resourceType.equals("image")) {
      resourceName = processDefinition.getDiagramResourceName();
    } else if (resourceType.equals("xml")) {
      resourceName = processDefinition.getResourceName();
    }
    resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    byte[] b = new byte[1024];
    int len = -1;
    while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
      response.getOutputStream().write(b, 0, len);
    }
  }

  /**
   * 删除部署的流程，级联删除流程实例
   *
   * @param deploymentId 流程部署ID
   */
  @RequestMapping(value = "/process/delete")
  @ResponseBody
  public Response<Integer> delete(@RequestParam("deploymentId") final String deploymentId) {
   
	 return new RestTemplateExecuter<Integer>() {

		@Override
		protected Response<Integer> execute() throws Exception {
			
			 repositoryService.deleteDeployment(deploymentId, true);
			    //return "redirect:/workflow/process-list";
			 resp.getMessage().setMsg("删除成功");
			 resp.setTag(1);
			return resp;
		}
	}.run();
	 
    
    
  }

  /**
   * 输出跟踪流程信息
   *
   * @param processInstanceId
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/process/trace")
  @ResponseBody
  public List<Map<String, Object>> traceProcess(@RequestParam("pid") String processInstanceId) throws Exception {
    List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
    return activityInfos;
  }

  /**
   * 读取带跟踪的图片
   */
  @RequestMapping(value = "/process/trace/auto/{executionId}")
  public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response)
          throws Exception {
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
    BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
    List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
    // 不使用spring请使用下面的两行代码
//    ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//    Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

    // 使用spring注入引擎请使用下面的这行代码
    Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());

    //InputStream imageStream = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
    
    
    //5.16.4
	 InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
	    .generateDiagram(bpmnModel, "png",activeActivityIds);

    // 输出资源内容到相应对象
    byte[] b = new byte[1024];
    int len;
    while ((len = imageStream.read(b, 0, 1024)) != -1) {
      response.getOutputStream().write(b, 0, len);
    }
  }

  @RequestMapping(value = "/deploy")
  public String deploy(/*@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,*/ @RequestParam(value = "file", required = false) MultipartFile file) {

	  
	
	  
    String fileName = file.getOriginalFilename();
    
    try {
		fileName=new String(fileName.getBytes("ISO8859-1"),"UTF-8");
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
    try {
      InputStream fileInputStream = file.getInputStream();
      Deployment deployment = null;

      String extension = FilenameUtils.getExtension(fileName);
      if (extension.equals("zip") || extension.equals("bar")) {
        ZipInputStream zip = new ZipInputStream(fileInputStream);
        deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
      } else {
        deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
      }

      List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

      for (ProcessDefinition processDefinition : list) {
        WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, "/tmp/ActivitiPlus");
      }

    } catch (Exception e) {
      logger.error("error on deploy process, because of file input stream", e);
    }

    return "redirect:/workflow/process-list";
  }

  @RequestMapping(value = "/process/convert-to-model/{processDefinitionId}")
  public String convertToModel(@PathVariable("processDefinitionId") String processDefinitionId)
          throws UnsupportedEncodingException, XMLStreamException {
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId).singleResult();
    InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
            processDefinition.getResourceName());
    XMLInputFactory xif = XMLInputFactory.newInstance();
    InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
    XMLStreamReader xtr = xif.createXMLStreamReader(in);
    BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

    
    //delete  20141203
    BpmnJsonConverter converter = new BpmnJsonConverter();
    
    //ObjectNode modelNode = converter.convertToJson(bpmnModel);
    ObjectNode modelNode =null;
    
    Model modelData = repositoryService.newModel();
    modelData.setKey(processDefinition.getKey());
    modelData.setName(processDefinition.getResourceName());
    modelData.setCategory(processDefinition.getDeploymentId());

    ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
    modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
    modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
    modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
    modelData.setMetaInfo(modelObjectNode.toString());

    repositoryService.saveModel(modelData);

    repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

    return "redirect:/workflow/model/list";
  }

  /**
   * 待办任务--Portlet
   */
  @RequestMapping(value = "/task/todo/list")
  @ResponseBody
  public List<Map<String, Object>> todoList(HttpSession session) throws Exception {
		  
	cn.finder.wae.business.domain.User user = ((cn.finder.wae.business.domain.User)session.getAttribute(Constant.KEY_SESSION_USER));
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    // 已经签收的任务
    List<Task> todoList = taskService.createTaskQuery().taskAssignee(user.getAccount()).active().list();
    for (Task task : todoList) {
      String processDefinitionId = task.getProcessDefinitionId();
      ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

      Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
      singleTask.put("status", "todo");
      result.add(singleTask);
    }

    // 等待签收的任务
    List<Task> toClaimList = taskService.createTaskQuery().taskCandidateUser(user.getAccount()).active().list();
    for (Task task : toClaimList) {
      String processDefinitionId = task.getProcessDefinitionId();
      ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

      Map<String, Object> singleTask = packageTaskInfo(sdf, task, processDefinition);
      singleTask.put("status", "claim");
      result.add(singleTask);
    }

    return result;
  }

  private Map<String, Object> packageTaskInfo(SimpleDateFormat sdf, Task task, ProcessDefinition processDefinition) {
    Map<String, Object> singleTask = new HashMap<String, Object>();
    singleTask.put("id", task.getId());
    singleTask.put("name", task.getName());
    singleTask.put("createTime", sdf.format(task.getCreateTime()));
    singleTask.put("pdname", processDefinition.getName());
    singleTask.put("pdversion", processDefinition.getVersion());
    singleTask.put("pid", task.getProcessInstanceId());
    return singleTask;
  }

  private ProcessDefinition getProcessDefinition(String processDefinitionId) {
    ProcessDefinition processDefinition = PROCESS_DEFINITION_CACHE.get(processDefinitionId);
    if (processDefinition == null) {
      processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
      PROCESS_DEFINITION_CACHE.put(processDefinitionId, processDefinition);
    }
    return processDefinition;
  }

  /**
   * 挂起、激活流程实例
   */
  @RequestMapping(value = "processdefinition/update/{state}/{processDefinitionId}")
  @ResponseBody
  public Response<Integer> updateState(@PathVariable("state") final String state, @PathVariable("processDefinitionId") final String processDefinitionId) {
   
    return new RestTemplateExecuter<Integer>() {

		@Override
		protected Response<Integer> execute() throws Exception {
			 if (state.equals("active")) {
			      //redirectAttributes.addFlashAttribute("message", "已激活ID为[" + processDefinitionId + "]的流程定义。");
			      repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
			    } else if (state.equals("suspend")) {
			      repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
			     // redirectAttributes.addFlashAttribute("message", "已挂起ID为[" + processDefinitionId + "]的流程定义。");
			    }
			 resp.setTag(1);
			return resp;
		}
	}.run();
  }

  /**
   * 导出图片文件到硬盘
   *
   * @return
   */
  @RequestMapping(value = "export/diagrams")
  @ResponseBody
  public List<String> exportDiagrams(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir) throws IOException {
    List<String> files = new ArrayList<String>();
    List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

    for (ProcessDefinition processDefinition : list) {
      files.add(WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir));
    }

    return files;
  }

  @Autowired
  public void setWorkflowProcessDefinitionService(WorkflowProcessDefinitionService workflowProcessDefinitionService) {
    this.workflowProcessDefinitionService = workflowProcessDefinitionService;
  }

  @Autowired
  public void setRepositoryService(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }

  @Autowired
  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @Autowired
  public void setTraceService(WorkflowTraceService traceService) {
    this.traceService = traceService;
  }

  @Autowired
  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }

  @Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
  
  /**
   * 读取开始的表单KEY
   * {processDefinitionId}
   */
  @RequestMapping(value = "get-form/start/prodefid.json")
  @ResponseBody
  public Response<Map<String, Object>> findStartFormKeyByProdefId(final @RequestParam("processDefinitionId") String processDefinitionId) throws Exception {

	  return  new RestTemplateExecuter<Map<String,Object>>() {

			@Override
			protected Response<Map<String, Object>> execute() throws Exception {
				    String processDefinitionId_tmp= URLDecoder.decode(processDefinitionId,"UTF-8");
				    String formKey = activitiServiceManager.getFormService().getStartFormKey(processDefinitionId_tmp);
				    
					Map<String,Object> mapData =JsonUtil.getMap4Json(formKey);
					resp.setPageCount(1);
					resp.setPageSize(1);
					resp.setTotalRecord(0l);
					resp.setTag(1l);
					resp.setEntity(mapData);
					
					return resp;
			}
		}.run();
	  
	  

    
    
  }
  
  
  

  /**
   * 读取开始的表单KEY
   * {processDefinitionKey}
   */
  @RequestMapping(value = "get-form/start/prodefkey.json")
  @ResponseBody
  public Response<Map<String, Object>> findStartFormKeyByProdefKey(final @RequestParam("processDefinitionKey") String processDefinitionKey) throws Exception {
	
	  
	 return  new RestTemplateExecuter<Map<String,Object>>() {

		@Override
		protected Response<Map<String, Object>> execute() throws Exception {
			  ProcessDefinition processDefinition =  activitiServiceManager.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
			    String formKey = activitiServiceManager.getFormService().getStartFormKey(processDefinition.getId());
				Map<String,Object> mapData =JsonUtil.getMap4Json(formKey);
				
				resp.setPageCount(1);
				resp.setPageSize(1);
				resp.setTotalRecord(0l);
				resp.setTag(1l);
				resp.setEntity(mapData);
				
				return resp;
		}
	}.run();
	  
	
    
  }
  
  

  /**
   * 读取Task的表单KEY
   * /{processDefinitionId}/{taskDefinitionKey}
   */
  @RequestMapping(value = "get-form/task/prodefid.json")
  @ResponseBody
  public Map<String,Object> findTaskFormKeyByProdefId(@RequestParam("processDefinitionId") String processDefinitionId,
		  @RequestParam("taskDefinitionKey") String taskDefinitionKey) throws Exception {
	  
	  processDefinitionId= URLDecoder.decode(processDefinitionId,"UTF-8");
    String formKey = activitiServiceManager.getFormService().getTaskFormKey(processDefinitionId, taskDefinitionKey);
    
    
    if(formKey==null){
    	return new HashMap<String, Object>();
    }else{
	    Map<String,Object> mapData =JsonUtil.getMap4Json(formKey);
	    
	    return mapData;
    }
  }
  
  

  
  /**
   * 读取Task的表单KEY
   * /{processDefinitionId}/{taskDefinitionKey}
   */
  @RequestMapping(value = "get-form/task/prodefkey.json")
  @ResponseBody
  public Map<String,Object> findTaskFormKeyByProdefKey(@RequestParam("processDefinitionId") String processDefinitionId,
		  @RequestParam("taskDefinitionKey") String taskDefinitionKey) throws Exception {
	  
	  Map<String,Object> mapData = new HashMap<String,Object>();
	  String showtableConfigId = (String) activitiServiceManager.getTaskService().getVariable(taskDefinitionKey, "showtableConfigId");
	  mapData.put("showtableConfigId", showtableConfigId);
	  return mapData;
	  
	  //ProcessDefinition processDefinition =  activitiServiceManager.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//    String formKey = activitiServiceManager.getFormService().getTaskFormKey(processDefinitionId, taskDefinitionKey);
//    
//   
//    
//    
//    if(formKey==null){
//    	return new HashMap<String, Object>();
//    }else{
//	    Map<String,Object> mapData =JsonUtil.getMap4Json(formKey);
//	    
//	    return mapData;
//    }
  }
  
  
  
  
  /***
   *  启动请假流程 服务
   * @param userId 
   * @param businessKey  业务ID  引用故障生成的guid
   * @return
   */
  @RequestMapping(value = "start.json", method = { RequestMethod.POST, RequestMethod.GET })
  @ResponseBody
  public Response<Integer> startWorkflowService(@RequestParam("userId") final String userId,@RequestParam("businessKey") final String businessKey,
		  @RequestParam("processDefinitionKey") final String processDefinitionKey) {
	  
	  logger.debug("===userId:"+userId+" , businessKey="+businessKey+",processDefinitionKey="+processDefinitionKey);
	  
	  return new RestTemplateExecuter<Integer>() {

			@Override
			protected Response<Integer> execute() throws Exception {
				
				try {
				      //User user = UserUtil.getUserFromSession(session);
				      // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
				      // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
					
						
					  Map<String, Object> variables=findVariables();
				      
				      
				      activitiServiceManager.getIdentityService().setAuthenticatedUserId(URLDecoder.decode(userId,"UTF-8"));
				      ProcessInstance processInstance = activitiServiceManager.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
				      String processInstanceId = processInstance.getId();
				      
				      
				      String msg  = String.format("{start process of {key={0}, bkey={1}, pid={2}}", processDefinitionKey, businessKey, processInstanceId);
				      logger.debug(msg);
				      
				      resp.getMessage().setMsg(processDefinitionKey+"启动成功!");
				      
				    } catch (ActivitiException e) {
				      if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				        logger.warn("没有部署流程!", e);
				        resp.getMessage().setMsg("没有部署流程!");
				      } else {
				        logger.error(processDefinitionKey+"启动失败：", e);
				        resp.getMessage().setMsg(processDefinitionKey+"启动失败!");
				      }
				    } catch (Exception e) {
				      logger.error(processDefinitionKey+"启动失败：", e);
				      resp.getMessage().setMsg(processDefinitionKey+"启动失败!");
				    }
				 resp.setTag(1);
				return resp;
			}
		}.run();
  }

  
  
  /***
   *  启动请假流程 服务
   * @param userId 
   * @param businessKey  业务ID  引用故障生成的guid
   * @return
   */
  @RequestMapping(value = "start_unstatus.json", method = { RequestMethod.POST, RequestMethod.GET })
  @ResponseBody
  public Response<Integer> startWorkflowServiceUnStatus(@RequestParam("userId") final String userId,@RequestParam("businessKey") final String businessKey,
		  @RequestParam("processDefinitionKey") final String processDefinitionKey,@RequestParam("act_data") final String act_data) {
	  
	  logger.debug("===userId:"+userId+" , businessKey="+businessKey+",processDefinitionKey="+processDefinitionKey +" ,act_data:"+act_data);
	  
	  return new RestTemplateExecuter<Integer>() {

			@Override
			protected Response<Integer> execute() throws Exception {
				
				try {
				      //User user = UserUtil.getUserFromSession(session);
				      // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
				      // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
					
						
					  Map<String, Object> variables=findVariables(act_data);
				      
				      
				      activitiServiceManager.getIdentityService().setAuthenticatedUserId(URLDecoder.decode(userId,"UTF-8"));
				      ProcessInstance processInstance = activitiServiceManager.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
				      String processInstanceId = processInstance.getId();
				      
				      
				      String msg  = String.format("{start process of {key={0}, bkey={1}, pid={2}}", processDefinitionKey, businessKey, processInstanceId);
				      logger.debug(msg);
				      
				      resp.getMessage().setMsg(processDefinitionKey+"启动成功!");
				      
				    } catch (ActivitiException e) {
				      if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				        logger.warn("没有部署流程!", e);
				        resp.getMessage().setMsg("没有部署流程!");
				      } else {
				        logger.error(processDefinitionKey+"启动失败：", e);
				        resp.getMessage().setMsg(processDefinitionKey+"启动失败!");
				      }
				    } catch (Exception e) {
				      logger.error(processDefinitionKey+"启动失败：", e);
				      resp.getMessage().setMsg(processDefinitionKey+"启动失败!");
				    }
				 resp.setTag(1);
				return resp;
			}
		}.run();
  }
  
  
  

  /***
   * 签收任务服务
   * @param taskId 任务ID 请求参数
   * @param userId 用户账户 请求参数
   * @return
   */
   @RequestMapping(value = "task/claim.json",method = { RequestMethod.POST, RequestMethod.GET })
   @ResponseBody
   public Response<Integer> claimService(@RequestParam("taskId") final String taskId,@RequestParam("userId") final String userId) {
 	  
 	  return new RestTemplateExecuter<Integer>() {

 			@Override
 			protected Response<Integer> execute() throws Exception {
 				try
 				{
 				    activitiServiceManager.getTaskService().claim(taskId, userId);
 				    
 				    resp.setTag(1);
 				    resp.getMessage().setMsg("任务已签收");
 				}catch(Exception e)
 				{
 					resp.getMessage().setMsg("签收失败");
 				}
 				return resp;
 			}
 	  }.run();
 	  
   }
  

   /**
    * 完成任务
    * 
    * @param id  任务ID
    * @return
    */
   @RequestMapping(value = "task/complete.json", method = { RequestMethod.POST, RequestMethod.GET })
   @ResponseBody
   public Response<Integer> complete(@RequestParam("taskId") final String taskId) {
     
     
 	    return new RestTemplateExecuter<Integer>() {
 	
 			@Override
 			protected Response<Integer> execute() throws Exception {
 				Map<String, Object> variables=findVariables();
 				
 				try{
 				    activitiServiceManager.getTaskService().complete(taskId, variables);
 				    resp.setTag(1);
 				    resp.getMessage().setMsg("任务处理完成");
 				}catch(Exception e)
 				{
 					 logger.error("error on complete task {}, variables={}", new Object[] { taskId, variables, e });
 					resp.getMessage().setMsg("任务处理失败");
 				}
 				return resp;
 			}
 	    }.run();
     
   }
   
   
   /****
    * 获取请求变量数据  act_data
    * 数据类型:dataType: int  long  bool string date 默认为string  然后通过参数  act_data进行传输为 JSON数组
			//[{pass:true,dataType:"int"}]或者 dataType可能不需要传入
    * @return
    */
   private Map<String,Object> findVariables(){
	   HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
				.getRequestAttributes()).getRequest();
	   Map<String, Object> variables=new HashMap<String, Object>();
		try
		{
			
			String reqData = request.getParameter("act_data");
			return findVariables(reqData);
		}
		catch(Exception e)
		{
			
		}
		
		
		return variables;
   }
   
   
   private Map<String,Object> findVariables(String reqData){
	   Map<String, Object> variables=new HashMap<String, Object>();
		try
		{
			
			if(!StringUtils.isEmpty(reqData)){
				reqData = URLDecoder.decode(reqData,"UTF-8");
				JSONArray reqDataJA = JSONArray.fromObject(reqData);
				if(reqDataJA!=null && reqDataJA.size()>0){
					
					for(int i=0;i<reqDataJA.size();i++){
						
						JSONObject jo = (JSONObject)reqDataJA.get(i);
						
						Map<String, Object> mapData = JsonUtil.getMap4Json(jo.toString());
						
						String dataTypeName = (String)mapData.get("dataType");
						
						
						Set<String> keyset = mapData.keySet();
						Iterator<String> ite = keyset.iterator();
						while(ite.hasNext()){
							String key = ite.next();
							if(!"dataType".equalsIgnoreCase(key)){
								if(StringUtils.isEmpty(dataTypeName)){
									variables.put(key, mapData.get(key));
	 							}else{
	 								
	 								variables.put(key, mapData.get(key));
	 							}
							}
								
						}
						
						
					}
				}
				
			}
		}
		catch(Exception e)
		{
			
		}
		
		
		return variables;
   }

}