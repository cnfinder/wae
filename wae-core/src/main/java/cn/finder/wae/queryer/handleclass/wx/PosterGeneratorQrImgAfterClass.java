package cn.finder.wae.queryer.handleclass.wx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.qrcode.QRCodeUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.request.GetQrRequest;
import cn.finder.wx.response.GetQrResponse;
import cn.finder.wx.service.WXService;

/**
 * @author: whl
 * @function:生成推广二维码 参数 user_openid, wx_appid
 *  使用在 新的粉丝未关注的时候弹出
 */
public class PosterGeneratorQrImgAfterClass extends QueryerDBAfterClass {
	
	private Logger logger=Logger.getLogger(PosterGeneratorQrImgAfterClass.class);

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		 super.handle(tableQueryResult, showTableConfigId, condition);
		 logger.info("==="+this.getClass());
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 Map<String,Object> item=new HashMap<String, Object>();
		 resultList.add(item);
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
		tableQueryResult.setCount(0l);
	 	
	 	
		 
		String user_openid = "";
		
		try{
			user_openid = data.get("user_openid").toString();
		}
		catch(Exception e){
			user_openid="";
		}
		
		
		String appid="";
		appid= data.get("wx_appid").toString();//必须
	 	
		
		String url="";
		if(StringUtils.isEmpty(user_openid)){
			
		}
		else{
			
		}
	 	
	 	

		WXService wxservice=new WXService();
		
		String currentUser=user_openid;
		
		String access_token=WXAppInfo.getAppInfo(appid).getAccessToken();
		logger.info("===local access token:"+access_token);
		
	
		
		
		//1.从数据库中 获取 用户信息  
		// 参数: openid => xmlEntity.getToUserName()
		
		
		
		GetQrRequest req=new GetQrRequest();
		
		req.setAction_name(GetQrRequest.ACTION_NAME_QR_LIMIT_STR_SCENE);
		Map<String,GetQrRequest.Scene> action_info=new  HashMap<String,GetQrRequest.Scene>();
		
		GetQrRequest.Scene scene=new GetQrRequest.Scene();
		scene.setScene_str(currentUser);
		action_info.put("scene", scene);
		
		req.setAction_info(action_info);
		
		String jsonStr=new JSONObject(req).toString();
		logger.info("====GetQrRequest.toJson:"+jsonStr);
		
		GetQrResponse resp= wxservice.createQr(access_token,req);
		logger.info("=====GetQrResponse:"+resp.getBody());
		if(true){
			
			
    		String ticket=resp.getTicket();
			url=resp.getUrl();
			//根据url获取二维码生成二维码
			logger.info("===qr.url:"+url);
			
			
			
			BufferedImage qrimg=null;
			try {
				qrimg = QRCodeUtil.createImage(url, null,true);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 推广二维码
					
			
			
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			
			JPEGImageEncoder   encoder = JPEGCodec.createJPEGEncoder(baos);     
		    
			try {
				encoder.encode(qrimg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] mediabytes =baos.toByteArray();
			
			item.put("binary_data", mediabytes);
			tableQueryResult.setCount(1l);
		} 
		
		
		return tableQueryResult;
		 
	}

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	
}
