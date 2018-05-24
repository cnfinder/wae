package cn.finder.wae.common.comm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Gzip {

	/***
	 * 
	 * @param pkgDir  压缩后文件目录
	 * @param pkgFileName  压缩文件名字
	 * @param filePaths  需要压缩的文件路径
	 * @param deleteOnCompressed  是否压缩后删除原始文件
	 * @return
	 */
	public String compress(String pkgDir,String pkgFileName,List<String> filePaths,boolean deleteOnCompressed){
		String zipFile="";
		try {  
		
			
			
			File pkgDirFileInfo = new File(pkgDir);
			if(!pkgDirFileInfo.exists()){
				pkgDirFileInfo.mkdir();
			}
			
			//zipFile=pkgDir+ File.separator+ funcName+".zip";
			zipFile = pkgFileName;
			
			
			
            FileOutputStream f = new FileOutputStream(zipFile);
            
            
            // 输出校验流,采用Adler32更快  
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());  
            //创建压缩输出流  
            ZipOutputStream zos = new ZipOutputStream(csum);  
            
            
            BufferedOutputStream out = new BufferedOutputStream(zos);  
            //设置Zip文件注释  
            zos.setComment(" 接口SDK生成");  
            for (String s : filePaths) {  
                //针对单个文件建立读取流  
               // BufferedReader bin = new BufferedReader(new FileReader(s));
                
                BufferedReader bin =  new  BufferedReader(
                        new  InputStreamReader(
                                new  FileInputStream(s),
                               "utf-8"));
                
                //ZipEntry ZIP 文件条目  
                //putNextEntry 写入新条目，并定位到新条目开始处  
                zos.putNextEntry(new ZipEntry(new File(s).getName()));  
               /* int c;  
                while ((c = bin.read()) != -1) {  
                    out.write(c);  
                }  */
                String line;
                while((line=bin.readLine())!=null)
                {
                	out.write(line.getBytes("UTF-8"));
                	out.write("\r".getBytes("UTF-8"));
                }
                
                bin.close();  
                out.flush();  
            }  
            out.close();  
            
            
           /* FileInputStream fi = new FileInputStream("D://test.zip");  
            CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());  
            ZipInputStream in2 = new ZipInputStream(csumi);  
            BufferedInputStream bis = new BufferedInputStream(in2);  
            ZipEntry ze;  
            while ((ze = in2.getNextEntry()) != null) {  
                System.out.println("Reader File " + ze);  
                int x;  
                while ((x = bis.read()) != -1)  
                    System.out.println(x);  
            }  
            //利用ZipFile解压压缩文件  
            ZipFile zf = new ZipFile("D:\\test.zip");  
            Enumeration e = zf.entries();  
            while(e.hasMoreElements()){  
                ZipEntry ze2 = (ZipEntry) e.nextElement();  
                System.out.println("File name : "+ze2);  
            }  */
            
            if(deleteOnCompressed)
            {
	            for(int i=0;i<filePaths.size();i++){
	            	new File(filePaths.get(i)).delete();
	            }
            }
            
            
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		
		return zipFile;
	}
	
	/***
	 * 压缩文件 返回 文件流字节码
	 * @param inputData  需要压缩的文件数据 或者图片等数据
	 * @param inputEntryName   对应文件数据的文件项名称， 一般为文件名称
	 * @param zipComment  压缩文件描述
	 * @return
	 * @throws IOException
	 */
	public byte[] compress(List<byte[]> inputData,List<String> inputEntryName,String zipComment) throws IOException{
		
		//数据打包
 		/*String zipFile=WebRoot_path+File.separator+"file"+File.separator+rp.getName()+".zip";
 		FileOutputStream f = new FileOutputStream(zipFile);*/
 		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
        
        // 输出校验流,采用Adler32更快  
        CheckedOutputStream csum = new CheckedOutputStream(outStream, new Adler32());  
        //创建压缩输出流  
        ZipOutputStream zos = new ZipOutputStream(csum);  
        
        
        BufferedOutputStream out = new BufferedOutputStream(zos);
        //设置Zip文件注释  
        zos.setComment(zipComment);  
        for (int k=0;k<inputData.size();k++) {  
            //针对单个文件建立读取流  
        	 //ZipEntry ZIP 文件条目   putNextEntry 写入新条目，并定位到新条目开始处
        	zos.putNextEntry(new ZipEntry(inputEntryName.get(k)));  
        	 
            
            
           byte[] item_bytes=inputData.get(k);
          
           ByteArrayInputStream inStream =  new  ByteArrayInputStream(item_bytes);
           
           
           
            int c=-1;  
            while ((c = inStream.read()) != -1) {  
                out.write(c);
            }  
            inStream.close();  
            out.flush();  
        }  
        out.close(); 
        
        return outStream.toByteArray();
        
	}
}
