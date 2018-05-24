package cn.finder.wx.pay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import cn.finder.wx.request.MchPayRequest;
import cn.finder.wx.response.MchPayResponse;
import cn.finder.wx.service.WXService;


public class WithdrawService {
	  private  Logger  logger = Logger.getLogger(WithdrawService.class);
    
	 /* public  String withdraw(WxWithdrawSendData data,String key){
	    //统一下单支付
	    String returnXml = null;
	    try {
	      //生成sign签名
	      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
	      parameters.put("mch_appid", data.getMch_appid()); 
	      parameters.put("mchid", data.getMchid());
	      parameters.put("nonce_str", data.getNonce_str());
	      parameters.put("partner_trade_no", data.getPartner_trade_no());
	      parameters.put("openid", data.getOpenid());
	      parameters.put("check_name", data.getCheck_name());
	      parameters.put("amount", data.getAmount());
	      parameters.put("spbill_create_ip", data.getSpbill_create_ip());
	      parameters.put("device_info", data.getDevice_info());
	      parameters.put("desc", data.getDesc());
	      parameters.put("re_user_name", data.getRe_user_name());
	      logger.info("====key:"+key);
	      
	      logger.info("SIGN:"+WxSign.createSign(parameters,key));
	      
	      data.setSign(WxSign.createSign(parameters,key));
	      
	      logger.info("=====WxWithdrawSendData data:"+data.toString());
	      
	      XStream xs = new XStream(new DomDriver("UTF-8",new XmlFriendlyNameCoder("-_", "_")));
	      xs.alias("xml", WxWithdrawSendData.class);
	      String xml = xs.toXML(data);
	      logger.info("提现到钱包xml为:\n" + xml);
	      
	      WebUtils.ClientCertHttpsSSL webUtil=new WebUtils.ClientCertHttpsSSL();
	      
	      returnXml =webUtil.doRequest("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers",
	    		  "POST", xml,"d:\\cert\\apiclient_cert.p12",data.getMchid());
	      
	      logger.info("=======返回结果:" + returnXml);
	    } catch (Exception e) {
	    	logger.error(e);
	    } 
	    return returnXml;
	  }
	  */
	  
	  
	  /***
	   * 
	   * @param data
	   * @param key
	   * @param certPath "d:\\cert\\apiclient_cert.p12"
	   * @return
	   */
	  public  MchPayResponse withdraw(MchPayRequest data,String key,String certPath){
		    //统一下单支付
		  MchPayResponse resp = null;
		    try {
		      //生成sign签名
		      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		      parameters.put("mch_appid", data.getMch_appid()); 
		      parameters.put("mchid", data.getMchid());
		      parameters.put("nonce_str", data.getNonce_str());
		      parameters.put("partner_trade_no", data.getPartner_trade_no());
		      parameters.put("openid", data.getOpenid());
		      parameters.put("check_name", data.getCheck_name());
		      parameters.put("amount", data.getAmount());
		      parameters.put("spbill_create_ip", data.getSpbill_create_ip());
		      parameters.put("device_info", data.getDevice_info());
		      parameters.put("desc", data.getDesc());
		      parameters.put("re_user_name", data.getRe_user_name());
		      logger.info("====key:"+key);
		      
		      logger.info("SIGN:"+WxSign.createSign(parameters,key));
		      
		      data.setSign(WxSign.createSign(parameters,key));
		      
		      logger.info("=====WxWithdrawSendData data:"+data.toString());
		      
		      WXService.WXPayService payService=new WXService.WXPayService();
		      
		      resp= payService.mchPay(data.getMchid(), certPath, data);
		      
		      logger.info("=======返回结果:" + resp.getBody());
		    } catch (Exception e) {
		    	logger.error(e);
		    } 
		    return resp;
		  }
	  
}
