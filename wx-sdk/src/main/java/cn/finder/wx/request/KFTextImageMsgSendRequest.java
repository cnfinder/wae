package cn.finder.wx.request;

import java.util.Map;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.request.JsonStringRequest;
import cn.finder.wx.response.WeixinResponse;

/***
 * 客服接口 - 发送图文语息
 * @author Administrator
 *
 */
public class KFTextImageMsgSendRequest extends JsonStringRequest<WeixinResponse> {

	
	private String touser;
	
	private String msgtype="voice";
	
	private NewsData news;
	
	
	
	public static class NewsData extends ApiObject{
		
		private String title;


		private String description;
		
		private String url;
		
		private String picurl;
		
		
		
		private ArticlesData articles;
		
		
		
		
		
		
		
		public String getTitle() {
			return title;
		}







		public void setTitle(String title) {
			this.title = title;
		}







		public String getDescription() {
			return description;
		}







		public void setDescription(String description) {
			this.description = description;
		}







		public String getUrl() {
			return url;
		}







		public void setUrl(String url) {
			this.url = url;
		}







		public String getPicurl() {
			return picurl;
		}







		public void setPicurl(String picurl) {
			this.picurl = picurl;
		}












		public ArticlesData getArticles() {
			return articles;
		}







		public void setArticles(ArticlesData articles) {
			this.articles = articles;
		}












		public static class ArticlesData extends ApiObject{
			
			
			private String title;


			private String description;
			
			private String url;
			
			private String picurl;

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getPicurl() {
				return picurl;
			}

			public void setPicurl(String picurl) {
				this.picurl = picurl;
			}
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public NewsData getNews() {
		return news;
	}

	public void setNews(NewsData news) {
		this.news = news;
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
