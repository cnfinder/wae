package cn.finder.wx.pay;

import java.security.MessageDigest;

/***
 * 编码 MD5 SHA1等
 * @author whl
 *
 */
public class EncoderHandler {

	private static final String ALGORITHM = "MD5";

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * encode string
	 *
	 * @param algorithm 算法名称 md5  or sha1
	 * @param str
	 * @return String
	 */
	public static String encode(String algorithm, String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes("UTF-8"));
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
 
	/***
	 * MD5编码后的字节数组
	 * @param encryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] md5Byte(String encryptStr) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(encryptStr.getBytes("UTF-8"));
		return md.digest();
	}
	
	
	/***
	 * SHA1编码后的字节数组
	 * @param encryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] sha1Byte(String encryptStr) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(encryptStr.getBytes("UTF-8"));
		return md.digest();
	}
	
	/**
	 * encode By MD5  返回16字节 32位的编码字符串
	 *
	 * @param str
	 * @return String
	 */
	public static String encodeByMD5(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
			messageDigest.update(str.getBytes("UTF-8"));
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	
	/**
	 * encode By SHA1 返回20字节 40位位的编码字符串
	 *
	 * @param str
	 * @return String
	 */
	public static String encodeBySHA1(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(str.getBytes("UTF-8"));
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 * 16HEX
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) { 			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("111111 MD5  :"
				+ EncoderHandler.encodeByMD5("111111"));
		System.out.println("111111 MD5  :"
				+ EncoderHandler.encode("MD5", "111111"));
		System.out.println("111111 SHA1 :"
				+ EncoderHandler.encode("SHA1", "111111"));
		
	}

}
