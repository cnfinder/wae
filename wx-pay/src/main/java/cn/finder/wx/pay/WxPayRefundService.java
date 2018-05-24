package cn.finder.wx.pay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import cn.finder.wx.request.WXPayRefundRequest;
import cn.finder.wx.response.WXPayRefundResponse;
import cn.finder.wx.service.WXService;

/***
 * 申请退款
 * @author Administrator
 *
 */
public class WxPayRefundService {
	  private  Logger  logger = Logger.getLogger(WxPayRefundService.class);
    
	 
	  /***
	   * 
	   * @param data
	   * @param key
	   * @param certPath "d:\\cert\\apiclient_cert.p12"
	   * @return
	   */
	  public  WXPayRefundResponse refund(WXPayRefundRequest data,String key,String certPath){
		  WXPayRefundResponse resp = null;
		    try {
		      //生成sign签名
		      SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		      parameters.put("appid", data.getAppid()); 
		      parameters.put("mch_id", data.getMch_id());
		      parameters.put("nonce_str", data.getNonce_str());
		      parameters.put("op_user_id", data.getOp_user_id());
		      parameters.put("out_refund_no", data.getOut_refund_no());
		      parameters.put("out_trade_no", data.getOut_trade_no());
		      parameters.put("refund_fee", data.getRefund_fee());
		      parameters.put("total_fee", data.getTotal_fee());
		      parameters.put("transaction_id", data.getTransaction_id());
		      parameters.put("device_info", data.getDevice_info());
		      parameters.put("refund_fee_type", data.getRefund_fee_type());
		      parameters.put("refund_account", data.getRefund_account());
		      logger.info("====key:"+key);
		      
		      logger.info("SIGN:"+WxSign.createSign(parameters,key));
		      
		      data.setSign(WxSign.createSign(parameters,key));
		      
		      logger.info("=====WXPayRefundRequest data:"+data.toString());
		      
		      WXService.WXPayService payService=new WXService.WXPayService();
		      
		      resp= payService.payRefund(data.getMch_id(), certPath, data);
		      
		      logger.info("=======返回结果:" + resp.getBody());
		    } catch (Exception e) {
		    	logger.error(e);
		    } 
		    return resp;
		  }
	  
}
