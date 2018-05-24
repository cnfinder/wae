package cn.finder.wae.common.thumbnail;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.ByteArrayOutputStream;



public class AffineTransImage {

	public byte[] buffer;
	
	private InputStream inStream;
	
    private int newWidth = 100;
	private String imgFormat="jpg";//图片格式 如 gif,jpg,png
    
	public AffineTransImage(InputStream inStream,int newWidth){
		this.inStream=inStream;
		this.newWidth=newWidth;
	}
	
	
	public void setImgFormat(String imgFormat){
		this.imgFormat=imgFormat;
	}
	
	public void resizeFix(){
		
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		try {  
        
            /* 
            AffineTransform 类表示 2D 仿射变换，它执行从 2D 坐标到其他 2D 
            坐标的线性映射，保留了线的“直线性”和“平行性”。可以使用一系 
            列平移、缩放、翻转、旋转和剪切来构造仿射变换。 
            */  
            AffineTransform transform = new AffineTransform();  
            BufferedImage bis = ImageIO.read(inStream); //读取图片  
            int w = bis.getWidth();  
            int h = bis.getHeight(); 
            if(w<=newWidth){
            	ByteArrayOutputStream baos=new ByteArrayOutputStream();
            	ImageIO.write(bis,imgFormat,baos);
            	
            	buffer=baos.toByteArray();
            	return ;
            }
            
             //double scale = (double)w/h;  
            int nh = (newWidth*h)/w ;  
            double sx = (double)newWidth/w;  
            double sy = (double)nh/h;  
            transform.setToScale(sx,sy); //setToScale(double sx, double sy) 将此变换设置为缩放变换。  
            System.out.println(w + " " +h);  
            /* 
             * AffineTransformOp类使用仿射转换来执行从源图像或 Raster 中 2D 坐标到目标图像或 
             *  Raster 中 2D 坐标的线性映射。所使用的插值类型由构造方法通过 
             *  一个 RenderingHints 对象或通过此类中定义的整数插值类型之一来指定。 
            如果在构造方法中指定了 RenderingHints 对象，则使用插值提示和呈现 
            的质量提示为此操作设置插值类型。要求进行颜色转换时，可以使用颜色 
            呈现提示和抖动提示。 注意，务必要满足以下约束：源图像与目标图像 
            必须不同。 对于 Raster 对象，源图像中的 band 数必须等于目标图像中 
            的 band 数。 
            */  
            AffineTransformOp ato = new AffineTransformOp(transform,null);  
            BufferedImage bid = new BufferedImage(newWidth,nh,BufferedImage.TYPE_3BYTE_BGR);  
            /* 
             * TYPE_3BYTE_BGR 表示一个具有 8 位 RGB 颜色分量的图像， 
             * 对应于 Windows 风格的 BGR 颜色模型，具有用 3 字节存 
             * 储的 Blue、Green 和 Red 三种颜色。 
            */  
            ato.filter(bis,bid);  
            ImageIO.write(bid,"jpeg",out);
            buffer=out.toByteArray();
            
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
	}


	public byte[] getBuffer() {
		return buffer;
	}
	
	
	
	public static void main(String[] args){
		try {
			/*FileInputStream fis=new FileInputStream("D:/aa.docx");
			AffineTransImage dit = new AffineTransImage(fis,100);
			dit.resizeFix();
			
			byte[] buffer=dit.getBuffer();
		    
			FileOutputStream fos=new FileOutputStream("d:/03_small.jpg");
			fos.write(buffer);
			fos.flush();
			fos.close();*/
			
			FileInputStream fis=new FileInputStream("c:/2.png");
			AffineTransImage dit = new AffineTransImage(fis,100);
			dit.setImgFormat("png");
			dit.resizeFix();
			
			byte[] buffer=dit.getBuffer();
		    
			FileOutputStream fos=new FileOutputStream("c:/22.png");
			fos.write(buffer);
			fos.flush();
			fos.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String GetFileSuffix(byte[] fileData)
    {
        if ((fileData != null) && (fileData.length >= 10))
        {
            if (((fileData[0] == 0x47) && (fileData[1] == 0x49)) && (fileData[2] == 70))
            {
                return "GIF";
            }
            if (((fileData[1] == 80) && (fileData[2] == 0x4e)) && (fileData[3] == 0x47))
            {
                return "PNG";
            }
            if (((fileData[6] == 0x4a) && (fileData[7] == 70)) && ((fileData[8] == 0x49) && (fileData[9] == 70)))
            {
                return "JPG";
            }
            if ((fileData[0] == 0x42) && (fileData[1] == 0x4d))
            {
                return "BMP";
            }
        }
        return null;
    }
	
}
