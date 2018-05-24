package cn.finder.wae.queryer.handleclass;
import java.util.Map;

import sun.misc.BASE64Decoder;
import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.business.module.auth.service.AuthService;
import cn.finder.wae.common.comm.AppApplicationContextUtil;

/***
 * 更新用户头像
 * @author xiaoht
 *
 */
public class UpdateUserImageQueryerAfter extends QueryerDBAfterClass {

	AuthService authService =AppApplicationContextUtil.getContext().getBean("authService", AuthService.class);
	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		super.handle(tableQueryResult, showTableConfigId, condition);
		@SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		String account = data.get("account").toString();
		String  img_binary = data.get("img_binary").toString();
		Object[] imgArr = img_binary.split(",");
		String  imgStr =imgArr[1].toString();
		if(imgStr!=""){
			//将64位编码字符串转换成字节数组
			
			BASE64Decoder  decoder = new BASE64Decoder ();
			try{
					// Base64解码  
					byte[] bytes = decoder.decodeBuffer(imgStr);  
					for (int i = 0; i < bytes.length; ++i) {  
						if (bytes[i] < 0) {// 调整异常数据  
						bytes[i] += 256;  
						}  
				}  
				String user_sql = "update t_user set img_binary = ? where account = ?";
				authService.addUser(user_sql, new Object[]{bytes,account});
			
			}catch(Exception e){
				e.printStackTrace();
				
			}
		}

		return tableQueryResult;
		
	}
	
	
	
}
