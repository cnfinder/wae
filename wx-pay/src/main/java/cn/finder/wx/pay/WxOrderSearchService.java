package cn.finder.wx.pay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import cn.finder.wx.pay.utils.BankTypeConstants;
import cn.finder.wx.request.WxOrderSearchRequest;
import cn.finder.wx.response.WxOrderSearchResponse;
import cn.finder.wx.service.WXService;

/*
 * 
 * 微信订单查询接口
 */
public class WxOrderSearchService {
	  private  Logger  logger = Logger.getLogger(WxOrderSearchService.class);
    
	
	  
	  public  WxOrderSearchResponse orderSearch(WxOrderSearchRequest req,String key){
		    
		  WxOrderSearchResponse resp=null;
		    try {
		      //生成sign签名
		      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		      parameters.put("appid", req.getAppid()); 
		      parameters.put("mch_id", req.getMch_id());
		      parameters.put("nonce_str", req.getNonce_str());
		      parameters.put("out_trade_no", req.getOut_trade_no());
		      
		      logger.info("====key:"+key);
		      
		      logger.info("SIGN:"+WxSign.createSign(parameters,key));
		      
		      req.setSign(WxSign.createSign(parameters,key));
		      
		      logger.info("=====WxOrderSearchSendData data:"+req.toString());
		      
		      
		      
		      WXService.WXPayService payService=new WXService.WXPayService();
		      
		       resp= payService.orderSearch(req);
		       
		       if(resp.isSuccess()){
		    	   resp.setBank_type_des(BankTypeConstants.getBankTypeDes(resp.getBank_type()));
		       }
		      
		      logger.info("返回结果:" + resp.getBody());
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
		    return resp;
		  }
	  
	  
}
