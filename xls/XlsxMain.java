package com.draw.drawMapOnline.xls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
/**
 * @TODO 根据路径解析xlsx文件
 */
public class XlsxMain {

  public static String readXlsx(String filePath) throws IOException{
//	InputStream is = new FileInputStream(filePath);
    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(filePath);
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode rootNode = objectMapper.createObjectNode();//根节点
    ArrayNode jsonArray = objectMapper.createArrayNode();
    // 循环工作表Sheet
    for(int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++){
      XSSFSheet xssfSheet = xssfWorkbook.getSheetAt( numSheet);
      if(xssfSheet == null){
        continue;
      }
      Map<String, String> map = new HashMap<String, String>();  
      // 循环行Row 
      for(int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++ ){
        XSSFRow xssfRow = xssfSheet.getRow( rowNum);
        XSSFRow firstXssfRow = xssfSheet.getRow(0);
        if(xssfRow == null){
          continue;
        }
        String serialLetter = "A",serialLetter2 ="A";
        int cellLength = 0;//设置列数限制
        if(firstXssfRow.getLastCellNum()>11){
        	cellLength = 11;
        }else{
        	cellLength = firstXssfRow.getLastCellNum();
        }
        // 循环列Cell   
        for(int cellNum = 0; cellNum <cellLength; cellNum++){
          XSSFCell xssfCell = xssfRow.getCell( cellNum);
          /*if(xssfCell == null){
            continue;
          }*/
          if(rowNum==0){
        	  if(cellNum>5){
        		  rootNode.put(serialLetter2+"cname", getValue(firstXssfRow.getCell(cellNum)));
	        	  serialLetter2 = aTob(serialLetter2);
        	  }
          }else{
        	  if(cellNum>5){
        		  map.put(serialLetter+"value",  getValue(xssfCell).toString());
        		  serialLetter =  aTob(serialLetter);
        	  }else{
        		  map.put(getValue(firstXssfRow.getCell(cellNum)),  getValue( xssfCell).toString());
        	  }
          }
        }
        if(map.size() >0){
        	String tmepStr = objectMapper.writeValueAsString(map);          	  
	       	JsonNode tempNode = objectMapper.readTree(tmepStr);  
	       	jsonArray.add(tempNode);
       }
      }
      rootNode.put("objectInfo",jsonArray);
//      System.out.println(rootNode);
    }
    return rootNode.toString();
  }
  
  @SuppressWarnings("static-access")
  private static String getValue(XSSFCell xssfCell){
	  if(xssfCell==null){
			return "";
	}else  if(xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN){
      return String.valueOf( xssfCell.getBooleanCellValue());
    }else if(xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC){
      return String.valueOf( xssfCell.getNumericCellValue());
    }else{
      return String.valueOf( xssfCell.getStringCellValue());
    }
/*
	  CELL_TYPE_NUMERIC = 0;
	  CELL_TYPE_STRING = 1;
	  CELL_TYPE_FORMULA = 2;
	  CELL_TYPE_BLANK = 3;
	  CELL_TYPE_BOOLEAN = 4;
	  CELL_TYPE_ERROR = 5;*/

  }
  private static String aTob(String a){//将大写字母加1 返回。如A>B
	  char b=a.charAt(0);
      String bStr = String.valueOf( (char)((int)b + 1));//a,b,c,d
	  return bStr;
  }
  /*
  //测试代码
  public static void main( String[] args) throws IOException {
    XlsxMain xlsxMain = new XlsxMain();
    xlsxMain.readXlsx("D:\\excel\\xlsx_test.xlsx");
  }*/
}