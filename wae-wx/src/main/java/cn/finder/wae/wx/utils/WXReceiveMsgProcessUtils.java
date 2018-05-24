package cn.finder.wae.wx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.finder.wx.domain.ReceiveXmlEntity;


public class WXReceiveMsgProcessUtils {

	private Logger logger=Logger.getLogger(WXReceiveMsgProcessUtils.class);
	public Object getMsgEntity(String strXml,Class c) {
		Object msg = null;
		try {
			if (strXml.length() <= 0 || strXml == null)
				return null;

			// 将字符串转化为XML文档对象
			Document document = DocumentHelper.parseText(strXml);
			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();

			// 利用反射机制，调用set方法
			// 获取该实体的元类型
			msg =  c.newInstance();// 创建这个实体的对象

			while (iter.hasNext()) {
				Element ele = (Element) iter.next();
				// 获取set方法中的参数字段（实体类的属性）
				Field field = c.getDeclaredField(ele.getName());
				// 获取set方法，field.getType())获取它的参数数据类型
				Method method = c.getDeclaredMethod("set" + ele.getName(),
						field.getType());
				logger.info("===element name:"+ele.getName());
				
				Iterator itr= ele.elementIterator();
				
				if(!itr.hasNext()){
					logger.info("===element name:"+ele.elements()==null);
					// 调用set方法
					method.invoke(msg, ele.getText());
				}
				else{
					
					
					Object childObj=field.getType().newInstance();
					method.invoke(msg, childObj);
					
					List childEle=ele.elements();//获取当前元素下的子元素
					
					for(int j=0;j<childEle.size();j++){
						Element currEle=(Element)childEle.get(j);
						
						Field field_c =childObj.getClass().getDeclaredField(currEle.getName());
						
						Iterator currEle_child= currEle.elementIterator();
						
						if(!currEle_child.hasNext()){
							Method method_c = childObj.getClass().getDeclaredMethod("set" + currEle.getName(),
									field_c.getType());
							// 调用set方法
							method_c.invoke(childObj, currEle.getText());
						}
					}
					
				}
				
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("xml 格式异常: " + strXml);
			e.printStackTrace();
		}
		return msg;
	}
	
	public static void main(String[] args) {
		String xml="<xml><ToUserName><![CDATA[wxe27fe0202a9b0ca5]]></ToUserName>"
		+"<FromUserName><![CDATA[config_admin]]></FromUserName>"
		+"<CreateTime>1442304061</CreateTime>"
		+"<MsgType><![CDATA[event]]></MsgType>"
		+"<Event><![CDATA[scancode_waitmsg]]></Event>"
		+"<EventKey><![CDATA[rselfmenu_0_0]]></EventKey>"
		+"<ScanCodeInfo><ScanType><![CDATA[qrcode]]></ScanType>"
		+"<ScanResult><![CDATA[RR,EXO000-code]]></ScanResult>"
		+"</ScanCodeInfo>"
		+"<AgentID>7</AgentID>"
		+"</xml>";
		
		/** 解析xml数据 */  
        ReceiveXmlEntity xmlEntity = (ReceiveXmlEntity)new WXReceiveMsgProcessUtils().getMsgEntity(xml,ReceiveXmlEntity.class);  
         
		System.out.println("====");
	}
}
