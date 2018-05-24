package cn.finder.wae.queryer.handleclass.sdk;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.ServiceInterface;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.module.common.service.CommonService;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
import cn.finder.wae.cache.ConstantsCache.DataType;
import cn.finder.wae.common.comm.AppApplicationContextUtil;
import cn.finder.wae.common.comm.Gzip;
import cn.finder.wae.common.thread.AppContent;
import cn.finder.wae.queryer.handleclass.SDKGeneratorAfterQueryer;




/***
 * android
 * @author whl
 *
 */
public class AndroidSdkenerator implements SdkGenerator{
	private Logger logger = Logger.getLogger(SDKGeneratorAfterQueryer.class);
	
	
	public final static String REQTYPE_DEFAULTREQUEST ="DefaultRequest";
	public final static String REQTYPE_SEARCHTREQUEST ="SearchRequest";
	public final static String REQTYPE_STREAMREQUEST ="StreamRequest";
	public final static String REQTYPE_UPLOADREQUEST ="UploadRequest";
	
	public final static String REQTYPE_JsonStringREQUEST="JsonStringRequest";
	
	String interfaceKey;
	
	String lang;
	
	String nameSpace;
	String funcName;
	String retModelName;
	String reqType;
	
	List<ShowDataConfig> showDataConfigs;
	
	ServiceInterface serviceInterface;
	
	List<String> zipFiles = new ArrayList<String>();
	
	@Override
	public boolean generator(SdkParam sdkparam) {
		 interfaceKey = sdkparam.getInterfaceKey();
			
		 lang = sdkparam.getLang();
		
		 nameSpace = sdkparam.getNameSpace();
		 funcName =sdkparam.getFuncName();
		 retModelName = sdkparam.getRetModelName();
		
		 reqType = sdkparam.getReqType();
		
		
		serviceInterface=ArchCache.getInstance().getServiceInterfaceCache().get(interfaceKey);
		
		long processShowtableConfigId = serviceInterface.getShowtableConfigId();
		
		ShowTableConfig showTableConfig = ArchCache.getInstance().getShowTableConfigCache().get(processShowtableConfigId);
		
		
		showDataConfigs = showTableConfig.getShowDataConfigs();
		
		if(StringUtils.isEmpty(retModelName)){
			retModelName = funcName+"Domain";
		}
		
		
		String modelStr="";
		
		modelStr = generatorModel();
		
		
		String requestStr = generatorRequest();
		String responseStr = generatorResponse();
		String demoStr = generatorInvokeDemo();
		
		
		
		HttpServletRequest req = AppContent.getRequest();
		String cxtPath = req.getRealPath("/");
		String dir = cxtPath+"file"+File.separator+"sdkgenerator";
		
		String modelDir = dir+File.separator+"Model";
		String modelFile = modelDir+File.separator+retModelName+".java";
		
		
		String reqDir = dir+File.separator+"Request";
		String reqFile = reqDir+File.separator+funcName+"Request"+".java";
		
		
		String respDir = dir+File.separator+"Response";
		String respFile = respDir+File.separator+funcName+"Response"+".java";
		
		String demoDir = dir+File.separator+"Demo";
		String demoFile = demoDir+File.separator+funcName+"Demo"+".java";
		
		zipFiles.add(modelFile);
		zipFiles.add(reqFile);
		zipFiles.add(respFile);
		zipFiles.add(demoFile);
		
		
		try {
			
			
			File modelDirInfo = new File(modelDir);
			if(!modelDirInfo.exists()){
				modelDirInfo.mkdir();
			}
			
			
			File modelFileInfo = new File(modelFile);
			if(!modelFileInfo.exists()){
				modelFileInfo.createNewFile();
			}
			
			
			
			FileOutputStream modelOutPutStream = new FileOutputStream(modelFile,false);
			modelOutPutStream.write(modelStr.toString().getBytes("UTF-8"));
			modelOutPutStream.flush();
			modelOutPutStream.close();
			
			
		
			
			File reqDirInfo = new File(reqDir);
			if(!reqDirInfo.exists()){
				reqDirInfo.mkdir();
			}
			
			File reqFileInfo = new File(reqFile);
			if(!reqFileInfo.exists()){
				reqFileInfo.createNewFile();
			}
			
			FileOutputStream reqOutPutStream = new FileOutputStream(reqFile,false);
			reqOutPutStream.write(requestStr.toString().getBytes("UTF-8"));
			
			reqOutPutStream.flush();
			reqOutPutStream.close();
			
			
			File respDirFileInfo = new File(respDir);
			if(!respDirFileInfo.exists()){
				respDirFileInfo.mkdir();
			}
			File respFileFileInfo = new File(respFile);
			if(!respFileFileInfo.exists()){
				respFileFileInfo.createNewFile();
			}
			
			FileOutputStream respOutPutStream = new FileOutputStream(respFile,false);
			respOutPutStream.write(responseStr.toString().getBytes("UTF-8"));
			
			respOutPutStream.flush();
			respOutPutStream.close();
			
			
			
			
			File demoDirFileInfo = new File(demoDir);
			if(!demoDirFileInfo.exists()){
				demoDirFileInfo.mkdir();
			}
			File demoFileFileInfo = new File(demoFile);
			if(!demoFileFileInfo.exists()){
				demoFileFileInfo.createNewFile();
			}
			
			FileOutputStream demoFileOutPutStream = new FileOutputStream(demoFile,false);
			demoFileOutPutStream.write(demoStr.toString().getBytes("UTF-8"));
			
			demoFileOutPutStream.flush();
			demoFileOutPutStream.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally{
			
			
			
			String pkgDir = dir+File.separator+"pkgs";
			String zipFileName=pkgDir+ File.separator+ funcName+".zip";
			
			Gzip gzip = new Gzip();
			
			String zipPath = gzip.compress(pkgDir,zipFileName,zipFiles,true);
			Map<String,Object> itemData = new HashMap<String, Object>();
			String zipThePath = "file"+File.separator+"sdkgenerator"+File.separator+"pkgs"+File.separator+funcName+".zip";
			itemData.put("file", zipThePath);
			
			CommonService commonService =AppApplicationContextUtil.getContext().getBean("commonService", CommonService.class);
			commonService.updateServiceInterfaceSDK(serviceInterface.getId(), zipThePath);
			
			
			//tableQueryResult.getResultList().add(itemData);
		}
		
		
		
		
		return true;
	}

	/***
	 * 生成Model
	 * @return
	 */
	private String generatorModel(){
		
		StringBuffer sb = new StringBuffer();
		
		String class_Name =retModelName;
		
		// 生成 package
		sb.append("package ").append(nameSpace).append(".").append("domain;");
		sb.append("\n");
		sb.append("import cn.finder.httpcommons.ApiObject;");
		sb.append("\n");
		sb.append("\n");
		
		
		
		
		
		sb.append("\n");
		sb.append("");
		sb.append("/***");
		
		sb.append("\n");
		sb.append("");
		sb.append("* ").append(serviceInterface.getInterfaceNameCn()).append("返回数据");
		
		sb.append("\n");
		sb.append("");
		sb.append("*/");
		
		sb.append("\n");
		sb.append("").append("public class ").append(class_Name).append(" extends ").append("ApiObject");
		sb.append("{");
		
		
		//字段生成
		for(ShowDataConfig sdc:showDataConfigs)
		{
			
			
			String fieldNameAlias = sdc.getFieldNameAlias();
		
			String clasType ="String";
			
			if(sdc.getDataType()==DataType.DATATYPE_STRING)
			{
				clasType ="String";
			}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
			{
				clasType ="Integer";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
			{
				clasType ="float";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_DATE)
			{
				clasType ="java.util.Date";
			}
			
			
			sb.append("\n");
			sb.append("\t");
			
			sb.append("private").append("\t").append(clasType).append("\t").append(fieldNameAlias).append(";");
			//sb.append("\n");
			
			
			
		}
		
		//属性生成 getXX方法  setXX()
		for(ShowDataConfig sdc:showDataConfigs)
		{
			
			sb.append("\n");
			
			String fieldNameAlias = sdc.getFieldNameAlias();
		
			String clasType ="String";
			
			if(sdc.getDataType()==DataType.DATATYPE_STRING)
			{
				clasType ="String";
			}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
			{
				clasType ="Integer";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
			{
				clasType ="float";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_DATE)
			{
				clasType ="java.util.Date";
			}
			
			
			sb.append("\n");
			sb.append("\t");
			
			sb.append("public").append("\t").append(clasType).append("\t").append("get").append(StringUtils.capitalize(fieldNameAlias)).append("()");
			sb.append("\n");
			sb.append("\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t");
			sb.append("return ").append("\t").append(fieldNameAlias).append(";");
		
			
			sb.append("\n");
			sb.append("\t");
			sb.append("}");
			
			
			sb.append("\n");
			sb.append("\t");
			
			
			sb.append("public").append(" ").append("void").append(" ").append("set").append(StringUtils.capitalize(fieldNameAlias)).append("(").append(clasType).append(" ").append(fieldNameAlias).append(")");
			sb.append("\n");
			sb.append("\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t");
			sb.append("this.").append(fieldNameAlias).append("=").append(fieldNameAlias).append(";");
		
			
			sb.append("\n");
			sb.append("\t");
			sb.append("}");
			
		}
		
		
		
		sb.append("\n");
		sb.append("}");
		
		return sb.toString();
	}

	
	
	/***
	 * 生成Request
	 */
	private String generatorRequest(){
		
		StringBuffer sb = new StringBuffer();
		
		String class_Name = funcName+"Request";
		
		String baseRequest = reqType;
		
		String resp_className = funcName+"Response";
		
		
		// 生成 using
		sb.append("package ").append(nameSpace).append(".").append("request;");
		
		
		sb.append("\n");
		sb.append("import ").append("cn.finder.httpcommons.request").append(".").append(baseRequest).append(";");

		sb.append("\n");
		sb.append("import ").append(nameSpace).append(".response").append(".").append(resp_className).append(";");
		
		
		
	
		
		sb.append("\n");
		
		
		if(REQTYPE_UPLOADREQUEST.equals(reqType)){
			sb.append("import ").append("java.util.Map;");
			sb.append("\n");
			sb.append("import ").append("java.util.HashMap;");
			
		}
		
		

		sb.append("\n");
		sb.append("");
		sb.append("/***");
		
		sb.append("\n");
		sb.append("");
		sb.append("* ").append(serviceInterface.getInterfaceNameCn()).append("请求");
		
		sb.append("\n");
		sb.append("");
		sb.append("*/");
		
		
		sb.append("\n");
		sb.append("").append("public class ").append(class_Name).append(" extends ").append(baseRequest).append("<").append(resp_className).append(">");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t").append("@Override");
		
		sb.append("\n");
		sb.append("\t").append("public").append(" ").append("").append(" ").append("String").append(" ").append("apiName()");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("{");

		sb.append("\n");
		sb.append("\t\t");
		sb.append("return").append("\t").append("\"").append(interfaceKey).append("\"").append(";");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
		sb.append("\n");
		
		sb.append("\n");
		sb.append("\t").append("@Override");
		
		sb.append("\n");
		sb.append("\t").append("public").append(" ").append("").append(" ").append("void").append(" ").append("validate()");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("{");

		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
		
		if(REQTYPE_UPLOADREQUEST.equals(reqType)){
			
			//UploadRequest
			sb.append("\n");
			sb.append("\t").append("@Override");
			sb.append("\t\t").append("public").append(" ").append(" ").append("Map<String, FileItem>").append(" ").append("fileParameters()");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("{");
	
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("Map<String, FileItem> files = new HashMap<String, FileItem>();");
			
			
			//请求属性生成
			for(ShowDataConfig sdc:showDataConfigs)
			{
				
				if(sdc.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE)
				{
			
					sb.append("\n");
					sb.append("\t\t\t");
					sb.append(" files.put(\""+StringUtils.uncapitalize(sdc.getFieldNameAlias())+"\", "+StringUtils.capitalize(sdc.getFieldNameAlias())+");");
					
				}
			}
			
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("return files;");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("}");
			
		}
		
		
		
		

		//请求属性生成
		for(ShowDataConfig sdc:showDataConfigs)
		{
			
			if(sdc.getShowDataType()==null || !"OUT".equalsIgnoreCase(sdc.getShowDataType().getCode()))
			{
				sb.append("\n");
				
				String fieldNameAlias = sdc.getFieldNameAlias();
			
				if("binary_data".equals(fieldNameAlias)){
					//二进制数据不需要作为参数
					continue;
				}
				
				String clasType ="String";
				
				if(sdc.getDataType()==DataType.DATATYPE_STRING)
				{
					clasType ="String";
				}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
				{
					clasType ="Integer";
				}
				else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
				{
					clasType ="float";
				}
				else if (sdc.getDataType()==DataType.DATATYPE_DATE)
				{
					clasType ="java.util.Date";
				}
				if(sdc.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
					if(REQTYPE_UPLOADREQUEST.equals(reqType)){
						
						clasType ="FileItem";
					}
					
				}
				
				sb.append("\n");
				sb.append("\t");
				
				sb.append("private").append("\t").append(clasType).append("\t").append(fieldNameAlias).append(";");
				
				
				
				sb.append("\n");
				sb.append("\t");
				
				sb.append("public").append(" ").append(clasType).append(" ").append("get").append(StringUtils.capitalize(fieldNameAlias)).append("()");
				sb.append("\n");
				sb.append("\t");
				sb.append("{");
				sb.append("\n");
				sb.append("\t\t");
				sb.append(" return ").append(fieldNameAlias).append(";");
				
				sb.append("\n");
				sb.append("\t");
				sb.append("}");
				
				sb.append("\n");
				sb.append("\t");
				
				sb.append("public").append(" ").append("void").append(" ").append("set").append(StringUtils.capitalize(fieldNameAlias)).append("(").append(clasType).append(" ").append(fieldNameAlias).append(")");
				sb.append("\n");
				sb.append("\t");
				sb.append("{");
				sb.append("\n");
				sb.append("\t\t");
				sb.append(" this.").append(fieldNameAlias).append("=").append(fieldNameAlias).append(";");
				
				sb.append("\n");
				sb.append("\t");
				sb.append("}");
				
			}
			
		}
		
		
		
		
		
		
		
		
		sb.append("\n");
		sb.append("");
		sb.append("}");
		
		
		return sb.toString();
	}
	
	
	/***
	 * 生成Response
	 */
	private String generatorResponse(){
		
		StringBuffer sb = new StringBuffer();
		
		
		String baseRequest = "ApiResponse";
		
		String resp_className = funcName+"Response";
		
		
		// 生成 using
		sb.append("package ").append(nameSpace).append(".").append("response;");
		sb.append("\n");
		
		sb.append("import cn.finder.httpcommons.attri.JsonArrayAttribute;");
		sb.append("\n");
		sb.append("import cn.finder.httpcommons.attri.JsonArrayItemAttribute;");
		
		sb.append("\n");
		sb.append("import cn.finder.httpcommons.response.ApiResponse;");
		sb.append("\n");
		
		sb.append("import java.util.List;");
		sb.append("\n");
		
		
		if(REQTYPE_SEARCHTREQUEST.equals(reqType)){
			sb.append("\n");
			sb.append("import ").append(nameSpace).append(".").append("domain.").append(retModelName).append(";");
		}
		
		sb.append("\n");
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/***");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("* ").append(serviceInterface.getInterfaceNameCn()).append("响应");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("*/");
		
		sb.append("\n");
		sb.append("").append("public class ").append(resp_className).append(" extends ").append(baseRequest);
		sb.append("{");
		
		if(REQTYPE_SEARCHTREQUEST.equals(reqType)){
			//StreamRequest 不需要此方法
			
			sb.append("\n");
			sb.append("\t");
			sb.append("private ").append("List<").append(retModelName).append(">").append(" entities").append(";");
			
			sb.append("\n");
			sb.append("\t");
			sb.append("@JsonArrayAttribute(name=\"entities\")");
		
			sb.append("\n");
			sb.append("\t");
			sb.append("@JsonArrayItemAttribute(clazzType="+retModelName+".class)");
			
			sb.append("\n");
			sb.append("\t").append("public").append(" ").append("void").append(" ").append("setEntities(").append("List<").append(retModelName).append(">").append(" entities)");
			
			sb.append("\n");
			sb.append("\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t");
			sb.append("this.entities = entities;");
			
			sb.append("\n");
			sb.append("\t");
			sb.append("}");
			
			
			sb.append("\n");
			sb.append("\t");
			
			sb.append("").append("public").append("\t").append("List<").append(retModelName).append("> ").append("getEntities()");
			
			sb.append("\n");
			sb.append("\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t");
			sb.append("return entities;");
			
			sb.append("\n");
			sb.append("\t");
			sb.append("}");
			
			
		}
			
		
		
		sb.append("\n");
		sb.append("}");
		
		return sb.toString();
	}
	
	/***
	 * 生成调用DEMO
	 */
	private String generatorInvokeDemo(){
		
		StringBuffer sb = new StringBuffer();
		
		String resp_className = funcName+"Response";
		
		
		// 生成 using
		sb.append("package ").append(nameSpace).append(";");
		
		
		
		sb.append("\n");
		sb.append("import ").append(nameSpace).append(".").append("domain;");
		
		sb.append("\n");
		sb.append("import ").append(nameSpace).append(".").append("response;");
		
		sb.append("\n");
		sb.append("import ").append(nameSpace).append(".").append("request;");
		
		
		sb.append("\n");
		sb.append("import cn.finder.wae.httpcommons.ApiConfig.ServiceInterfaceConfig;");
		
		sb.append("\n");
		sb.append("import cn.finder.httpcommons.response.ApiResponse;");
		sb.append("\n");
		sb.append("import cn.finder.wae.httpcommons.HttpGetClient;");
		sb.append("\n");
		sb.append("import cn.finder.httpcommons.IClient;");
		
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/***");
		
		sb.append("\n");
		sb.append("");
		sb.append("* ").append(serviceInterface.getInterfaceNameCn()).append("测试");
		
		sb.append("\n");
		sb.append("");
		sb.append("*/");
		
		sb.append("\n");
		sb.append("").append("public class ").append(funcName).append("Test");
		sb.append("{");
		
		
		sb.append("\n");
		sb.append("\t");
		
		HttpServletRequest req = AppContent.getRequest();
		String rootPath ="http://"+ req.getLocalAddr() +":"+req.getLocalPort()+""+ req.getContextPath();
		
		sb.append("static string url = \""+rootPath+"/service/rest/interface?excludeProperties=fields\";");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("static string appKey = \"testKey\";");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("static string appSecret = \"testSecret\";");
		
		
		sb.append("\n");
		sb.append("\n");
		sb.append("\t").append("public static void main(String[] args)");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t\t");
		
		sb.append("IClient client = new HttpGetClient(url, appKey, appSecret);");
		
		sb.append("\n");
		sb.append("\t\t");
		
		String reqName = funcName+"Request";
		
		
		
		sb.append(reqName).append("\t").append(StringUtils.uncapitalize(reqName)).append("=").append("new ").append(reqName).append("();");
		
		sb.append("\n");
		sb.append("\t\t");
		
		sb.append("//请求参数设置");
		
		sb.append("\n");
		sb.append("\t\t");
		
		String respName = funcName+"Response";
		String respInstName = StringUtils.uncapitalize(resp_className);
		sb.append(respName).append("\t").append(respInstName).append("=").append(" client.execute(").append(StringUtils.uncapitalize(reqName)).append(");");
		
		
		sb.append("\n");
		sb.append("\t\t");
		
		
		if(!REQTYPE_STREAMREQUEST.equals(reqType)){
			
			sb.append("if(").append(respInstName).append(".getMessage().getStatusCode() == cn.finder.httpcommons.domain.Message.StatusCode_OK)");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("{");
			
			sb.append("\n");
			sb.append("\t\t\t");
			
			sb.append("List<").append(retModelName).append(">").append("\t").append(StringUtils.uncapitalize(retModelName)).append("s").append("=").append(respInstName).append(".getEntities();");
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("System.out.println(\"调用成功\");");
			
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("}");
			
			sb.append("\n");
			sb.append("\t\t");
			
			sb.append("else{");
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("System.out.println(\"调用失败\");");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("}");
			
		}
		else{
			
			sb.append("//流请求地址");
			sb.append("\n");
			sb.append("\t\t");
			sb.append("String streamUrl = ").append(respInstName).append(".getBody();");
		}
		
		
		
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
				
		
		sb.append("\n");
		sb.append("}");
		
		return sb.toString();
	}
}
