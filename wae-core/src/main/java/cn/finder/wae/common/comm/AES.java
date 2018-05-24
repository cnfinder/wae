package cn.finder.wae.common.comm;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/***
 * 
 * 1、到以下网址：http://www.oracle.com/technetwork/java/javase/downloads/index.html下载Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 6得到包jce_policy-6.zip
2、解决jce_policy-6.zip将里面的local_policy.jar和US_export_policy.jar复制并覆盖%JAVA_HOME%/jre/lib/security下的local_policy.jar和US_export_policy.jar
 * @author Administrator
 *
 */
public class AES {  
	public static boolean initialized = false;  
	
	/**
	 * AES解密
	 * @param content 密文
	 * @return
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchProviderException 
	 */
	public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
		initialize();
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			Key sKeySpec = new SecretKeySpec(keyByte, "AES");
			
			cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化 
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();  
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();  
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}  
	
	public static void initialize(){  
        if (initialized) return;  
        Security.addProvider(new BouncyCastleProvider());  
        initialized = true;  
    }
	//生成iv  
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception{  
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");  
        params.init(new IvParameterSpec(iv));  
        return params;  
    }  
    
    
    public static void main(String[] args){
    	
    	String encryptedData="Yg8FeOcKXesXgaq5/jZKdEs6QDhTJleRV9o/2X8GTRpKoYvpHB6qtxtQ6YQ/RS+FJqq29URdxyIzBShTtUF3QX0mE7lU+aKfdO0S3aLCduIO2v0FNvmrJIMMdW2sqeiwqBIQe3lRj5ZLMwsK5EmWjlu2MF4dHg1G8hGDup2cch1oiaSoJvYp744j8BL28rEiNXQI1vFHqUKmUqx2fBeom+egBtkhyC+psNPlZ5BgRIMdH6nvKq948HCSmV1dptvwcS2o71oFABsLHgFuyoWBxgmD5mh6/MUWk1tUs9MBdyJDpYz1E8id1rfTzgUm+bQ+V1VIFzVQ3p7cABKC2gD/tVkBMgHD//oN38SggkOpfFo5mc6D820DPYWvcezdW7EMP5F8BZj3EsXPKmNIZemVE5aBHyQqKmuaHjlHKPHky/QMmeWAvTMbbMss61qOYA6ZpGldNGNq9T47k3R9kNf2Qw==";
    	
    	String iv="uF/Zb9wz8RUJzlssR6rhhg==";
    	String wx_session="a66fe2610cab437d9b99f4403d6f7a0f";
    	
    	
    	try {
			
			AES aes=new AES();
			
	        byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(wx_session), Base64.decodeBase64(iv));
	        if(null != resultByte && resultByte.length > 0){
	            String userInfo = new String(resultByte, "UTF-8");
	            
	            
	        }
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
    }
}  