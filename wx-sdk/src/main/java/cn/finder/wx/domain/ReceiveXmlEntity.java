package cn.finder.wx.domain;

/***
 * 接收消息 基类 一般包含所有接收消息的属性
 * 
 * @author whl
 * 
 */
public class ReceiveXmlEntity {

	public static String MsgType_text="text";
	public static String MsgType_image="image";
	public static String MsgType_voice="voice";
	public static String MsgType_video="video";
	public static String MsgType_shortvideo="shortvideo";
	public static String MsgType_location="location";
	
	public static String MsgType_event="event";
	
	public static String Event_subscribe="subscribe";
	public static String Event_unsubscribe="unsubscribe";
	public static String Event_LOCATION="LOCATION";
	public static String Event_CLICK="CLICK";
	public static String Event_VIEW="VIEW";
	
	public static String Event_scancode_push="scancode_push";
	public static String Event_scancode_waitmsg="scancode_waitmsg";
	public static String Event_pic_sysphoto="pic_sysphoto";
	public static String Event_pic_photo_or_album="pic_photo_or_album";
	public static String Event_pic_weixin="pic_weixin";
	public static String Event_location_select="location_select";
	public static String Event_enter_agent="enter_agent";
	public static String Event_batch_job_result="batch_job_result";
	
	public static String Event_scan="scan";
	
	
	private String ToUserName = "";
	private String FromUserName = "";
	private String CreateTime = "";
	private String MsgType = "";
	private String MsgId = ""; //企业号使用小写这个
	private String MsgID="";
	private String Event = "";
	private String EventKey = "";
	private String Ticket = "";
	private String Latitude = "";
	private String Longitude = "";
	private String Precision = "";
	private String PicUrl = "";
	private String MediaId = "";
	private String Title = "";
	private String Description = "";
	private String Url = "";
	private String Location_X = "";
	private String Location_Y = "";
	private String Scale = "";
	private String Label = "";
	private String Content = "";
	private String Format = "";
	private String Recognition = "";

	
	private String Status="";
	
	
	private String AgentID="";//消息id，64位整型,企业号
	private String ThumbMediaId="";//视频媒体文件id，可以调用获取媒体文件接口拉取数据
	private ScanCodeInfo ScanCodeInfo=new ScanCodeInfo();
	
	private String MenuId;
	
	
	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String locationX) {
		Location_X = locationX;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String locationY) {
		Location_Y = locationY;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}


	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getMsgID() {
		return MsgID;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	}

	public String getAgentID() {
		return AgentID;
	}

	public void setAgentID(String agentID) {
		AgentID = agentID;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}


	
	
	public ScanCodeInfo getScanCodeInfo() {
		return ScanCodeInfo;
	}

	public void setScanCodeInfo(ScanCodeInfo scanCodeInfo) {
		ScanCodeInfo = scanCodeInfo;
	}




	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}




	public static class ScanCodeInfo{
		private String ScanType="";
		private String ScanResult="";
		public String getScanType() {
			return ScanType;
		}
		public void setScanType(String scanType) {
			ScanType = scanType;
		}
		public String getScanResult() {
			return ScanResult;
		}
		public void setScanResult(String scanResult) {
			ScanResult = scanResult;
		} 
		
		
	}
	

}
