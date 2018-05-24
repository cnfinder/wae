package cn.finder.wae.common.thumbnail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.*;


/***
 * 效果比较差  比不上AfflineTransImage
 * @author finder
 *
 */
@Deprecated
public class ImageSizer {
	  /** 
     * 压缩图片方法 
     *  
     * @param oldFile  将要压缩的图片 
     * @param width  不能超过的最大压缩宽 
     * @param height  不能超过的最大压缩长 
     * @param quality  压缩清晰度 <b>建议为1.0</b> 
     * @param smallIcon   压缩图片后,添加的扩展名 
     * @return 
      * @throws IOException  
     */  
 public static void imageZip(File oldFile, File destFile, String format, int maxWidth, int maxHeight, float quality) throws IOException {  
     FileOutputStream out = null;  
     try {  
         // 文件不存在时  
         if (!oldFile.exists())  
             return;  
         /** 对服务器上的临时文件进行处理 */  
         Image srcFile = ImageIO.read(oldFile);  
         int new_w = 0, new_h = 0;  
         // 获取图片的实际大小 高度  
         int h = (int) srcFile.getHeight(null);  
         // 获取图片的实际大小 宽度  
         int w = (int) srcFile.getWidth(null);  
         // 为等比缩放计算输出的图片宽度及高度  
         if ((((double) w) > (double) maxWidth) || (((double) h) > (double) maxHeight)) {  
             // 为等比缩放计算输出的图片宽度及高度  
             double rateW = ((double) srcFile.getWidth(null)) / (double) maxWidth * 1.0;  
             double rateH = ((double) srcFile.getHeight(null)) / (double) maxHeight * 1.0;  
             // 根据缩放比率大的进行缩放控制  
             //double rate = rateW > rateH ? rateW : rateH;  
             double rate;  
             char zipType;  
             if(rateW > rateH){  
                 rate = rateW;  
                 zipType = 'W';  
             } else {  
                 rate = rateH;  
                 zipType = 'H';  
             }  
             new_w = (int) (((double) srcFile.getWidth(null)) / rate);  
             new_h = (int) (((double) srcFile.getHeight(null)) / rate);  
               
             double rate2 = 0;  
             if(zipType == 'W' && new_h > maxHeight){  
                 rate = (double) new_h / (double) maxHeight * 1.0;  
             } else if(zipType == 'H' && new_w > maxWidth){  
                 rate = (double) new_w / (double) maxWidth * 1.0;  
             }  
             if(rate2 != 0){  
                 new_w = (int) (((double) new_w) / rate);  
                 new_h = (int) (((double) new_h) / rate);  
                 System.out.println("2次修改宽高。");  
             }  
         } else {  
             new_w = w;  
             new_h = h;  
         }  
           
           if ( new_w < 1 )   
               throw new IllegalArgumentException( "image width " + new_w + " is out of range" );  
            if ( new_h < 1 )   
               throw new IllegalArgumentException( "image height " + new_h + " is out of range" );  

         /** 宽,高设定 */  
         BufferedImage tag = new BufferedImage(new_w, new_h,  
                 BufferedImage.TYPE_INT_RGB);  
         tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);  
           
         out = new FileOutputStream(destFile);  
         JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
         JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);  
         /** 压缩质量 */  
         jep.setQuality(quality, true);  
         encoder.encode(tag, jep);  
         out.close();  
         srcFile.flush();  
     } finally{  
         if(out != null)out.close();  
     }  
 }  

   
   public static final MediaTracker tracker = new MediaTracker(new Component() {  
         private static final long serialVersionUID = 1234162663955668507L;}   
     );  
     
       
     /**方法二  
[java] view plaincopy
      * @param originalFile 原图像  
      * @param resizedFile 压缩后的图像  
      * @param width 图像宽  
      * @param format 图片格式 jpg, png, gif(非动画)  
      * @throws IOException  
      */  
     public static void resize(File originalFile, File resizedFile, int width, String format) throws IOException {  
         FileInputStream fis = null;  
         ByteArrayOutputStream byteStream = null;  
         try{  
             if(format!=null && "gif".equals(format.toLowerCase())){  
                 resize(originalFile, resizedFile, width, 1);  
                 return;  
             }  
             fis = new FileInputStream(originalFile);  
             byteStream = new ByteArrayOutputStream();  
             int readLength = -1;  
             int bufferSize = 1024;  
             byte bytes[] = new byte[bufferSize];  
             while ((readLength = fis.read(bytes, 0, bufferSize)) != -1) {  
                 byteStream.write(bytes, 0, readLength);  
             }  
             byte[] in = byteStream.toByteArray();  
             fis.close();  
             byteStream.close();  
           
             Image inputImage = Toolkit.getDefaultToolkit().createImage( in );  
             waitForImage( inputImage );  
             int imageWidth = inputImage.getWidth( null );  
             if ( imageWidth < 1 )   
                throw new IllegalArgumentException( "image width " + imageWidth + " is out of range" );  
             int imageHeight = inputImage.getHeight( null );  
             if ( imageHeight < 1 )   
                throw new IllegalArgumentException( "image height " + imageHeight + " is out of range" );  
               
             // Create output image.  
             int height = -1;  
             double scaleW = (double) imageWidth / (double) width;  
             double scaleY = (double) imageHeight / (double) height;  
             if (scaleW >= 0 && scaleY >=0) {  
                 if (scaleW > scaleY) {  
                     height = -1;  
                 } else {  
                     width = -1;  
                 }  
             }  
             Image outputImage = inputImage.getScaledInstance( width, height, java.awt.Image.SCALE_DEFAULT);  
             checkImage( outputImage );          
             encode(new FileOutputStream(resizedFile), outputImage, format);      
         }finally{  
             try {  
                 if(byteStream != null) {  
                     byteStream.close();  
                 }  
                 if(fis != null) {  
                     fis.close();  
                 }  
             } catch (IOException e) {  
                 e.printStackTrace();  
             }  
         }  
     }      

     /** Checks the given image for valid width and height. */  
     private static void checkImage( Image image ) {  
        waitForImage( image );  
        int imageWidth = image.getWidth( null );  
        if ( imageWidth < 1 )   
           throw new IllegalArgumentException( "image width " + imageWidth + " is out of range" );  
        int imageHeight = image.getHeight( null );  
        if ( imageHeight < 1 )   
           throw new IllegalArgumentException( "image height " + imageHeight + " is out of range" );  
     }  

     /** Waits for given image to load. Use before querying image height/width/colors. */  
     private static void waitForImage( Image image ) {  
        try {  
           tracker.addImage( image, 0 );  
           tracker.waitForID( 0 );  
           tracker.removeImage(image, 0);  
        } catch( InterruptedException e ) { e.printStackTrace(); }  
     }   

     /** Encodes the given image at the given quality to the output stream. */  
     private static void encode( OutputStream outputStream, Image outputImage, String format )   
        throws java.io.IOException {  
         try {  
            int outputWidth  = outputImage.getWidth( null );  
            if ( outputWidth < 1 )   
               throw new IllegalArgumentException( "output image width " + outputWidth + " is out of range" );  
            int outputHeight = outputImage.getHeight( null );  
            if ( outputHeight < 1 )   
               throw new IllegalArgumentException( "output image height " + outputHeight + " is out of range" );  
   
            // Get a buffered image from the image.  
            BufferedImage bi = new BufferedImage( outputWidth, outputHeight,  
               BufferedImage.TYPE_INT_RGB );                                                     
            Graphics2D biContext = bi.createGraphics();  
            biContext.drawImage( outputImage, 0, 0, null );  
            ImageIO.write(bi, format, outputStream);  
            outputStream.flush();      
         }finally{  
             if(outputStream != null) {  
                 outputStream.close();  
             }  
         }  
     }   
       
     /** 
      * 缩放gif图片 
      * @param originalFile 原图片 
      * @param resizedFile 缩放后的图片 
      * @param newWidth 宽度 
      * @param quality 缩放比例 (等比例) 
      * @throws IOException 
      */  
     private static void resize(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {  
         if (quality < 0 || quality > 1) {  
             throw new IllegalArgumentException("Quality has to be between 0 and 1");  
         }   
         ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());  
         Image i = ii.getImage();  
         Image resizedImage = null;   
         int iWidth = i.getWidth(null);  
         int iHeight = i.getHeight(null);   
         if (iWidth > iHeight) {  
             resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);  
         } else {  
             resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);  
         }   
         // This code ensures that all the pixels in the image are loaded.  
         Image temp = new ImageIcon(resizedImage).getImage();   
         // Create the buffered image.  
         BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),  
                                                         BufferedImage.TYPE_INT_RGB);   
         // Copy image to buffered image.  
         Graphics g = bufferedImage.createGraphics();   
         // Clear background and paint the image.  
         g.setColor(Color.white);  
         g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));  
         g.drawImage(temp, 0, 0, null);  
         g.dispose();   
         // Soften.  
         float softenFactor = 0.05f;  
         float[] softenArray = {0, softenFactor, 0, softenFactor, 1-(softenFactor*4), softenFactor, 0, softenFactor, 0};  
         Kernel kernel = new Kernel(3, 3, softenArray);  
         ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
         bufferedImage = cOp.filter(bufferedImage, null);   
         // Write the jpeg to a file.  
         FileOutputStream out = new FileOutputStream(resizedFile);          
         // Encodes image as a JPEG data stream  
         JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);   
         JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);   
         param.setQuality(quality, true);   
         encoder.setJPEGEncodeParam(param);  
         encoder.encode(bufferedImage);  
     }  
       
 
     /** 
      * 图片缩放(图片等比例缩放为指定大小，空白部分以白色填充) 
      *  
      * @param srcBufferedImage  源图片 
      * @param destFile缩放后的图片文件 
      * @param destHeight 
      * @param destWidth 
      */  
     public static void zoom(BufferedImage srcBufferedImage, File destFile, String format, int destHeight, int destWidth) {  
         try {  
             int imgWidth = destWidth;  
             int imgHeight = destHeight;  
             int srcWidth = srcBufferedImage.getWidth();  
             int srcHeight = srcBufferedImage.getHeight();  
             double scaleW = destWidth * 1.0 / srcWidth;  
             double scaleH = destHeight * 1.0 / srcHeight;  
             if (scaleW >= scaleH) {  
                 double imgWidth1 = scaleH * srcWidth;  
                 double imgHeight1 = scaleH * srcHeight;   
                 imgWidth = (int)imgWidth1;  
                 imgHeight = (int)imgHeight1;  
             } else {  
                 double imgWidth1 = scaleW * srcWidth;  
                 double imgHeight1 = scaleW * srcHeight;   
                 imgWidth = (int)imgWidth1;  
                 imgHeight = (int)imgHeight1;  
             }  
             BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);  
             Graphics2D graphics2D = destBufferedImage.createGraphics();  
             graphics2D.setBackground(Color.WHITE);   
             graphics2D.clearRect(0, 0, destWidth, destHeight);  
             graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), (destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);  
             graphics2D.dispose();  
             ImageIO.write(destBufferedImage, format, destFile);  
         } catch (IOException e) {  
             e.printStackTrace();  
         }  
     } 
     
     
     
     public static void main(String[] arg){
    	 
    	 
    	 File f1=new File("c:/1.png");
    	 File f2=new File("c:/11.png");
    	 try {
			resize(f1,f2,100,"png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
}
