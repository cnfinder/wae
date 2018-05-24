package cn.finder.wae.common.comm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.Logger;

import cn.finder.wae.business.domain.ShowDataConfig;


public class ExcelUtil {

	private static Logger logger = Logger.getLogger(ExcelUtil.class);
	
	public  ByteArrayOutputStream exportExcel(List<String> headerTextList,List<Map<String,Object>> dataList,InputStream inStream,String sheetName) throws Exception{
		
	
		ByteArrayOutputStream outStream =new ByteArrayOutputStream();
		JXLExcel jxlExcel=new JXLExcel(inStream,outStream);
		

		//设置标题
		WritableSheet sheet=jxlExcel.getSheet();
		sheet.setName(sheetName);
		 
		//sheet.mergeCells(0, 0, headerTextList.size(), 0);  //col,row,width,heigh
		//sheet.addCell(new Label(0, 0, sheetName,getTitleStyle()));  //设置标题
		
		for (int i = 0; i < headerTextList.size(); i++) {   //列头类容 
			sheet.setColumnView(i,25);    //设置列宽度 
			Label label = new Label(i, 0,  (String)headerTextList.get(i),getHeader()); //添加第一行头文件  1表示行
			sheet.addCell(label); 
			//jxlExcel.addObject(i, 1, 25, -1, (String)headerTextList.get(i),)
		}
		//设置数据
		if(dataList!=null && dataList.size()>0){
			for(int i=0;i<dataList.size();i++){
				 Map<String, Object> item=dataList.get(i);
				
				for(int j=0;j<headerTextList.size();j++){
					
					//获取到值
					Object value = item.get(headerTextList.get(j));
					
					jxlExcel.addObject(j, i+1,-1,-1, value);
				}
				
			}
		}
		
		jxlExcel.close();
		return outStream;
		
		
	}
	
	
	/**
	 * 标题样式
	 */
	public WritableCellFormat getTitleStyle(){
		jxl.write.WritableFont titleFont = new jxl.write.WritableFont(
			      WritableFont.createFont("宋体"), 25, WritableFont.BOLD);
		jxl.write.WritableCellFormat titleFormat = new jxl.write.WritableCellFormat(
			      titleFont);
			    try {
					titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
					titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				    titleFormat.setAlignment(Alignment.CENTRE); // 水平对齐
				    titleFormat.setWrap(true); // 是否换行
				    titleFormat.setBackground(Colour.GRAY_25);// 背景色暗灰-25%
				} catch (WriteException e) { 
					e.printStackTrace();
				} // 线条
			    
			    return titleFormat;
	}
	
	/**  
     * 列头样式  
     */  
    public  WritableCellFormat getHeader() {   
        WritableFont font = new WritableFont(WritableFont.TIMES, 16,   
                WritableFont.BOLD);// 定义字体   
        try {   
            font.setColour(Colour.BLACK);// 字体颜色   
        } catch (WriteException e1) {   
            // TODO 自动生成 catch 块   
            e1.printStackTrace();   
        }   
        WritableCellFormat format = new WritableCellFormat(font);   
        try {   
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中   
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中   
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);// 黑色边框   
            //format.setBackground(Colour.YELLOW);// 黄色背景   
        } catch (WriteException e) {   
            e.printStackTrace();   
        }   
        return format;   
    }   

    /**  
     * 设置其他单元格样式  
     * @return  
     */  
    private  WritableCellFormat getNormolCell() {// 12号字体,上下左右居中,带黑色边框   
        WritableFont font = new WritableFont(WritableFont.TIMES, 12);   
        WritableCellFormat format = new WritableCellFormat(font);   
        try {   
            format.setAlignment(jxl.format.Alignment.CENTRE);   
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);   
            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);   
        } catch (WriteException e) {   
            e.printStackTrace();   
        }   
        return format;   
    }
}
