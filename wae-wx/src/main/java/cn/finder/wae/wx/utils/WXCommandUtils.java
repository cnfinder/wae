package cn.finder.wae.wx.utils;

import java.io.ByteArrayOutputStream;

import org.apache.batik.css.engine.MediaRule;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.finder.httpcommons.utils.FileItem;
import cn.finder.wae.business.domain.wx.WXCommand;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.common.qrcode.QRCodeUtil;
import cn.finder.wae.wx.data.WXAppInfo;
import cn.finder.wx.domain.ReceiveXmlEntity;
import cn.finder.wx.domain.resp.ImgMsgSend;
import cn.finder.wx.domain.resp.ImgMsgSend.Image;
import cn.finder.wx.domain.resp.MsgSendInterface;
import cn.finder.wx.domain.resp.TextImgMsgSend;
import cn.finder.wx.domain.resp.TextImgMsgSend.Articles;
import cn.finder.wx.domain.resp.TextMsgSend;
import cn.finder.wx.request.MediaAddTempRequest;
import cn.finder.wx.response.MediaAddTempResponse;
import cn.finder.wx.service.WXService;

public class WXCommandUtils {

	private Logger logger=org.apache.log4j.Logger.getLogger(WXCommandUtils.class);
	ReceiveXmlEntity xmlEntity;
	
	public WXCommandUtils(ReceiveXmlEntity xmlEntity){
		this.xmlEntity=xmlEntity;
	}
	
	public MsgSendInterface  findWXMsgSend(String content){
		logger.info("====findWXMsgSend:"+content);
		 MsgSendInterface msgSend=null;
		 
		 if(StringUtils.isEmpty(content)){
		 }
		 else if(ArchCache.getInstance().getWxCommandCache().contains(content)){
			 //包含
			 WXCommand wxCmd=ArchCache.getInstance().getWxCommandCache().get(content);
			 
			 logger.info("=====cmd str:"+wxCmd.getCommandStr() + "  "+wxCmd.getMsgType());
			 
			 if("text".equals(wxCmd.getMsgType()))
			 {
				TextMsgSend txtMsg=new TextMsgSend();
	    		txtMsg.setFromUserName(xmlEntity.getToUserName());
	    		txtMsg.setToUserName(xmlEntity.getFromUserName());
	    		txtMsg.setContent(wxCmd.getItemDesc());
	    		msgSend = txtMsg;
	    		
			 }
			 
			 else if("url".equals(wxCmd.getMsgType())){
				TextMsgSend txtMsg=new TextMsgSend();
	    		txtMsg.setFromUserName(xmlEntity.getToUserName());
	    		txtMsg.setToUserName(xmlEntity.getFromUserName());
	    		txtMsg.setContent(wxCmd.getUrl());
	    		msgSend = txtMsg;
				
			 }
			 
			 else if("news".equals(wxCmd.getMsgType())){
				 
				TextImgMsgSend txtImgMsg=new TextImgMsgSend();
         		
         		txtImgMsg.setFromUserName(xmlEntity.getToUserName());
         		txtImgMsg.setToUserName(xmlEntity.getFromUserName());
         		txtImgMsg.setArticleCount(1);
         		
         		
         		Articles item=new Articles();
         		item.setTitle(wxCmd.getName());
         		item.setDescription(wxCmd.getItemDesc());
         		item.setUrl(wxCmd.getUrl());
         		item.setTitle(wxCmd.getName());
             	txtImgMsg.getArticles().add(item);
         		msgSend = txtImgMsg;
			 }
			 
			 else if("qr".equals(wxCmd.getMsgType())){
				ImgMsgSend imgMsg=new ImgMsgSend();
				imgMsg.setFromUserName(xmlEntity.getToUserName());
				imgMsg.setToUserName(xmlEntity.getFromUserName());
	    		//发送二维码给用户
				String codeText= wxCmd.getItemDesc();
				ByteArrayOutputStream outStream=new ByteArrayOutputStream();
				try {
					logger.info("===正生成二维码 ");
					QRCodeUtil.encode(codeText, outStream);
					byte[]  buffer = outStream.toByteArray();
					
					FileItem  file=new FileItem("media", buffer);
					
					
					WXService service=new WXService();
					//
					String accessToken=WXAppInfo.getAppInfoByOpenid(xmlEntity.getToUserName()).getAccessToken();
					
				    MediaAddTempResponse mediaAddTempResponse=service.mediaAdd(accessToken, MediaAddTempRequest.MEDIA_TYPE_IMAGE, file);
				    
					String mediaId=mediaAddTempResponse.getMedia_id();
					
					Image image=new Image();
					image.setMediaId(mediaId);
					imgMsg.setImage(image);
					
					
					
					msgSend = imgMsg;
				}
				catch(Exception e){
					
				}
	    		
				
			 }
			 
		 }
		 
		 return msgSend;
	}
}
