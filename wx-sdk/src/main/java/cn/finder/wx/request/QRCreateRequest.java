package cn.finder.wx.request;

import cn.finder.wx.response.QRCreateResponse;

/***
 * 创建二维码   --永久
 * @author whl
 *
 */

@Deprecated
public class QRCreateRequest extends WeixinRequest<QRCreateResponse>{

	
	private String action_name="QR_LIMIT_SCENE";
	private ActionInfo action_info;


	public String getAction_name() {
		return action_name;
	}


	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}


	public ActionInfo getAction_info() {
		return action_info;
	}


	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}


	public static class ActionInfo
	{
		
		private Scene scene;
		
		
		
		public Scene getScene() {
			return scene;
		}



		public void setScene(Scene scene) {
			this.scene = scene;
		}



		public static class Scene{
			
			private String scene_id;

			public String getScene_id() {
				return scene_id;
			}

			public void setScene_id(String scene_id) {
				this.scene_id = scene_id;
			}
			
			
		}
	}
	
	
}
