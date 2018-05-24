package cn.finder.wae.common.third;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import cn.finder.pr.FinderHtmlUnit;
import common.Logger;

public class ThirdUtils {

	private static Logger logger=Logger.getLogger(ThirdUtils.class);
	
	/***
	 * 短链接生成
	 * @param long_url
	 * @return
	 * @throws IOException 
	 */
	@Deprecated
	public synchronized static String toShortUrl(String long_url) throws IOException{
		
		FinderHtmlUnit fhu=new FinderHtmlUnit(300);
    	String url="http://www.atool.org/shorturl.php";
    	HtmlPage  page= fhu.collect(url);
    	
    	if(page==null){
    		logger.error(url+" 静态化失败..." );
    	}else{
    		
    		HtmlInput input= (HtmlInput)page.getDocumentElement().getElementsByAttribute("input", "name", "url").get(0);
    		input.type(long_url);
    		
    		ScriptResult sr= page.executeJavaScript("javascript:gen(1)");
    		

        	HtmlPage shortResultPage = (HtmlPage) sr.getNewPage();
        	
        	String shortUrl=shortResultPage.getElementById("short_url_input").getAttribute("value");
        	return shortUrl;
		}
    	return "";
    	
    	
	}
}
