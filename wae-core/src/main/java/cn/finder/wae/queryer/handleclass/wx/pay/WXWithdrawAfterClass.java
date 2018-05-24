package cn.finder.wae.queryer.handleclass.wx.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.AppInfo;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.pay.WithdrawService;
import cn.finder.wx.pay.WxSign;
import cn.finder.wx.request.MchPayRequest;
import cn.finder.wx.response.MchPayResponse;

/**
 * @author: finder
 * @function:提现
 */
public class WXWithdrawAfterClass extends QueryerDBAfterClass {
	 
	private Logger logger=Logger.getLogger(WXWithdrawAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 logger.info("===cn.finder.wae.queryer.handleclass.wx.pay.WXWithdrawAfterClass");
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		
		Map<String,Object> item =new HashMap<String, Object>();
		resultList.add(item);
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
	 	
	 	String user_openid=data.get("user_openid").toString();
	 	String wx_appid=data.get("wx_appid").toString();
	 	float amount=Float.parseFloat(data.get("amount").toString()); //单位元 总费用
	 	String mchid=WXAppInfo.getAppInfo(wx_appid).getMerchantInfo().getMerchant_id();
	 	
	 	
	 	
	 	
	 	String ip="";//客户端IP地址
	 	try
	 	{
	 		ip=data.get("ip").toString();
	 	}
	 	catch(Exception e){
	 		ip="192.168.1.1";
	 	}
	 	
	 	
	 	String pay_desc="";//付款说明
	 	try
	 	{
	 		pay_desc=data.get("pay_desc").toString();
	 	}
	 	catch(Exception e){
	 		pay_desc="";
	 	}
	 	
	 	
	 	
	 	String re_user_name="";
	 	try
	 	{
	 		re_user_name=data.get("re_user_name").toString();
	 	}
	 	catch(Exception e){
	 		re_user_name="";
	 	}
	 	
	 	
	 	String device_info=""; //客户端设备号
	 	try
	 	{
	 		device_info=data.get("device_info").toString();
	 	}
	 	catch(Exception e){
	 		device_info="";
	 	}
	 	
	 	String certPath=""; //支付证书
	 	try
	 	{
	 		device_info=data.get("certPath").toString();
	 	}
	 	catch(Exception e){
	 		certPath="";
	 	}
	 	
	 	
	 	AppInfo appInfo=WXAppInfo.getAppInfo(wx_appid);
	 	
	 	//1. 判断提现额度是否大于现金账户
	 	
	 	MchPayRequest wp_data = new MchPayRequest();
	 	
        wp_data.setMch_appid(wx_appid);
        wp_data.setMchid(mchid);
        wp_data.setNonce_str(WxSign.getNonceStr());
        
        wp_data.setPartner_trade_no(System.currentTimeMillis()+"");
        
        wp_data.setSpbill_create_ip(ip);
        wp_data.setOpenid(user_openid);
        wp_data.setCheck_name("NO_CHECK");
        wp_data.setAmount((int)(amount*100));//必须大于100分
        wp_data.setDesc(pay_desc);
        wp_data.setRe_user_name(re_user_name);
        wp_data.setDevice_info(device_info);
        
        logger.info("===WxWithdrawSendData:"+wp_data.toString());
        
        WithdrawService ws=new WithdrawService();
        
        
        
        MchPayResponse reData = ws.withdraw(wp_data,appInfo.getMerchantInfo().getMerchant_key(),certPath);
        
        item.put("return_code",reData.getReturn_code());
        item.put("return_msg",reData.getReturn_msg());
        item.put("result_code",reData.getResult_code());
        item.put("mch_appid", reData.getMch_appid());
        
        item.put("mchid", reData.getMchid());
        
        
        item.put("timeStamp", WxSign.getTimeStamp());
        item.put("nonceStr", reData.getNonce_str());
        item.put("partner_trade_no",reData.getPartner_trade_no());
        item.put("err_code_des",reData.getErr_code_des());
        item.put("err_code",reData.getErr_code());
        item.put("payment_no",reData.getPayment_no());
        item.put("payment_time",reData.getPayment_time());
	 	
        
        
	 	
        tableQueryResult.setCount(1l);
        tableQueryResult.setPageSize(1);
        tableQueryResult.setPageIndex(1);
		 
		 return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
