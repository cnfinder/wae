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
import cn.finder.wx.pay.WxOrderSearchService;
import cn.finder.wx.pay.WxSign;
import cn.finder.wx.request.WxOrderSearchRequest;
import cn.finder.wx.response.WxOrderSearchResponse;

/**
 * @author: finder
 * @function:微信订单查询
 */
public class OrderSearchAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(OrderSearchAfterClass.class);

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
	 	
	 	
	 	String wx_appid=data.get("wx_appid").toString();
	 	String out_trade_no=data.get("out_trade_no").toString();//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	 	
	 	
	 	
	 	
	 	AppInfo appInfo=WXAppInfo.getAppInfo(wx_appid);
	 	
	 	
	 	WxOrderSearchRequest req = new WxOrderSearchRequest();
	 	req.setAppid(wx_appid);
	 	req.setMch_id(appInfo.getMerchantInfo().getMerchant_id());
	 	req.setNonce_str(WxSign.getNonceStr());
        
	 	req.setOut_trade_no(out_trade_no);
        
        
       
        WxOrderSearchService orderSearchService=new WxOrderSearchService();
        WxOrderSearchResponse reData=  orderSearchService.orderSearch(req, appInfo.getMerchantInfo().getMerchant_key());
        
        item.put("return_code", reData.getReturn_code());
        item.put("result_code", reData.getResult_code());
        item.put("err_code", reData.getErr_code());
        item.put("err_code_des", reData.getErr_code_des());
	 	item.put("total_fee", reData.getTotal_fee());
	 	item.put("bank_type", reData.getBank_type());
	 	item.put("bank_type_des", reData.getBank_type_des());
	 	item.put("trade_state", reData.getTrade_state());
	 	item.put("cash_fee ", reData.getCash_fee());
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
