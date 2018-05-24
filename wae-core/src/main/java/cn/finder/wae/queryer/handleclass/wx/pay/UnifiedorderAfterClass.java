package cn.finder.wae.queryer.handleclass.wx.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.AppInfo;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.pay.UnifiedorderService;
import cn.finder.wx.pay.WxSign;
import cn.finder.wx.request.WxUnifiedorderRequest;
import cn.finder.wx.response.WxUnifiedorderResponse;

/**
 * @author: finder
 * @function:签名，统一下单
 */
public class UnifiedorderAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(UnifiedorderAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 logger.info("==="+this.getClass());
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
	 	
	 	float total_fee=Float.parseFloat(data.get("total_fee").toString()); //单位元 总费用
	 	
	 	String out_trade_no=data.get("out_trade_no").toString();//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	 	
	 	String attach=data.get("attach").toString(); // 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	 	
	 	String device_info="WEB";
	 	try
	 	{
	 		 device_info=data.get("device_info").toString();
	 	}
	 	catch(Exception e){
	 		device_info="WEB";
	 	}
	 	
	 	String ip="192.168.1.1";
	 	try
	 	{
	 		ip=data.get("ip").toString();
	 	}
	 	catch(Exception e){
	 		ip="192.168.1.1";
	 	}
	 	/*
	 	 * 
	 	 * 商品描述交易字段格式根据不同的应用场景按照以下格式：
		（1）PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
		（2） 公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
		（3） H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
		（4） 线下门店——门店品牌名-城市分店名-实际商品名称，例如： image形象店-深圳腾大- QQ公仔）
		（5） APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
	 	 */
	 	String body=data.get("body").toString(); //
	 	
	 	
	 	AppInfo appInfo=WXAppInfo.getAppInfo(wx_appid);
	 	
	 	
	 	WxUnifiedorderRequest wp_data = new WxUnifiedorderRequest();
        wp_data.setAppid(wx_appid);
        wp_data.setAttach(attach);
        wp_data.setBody(body);
        wp_data.setMch_id(appInfo.getMerchantInfo().getMerchant_id());
        wp_data.setNonce_str(WxSign.getNonceStr());
        
        wp_data.setNotify_url(appInfo.getMerchantInfo().getNotify_url());
        
        wp_data.setOut_trade_no(out_trade_no);
        wp_data.setTotal_fee((int)(total_fee*100));//单位：分
        wp_data.setTrade_type("JSAPI");
        wp_data.setSpbill_create_ip(ip);
        wp_data.setOpenid(user_openid);
        wp_data.setDevice_info(device_info);
        
        logger.info("===WxPaySendData:"+wp_data.toString());
        
        UnifiedorderService us=new UnifiedorderService();
        
        WxUnifiedorderResponse reData= us.unifiedOrder(wp_data,appInfo.getMerchantInfo().getMerchant_key());
        
        
      /*  WxPayReturnData reData = new WxPayReturnData();
        XStream xs1 = new XStream(new DomDriver());
        xs1.alias("xml", WxPayReturnData.class);
        reData = (WxPayReturnData) xs1.fromXML(returnXml);*/
	 	
        
        
        SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
        signMap.put("appId", reData.getAppid()); 
        signMap.put("timeStamp", WxSign.getTimeStamp());
        signMap.put("nonceStr", reData.getNonce_str());
        signMap.put("package", "prepay_id="+reData.getPrepay_id());
        signMap.put("signType", "MD5");
       
        
        
        String sign=WxSign.createSign(signMap,appInfo.getMerchantInfo().getMerchant_key());
        logger.info("PaySIGN:"+sign);
        
        item.put("return_code",reData.getReturn_code());
        item.put("appId", reData.getAppid()); 
        item.put("timeStamp", WxSign.getTimeStamp());
        item.put("nonceStr", reData.getNonce_str());
        item.put("package", "prepay_id="+reData.getPrepay_id());
        item.put("signType", "MD5");
        item.put("paySign",sign);
        
        item.put("result_code", reData.getResult_code());
        item.put("err_code", reData.getErr_code());
        item.put("err_code_des", reData.getErr_code_des());
	 	
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
