package cn.finder.wae.common.comm;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


@SuppressWarnings("all")
public class CryptUtil
{
	// 加密因子
	private static String key = "";

	/**
	 * 对外公开的加密方法，返回加密字符串,不能解密，如果密码忘了的话 就随机生成一个密码
	 * 
	 * @param str
	 *            明文
	 * @return 密文
	 */
	public static String EncryptByMD5(String str)
	{
		return EncryptByMD5(str, key);
	}

	/*public static String DecryptByMD5(String str)
	{
		return DeEncryptByMD5(str, key);
	}*/

	private static String toHexString(char[] c)
	{
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < c.length; i++)
		{
			int val = ((int) c[i]) & 0xff;// 0x11111111
			if (val < 16)
				sb.append("0");
			sb.append(Integer.toHexString(val));
		}
		return sb.toString();
	}
	
	private static String toHexString(String str)
	{
		return toHexString(str.toCharArray());
	}
	
	private static String toHexString(byte[] str)
	{
		return toHexString(new String(str).toCharArray());
	}

	private static String EncryptByMD5(String str, String key)
	{

		try
		{
			String encry = null;
			str += key;
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());

			//BASE64Encoder encoder = new BASE64Encoder();
			//encry = encoder.encode(data);
			return toHexString(data);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new RuntimeException("加密出错了");
		}
	}
/*
	private static String DeEncryptByMD5(String str, String key)
	{
		try
		{
			str += key;
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());

			BASE64Decoder decoder = new BASE64Decoder();
			data = decoder.decodeBuffer(str);

			return new String(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("解密出错了");
		}

	}*/
	
	
	
	/**
	 * DES 
	 * @param args
	 */
	
	/**
    * 加密方法DES
    * 
    * @param rawKeyData
    * @param str
    * @return
    * @throws InvalidKeyException
    * @throws NoSuchAlgorithmException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    * @throws NoSuchPaddingException
    * @throws InvalidKeySpecException
    */
    public static byte[] encrypt(byte rawKeyData[], String str)
            throws InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException, InvalidKeySpecException {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // 现在，获取数据并加密
        byte data[] = str.getBytes();
        // 正式执行加密操作
        byte[] encryptedData = cipher.doFinal(data);

        System.out.println("加密后===>" + encryptedData);
        return encryptedData;
    }

    /**
    * 解密方法DES
    * 
    * @param rawKeyData
    * @param encryptedData
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    * @throws InvalidKeyException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeySpecException
    */
    public static String decrypt(byte rawKeyData[], byte[] encryptedData)
            throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(encryptedData);
        System.out.println("解密后===>" + new String(decryptedData));
        return new String(decryptedData);
    }

	public static void main(String args[])
	{
		String str = EncryptByMD5("asa");
		System.out.println(str);
		
	}
	
/*
	public static String MD5(String inStr)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++)
		{
			int val = ((int) md5Bytes[i]) & 0xff;// 0x11111111
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// 可逆的加密算法
	public static String KL(String inStr)
	{
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
		{
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	// 加密后解密
	public static String JM(String inStr)
	{
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
		{
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	// 测试主函数
	public static void main(String args[])
	{
		String s = new String("a");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + MD5(s));
		System.out.println("MD5后再加密：" + KL(MD5(s)));
		System.out.println("解密为MD5后的：" + JM(KL(MD5(s))));
	}
*/
	/*
	 * public static void main(String[] args) { String en=EncryptByMD5("111");
	 * System.out.println(en); System.out.println(DecryptByMD5(en)); }
	 */
}
