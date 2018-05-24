package cn.finder.wae.activiti.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author HenryYan
 */
public class WorkflowUtils {

  private static Logger logger = LoggerFactory.getLogger(WorkflowUtils.class);

  /**
   * 转换流程节点类型为中文说明
   * 
   * @param type
   *          英文名称
   * @return 翻译后的中文名称
   */
  public static String parseToZhType(String type) {
    Map<String, String> types = new HashMap<String, String>();
    types.put("userTask", "用户任务");
    types.put("serviceTask", "系统任务");
    types.put("startEvent", "开始节点");
    types.put("endEvent", "结束节点");
    types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
    types.put("inclusiveGateway", "并行处理任务");
    types.put("callActivity", "子流程");
    return types.get(type) == null ? type : types.get(type);
  }

  /**
   * 导出图片文件到硬盘
   * 
   * @return 文件的全路径
   */
  public static String exportDiagramToFile(RepositoryService repositoryService, ProcessDefinition processDefinition, String exportDir) throws IOException {
    String diagramResourceName = processDefinition.getDiagramResourceName();
    String key = processDefinition.getKey();
    int version = processDefinition.getVersion();
    String diagramPath = "";

    InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
    byte[] b = new byte[resourceAsStream.available()];

    @SuppressWarnings("unused")
    int len = -1;
    resourceAsStream.read(b, 0, b.length);

    // create file if not exist
    String diagramDir = exportDir + "/" + key + "/" + version;
    File diagramDirFile = new File(diagramDir);
    if (!diagramDirFile.exists()) {
      diagramDirFile.mkdirs();
    }
    diagramPath = diagramDir + "/" + diagramResourceName;
    File file = new File(diagramPath);

    // 文件存在退出
    if (file.exists()) {
      // 文件大小相同时直接返回否则重新创建文件(可能损坏)
      logger.debug("diagram exist, ignore... : {}", diagramPath);
      return diagramPath;
    } else {
      file.createNewFile();
    }

    logger.debug("export diagram to : {}", diagramPath);

    // wirte bytes to file
    FileUtils.writeByteArrayToFile(file, b, true);
    return diagramPath;
  }
  
  
  /***
   * 获取高亮显示的线
   * @param processDefinitionEntity
   * @param historicActivityInstances
   * @return
   */
  public static List<String> getHighLightedFlows(
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


}
