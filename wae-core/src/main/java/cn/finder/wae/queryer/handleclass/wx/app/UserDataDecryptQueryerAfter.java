package cn.finder.wae.queryer.handleclass.wx.app;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import cn.finder.httpcommons.utils.JsonUtils;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.comm.AES;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;
import cn.finder.wae.wx.cache.WXCache;

/***
 * 敏感数据解密
 * 可返回  unionid
 * 
 * 密文和偏移向量由客户端发送给服务端，对这两个参数在服务端进行Base64_decode解编码操作。
根据3rdSessionId从缓存中获取session_key，对session_key进行Base64_decode可以得到aesKey，aes密钥。
调用aes解密方法，算法为 AES-128-CBC，数据采用PKCS#7填充。
 * @author whl
 *
 */
public class UserDataDecryptQueryerAfter extends QueryerDBAfterClass {
 
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		tableQueryResult= new TableQueryResult();
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
		 	
		
 		
		tableQueryResult.setResultList(resultList);
		 	
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	
	 	tableQueryResult.setCount(0l);
	 			
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
	 	
		
		String encryptedDataStr=data.get("encryptedData").toString();
		String iv=data.get("iv").toString();
		String wx_session = data.get("wx_session").toString();
	 	
		
		try {
			
			AES aes=new AES();
			
		    String session_key=WXCache.getInstance().getWXSessionCache().get(wx_session).get("session_key").toString();
			
			byte[] session_key_decode=Base64.decodeBase64(session_key);
			//aeskey = Base64_Decode(session_key), aeskey 是16字节
			
			logger.info("==========aeskey字节:"+session_key_decode.length);
			
	        byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedDataStr), session_key_decode, Base64.decodeBase64(iv));
	        if(null != resultByte && resultByte.length > 0){
	            String userInfo = new String(resultByte, "UTF-8");
	            
	            Map<String,Object>  mapItem=JsonUtils.getMap4Json(userInfo);
	            
	            resultList.add(mapItem);
	            tableQueryResult.setCount(1l);
	        }
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	 	return tableQueryResult;
	 	
		
	}
}
