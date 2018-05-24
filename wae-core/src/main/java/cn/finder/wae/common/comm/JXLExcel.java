package cn.finder.wae.common.comm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import jxl.Cell;
import jxl.CellView;
import jxl.Image;
import jxl.Workbook;
import jxl.biff.EmptyCell;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JXLExcel {

	private final static Logger logger=Logger.getLogger(JXLExcel.class);
	
	private InputStream inStream;
	
	private OutputStream outStream;
	
	private WritableWorkbook wwb;
	
	private WritableSheet sheet;
	
	public JXLExcel(InputStream inStream,OutputStream outStream)
	{
		this.inStream=inStream;
		this.outStream=outStream;
		
		
		init();
	}
	
	private void init()
	{
		try
		{
			//创建只读的Excel工作薄的对象
			jxl.Workbook rw = jxl.Workbook.getWorkbook(inStream);
			//创建可写入的Excel工作薄对象
			wwb = Workbook.createWorkbook(outStream,rw);
			
			//获取第一个工作簿
			sheet=wwb.getSheet(0);
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	

	public void addObject(int col,int row,int width,int height,Object content)
	{
	//	logger.debug(" ===== data type:" + content.getClass().getName());
		if(content == null)
			content ="";
		if(content instanceof String)
		{
			addString(col, row, (String)content);
		}
		else if(content instanceof java.util.Date)
		{
			addDate(col, row, (java.util.Date)content);
		}
		else if(content instanceof Float)
		{
			addNumber(col, row, (Float)content);
		}
		else if(content instanceof byte[])
		{
			addImage(col, row,width,height,(byte[])content);
		}
		else if(content instanceof Integer)
		{
			addNumber(col, row, ((Integer)content).floatValue());
		}
		else if(content instanceof Long)
		{
			addNumber(col, row, ((Long)content).floatValue());
		}
	}
	
	public void addString(int col,int row,String content)
	{
		 if(content ==null)
			 content="";
		
		 try {			 
			 WritableCellFormat font = new WritableCellFormat();
			//把垂直对齐方式指定为居中
			 font.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				font.setWrap(true);
				// 把水平对齐方式指定为居中
			//	font.setAlignment(jxl.format.Alignment.CENTRE);
				font.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
			
			 Label  cell = new Label(col,row,content,font);
			sheet.addCell(cell);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addDate(int col,int row,java.util.Date content)
	{  
		addDate(col,row,content,"yyyy-MM-dd");
	}
	
	public void addDate(int col,int row,java.util.Date content,String patter)
	{  
		 try {
			 jxl.write.DateFormat df = new jxl.write.DateFormat(patter);
	         jxl.write.WritableCellFormat wcfDF = new jxl.write.WritableCellFormat(df);
	         wcfDF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	         wcfDF.setAlignment(jxl.format.Alignment.LEFT);
	         wcfDF.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
			jxl.write.DateTime cell = new jxl.write.DateTime(col, row, content,wcfDF);
			sheet.addCell(cell);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void addNumber(int col,int row,float content)
	{
		
		try {
			WritableCellFormat font = new WritableCellFormat();
			font.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
			jxl.write.Number number  =   new  jxl.write.Number(col , row , content ,font);
			sheet.addCell(number);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/****
	 * 需要设置居中
	 * @param col
	 * @param row
	 * @param width 传入图片的宽度
	 * @param height 传入图片的高度
	 * @param imageData
	 *//*
	public void addImage(int col,int row,double width,double height,byte[] imageData)
	{
		
		 * x - the column number at which to position the image
			y - the row number at which to position the image
			width - the number of columns cells which the image spans
			height - the number of rows which the image spans
			imageData - the image data
		 
		
		double left=0.0;
		double top=0.0;

		//获取不到单元格的宽度和高度
		WritableCell wCell=sheet.getWritableCell(col, row);
		//getSize:the width of the column in characters multiplied by 256, or the height of the row in 1/20ths of a point
		//Gets the column width for the specified column
		
		//在EXCEL中行高是用像素表示
		//在EXCEL中列宽是用毫米表示
		CellView colCV=sheet.getColumnView(col);
		
		logger.debug("column cell view depUsed:"+colCV.depUsed()+" size:"+sheet.getColumnView(col).getSize());
		double cellWidth=sheet.getColumnView(col).getSize()/256/10;
		
		CellView rowCV=sheet.getColumnView(row);
		logger.debug("row cell view depUsed:"+rowCV.depUsed()+" size:"+sheet.getColumnView(row).getSize());
		//
		//double cellHeight=sheet.getRowView(row).getSize()/256/2;
		double cellHeight=20.;
		
		if (height > cellHeight)//图片高比单元格大，按比例缩小
        {
            width=width/height*cellHeight;
            height = cellHeight;
        }
        if (width > cellWidth - 2) width = cellWidth - 2;
        left = left + (cellWidth - width) / 2;
        top = top - (cellHeight - height) / 2+1;
		
		
		//row+0.01、0.015、01101 图片向上  row+0.1 、  row+0.106 、0.1065、0.103、  row+0.3 、row+0.11 、0.0111、0.0115、0.02、0.011001、0.13、0.12图片向下
        WritableImage image = new WritableImage(col+0.1, row+0.1, cellWidth, cellHeight-0.1, imageData); // 设置图片显示位置
      
		try {
			sheet.addImage(image);
			WritableCellFormat wc = new WritableCellFormat();
			wc.setAlignment(Alignment.CENTRE); // 设置居中
			wc.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK); // 设置边框线
			Label label = new Label(1, 5, "字体", wc);
			sheet.addCell(label);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}*/
	
	
	/****
	 * 需要设置居中
	 * @param col
	 * @param row
	 * @param width 传入图片的宽度
	 * @param height 传入图片的高度
	 * @param imageData
	 */
	public void addImage(int col,int row,double width,double height,byte[] imageData)
	{
		/*
		 * x - the column number at which to position the image
			y - the row number at which to position the image
			width - the number of columns cells which the image spans
			height - the number of rows which the image spans
			imageData - the image data
		 */
		
		double left=0.0;
		double top=0.0;

		//获取不到单元格的宽度和高度
		WritableCell wCell=sheet.getWritableCell(col, row);
		//getSize:the width of the column in characters multiplied by 256, or the height of the row in 1/20ths of a point
		//Gets the column width for the specified column
		
		//在EXCEL中行高是用像素表示
		//在EXCEL中列宽是用毫米表示
		CellView colCV=sheet.getColumnView(col);
		
		logger.debug("column cell view depUsed:"+colCV.depUsed()+" size:"+sheet.getColumnView(col).getSize());
		double cellWidth=sheet.getColumnView(col).getSize()/256/10;
		
		CellView rowCV=sheet.getColumnView(row);
		logger.debug("row cell view depUsed:"+rowCV.depUsed()+" size:"+sheet.getColumnView(row).getSize());
		//
		double cellHeight=sheet.getRowView(row).getSize()/256/2;
		/*
		if (height > cellHeight)//图片高比单元格大，按比例缩小
        {
            width=width/height*cellHeight;
            height = cellHeight;
        }
        if (width > cellWidth - 2) width = cellWidth - 2;
        left = left + (cellWidth - width) / 2;
        top = top - (cellHeight - height) / 2+1;
		
		*/
		//row+0.01、0.015、01101 图片向上  row+0.1 、  row+0.106 、0.1065、0.103、  row+0.3 、row+0.11 、0.0111、0.0115、0.02、0.011001、0.13、0.12图片向下 
	    // 图片实际的高度，宽度  
	    double picCellWidth = 0.0;  
	    double picCellHeight = 0.0; 
		
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(imageData);    //将b作为输入流；
			BufferedImage buImage = ImageIO.read( in);
			
			// 取得图片的像素高度，宽度  
		    int picWidth = buImage.getWidth();  
		    int picHeight = buImage.getHeight();  
		    // 计算图片的实际宽度  
		    int picWidth_t = picWidth * 32;  //具体的实验值，原理不清楚。  
		    for (int x = 0; x < 1234; x++) {  
		        int bc = (int) Math.floor(col + x);  
		        // 得到单元格的宽度  
		        int v = sheet.getColumnView(bc).getSize();  
		        double offset0_t = 0.0;  
		        if (0 == x)  
		            offset0_t = (col - bc) * v;  
		        if (0.0 + offset0_t + picWidth_t > v) {  
		            // 剩余宽度超过一个单元格的宽度  
		            double ratio_t = 1.0;  
		            if (0 == x) {  
		                ratio_t = (0.0 + v - offset0_t) / v;  
		            }  
		            picCellWidth += ratio_t;  
		            picWidth_t -= (int) (0.0 + v - offset0_t);  
		        } else { //剩余宽度不足一个单元格的宽度  
		            double ratio_r = 0.0;  
		            if (v != 0)  
		                ratio_r = (0.0 + picWidth_t) / v;  
		            picCellWidth += ratio_r;  
		            break;  
		        }  
		    } 
		    
		    // 计算图片的实际高度  
		    int picHeight_t = picHeight * 15;  
		    for (int x = 0; x < 1234; x++) {  
		        int bc = (int) Math.floor(row + x);  
		        // 得到单元格的高度  
		        int v = sheet.getRowView(bc).getSize();  
		        double offset0_r = 0.0;  
		        if (0 == x)  
		            offset0_r = (row - bc) * v;  
		        if (0.0 + offset0_r + picHeight_t > v) {  
		            // 剩余高度超过一个单元格的高度  
		            double ratio_q = 1.0;  
		            if (0 == x)  
		                ratio_q = (0.0 + v - offset0_r) / v;  
		            picCellHeight += ratio_q;  
		            picHeight_t -= (int) (0.0 + v - offset0_r);  
		        } else {//剩余高度不足一个单元格的高度  
		            double ratio_m = 0.0;  
		            if (v != 0)  
		                ratio_m = (0.0 + picHeight_t) / v;  
		            picCellHeight += ratio_m;  
		            break;  
		        }  
		    }  
		    
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		picCellWidth = picCellWidth/1.8;
		picCellHeight = picCellHeight/2;
		WritableImage image = new WritableImage(col+0.1, row+0.05, picCellWidth, 1, imageData); // 设置图片显示位置
      
		try {

			sheet.addImage(image);
			//得到单元格的高度  
			int cellheig =sheet.getRowView(row).getSize();
			int rowHeight = (int)(cellheig*picCellHeight)+10;
			sheet.setRowView(row, rowHeight, false);//设置行高
			
			/*WritableCellFormat wc = new WritableCellFormat();
			wc.setAlignment(Alignment.CENTRE); // 设置居中
			wc.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK); // 设置边框线
			Label label = new Label(1, 5, "字体", wc);
			sheet.addCell(label);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public   Cell findCell(String matchString)
	{
		//查找匹配的单元格
	    java.util.regex.Pattern pattern = Pattern.compile(matchString);
	    return  sheet.findCell(pattern, 0, 0, 100, 100, false);
	}

	
	
	//替换查找到的单元格的内容
	public Cell replaceFindCell(String matchString,int width,int height,Object content)
	{
		matchString=matchString.replace("[", "\\[").replace("]","\\]");
		Cell cell=findCell(matchString);
		
		if(cell!=null)
			addObject(cell.getColumn(), cell.getRow(),width,height, content);
		return cell;
	}
	
	
	
	public Cell findCell(int col,int row){
		return sheet.getCell(col, row);
	}
	
	/*
	 * 
	 * 查找单元格内容
	 */
	public String findCellValueString(int col,int row)
	{
		Cell cell = sheet.getCell(col, row);
		return cell.getContents();
		
	}
	
	/*
	 * 
	 * 查找单元格内容
	 */
	public File findCellValueFile(int col,int row)
	{
		WritableImage cell = (WritableImage)sheet.getCell(col, row);
		return cell.getImageFile();
		
	}
	
	/***
	 * 获取单元数据
	 * @param col
	 * @param row
	 * @return
	 */
	public Object findCellValue(int col,int row){
		Object data=null;
		Cell cell = sheet.getCell(col, row);
		if(cell instanceof Label){
			data=((Label)cell).getContents();
			
			if(data!=null){
				String cellData = data.toString();
				
				if(cellData.startsWith("0x")){
					data=HexadecimalUtil.hexStringToBytes(cellData);
				}
			}
			
		}else if(cell instanceof jxl.write.DateTime){
			data=((jxl.write.DateTime)cell).getDate();
		}else if(cell instanceof jxl.write.Number){
			data=((jxl.write.Number)cell).getContents();
		}else if(cell instanceof jxl.write.WritableImage){
			data=((jxl.write.WritableImage)cell).getImageData();
		}
		else if(cell instanceof EmptyCell){
			Image image = sheet.getDrawing(row);        
			byte[] imageData = image.getImageData();
			data = imageData;
		}
		
		
		return data;
	}
	
	/***
	 * 获取单元数据(不支持图片)
	 * @param col
	 * @param row
	 * @return
	 */
	public Object findCellValueNoImage(int col,int row){
		Object data=null;
		Cell cell = sheet.getCell(col, row);
		if(cell instanceof Label){
			data=((Label)cell).getContents();
			
			if(data!=null){
				String cellData = data.toString();
				
				if(cellData.startsWith("0x")){
					data=HexadecimalUtil.hexStringToBytes(cellData);
				}
			}
			
		}else if(cell instanceof jxl.write.DateTime){
			data=((jxl.write.DateTime)cell).getDate();
		}else if(cell instanceof jxl.write.Number){
			data=((jxl.write.Number)cell).getContents();
		}else if(cell instanceof jxl.write.WritableImage){
			data=((jxl.write.WritableImage)cell).getImageData();
		}
		else if(cell instanceof EmptyCell){
			
			data = "";
		}
		
		
		return data;
	}
	
	
	public void close()
	{
		try {
			
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public WritableSheet getSheet() {
		return sheet;
	}
	
  
	
	public void mergeCell(int beginColumnIndex,int beginRowIndex,int endColumnIndex ,int endRowIndex){
		
		//合并单元格，参数依次为：列索引、行索引、列索引+需要合并的列的个数、行索引+需要合并的行的个数
		 try {
			sheet.mergeCells(beginColumnIndex, beginRowIndex, endColumnIndex, endRowIndex);
			
			
		} catch (RowsExceededException e) {
			
			e.printStackTrace();
			
		} catch (WriteException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
}
