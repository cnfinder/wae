package cn.finder.wx.response;

import java.util.List;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;


public class MediaListResponse extends WeixinResponse{

	
	private int total_count;
	
	private int item_count;
	
	
	private List<MediaItem> item;
	
	
	public int getTotal_count() {
		return total_count;
	}


	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}


	public int getItem_count() {
		return item_count;
	}


	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}


	public List<MediaItem> getItem() {
		return item;
	}




	@JsonArrayAttribute(name="item")
	@JsonArrayItemAttribute(clazzType=MediaItem.class)
	public void setItem(List<MediaItem> item) {
		this.item = item;
	}





	public static class MediaItem extends ApiObject{
		
		private String media_id;  //几种素材共有
		
		
		private Content content; //图文独有
		private String update_time;//图文独有
		
		
		
		//===========其他类型（图片、语音、视频）的返回如下：
		
		private String name; 
		
		
		
		private String url;
		
		//==============
		
		
		public String getMedia_id() {
			return media_id;
		}




		public void setMedia_id(String media_id) {
			this.media_id = media_id;
		}




		public String getName() {
			return name;
		}




		public void setName(String name) {
			this.name = name;
		}




		public String getUpdate_time() {
			return update_time;
		}




		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}




		public String getUrl() {
			return url;
		}




		public void setUrl(String url) {
			this.url = url;
		}




		public Content getContent() {
			return content;
		}




		public void setContent(Content content) {
			this.content = content;
		}




		public static class Content extends ApiObject{
			
			
			private List<NewsItem> news_item;
			
			
			
			
			public List<NewsItem> getNews_item() {
				return news_item;
			}

			@JsonArrayAttribute(name="news_item")
			@JsonArrayItemAttribute(clazzType=NewsItem.class)
			public void setNews_item(List<NewsItem> news_item) {
				this.news_item = news_item;
			}


			


			public static class NewsItem extends ApiObject{
				private String title;
				
				private String thumb_media_id;
				
				private String show_cover_pic;
				
				private String author;
				
				private String digest;
				
				private String content;
				private String url;
				
				private String content_source_url;
				
				private String thumb_url;
				
				

				public String getThumb_url() {
					return thumb_url;
				}

				public void setThumb_url(String thumb_url) {
					this.thumb_url = thumb_url;
				}

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getThumb_media_id() {
					return thumb_media_id;
				}

				public void setThumb_media_id(String thumb_media_id) {
					this.thumb_media_id = thumb_media_id;
				}

				public String getShow_cover_pic() {
					return show_cover_pic;
				}

				public void setShow_cover_pic(String show_cover_pic) {
					this.show_cover_pic = show_cover_pic;
				}

				public String getAuthor() {
					return author;
				}

				public void setAuthor(String author) {
					this.author = author;
				}

				public String getDigest() {
					return digest;
				}

				public void setDigest(String digest) {
					this.digest = digest;
				}

				public String getContent() {
					return content;
				}

				public void setContent(String content) {
					this.content = content;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}

				public String getContent_source_url() {
					return content_source_url;
				}

				public void setContent_source_url(String content_source_url) {
					this.content_source_url = content_source_url;
				}
				
				
				
			}
		}
		
		
	}
	
	
}
