package cn.finder.wae.queryer.handleclass.qr;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.common.qrcode.QRCodeUtil;
import cn.finder.wae.queryer.handleclass.QueryerDBAfterClass;

/***
 * 根据文本生成二维码
 * 参数: 
 *    code_text  :码文本
 * @author whl
 *
 */
public class GeneratorQRByCodeTextQueryerAfter extends QueryerDBAfterClass {

	@Override
	public TableQueryResult handle(TableQueryResult tableQueryResult,
			long showTableConfigId, QueryCondition<Object[]> condition) {
		
		 super.handle(tableQueryResult, showTableConfigId, condition);
		
		
		 tableQueryResult =new TableQueryResult();
		 List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		 	
	 	Map<String,Object> mapItem = new HashMap<String, Object>();
	 	
	 	
	 	resultList.add(mapItem);
	 	tableQueryResult.setResultList(resultList);
	 	tableQueryResult.setPageIndex(condition.getPageIndex());
	 	tableQueryResult.setPageSize(condition.getPageSize());
	 	tableQueryResult.setCount(1l);
	 	tableQueryResult.getMessage().setMsg("验证成功，可以通用");
		 	
		 
		 @SuppressWarnings("unchecked")
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
			
		 
	 	//微信 随机码
	 	String code_text =data.get("code_text").toString();
		 
		 
		//不需要访客验证,直接生成二维码
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		try {
			QRCodeUtil.encode(code_text, outStream);
			byte[]  buffer = outStream.toByteArray();
			
			mapItem.put("binary_data", buffer);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tableQueryResult;
		
	}
	
	
	
}
