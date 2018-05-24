package cn.finder.wae.common.comm;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.encode.EAN8Encoder;
import org.jbarcode.paint.EAN8TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

/**
 * 
 * @author whl 
 * 支持EAN13, EAN8, UPCA, UPCE, Code 3 of 9, Codabar, Code 11, Code
 *         93, Code 128, MSI/Plessey, Interleaved 2 of PostNet等
 *         利用jbarcode生成各种条形码
 */
public class OneBarcodeUtil {

	public static void main(String[] paramArrayOfString) {
		try {
			
			JBarcode localJBarcode = new JBarcode(EAN8Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					EAN8TextPainter.getInstance());
			String str = "2219644";
			BufferedImage localBufferedImage = localJBarcode.createBarcode(str);

			saveToJPEG(localBufferedImage, "EAN8.jpg");
			
			
			
			localJBarcode = new JBarcode(EAN13Encoder.getInstance(),
					WidthCodedPainter.getInstance(),
					EAN8TextPainter.getInstance());
			str = "221964436255";
			localBufferedImage = localJBarcode.createBarcode(str);

			saveToJPEG(localBufferedImage, "EAN13.jpg");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static void saveToJPEG(BufferedImage paramBufferedImage, String paramString) {
		saveToFile(paramBufferedImage, paramString, "jpeg");
	}

	public static void saveToFile(BufferedImage paramBufferedImage,
			String paramString1, String paramString2) {
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(
					"C:\\Users\\Administrator\\Desktop/" + paramString1);
			org.jbarcode.util.ImageUtil.encodeAndWrite(paramBufferedImage, paramString2,
					localFileOutputStream, 96, 96);
			localFileOutputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}
}
