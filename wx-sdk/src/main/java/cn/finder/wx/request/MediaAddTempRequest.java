package cn.finder.wx.request;

import java.util.HashMap;
import java.util.Map;

import cn.finder.httpcommons.utils.FileItem;
import cn.finder.wx.response.MediaAddTempResponse;



/***
 * 素材管理-新增临时素材
 * @author whl
 *
 */
public class MediaAddTempRequest extends  WeixinUploadRequest<MediaAddTempResponse>{

	
	public static String MEDIA_TYPE_IMAGE="image";
	public static String MEDIA_TYPE_VOICE="voice";
	public static String MEDIA_TYPE_VIDEO="video";
	public static String MEDIA_TYPE_THUMB="thumb";
	
	//private String type;
	
	private FileItem media;

	/*public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}*/

	public FileItem getMedia() {
		return media;
	}

	public void setMedia(FileItem media) {
		this.media = media;
	}

	@Override
	public Map<String, FileItem> fileParameters() {
		Map<String,FileItem> files=new HashMap<String, FileItem>();
		files.put(media.getFileName(), media);
		return files;
	}
	

	
	
	
	
}
