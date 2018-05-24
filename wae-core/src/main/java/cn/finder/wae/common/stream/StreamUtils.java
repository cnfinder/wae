package cn.finder.wae.common.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class StreamUtils {

	private static Logger logger=Logger.getLogger(StreamUtils.class);
	
	/***
	 * 获取文件MimeType
	 * @param fileData
	 * @return
	 */
	 public static String getMimeType(byte[] fileData)
     {
     	 String fileSuffix = getFileSuffix(fileData);
         if (fileSuffix != null)
         {
             int num;
             Map<String, Integer> dictionary = new HashMap<String, Integer>(4);
             dictionary.put("JPG", 0);
             dictionary.put("GIF", 1);
             dictionary.put("PNG", 2);
             dictionary.put("BMP", 3);
             
             Object numObj = dictionary.get(fileSuffix);
             
             if(numObj!=null)
             {
             	num = ((Integer)numObj).intValue();
             
             
                 switch (num)
                 {
                     case 0:
                         return "image/jpeg";

                     case 1:
                         return "image/gif";

                     case 2:
                         return "image/png";

                     case 3:
                         return "image/bmp";
                 }
             }
         }
         return "application/octet-stream";
     }
	 
	 
	 /***
	  * 获取文件扩展名
	  * @param fileData
	  * @return
	  */
	 public static String getFileSuffix(byte[] fileData)
     {
         if ((fileData != null) && (fileData.length >= 10))
         {
        	 logger.info("=========="+Integer.toHexString(fileData[0])+" "+Integer.toHexString(fileData[1])+" "+Integer.toHexString(fileData[2])+" "+Integer.toHexString(fileData[3])+" "+Integer.toHexString(fileData[4])+" "+Integer.toHexString(fileData[5])+" "+Integer.toHexString(fileData[6])+" "+Integer.toHexString(fileData[7])+" "+Integer.toHexString(fileData[8])+" "+Integer.toHexString(fileData[9]));
             if (((fileData[0] == 0x47) && (fileData[1] == 0x49)) && (fileData[2] == 70))
             {
                 return "GIF";
             }
             if (((fileData[1] == 80) && (fileData[2] == 0x4e)) && (fileData[3] == 0x47))
             {
                 return "PNG";
             }
            
             if ((fileData[0] == 0x42) && (fileData[1] == 0x4d))
             {
                 return "BMP";
             }
             if ((fileData[6] == 0x4a && fileData[7] == 70 && fileData[8] == 0x49 && fileData[9] == 70)||(fileData[6] == 69 && fileData[7] == 120 && fileData[8] == 105 && fileData[9] == 102)||
            		 (fileData[6] == 0  && fileData[8] == 9 && fileData[9] == 10) || (fileData[0]==(byte)0xFF && fileData[1]==(byte)0xD8))
             {
                 return "JPG";
             }
         }
         return null;
     }
	 
	 
	 public static void main(String[] args) throws IOException{
		 
		 String file="F:/study/装修/信息/789426289479967560.jpg";
		 FileInputStream is=new FileInputStream(new File(file));
		 byte[] data=new byte[is.available()];
		 is.read(data);
		 is.close();
		 
		 String suffix=getFileSuffix(data);
		 System.out.println("====:"+suffix);
		 
		 
		 
	 }


}
