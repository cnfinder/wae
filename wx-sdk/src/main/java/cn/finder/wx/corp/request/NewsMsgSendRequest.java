package cn.finder.wx.corp.request;

import java.util.List;

/***
 * 发送图文消息
 * @author whl
 *
 */
public class NewsMsgSendRequest extends MsgSendBaseRequest {

	
	private String msgtype="news";
	
	private News news;

	
	public String getMsgtype() {
		return msgtype;
	}





	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}


	public News getNews() {
		return news;
	}


	public void setNews(News news) {
		this.news = news;
	}





	public static class News{

		private List<Article> articles;
		
		public List<Article> getArticles() {
			return articles;
		}


		public void setArticles(List<Article> articles) {
			this.articles = articles;
		}





		public static class Article{
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
}
