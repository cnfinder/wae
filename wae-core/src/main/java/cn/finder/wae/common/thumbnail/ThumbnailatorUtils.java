package cn.finder.wae.common.thumbnail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ThumbnailatorUtils {
	/***
	 * size(width,height) 若图片横比200小，高比300小，不变
	 * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
	 * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
	 * @param in
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage resize(InputStream in,int width,int height){
		
		BufferedImage img=null;
		try {
			BufferedImage bis = ImageIO.read(in);
			img=Thumbnails.of(bis).size(width, height).asBufferedImage();
		} catch (IOException e) {
			img=null;
		}
		return img;
	}
	
	
	
	/***
	 * 按照宽度等比例缩图 （主要按照宽度缩放）
	 * 如果 理想宽度大于 实际宽度 则宽度不进行缩放， 
	 * @param in
	 * @param nWidth 理想缩放到的宽度
	 * @return
	 * @throws IOException 
	 */
	public static ByteArrayOutputStream thumbnailator(InputStream in,int nWidth) throws IOException{
		
		 BufferedImage newBi=null;
		
		 BufferedImage bis = ImageIO.read(in); //读取图片  
         int w = bis.getWidth();  
         if(w<=nWidth){
        	 newBi=bis;
         }
         else{
        	 
        	 double scale =1.0f*nWidth/w;
        	 newBi=Thumbnails.of(bis).scale(scale).asBufferedImage();
        	 
         }
         
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
     	JPEGImageEncoder encode = JPEGCodec.createJPEGEncoder(bos);
     	encode.encode(newBi);
         return bos;
		
	}
	
	
	
	
	/***
	 * 按照大小进行缩放,
	 * @param in
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage resizeRatio(InputStream in,int width,int height){
		
		BufferedImage img=null;
		try {
			BufferedImage bis = ImageIO.read(in);
			img=Thumbnails.of(bis).size(width, height).keepAspectRatio(false).asBufferedImage();
			
		} catch (IOException e) {
			img=null;
		}
		return img;
	}
	
	
	/***
	 * 按照比例缩放
	 * @param in
	 * @param scale
	 * @return
	 */
	public static BufferedImage scale(InputStream in,double scale){
		BufferedImage img=null;
		try {
			BufferedImage bis = ImageIO.read(in);
			img=Thumbnails.of(bis).scale(scale).asBufferedImage();
		} catch (IOException e) {
			img=null;
		}
		return img;
	}
	
	
	/***
	 * 旋转 
	 * @param in
	 * @param rotate 角度 如90
	 * @return
	 */
	public static BufferedImage rotate(InputStream in,int rotate){
		BufferedImage img=null;
		try {
			BufferedImage bis = ImageIO.read(in);
			img=Thumbnails.of(bis).rotate(rotate).asBufferedImage();
		} catch (IOException e) {
			img=null;
		}
		return img;
	}
	
	
	/***
	 * 转化图像格式
	 * @param in
	 * @param format  如 png
	 * @return
	 */
	public static BufferedImage convertFormat(InputStream in,String format){
		BufferedImage img=null;
		try {
			BufferedImage bis = ImageIO.read(in);
			img=Thumbnails.of(bis).outputFormat(format).asBufferedImage();
		} catch (IOException e) {
			img=null;
		}
		return img;
	}
	
	
	/***
	 * 图片文件 转入到输出流
	 * @param in
	 * @param out
	 */
	public static void convertFormat(InputStream in,OutputStream out){
		try {
			BufferedImage bis = ImageIO.read(in);
			Thumbnails.of(bis).toOutputStream(out);
		} catch (IOException e) {
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		FileInputStream fis=new FileInputStream("c:/1.png");
		
		try {
			int nWidth=100;
			 BufferedImage bis = ImageIO.read(fis); //读取图片  
	         int w = bis.getWidth();  
	         if(w<=nWidth){
	        	 
	         }
	         else{
	        	 
	        	 double scale =1.0f*nWidth/w;
	        	// Thumbnails.of("c:/1.png").scale(scale).asBufferedImage();
	        	 
	        	 Thumbnails.of(bis).scale(scale).asBufferedImage();
	        	 
	         }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*try {
			thumbnailator(fis,200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
}
