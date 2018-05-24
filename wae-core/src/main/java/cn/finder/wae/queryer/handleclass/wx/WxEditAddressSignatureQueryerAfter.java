package cn.finder.wae.queryer.handleclass.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.EncoderHandler;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/***
 * 共享地址编辑
 * 微信支付功能一定要开启
 * noncestr=Wm3WZYTPz0wzccnW
   jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
   timestamp=1414587457
   url=http://mp.weixin.qq.com?params=value
   返回: signature 值
   https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_9
 * @author whl
 *
 */
public class WxEditAddressSignatureQueryerAfter extends QueryerDBAfterClass {
 
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
		
		
		String wx_appid="";
		
		try{
			wx_appid=data.get("wx_appid").toString();
		}
		catch(Exception e){
			
			wx_appid="";
		}
		
		String access_token=null;
		
		try{
			access_token=data.get("access_token").toString();
		}
		catch(Exception e){
			logger.error(e);
		}
		logger.info("==============jsapi_ticket:"+access_token);
		
		
		
		
		
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
		sb.append("accesstoken=").append(access_token);
		sb.append("&appid=").append(wx_appid);
		sb.append("&noncestr=").append(noncestr);
		sb.append("&timestamp=").append(timestamp);
		sb.append("&url=").append(url);
		
		
		String signature=EncoderHandler.encode("SHA1", sb.toString());
		
		logger.info("=========signature:"+signature);
		
		
		List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
	 	Map<String,Object> item =new HashMap<String, Object>();
	 	item.put("noncestr", noncestr);
	 	item.put("access_token", access_token);
	 	item.put("timestamp", timestamp);
	 	item.put("url",url);
	 	item.put("signature", signature);
	 	
	 	listData.add(item);
	 	tableQueryResult.setResultList(listData);
	 	tableQueryResult.getMessage().setMsg("签名成功");
		return tableQueryResult;
		
	}
	
	
	
}
