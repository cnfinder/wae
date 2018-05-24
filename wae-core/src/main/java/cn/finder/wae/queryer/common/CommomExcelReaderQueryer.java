package cn.finder.wae.queryer.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
  

  

import cn.finder.ui.webtool.QueryCondition;
import cn.finder.wae.business.domain.ShowDataConfig;
import cn.finder.wae.business.domain.ShowTableConfig;
import cn.finder.wae.business.domain.TableQueryResult;
import cn.finder.wae.business.dto.MapParaQueryConditionDto;
import cn.finder.wae.cache.ArchCache;
import cn.finder.wae.cache.ConstantsCache;
/**
 * 
 * @author wanggan
 * CommomExcelReaderQueryer.java
 * 2014-5-20
 * TODO
 */
public class CommomExcelReaderQueryer extends BaseCommonDBQueryer {

	@Override
	public void setJDBCDataSource(DataSource dataSource) {
		super.setJdbcDataSource(dataSource);
	}

	@Override
	public TableQueryResult queryTableQueryResult(long showTableConfigId,
			QueryCondition<Object[]> condition) {
		
		logger.debug("=====go into CommomExcelReaderQueryer======");
		
		logger.debug("==showTableConfigId:"+showTableConfigId);
		Map<String, Object> data = ((MapParaQueryConditionDto<String, Object>)condition).getMapParas();
		ShowTableConfig showTabelConfig= ArchCache.getInstance().getShowTableConfigCache().get(showTableConfigId);
		List<ShowDataConfig> showDataConfigs = ArchCache.getInstance().getShowTableConfigCache().getSearchShowDataConfigs(showTableConfigId);
		String filePath=String.valueOf(data.get("filePath"));
		String WebRoot=String.valueOf(data.get("WebRoot_path"));
		String fileRealPath=WebRoot+filePath;
		List<Map<String,Object>> list =null;
		try {
			list=readXls(showDataConfigs,fileRealPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final TableQueryResult qr =new TableQueryResult();
		qr.setResultList(list);
		return qr;
	}
	
	private List<Map<String,Object>> readXls(List<ShowDataConfig> showDataConfigs ,String fileRealPath) throws IOException {  
        Workbook hssfWorkbook = read(fileRealPath);  
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();  
        // 循环工作表Sheet 这里只读取sheet1 hssfWorkbook.getNumberOfSheets()
        for (int numSheet = 0; numSheet <1 ; numSheet++) {  
        	Sheet sheet = hssfWorkbook.getSheetAt(numSheet);  
            if (sheet == null) {  
                continue;  
            }  
            // 循环行Row  
            for (int rowNum = 1; rowNum <= sheet.getPhysicalNumberOfRows(); rowNum++) {  
                Row row = sheet.getRow(rowNum);  
                if (row == null) {  
                    continue;  
                } 
                Map xlsDto = new HashMap<String, Object>();  
                for(ShowDataConfig ds:showDataConfigs){
                	//if(ds.getDataType()==ConstantsCache.DataType.DATATYPE_NUMBER)
                	xlsDto.put(ds.getFieldName(), getValue(row.getCell(ds.getSort())));
                	//System.out.println(getValue(hssfRow.getCell(ds.getSort()))+"---"+ds.getFieldName());
                }
                list.add(xlsDto);  
            }  
        }  
        return list;  
    } 
	
	
	
	public Workbook read(String filePath)
	{
		
		Workbook wb = null;
		File file = new File(filePath);
		InputStream is = null;
		
		try
		{
			/** 根据版本选择创建Workbook的方式 */
			is = new FileInputStream(file);
			if (isExcel2007(filePath))
			{
				wb = new XSSFWorkbook(is);
			}
			else
			{
				wb = new HSSFWorkbook(is);
			}
			
		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		finally
		{

			if (is != null)
			{

				try
				{

					is.close();

				}
				catch (IOException e)
				{

					is = null;

					e.printStackTrace();

				}

			}

		}

		return wb;

	}
	
	private  boolean isExcel2007(String filePath)
	{

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}
	
	private String getValue(Cell cell) {  
		String cellValue = "";
		
		if (null != cell)
		{
			// 以下是判断数据的类型
			switch (cell.getCellType())
			{
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					cellValue=cell.getDateCellValue().toLocaleString();
				}
				else{
					double dou=cell.getNumericCellValue();	
					if(dou-(double)((long)dou) < Double.MIN_VALUE)
					{
						Long num = new Long((long)dou);
						cellValue=String.valueOf(num);
					}
					else
					{
						cellValue=cell.getNumericCellValue()+"";
					}
				}
				//cellValue = cell.getNumericCellValue()+"";
				break;


			case Cell.CELL_TYPE_STRING: // 字符串
				cellValue = cell.getStringCellValue();
				break;

			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = cell.getBooleanCellValue() + "";
				break;

			case Cell.CELL_TYPE_FORMULA: // 公式
				cellValue = cell.getCellFormula() + "";
				break;

			case Cell.CELL_TYPE_BLANK: // 空值
				cellValue = "";
				break;

			case Cell.CELL_TYPE_ERROR: // 故障
				cellValue = "非法字符";
				break;

			default:
				cellValue = "未知类型";
				break;
			}
		}
		return cellValue;
    }  
}
