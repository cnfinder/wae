package cn.finder.wae.queryer.handleclass;

import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.sdk.AndroidSdkenerator;
import cn.finder.wae.queryer.handleclass.sdk.CSharpSdkenerator;
import cn.finder.wae.queryer.handleclass.sdk.SdkGenerator;
import cn.finder.wae.queryer.handleclass.sdk.SdkParam;

import common.Logger;

/**
 * @author: wuhualong
 * @data:2014-12-27
 * @function: 生成SDK 处理
 */
public class SDKGeneratorAfterQueryer  implements QueryerAfterClass{
	
	private Logger logger = Logger.getLogger(SDKGeneratorAfterQueryer.class);
	
	
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		
		
		
		
		logger.debug("===生成SDK ...:");
		
		
		final TableQueryResult qr =new TableQueryResult();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		
		String interfaceKey;
		
		String lang;
		
		String nameSpace;
		String funcName;
		String retModelName;
		String reqType;
		
		 interfaceKey = data.get("interface_key").toString();
		
		 lang = data.get("lang_name").toString();
		
		 nameSpace = data.get("namespace").toString();
		 funcName =data.get("func_name").toString();
		 retModelName = data.get("ret_modelname").toString();
		
		 reqType = data.get("req_type").toString();
		 
		 SdkParam sdkparam=new SdkParam();
		 sdkparam.setInterfaceKey(interfaceKey);
		 sdkparam.setLang(lang);
		 sdkparam.setNameSpace(nameSpace);
		 sdkparam.setFuncName(funcName);
		 sdkparam.setRetModelName(retModelName);
		 sdkparam.setReqType(reqType);
		
		 SdkGenerator sdkGenerator=null;
		if("CSharp".equalsIgnoreCase(lang)){
			sdkGenerator= new CSharpSdkenerator();
			
		}else if("JAVA".equals(lang)){
			sdkGenerator=new AndroidSdkenerator();
		}else if("android".equalsIgnoreCase(lang)){
			sdkGenerator=new AndroidSdkenerator();
		}
		sdkGenerator.generator(sdkparam);
		return tableQueryResult;
	}
	
	
	
	
}