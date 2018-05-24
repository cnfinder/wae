package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.GetQrResponse;

/***
 * 获取二维码
 * @author whl
 *
 */
public class GetQrRequest extends JsonStringRequest<GetQrResponse>{

	public static String ACTION_NAME_QR_SCENE="QR_SCENE";
	public static String ACTION_NAME_QR_LIMIT_SCENE="QR_LIMIT_SCENE";
	public static String ACTION_NAME_QR_LIMIT_STR_SCENE="QR_LIMIT_STR_SCENE";
	
	
	private String action_name;
	
	private Map<String,Scene> action_info;
	
	
	
	
	
	public static class Scene extends ApiObject{
		/*
		 * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
		 */
		private String scene_id;
		
		/*
		 * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
		 */
		private String scene_str;

		public String getScene_str() {
			return scene_str;
		}

		public void setScene_str(String scene_str) {
			this.scene_str = scene_str;
		}

		public String getScene_id() {
			return scene_id;
		}

		public void setScene_id(String scene_id) {
			this.scene_id = scene_id;
		}
		
		
		
	}





	public String getAction_name() {
		return action_name;
	}





	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}





	public Map<String, Scene> getAction_info() {
		return action_info;
	}





	public void setAction_info(Map<String, Scene> action_info) {
		this.action_info = action_info;
	}





	@Override
	public String apiName() {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
