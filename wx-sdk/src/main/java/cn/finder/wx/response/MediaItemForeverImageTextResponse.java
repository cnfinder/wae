package cn.finder.wx.response;

import java.util.List;

import cn.finder.httpcommons.ApiObject;
import cn.finder.httpcommons.attri.JsonArrayAttribute;
import cn.finder.httpcommons.attri.JsonArrayItemAttribute;

public class MediaItemForeverImageTextResponse extends WeixinResponse {

	private List<NewsItem> news_item;

	private long create_time;
	private long update_time;
	
	
	
	
	
	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public List<NewsItem> getNews_item() {
		return news_item;
	}

	@JsonArrayAttribute(name = "news_item")
	@JsonArrayItemAttribute(clazzType = NewsItem.class)
	public void setNews_item(List<NewsItem> news_item) {
		this.news_item = news_item;
	}

	public static class NewsItem extends ApiObject {
		private String title;

		private String thumb_media_id;

		private String thumb_url;
		
		private String show_cover_pic;

		private String author;

		private String digest;

		private String content;
		private String url;

		private String content_source_url;

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

	@Override
	public boolean isSuccess() {
		if(errcode==null){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	

}
