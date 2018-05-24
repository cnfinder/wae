package cn.finder.wae.queryer.handleclass.wx.corp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.comm.EncoderHandler;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;

/***
 * JSSDK-签名 -企业号
 * app
 * noncestr=Wm3WZYTPz0wzccnW
   jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
   timestamp=1414587457
   url=http://mp.weixin.qq.com?params=value
   返回: signature 值
 * @author whl
 *
 */
public class JsapiSignatureQueryerAfter extends QueryerDBAfterClass {
 
	@SuppressWarnings("unchecked")
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
	 	tableQueryResult.getMessage().setMsg("签名成功");
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
			
	 			
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();

		
		String noncestr="";
		
		try{
			noncestr=data.get("noncestr").toString();
		}
		catch(Exception e){
			
			noncestr=UUID.randomUUID().toString().replace("-", "");
		}
		
		String wx_appid=data.get("wx_appid").toString();
		
		
		String jsapi_ticket=null;
		
		try{
			jsapi_ticket=data.get("jsapi_ticket").toString();
			if(StringUtils.isEmpty(jsapi_ticket)){
				try{
				  
					String corpid=wx_appid;
					
					//新版的腾讯架构 已经改成  token 和应用对应
					jsapi_ticket=WXAppInfo.getCorpAccessTokenInfo(corpid).getJsapi_ticket();
					
					
				}
				finally{
					
				}
			
			}
		}
		catch(Exception e){
			try{
				String corpid=wx_appid;
				jsapi_ticket=WXAppInfo.getCorpAccessTokenInfo(corpid).getJsapi_ticket();
			}
			catch(Exception exp){
				
			}
		}
		logger.info("==============corp jsapi_ticket:"+jsapi_ticket);
		
		String timestamp="";
		
		try{
			timestamp=data.get("timestamp").toString();
		}
		catch(Exception e){
			timestamp=new Date().getTime()+"";
		}
		
		String url="";
		
		try{
			url=data.get("url").toString();
		}
		catch(Exception e){
			url=((MapParaQueryConditionDto<String, Object>)condition).getForwardParams().get("wae_request_url").toString();
		}
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("jsapi_ticket=").append(jsapi_ticket);
		sb.append("&noncestr=").append(noncestr);
		sb.append("&timestamp=").append(timestamp);
		sb.append("&url=").append(url);
		
		logger.info("===签名数据:"+sb.toString());
		String signature=EncoderHandler.encode("SHA1", sb.toString());
		
		logger.info("=========signature:"+signature);
		
		
		List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
	 	Map<String,Object> item =new HashMap<String, Object>();
	 	item.put("noncestr", noncestr);
	 	item.put("jsapi_ticket", jsapi_ticket);
	 	item.put("timestamp", timestamp);
	 	item.put("url",url);
	 	item.put("signature", signature);
	 	
	 	listData.add(item);
	 	tableQueryResult.setResultList(listData);
	 	tableQueryResult.getMessage().setMsg("签名成功");
		return tableQueryResult;
		
	}
	
	
}
