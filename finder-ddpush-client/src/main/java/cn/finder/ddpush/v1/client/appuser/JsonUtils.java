package cn.finder.ddpush.v1.client.appuser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class JsonUtils {

	
	/***
	 * 把对象转换成 Json 字符串
	 * @param obj 任意可以被序列化的对象
	 * @return
	 */
	public static String objToJsonString(Object obj){
		
		/*JSONObject jObj = JSONObject.fromObject(obj);
		return jObj.toString();*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	
	 public static Object getObject4JsonString(String jsonString,Class pojoCalss){   
	        Object pojo=null;   
	     /*   JSONObject jsonObject = JSONObject.fromObject( jsonString );     
	        pojo = JSONObject.toBean(jsonObject,pojoCalss);   */
	        ObjectMapper mapper = new ObjectMapper();
	        try {
				pojo=mapper.readValue(jsonString,pojoCalss);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return pojo;
	    }   
	       
	       
	       
	      /***
	       * 值有可能是List<Map<String,Object>> 类型
	       * @param jsonString
	       * @return
	       */
	    public static Map<String,Object> getMap4Json(String jsonString){   
	    	  //LinkedHashMap有序
	        LinkedHashMap<String,Object> valueMap = new LinkedHashMap<String, Object>();
	    	
	    	/*JSONObject jsonObject = JSONObject.fromObject( jsonString );     
	        Iterator  keyIter = jsonObject.keys();   
	        String key;   
	        Object value;   
	      
	  
	        while( keyIter.hasNext())   
	        {   
	            key = (String)keyIter.next();   
	            value = jsonObject.get(key);   
	            
	            if(value!=null&&value.toString().length()>1){
		           
	            	if(value.toString().startsWith("{")){
	            		JSONObject jsonObject01 = JSONObject.fromObject(value); 
			            
			            if(jsonObject01!=null){
			            	value = getMap4Json(jsonObject01.toString());
			            }
	            	}
	            	
		            else if(value.toString().startsWith("["))
		            {
		            	
		            	JSONArray jsonArray = JSONArray.fromObject(value); 
		            	if(jsonArray!=null && jsonArray.size()>0){
		            		List<Map<String,Object>> list = new  ArrayList<Map<String,Object>>();
		            		for(int i=0;i<jsonArray.size();i++){
		            			JSONObject jo= (JSONObject)jsonArray.get(i);
		            			list.add(getMap4Json(jo.toString()));
		            			
		            		}
		            		
		            		value = list;
		            	}
			            
		            }
	            }
	            
	            valueMap.put(key, value);   
	        }   */
	        
	       /* ObjectMapper mapper = new ObjectMapper();
	        Map<String,Object> mapData=null;
	        try {
	        	mapData=mapper.readValue(jsonString,Map.class);
	        	
	        	
	        	
	            Set<Entry<String,Object>>  keyIter = mapData.entrySet();  
	            
	            Iterator<Entry<String, Object>>  itr= keyIter.iterator();
	            
	 	       
	 	  
	 	        while(itr.hasNext())   
	 	        {   
	 	        	 String key;   
	 	 	        Object value;   
	 	 	      
	 	 	        Entry<String, Object> entry= itr.next();
	 	 	        
	 	            key = entry.getKey();
	 	            value = entry.getValue(); 
	 	            
	 	            if(value instanceof Map){
	 		           
	 	            	if(value.toString().startsWith("{")){
	 	            		Map<String,Object> jsonObject01 = new ObjectMapper().readValue(value.toString(),Map.class); 
	 			            
	 			            if(jsonObject01!=null){
	 			            	value = getMap4Json(jsonObject01.toString());
	 			            }
	 	            	}
	 	            	
	 		            else if(value.toString().startsWith("["))
	 		            {
	 		            	
	 		            	List<Map<String,Object>> jsonArray = mapper.readValue(value.toString(),List.class); 
	 		            	if(jsonArray!=null && jsonArray.size()>0){
	 		            		List<Map<String,Object>> list = new  ArrayList<Map<String,Object>>();
	 		            		for(int i=0;i<jsonArray.size();i++){
	 		            			Map<String,Object> jo= (Map<String,Object>)jsonArray.get(i);
	 		            			
	 		            			list.add(getMap4Json(jo.toString()));
	 		            			
	 		            		}
	 		            		
	 		            		value = list;
	 		            	}
	 			            
	 		            }
	 	            }
	 	            
	 	            valueMap.put(key, value);   
	 	        }   
	        	
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	       
	           */
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String,Object> mapData=null;
	        try {
	        	mapData=mapper.readValue(jsonString,Map.class);
	        }
	        catch(Exception e){
	        	
	        }
	        return mapData;   
	    }   
	       
	       
	     
	    @Deprecated
	    public static Object[] getObjectArray4Json(String jsonString){   
	      /*  JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        return jsonArray.toArray();   */
	    	
	    	
	    	
	    	return null;
	           
	    }   
	       
	       
	      
	    public static List getList4Json(String jsonString, Class pojoClass){   
	           
	       /* JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        JSONObject jsonObject;   
	        Object pojoValue;   
	           
	        List list = new ArrayList();   
	        for ( int i = 0 ; i<jsonArray.size(); i++){   
	               
	            jsonObject = jsonArray.getJSONObject(i);   
	            pojoValue = JSONObject.toBean(jsonObject,pojoClass);   
	            list.add(pojoValue);   
	               
	        }   
	        return list;   */
	    	
	    	 ObjectMapper mapper = new ObjectMapper();
	    	 
	    	 try {
				List<Map<String,Object>> jsonArray = mapper.readValue(jsonString,List.class);
				
				   List list = new ArrayList();   
			        for (int i = 0 ; i<jsonArray.size(); i++){   
			            
			        	
			        	Object pojoValue =mapper.readValue(objToJsonString(jsonArray.get(i)),pojoClass);
			        	
			            list.add(pojoValue);   
			               
			        }   
			        return list; 
				
			} catch (Exception e) {
			
			} 
	    	
	    	 
	    	 
	    	
	    	return null;
	  
	    }   
	       
	      @Deprecated
	    public static String[] getStringArray4Json(String jsonString){   
	           
	      /*  JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        String[] stringArray = new String[jsonArray.size()];   
	        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
	            stringArray[i] = jsonArray.getString(i);   
	               
	        }   
	           
	        return stringArray; */  
	    	
	    	
	    	
	    	
	    	return null;
	    }   
	       
	      @Deprecated
	    public static Long[] getLongArray4Json(String jsonString){   
	           
	      /*  JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        Long[] longArray = new Long[jsonArray.size()];   
	        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
	            longArray[i] = jsonArray.getLong(i);   
	               
	        }   
	        return longArray;   */
	    	
	    	return null;
	    }   
	       
	      @Deprecated
	    public static Integer[] getIntegerArray4Json(String jsonString){   
	           
	      /*  JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        Integer[] integerArray = new Integer[jsonArray.size()];   
	        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
	            integerArray[i] = jsonArray.getInt(i);   
	               
	        }   
	        return integerArray;  */ 
	    	return null;
	    }   
	      
	      @Deprecated
	    public static Double[] getDoubleArray4Json(String jsonString){   
	           
	       /* JSONArray jsonArray = JSONArray.fromObject(jsonString);   
	        Double[] doubleArray = new Double[jsonArray.size()];   
	        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
	            doubleArray[i] = jsonArray.getDouble(i);   
	               
	        }   
	        return doubleArray;   */
	    	return null;
	    }   
	       
	       
	      
	    public static String getJsonString4JavaPOJO(Object javaObj){   
	           
	    	
	    	
	    	return objToJsonString(javaObj);
	           
	    }   
	       
	       
	       
	       
	       
	     
	  
}
