package cn.finder.wae.queryer.handleclass.wx.app;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.EncoderHandler;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.cache.WXCache;

/***
 * 检查SESSION是否存在或者过期
 * is_exist:1=>存在
 * @author whl
 *
 */
public class CheckSessionQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 	
		
		Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
	 	
 		resultList.add(mapItem);
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
			
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	String wx_session = data.get("wx_session").toString();
	 	
	 	String rawData=data.get("rawData").toString();
	 	String signature=data.get("signature").toString();
	 	
	 	
	 	
	 	
	 	
	 	
	 	if(WXCache.getInstance().getWXSessionCache().constains(wx_session)){
	 		logger.info("=============包含: wx_session:"+wx_session);
	 		String session_key=WXCache.getInstance().getWXSessionCache().get(wx_session).get("session_key").toString();
	 		logger.info("============= session_key:"+session_key);
	 		
	 		StringBuffer sb = new StringBuffer(rawData);
			sb.append(session_key);

			byte[] encryData=null;
			try {
				encryData = EncoderHandler.sha1Byte(sb.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String encryDatastr="";
			try {
				encryDatastr = new String(encryData,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Boolean checkStatus = Arrays.equals(encryData, signatureData);
	 		
			logger.info("===encryDatastr:"+encryDatastr);
			logger.info("===signature:"+signature);
			
			if(signature.equals(encryDatastr)){
				mapItem.put("is_exist", 1);
		 		mapItem.put("openid", WXCache.getInstance().getWXSessionCache().get(wx_session).get("openid"));
			}
			else{
				mapItem.put("is_exist", 0);
			}
	 		
	 	}else{
	 		mapItem.put("is_exist", 0);
	 	}
	 	
	 	return tableQueryResult;
	 	
		
	}
	
}
