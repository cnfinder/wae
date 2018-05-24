package cn.finder.wae.common.comm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("all")
public class ImageUtil
{
	private static String[] chars = { "1", "3", "5", "7", "8", "9", "0", "A",
			"B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
	private static final int WIDTH = 200;
	private static final int HEIGHT = 100;
	private static final int NUM = 5;
	private static final int LINE = 20;

	/**
	 */
	public static Map getImage()
	{
		Map map = new HashMap();
		String s = "";

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = image.getGraphics();
		Random ran = new Random();

		graphics.setColor(getRandomColor(200,250));
		graphics.fillRect(0, 0, WIDTH, HEIGHT);

		for (int i = 0; i < NUM; i++)
		{
			graphics.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran
					.nextInt(255)));
			graphics.setFont(new Font(null, Font.BOLD, ran.nextInt(20) + 50));
			String ranstr = chars[ran.nextInt(chars.length)];
			s += ranstr;
			graphics.drawString(ranstr, WIDTH / NUM * (i), HEIGHT / 2);
		}

		for (int j = 0; j < LINE; j++)
		{
			graphics.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran
					.nextInt(255)));
			graphics.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT), ran
					.nextInt(WIDTH), ran.nextInt(HEIGHT));
		}
		map.put("code", s);
		map.put("image", image);
		return map;
	}

	/**
	 * @param image
	 * @return
	 * @throws IOException
	 */
	public static ByteArrayInputStream getInputStream(BufferedImage image)
			throws IOException
	{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	//	JPEGImageEncoder encode = JPEGCodec.createJPEGEncoder(bos);
	//	encode.encode(image);

		byte[] byteArr = bos.toByteArray();
		return new ByteArrayInputStream(byteArr);
	}

	private static  Color getRandomColor(int fc, int bc)
	{
		// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);

		
	}
	
	
	
	
	/***
	 * base64字符串转化成图片
	 * 
	 * @param imgStr
	 * @return
	 */
	public static byte[]  generateBase64ToImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return null;
		imgStr = imgStr.replace("data:image/png;base64,", "");
		imgStr = imgStr.replace("data:image/jpeg;base64,", "");
		imgStr = imgStr.replace("data:image/gif;base64,", "");
		imgStr = imgStr.replace("data:image/bmp;base64,", "");
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/***
	 * 下载远程文件
	 * @param url
	 * @return
	 */
	public static byte[] downloadHttpFile(String url){
		byte[] buffer = null;
		ByteArrayOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL theurl = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			theurl = new URL(url);
			httpUrl = (HttpURLConnection) theurl.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			fos = new ByteArrayOutputStream();
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}

			buffer = fos.toByteArray();

			fos.flush();
			fos.close();
		} catch (IOException e) {
		} catch (ClassCastException e) {
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (IOException e) {
			} catch (NullPointerException e) {
			}
		}
		
		return buffer;
		
	}

	/***
	 *  图片转化成base64字符串
	 * @param imgData
	 * @return
	 */
	public static String getImageToBase64(byte[] imgData) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = imgData;
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
}
