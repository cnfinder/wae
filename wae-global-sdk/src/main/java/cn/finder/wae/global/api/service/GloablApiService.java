package cn.finder.wae.global.api.service;

import cn.finder.httpcommons.IClient;
import cn.finder.httpcommons.response.ApiResponse;
import cn.finder.wae.global.api.domain.Media;
import cn.finder.wae.global.api.request.MediaDeleteRequest;
import cn.finder.wae.global.api.request.MediaDescInfoRequest;
import cn.finder.wae.global.api.request.MediaGetRequest;
import cn.finder.wae.global.api.request.MediaThumbnailGetRequest;
import cn.finder.wae.global.api.request.MediaUploadMultiRequest;
import cn.finder.wae.global.api.request.MediaUploadRequest;
import cn.finder.wae.global.api.response.MediaDescInfoResponse;
import cn.finder.wae.global.api.response.MediaUploadMultiResponse;
import cn.finder.wae.global.api.response.MediaUploadResponse;
import cn.finder.wae.httpcommons.ApiConfig.ServiceInterfaceConfig;
import cn.finder.wae.httpcommons.HttpPostClient;
import cn.finder.wae.httpcommons.HttpPostMultiClient;

public class GloablApiService {

	/***
	 * 上传资源文件
	 * @param req
	 * @return
	 */
	public Media uploadMedia(String url,MediaUploadRequest req){
		
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUploadStreamUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		
		MediaUploadResponse resp= client.execute(req);
		if(resp.isSuccess())
			return resp.getEntity();
		return null;
	}
	
	
	/***
	 * 上传资源文件
	 * @param req
	 * @return
	 */
	public Media uploadMediaMulti(String url,MediaUploadMultiRequest req){
		
		IClient client = new HttpPostMultiClient(ServiceInterfaceConfig.getServiceInterfaceUploadStreamUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		
		MediaUploadMultiResponse resp= client.execute(req);
		if(resp.isSuccess())
			return resp.getEntity();
		return null;
	}

	/***
	 * 获取媒体字段描述
	 * @param req
	 * @return
	 */
	public Media findMediaDescInfo(String url,String media_id){
		
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		MediaDescInfoRequest req=new MediaDescInfoRequest();
		req.setMedia_id(media_id);
		
		MediaDescInfoResponse resp=  client.execute(req);
		if(resp.isSuccess())
			return resp.getEntity();
		return null;
	}
	
	
	/***
	 * 媒体删除
	 * @param req
	 * @return
	 */
	public boolean mediaDelete(String url,String media_id){
		
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceUrlExt(url,"fields"), ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		MediaDeleteRequest req=new MediaDeleteRequest();
		req.setMedia_id(media_id);
		
		ApiResponse resp=  client.execute(req);
		if(resp.isSuccess() && resp.getTag().equals(1))
			return true;
		return false;
	}
	
	
	/***
	 *获取媒体文件
	 * @param req
	 * @return
	 */
	public String mediaGet(String url,String media_id){
		
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceStreamUrlExt(url,"fields"),"json",true, ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		MediaGetRequest req=new MediaGetRequest();
		req.setMedia_id(media_id);
		
		ApiResponse resp=  client.execute(req);
		return resp.getBody();
	}
	
	/***
	 *获取媒体文件
	 * @param req
	 * @return
	 */
	public String mediaThumbnailGet(String url,String media_id){
		
		IClient client = new HttpPostClient(ServiceInterfaceConfig.getServiceInterfaceStreamUrlExt(url,"fields"),"json",true, ServiceInterfaceConfig.AppKey, ServiceInterfaceConfig.AppSecret);
		MediaThumbnailGetRequest req=new MediaThumbnailGetRequest();
		req.setMedia_id(media_id);
		
		ApiResponse resp=  client.execute(req);
		return resp.getBody();
	}

	
	
}
