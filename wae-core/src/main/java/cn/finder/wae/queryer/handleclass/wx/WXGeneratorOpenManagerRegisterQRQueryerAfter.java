package cn.finder.wae.queryer.handleclass.wx;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.domain.wx.AppInfo;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.rowmapper.RowMapperFactory;
import cn.finder.wae.common.qrcode.QRCodeUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/***
 *  生成微信公众平台 管理员注册 二维码
 * @author whl
 *
 */
public class WXGeneratorOpenManagerRegisterQRQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
	 	tableQueryResult.getMessage().setMsg("生成微信公众平台 管理员注册 二维码");
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
			
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getForwardParams();
	 	String id = ((String[])data.get("id"))[0].toString();
	 	System.out.println(id);
	 	String appinfosql="select * from  wx_t_appinfo where id=?";
	 	
	 	AppInfo appinfo = queryForSingle(appinfosql, new Object[]{id},new RowMapperFactory.AppInfoRowMapper());
	 	
	 	String appid=appinfo.getAppid();
	 	
	 	String qr_str = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid;
	 	try {
			qr_str+="&redirect_uri="+URLEncoder.encode("http://iv.cwintop.com/iv/visit/wx_mgr_register.jsp?appinfoid="+id,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	 	qr_str+="&response_type=code&scope=snsapi_base&state=1#wechat_redirect";

		List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
	 	Map<String,Object> item =new HashMap<String, Object>();
	 	
	 	
	 	//不需要访客验证,直接生成二维码
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		try {
			QRCodeUtil.encode(qr_str, outStream);
			byte[]  buffer = outStream.toByteArray();
			item.put("binary_data", buffer);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	 	listData.add(item);
	 	tableQueryResult.setResultList(listData);
	 	tableQueryResult.getMessage().setMsg("生成微信公众平台 管理员注册 二维码");
		return tableQueryResult;
		
	}
	
	
}
