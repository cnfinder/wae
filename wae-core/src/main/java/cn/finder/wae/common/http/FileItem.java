package cn.finder.wae.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileItem {

	   private byte[] content;
       private File fileInfo;
       private String fileName;
       private String mimeType;

       public FileItem(File fileInfo)
       {
           if ((fileInfo == null) || !fileInfo.exists())
           {
               throw new RuntimeException("fileInfo is null or not exists!");
           }
           this.fileInfo = fileInfo;
       }

       public FileItem(String filePath)
       {
    	   this(new File(filePath));
       }

       public FileItem(String fileName, byte[] content)
       {
           if (CommonUtils.isEmpty(fileName))
           {
               throw new RuntimeException("fileName");
           }
           if ((content == null) || (content.length == 0))
           {
               throw new RuntimeException("content");
           }
           this.fileName = fileName;
           this.content = content;
       }

       public FileItem(String fileName, byte[] content, String mimeType)
       {
    	   this(fileName, content);
           if (CommonUtils.isEmpty(mimeType))
           {
               throw new RuntimeException("mimeType");
           }
           this.mimeType = mimeType;
       }

       public byte[] getContent()
       {
           if (((this.content == null) && (this.fileInfo != null)) && this.fileInfo.exists())
           {
               /*using (Stream stream = this.fileInfo.OpenRead())
               {
                   this.content = new byte[stream.Length];
                   stream.Read(this.content, 0, this.content.Length);
               }*/
        	   
        	   FileInputStream fis;
			try {
				fis = new FileInputStream(fileInfo);
				
				 int len = fis.available();
	        	   byte[] buffer = new byte[len];
	        	   fis.read(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	   
        	  
        	   
           }
           return this.content;
       }

       public String getFileName()
       {
           if (((this.fileName == null) && (this.fileInfo != null)) && this.fileInfo.exists())
           {
               this.fileName = this.fileInfo.getPath();
           }
           return this.fileName;
       }

       public String getMimeType()
       {
           if (this.mimeType == null)
           {
               this.mimeType = ApiUtils.GetMimeType(this.getContent());
           }
           return this.mimeType;
       }
}
