package cn.finder.wx.domain.resp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/***
 * 回复图文消息
 * @author Administrator
 *
 */
public class TextImgMsgSend implements MsgSendInterface{

	private String toUserName;
	
	private String fromUserName;
	
	
	private int articleCount;
	
	private String msgType="news";
	
	private List<Articles> articles=new ArrayList<Articles>();
	
	public static class Articles {
		private String title;
		
		private String description;
		private String picUrl;
		
		private String url;

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

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
		
		
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}


	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public List<Articles> getArticles() {
		return articles;
	}

	public void setArticles(List<Articles> articles) {
		this.articles = articles;
	}


	public String toXml() {
		StringBuffer sb = new StringBuffer();  
        Date date = new Date(); 
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(toUserName).append("]]></ToUserName>");  
        sb.append("<FromUserName><![CDATA[").append(fromUserName).append("]]></FromUserName>");  
        sb.append("<CreateTime>").append(date.getTime()).append("</CreateTime>");  
        sb.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>"); 
        
        sb.append("<ArticleCount>").append(articles.size()).append("</ArticleCount>");
        sb.append("<Articles>");
        for(Articles article:articles){
        	sb.append("<item>");
        	sb.append("<Title><![CDATA[").append(article.getTitle()).append("]]></Title>");
        	sb.append("<Description><![CDATA[").append(article.getDescription()).append("]]></Description>");
        	sb.append("<PicUrl><![CDATA[").append(article.getPicUrl()).append("]]></PicUrl>");
        	sb.append("<Url><![CDATA[").append(article.getUrl()).append("]]></Url>");
        	sb.append("</item>");
        }
        sb.append("</Articles>");
        
       
        sb.append("</xml>");
        return sb.toString();  
	}
	
	
	
}
