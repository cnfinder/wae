package cn.finder.wae.common.comm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finder.wae.common.type.FinderLinkedMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
  
  
  
public class JsonUtil{   
  
      
    public static Object getObject4JsonString(String jsonString,Class pojoCalss){   
        Object pojo;   
        JSONObject jsonObject = JSONObject.fromObject( jsonString );     
        pojo = JSONObject.toBean(jsonObject,pojoCalss);   
        return pojo;
    }   
       
       
       
      
    public static Map<String,Object> getMap4Json(String jsonString){   
        JSONObject jsonObject = JSONObject.fromObject( jsonString );     
        Iterator  keyIter = jsonObject.keys();   
        String key;   
        Object value;   
        //LinkedHashMap有序
        LinkedHashMap<String,Object> valueMap = new FinderLinkedMap<String, Object>();
  
        while( keyIter.hasNext())   
        {   
            key = (String)keyIter.next();   
            value = jsonObject.get(key);   
            valueMap.put(key, value);   
        }   
           
        return valueMap;   
    }   
       
       
      
    public static Object[] getObjectArray4Json(String jsonString){   
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        return jsonArray.toArray();   
           
    }   
       
       
      
    public static List getList4Json(String jsonString, Class pojoClass){   
           
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        JSONObject jsonObject;   
        Object pojoValue;   
           
        List list = new ArrayList();   
        for ( int i = 0 ; i<jsonArray.size(); i++){   
               
            jsonObject = jsonArray.getJSONObject(i);   
            pojoValue = JSONObject.toBean(jsonObject,pojoClass);   
            list.add(pojoValue);   
               
        }   
        return list;   
  
    }   
       
      
    public static String[] getStringArray4Json(String jsonString){   
           
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        String[] stringArray = new String[jsonArray.size()];   
        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
            stringArray[i] = jsonArray.getString(i);   
               
        }   
           
        return stringArray;   
    }   
       
      
    public static Long[] getLongArray4Json(String jsonString){   
           
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        Long[] longArray = new Long[jsonArray.size()];   
        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
            longArray[i] = jsonArray.getLong(i);   
               
        }   
        return longArray;   
    }   
       
      
    public static Integer[] getIntegerArray4Json(String jsonString){   
           
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        Integer[] integerArray = new Integer[jsonArray.size()];   
        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
            integerArray[i] = jsonArray.getInt(i);   
               
        }   
        return integerArray;   
    }   
       
      
//    public static Date[] getDateArray4Json(String jsonString,String DataFormat){   
//           
//        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
//        Date[] dateArray = new Date[jsonArray.size()];   
//        String dateString;   
//        Date date;   
//           
//        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
//            dateString = jsonArray.getString(i);   
//            date = DateUtil.stringToDate(dateString, DataFormat);   
//            dateArray[i] = date;   
//               
//        }   
//        return dateArray;   
//    }   
//       
      
    public static Double[] getDoubleArray4Json(String jsonString){   
           
        JSONArray jsonArray = JSONArray.fromObject(jsonString);   
        Double[] doubleArray = new Double[jsonArray.size()];   
        for( int i = 0 ; i<jsonArray.size() ; i++ ){   
            doubleArray[i] = jsonArray.getDouble(i);   
               
        }   
        return doubleArray;   
    }   
       
       
      
    public static String getJsonString4JavaPOJO(Object javaObj){   
           
        JSONObject json;   
        json = JSONObject.fromObject(javaObj);   
        return json.toString();   
           
    }   
       
       
       
       
      
//    public static String getJsonString4JavaPOJO(Object javaObj , String dataFormat){   
//           
//        JSONObject json;   
//        JsonConfig jsonConfig = configJson(dataFormat);   
//        json = JSONObject.fromObject(javaObj,jsonConfig);   
//        return json.toString();   
//           
//           
//    }   
       
       
       
      
    public static void main(String[] args) {   
        // TODO 自动生成方法存根   
  
    }   
       
      
//    public static JsonConfig configJson(String datePattern) {      
//            JsonConfig jsonConfig = new JsonConfig();      
//            jsonConfig.setExcludes(new String[]{""});      
//            jsonConfig.setIgnoreDefaultExcludes(false);      
//            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);      
//            jsonConfig.registerJsonValueProcessor(Date.class,      
//                new DateJsonValueProcessor(datePattern));      
//             
//            return jsonConfig;      
//        }     
//       
//      
//    public static JsonConfig configJson(String[] excludes,      
//            String datePattern) {      
//            JsonConfig jsonConfig = new JsonConfig();      
//            jsonConfig.setExcludes(excludes);      
//            jsonConfig.setIgnoreDefaultExcludes(false);      
//            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);      
//            jsonConfig.registerJsonValueProcessor(Date.class,      
//                new DateJsonValueProcessor(datePattern));      
//             
//            return jsonConfig;      
//        }     
  
}  