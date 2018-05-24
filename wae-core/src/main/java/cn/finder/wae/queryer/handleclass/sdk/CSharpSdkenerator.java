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


public class CSharpSdkenerator implements SdkGenerator{
	private Logger logger = Logger.getLogger(CSharpSdkenerator.class);
	
	
	public final static String REQTYPE_DEFAULTREQUEST ="DefaultRequest";
	public final static String REQTYPE_SEARCHTREQUEST ="SearchRequest";
	public final static String REQTYPE_STREAMREQUEST ="StreamRequest";
	public final static String REQTYPE_UPLOADREQUEST ="UploadRequest";
	
	
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
			retModelName = funcName+"Model";
		}
		
		
		String modelStr="";
		
	//	if(!REQTYPE_STREAMREQUEST.endsWith(reqType)){
			modelStr = generatorModel();
		//}
		
		
		String requestStr = generatorRequest();
		String responseStr = generatorResponse();
		String demoStr = generatorInvokeDemo();
		
		HttpServletRequest req = AppContent.getRequest();
		String cxtPath = req.getRealPath("/");
		String dir = cxtPath+"file"+File.separator+"sdkgenerator";
		
		String modelDir = dir+File.separator+"Model";
		String modelFile = modelDir+File.separator+retModelName+".cs";
		
		
		String reqDir = dir+File.separator+"Request";
		String reqFile = reqDir+File.separator+funcName+"Request"+".cs";
		
		
		String respDir = dir+File.separator+"Response";
		String respFile = respDir+File.separator+funcName+"Response"+".cs";
		
		String demoDir = dir+File.separator+"Demo";
		String demoFile = demoDir+File.separator+funcName+"Demo"+".cs";
		
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
			modelOutPutStream.flush();modelOutPutStream.close();
			
			
		
			
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
			
			
			
			String pkgDir = dir+"/"+"pkgs";
			String zipFileName=pkgDir+ File.separator+ funcName+".zip";
			
			Gzip gzip = new Gzip();
			
			String zipPath = gzip.compress(pkgDir,zipFileName,zipFiles,true);
			
			
			Map<String,Object> itemData = new HashMap<String, Object>();
			String zipThePath = "file/sdkgenerator/"+"pkgs/"+funcName+".zip";
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
		
		// 生成 using
		sb.append("using System;");
		sb.append("\n");
		sb.append("using System.Collections.Generic;");
		sb.append("\n");
		/*	sb.append("using System.Linq;");
		sb.append("using System.Text;");
		sb.append("using System.Xml.Serialization;");
		sb.append("using System.ComponentModel;");*/
		sb.append("using Iron.Aps.Api;");
		sb.append("\n");
		sb.append("using Newtonsoft.Json;");
		sb.append("\n");
		
		
		
		sb.append("namespace ").append(nameSpace).append(".").append("Model");
		sb.append("\n");
		sb.append("{");
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// <summary>");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// ").append(serviceInterface.getInterfaceNameCn()).append("返回数据");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// </summary>");
		
		sb.append("\n");
		sb.append("\t").append("public class ").append(class_Name).append(":").append("ApiObject");
		sb.append("{");
		
		
		//字段生成
		for(ShowDataConfig sdc:showDataConfigs)
		{
			
			
			String fieldNameAlias = sdc.getFieldNameAlias();
		
			String clasType ="string";
			
			if(sdc.getDataType()==DataType.DATATYPE_STRING)
			{
				clasType ="string";
			}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
			{
				clasType ="int";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
			{
				clasType ="float";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_DATE)
			{
				clasType ="DateTime";
			}
			
			
			sb.append("\n");
			sb.append("\t\t");
			
			sb.append("private").append("\t").append(clasType).append("\t").append(fieldNameAlias).append(";");
			//sb.append("\n");
			
			
			
		}
		
		//属性生成
		for(ShowDataConfig sdc:showDataConfigs)
		{
			
			sb.append("\n");
			
			String fieldNameAlias = sdc.getFieldNameAlias();
		
			String clasType ="string";
			
			if(sdc.getDataType()==DataType.DATATYPE_STRING)
			{
				clasType ="string";
			}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
			{
				clasType ="int";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
			{
				clasType ="float";
			}
			else if (sdc.getDataType()==DataType.DATATYPE_DATE)
			{
				clasType ="DateTime";
			}
			
			
			sb.append("\n");
			sb.append("\t\t");
			
			sb.append("public").append("\t").append(clasType).append("\t").append(StringUtils.capitalize(fieldNameAlias));
			sb.append("\n");
			sb.append("\t\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("get{");
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("return ").append("\t").append(fieldNameAlias).append(";");
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("}");
			
			
			
			
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("set{");
			
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append(fieldNameAlias).append("=").append("value").append(";");
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("NotifyPropertyChanged(\"").append(StringUtils.capitalize(fieldNameAlias)).append("\")").append(";");
			
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("}");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("}");
			
		}
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
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
		sb.append("using System;");
		sb.append("\n");
		sb.append("using System.Text;");
		
		sb.append("\n");
		sb.append("using System.Collections.Generic;");
		sb.append("\n");
		sb.append("using Iron.Aps.Api.Request;");
		sb.append("\n");
		
		
		
		
		sb.append("using ").append(nameSpace).append(".").append("Response;");
		
		sb.append("\n");
		
		
		if(REQTYPE_UPLOADREQUEST.equals(reqType)){
			sb.append("using Iron.Aps.Api.Util;");
			sb.append("\n");
		}
		
		
		sb.append("namespace ").append(nameSpace).append(".").append("Request");
		sb.append("\n");
		sb.append("{");

		sb.append("\n");
		sb.append("\t");
		sb.append("/// <summary>");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// ").append(serviceInterface.getInterfaceNameCn()).append("请求");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// </summary>");
		
		
		sb.append("\n");
		sb.append("\t").append("public class ").append(class_Name).append(":").append(baseRequest).append("<").append(resp_className).append(">");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t\t").append("public").append(" ").append("override").append(" ").append("string").append(" ").append("GetApiName()");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("{");

		sb.append("\n");
		sb.append("\t\t\t");
		sb.append("return").append("\t").append("\"").append(interfaceKey).append("\"").append(";");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("}");
		
		sb.append("\n");
		sb.append("\t\t").append("public").append(" ").append("override").append(" ").append("void").append(" ").append("Validate()");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("{");

		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("}");
		
		
		if(REQTYPE_UPLOADREQUEST.equals(reqType)){
			
			//UploadRequest
			sb.append("\n");
			sb.append("\t\t").append("public").append(" ").append("override").append(" ").append("IDictionary<string, FileItem>").append(" ").append("GetFileParameters()");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("{");
	
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("IDictionary<string, FileItem> files = new Dictionary<string, FileItem>();");
			
			
			//请求属性生成
			for(ShowDataConfig sdc:showDataConfigs)
			{
				
				if(sdc.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE)
				{
			
					sb.append("\n");
					sb.append("\t\t\t");
					sb.append(" files.Add(\""+StringUtils.uncapitalize(sdc.getFieldNameAlias())+"\", "+StringUtils.capitalize(sdc.getFieldNameAlias())+");");
					
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
				
				String clasType ="string";
				
				if(sdc.getDataType()==DataType.DATATYPE_STRING)
				{
					clasType ="string";
				}else if (sdc.getDataType()==DataType.DATATYPE_NUMBER)
				{
					clasType ="int";
				}
				else if (sdc.getDataType()==DataType.DATATYPE_FLOAT)
				{
					clasType ="float";
				}
				else if (sdc.getDataType()==DataType.DATATYPE_DATE)
				{
					clasType ="DateTime";
				}
				if(sdc.getShowType() == ConstantsCache.ControlType.CONTROLTYPE_BINARY_FILE){
					if(REQTYPE_UPLOADREQUEST.equals(reqType)){
						
						clasType ="FileItem";
					}
					
				}
				
				sb.append("\n");
				sb.append("\t\t");
				
				sb.append("public").append(" ").append(clasType).append(" ").append(StringUtils.capitalize(fieldNameAlias));
				sb.append("\n");
				sb.append("\t\t");
				sb.append("{");
				sb.append("\n");
				sb.append("\t\t\t");
				sb.append("get;");
				sb.append("\n");
				sb.append("\t\t\t");
				sb.append("set;");
				
				
				sb.append("\n");
				sb.append("\t\t");
				sb.append("}");
				
			}
			
		}
		
		
		
		
		
		
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
		sb.append("\n");
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
		sb.append("using System;");
		sb.append("\n");
		sb.append("using System.Text;");
		sb.append("\n");
		sb.append("using System.Collections.Generic;");
		
		sb.append("\n");
		sb.append("using Iron.Aps.Api.Response;");
		sb.append("\n");
		sb.append("using Iron.Aps.Api.Attri;");
		
		if(REQTYPE_SEARCHTREQUEST.equals(reqType)){
			sb.append("\n");
			sb.append("using ").append(nameSpace).append(".").append("Model;");
		}
		
		sb.append("\n");
		
		
		sb.append("namespace ").append(nameSpace).append(".").append("Response");
		sb.append("\n");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// <summary>");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// ").append(serviceInterface.getInterfaceNameCn()).append("响应");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// </summary>");
		
		sb.append("\n");
		sb.append("\t").append("public class ").append(resp_className).append(":").append(baseRequest);
		sb.append("{");
		
		if(REQTYPE_SEARCHTREQUEST.equals(reqType)){
			//StreamRequest 不需要此方法
			sb.append("\n");
			sb.append("\t\t");
			sb.append("[JsonArrayAttribute(\"entities\"), JsonArrayItemAttribute(\"\")]");
			sb.append("\n");
			sb.append("\t\t").append("public").append("\t").append("List<").append(retModelName).append(">").append("\t").append("Entities");
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("{");
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("get;");
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("set;");
			
			
			sb.append("\n");
			sb.append("\t\t");
			sb.append("}");
		}
			
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
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
		sb.append("using System;");
		sb.append("\n");
		sb.append("using System.Text;");
		sb.append("\n");
		sb.append("using System.Collections.Generic;");
		
		sb.append("\n");
		sb.append("using Iron.Aps.Api;");
		
		
		sb.append("\n");
		sb.append("using ").append(nameSpace).append(".").append("Model;");
		
		sb.append("\n");
		sb.append("using ").append(nameSpace).append(".").append("Response;");
		
		sb.append("\n");
		sb.append("using ").append(nameSpace).append(".").append("Request;");
		
		
		
		
		sb.append("\n");
		sb.append("namespace ").append(nameSpace);
		
		sb.append("\n");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// <summary>");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// ").append(serviceInterface.getInterfaceNameCn()).append("测试");
		
		sb.append("\n");
		sb.append("\t");
		sb.append("/// </summary>");
		
		sb.append("\n");
		sb.append("\t").append("public class ").append(funcName).append("Test");
		sb.append("{");
		
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("static string url = \"http://localhost:8080/APS/service/rest/interface?excludeProperties=fields\";");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("static string appKey = \"testKey\";");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("static string appSecret = \"testSecret\";");
		
		
		sb.append("\n");
		sb.append("\n");
		sb.append("\t\t").append("public static void Main(string[] args)");
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("{");
		
		sb.append("\n");
		sb.append("\t\t\t");
		
		sb.append("IClient client = new DefaultClient(url, appKey, appSecret);");
		
		sb.append("\n");
		sb.append("\t\t\t");
		
		String reqName = funcName+"Request";
		
		
		
		sb.append(reqName).append("\t").append(StringUtils.uncapitalize(reqName)).append("=").append("new ").append(reqName).append("();");
		
		sb.append("\n");
		sb.append("\t\t\t");
		
		sb.append("//请求参数设置");
		
		sb.append("\n");
		sb.append("\t\t\t");
		
		String respName = funcName+"Response";
		String respInstName = StringUtils.uncapitalize(resp_className);
		sb.append(respName).append("\t").append(respInstName).append("=").append(" client.Execute(").append(StringUtils.uncapitalize(reqName)).append(");");
		
		
		sb.append("\n");
		sb.append("\t\t\t");
		
		
		if(!REQTYPE_STREAMREQUEST.equals(reqType)){
			
			sb.append("if(").append(respInstName).append(".Message.StatusCode == Iron.Aps.Api.Domain.Message.StatusCode_OK)");
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("{");
			
			sb.append("\n");
			sb.append("\t\t\t\t");
			
			sb.append("List<").append(retModelName).append(">").append("\t").append(StringUtils.uncapitalize(retModelName)).append("s").append("=").append(respInstName).append(".Entities;");
			
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("Console.WriteLine(\"调用成功\");");
			
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("}");
			
			sb.append("\n");
			sb.append("\t\t\t");
			
			sb.append("else{");
			sb.append("\n");
			sb.append("\t\t\t\t");
			sb.append("Console.WriteLine(\"调用失败\");");
			
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("}");
			
		}
		else{
			
			sb.append("//流请求地址");
			sb.append("\n");
			sb.append("\t\t\t");
			sb.append("String streamUrl = ").append(respInstName).append(".Body;");
		}
		
		
		
		
		
		sb.append("\n");
		sb.append("\t\t");
		sb.append("}");
				
		
		
		sb.append("\n");
		sb.append("\t");
		sb.append("}");
		
		sb.append("\n");
		sb.append("}");
		
		return sb.toString();
	}
}
