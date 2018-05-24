package cn.finder.wx.pay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import cn.finder.wx.request.WxUnifiedorderRequest;
import cn.finder.wx.response.WxUnifiedorderResponse;
import cn.finder.wx.service.WXService;


public class UnifiedorderService {
	  private  Logger  logger = Logger.getLogger(UnifiedorderService.class);
    
	 /* public  String unifiedOrder(WxPaySendData data,String key){
	    //统一下单支付
	    String returnXml = null;
	    try {
	      //生成sign签名
	      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
	      parameters.put("appid", data.getAppid()); 
	      parameters.put("attach", data.getAttach());
	      parameters.put("body", data.getBody());
	      parameters.put("mch_id", data.getMch_id());
	      parameters.put("nonce_str", data.getNonce_str());
	      parameters.put("notify_url", data.getNotify_url());
	      parameters.put("out_trade_no", data.getOut_trade_no());
	      parameters.put("trade_type", data.getTrade_type());
	      parameters.put("spbill_create_ip", data.getSpbill_create_ip());
	      parameters.put("openid", data.getOpenid());
	      parameters.put("device_info", data.getDevice_info());
	      
	      parameters.put("total_fee", data.getTotal_fee());
	      
	      logger.info("====key:"+key);
	      
	      logger.info("SIGN:"+WxSign.createSign(parameters,key));
	      
	      data.setSign(WxSign.createSign(parameters,key));
	      
	      logger.info("=====WxPaySendData data:"+data.toString());
	      
	      XStream xs = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
	      xs.alias("xml", WxPaySendData.class);
	      String xml = xs.toXML(data);
	      logger.info("统一下单xml为:\n" + xml);
	      
	      WebUtils webUtil=new WebUtils();
	      
	      returnXml =webUtil.doPostData("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
	      
	      logger.info("返回结果:" + returnXml);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } 
	    return returnXml;
	  }*/
	  
	  public  WxUnifiedorderResponse unifiedOrder(WxUnifiedorderRequest req,String key){
		    //统一下单支付
		  WxUnifiedorderResponse resp=null;
		    try {
		      //生成sign签名
		      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		      parameters.put("appid", req.getAppid()); 
		      parameters.put("attach", req.getAttach());
		      parameters.put("body", req.getBody());
		      parameters.put("mch_id", req.getMch_id());
		      parameters.put("nonce_str", req.getNonce_str());
		      parameters.put("notify_url", req.getNotify_url());
		      parameters.put("out_trade_no", req.getOut_trade_no());
		      parameters.put("trade_type", req.getTrade_type());
		      parameters.put("spbill_create_ip", req.getSpbill_create_ip());
		      parameters.put("openid", req.getOpenid());
		      parameters.put("device_info", req.getDevice_info());
		      
		      parameters.put("total_fee", req.getTotal_fee());
		      
		      logger.info("====key:"+key);
		      
		      logger.info("SIGN:"+WxSign.createSign(parameters,key));
		      
		      req.setSign(WxSign.createSign(parameters,key));
		      
		      logger.info("=====WxPaySendData data:"+req.toString());
		      
		      
		      
		      WXService.WXPayService payService=new WXService.WXPayService();
		      
		       resp= payService.unifiedorder(req);
		      
		      logger.info("返回结果:" + resp.getBody());
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
		    return resp;
		  }
	  
	  
}
