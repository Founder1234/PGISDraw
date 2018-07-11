package com.draw.drawMapOnline.xls;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
/**
 *@TODO 解析xls文件 
 */
public class XlsMain {

  public static String readXls(String filePath) throws IOException{
    InputStream is = new FileInputStream(filePath);
    HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is); 
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode rootNode = objectMapper.createObjectNode();//根节点
    ArrayNode jsonArray = objectMapper.createArrayNode();
//    JSONPObject json = new JSONObject();
//    JSONArray jsonArray = new JSONArray();//数据数组
    for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){ // 循环工作表Sheet
      HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
      if(hssfSheet == null){
        continue;
      }
      Map<String, String> map = new HashMap<String, String>();  
//      String sheetName = hssfSheet.getSheetName();//工作表名称
      for(int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++){ // 循环行Row
    	HSSFRow firstHssfRow = hssfSheet.getRow(0);
        HSSFRow hssfRow = hssfSheet.getRow(rowNum);
        if(hssfRow == null){
          continue;
        }
        String serialLetter = "A",serialLetter2 ="A";
        int cellLength = 0;
        if(firstHssfRow.getLastCellNum()>11){
        	cellLength = 11;
        }else{
        	cellLength = firstHssfRow.getLastCellNum();
        }
        for(int cellNum = 0; cellNum <cellLength; cellNum++){ // 循环列Cell  
          HSSFCell hssfCell = hssfRow.getCell( cellNum);
        /*  if(hssfCell == null){
              continue;
          }*/
          if(rowNum==0){
        	  if(cellNum>5){
        		  rootNode.put(serialLetter2+"cname", getValue(firstHssfRow.getCell(cellNum),hssfWorkbook));
	        	  serialLetter2 = aTob(serialLetter2);
        	  }
          }else{
        	  if(cellNum>5){
        		  map.put(serialLetter+"value",  getValue(hssfCell,hssfWorkbook).toString());
        		  serialLetter =  aTob(serialLetter);
        	  }else{
        		  map.put(getValue(firstHssfRow.getCell(cellNum),hssfWorkbook),  getValue(hssfCell,hssfWorkbook).toString());
        	  }
          }
        }
        if(map.size() >0){
//        	ObjectMapper tempJson = JSONObject.fromObject(map);  
        	 String tmepStr = objectMapper.writeValueAsString(map);          	  
        	 JsonNode tempNode = objectMapper.readTree(tmepStr);  
        	 jsonArray.add(tempNode);
        }
      }
      rootNode.put("objectInfo",jsonArray);//合并sheet到json中
      System.out.println(rootNode);
    }
    return rootNode.toString();
  }
  
  private static String getValue(HSSFCell hssfCell,HSSFWorkbook hssfWorkbook){
	  if(hssfCell != null){
		  FormulaEvaluator evaluator = hssfWorkbook.getCreationHelper().createFormulaEvaluator();  
		  switch (hssfCell.getCellType()) {  
	      case HSSFCell.CELL_TYPE_BOOLEAN:  
	    	  return String.valueOf(hssfCell.getBooleanCellValue());  
	      case HSSFCell.CELL_TYPE_NUMERIC:  
	    	  return String.valueOf(hssfCell.getNumericCellValue());  
	      case HSSFCell.CELL_TYPE_STRING:  
	    	  return String.valueOf(hssfCell.getStringCellValue());  
	      case HSSFCell.CELL_TYPE_BLANK:  
	    	  return "";
	      case HSSFCell.CELL_TYPE_ERROR:  
	    	  return String.valueOf(hssfCell.getErrorCellValue());  
	
	      // CELL_TYPE_FORMULA will never occur  
	      case HSSFCell.CELL_TYPE_FORMULA:   
	          System.out.println(hssfCell.getCellFormula());  
	          evaluator.evaluateFormulaCell(hssfCell);  
	          return String.valueOf(hssfCell.getNumericCellValue());  
		  }  
	  }else {
		  return "";
	  }
	return "";
  }
  private static String aTob(String a){//将大写字母加1 返回。如A>B
	  char b=a.charAt(0);
      String bStr = String.valueOf( (char)((int)b + 1));//a,b,c,d
	  return bStr;
  }
  
  /*//测试代码
  public static void main( String[] args) throws IOException {
	    XlsMain xlsMain = new XlsMain();
	    xlsMain.readXls("D:\\excel\\UserTimeStat.xls");
	  }
*/
}

