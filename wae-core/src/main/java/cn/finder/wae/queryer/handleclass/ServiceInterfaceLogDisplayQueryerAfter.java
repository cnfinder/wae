package cn.finder.wae.queryer.handleclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;

import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/**
 * @author: wuhualong
 * @data:2014-4-23下午2:15:59
 * @function:生成设备 槽位  参数验证
 */
public class ServiceInterfaceLogDisplayQueryerAfter  extends QueryerDBAfterClass {

	/* (non-Javadoc)
	 * @see cn.finder.wae.queryer.handleclass.QueryerAfterClass#handle(cn.finder.wae.business.domain.TableQueryResult, long, cn.finder.ui.webtool.QueryCondition)
	 */
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,long showTableConfigId,QueryCondition<Object[]> condition){
		super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		
		if(tableQueryResult==null || tableQueryResult.getResultList()==null || tableQueryResult.getResultList().size()==0){
			tableQueryResult.setFields(null);
			return tableQueryResult;
		}
		
		List<Map<String, Object>> resultList= tableQueryResult.getResultList();
		
		String data =(String) (resultList.get(0).get("input_content"));
		
		
		
		//List<Map<String,Object>> tmp = (List)JSONArray.toList(JSONArray.fromObject(data)); //将json转回map
		List<Map<String,Object>> tmp = (List<Map<String,Object>>)JSONArray.toCollection(JSONArray.fromObject(data),Map.class);
		Object object = tmp.get(0).get("data");
		Map<String,Object> map = new HashMap<String,Object>(); 
		map.put("jsonData", object);
		resultList.add(0, map);
		tableQueryResult.setResultList(resultList);
		
		return tableQueryResult;
	}
	
	
	
	 /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL)
    {
    Map<String, String> mapRequest = new HashMap<String, String>();
   
      String[] arrSplit=null;
     
    String strUrlParam=TruncateUrlPage(URL);
    if(strUrlParam==null)
    {
        return mapRequest;
    }
      //每个键值为一组
    arrSplit=strUrlParam.split("[&]");
    for(String strSplit:arrSplit)
    {
          String[] arrSplitEqual=null;         
          arrSplitEqual= strSplit.split("[=]");
         
          //解析出键值
          if(arrSplitEqual.length>1)
          {
              //正确解析
              mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
             
          }
          else
          {
              if(arrSplitEqual[0]!="")
              {
              //只有参数没有值，不加入
              mapRequest.put(arrSplitEqual[0], "");       
              }
          }
    }   
    return mapRequest;   
    }
   
    
    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL)
    {
    String strAllParam=null;
      String[] arrSplit=null;
     
      //strURL=strURL.trim().toLowerCase();
     
      arrSplit=strURL.split("[?]");
      if(strURL.length()>1)
      {
          if(arrSplit.length>1)
          {
                  if(arrSplit[1]!=null)
                  {
                  strAllParam=arrSplit[arrSplit.length-1];
                  }
          }
      }
     
    return strAllParam;   
    }
	
	
	
}
