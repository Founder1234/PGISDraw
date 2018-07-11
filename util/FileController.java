package com.draw.drawMapOnline.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
  
public class FileController {  
  
  
    public void fileDownload(HttpServletResponse response,String filePath){  
        //获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载  
        //创建文件
        File file = new File(filePath);  
        //获取文件名
        String fileName=file.getName();
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("multipart/form-data");  
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)  
        response.setHeader("Content-Disposition", "attachment;fileName="+fileName);  
        ServletOutputStream out;  
        //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)  
        try {  
            FileInputStream inputStream = new FileInputStream(file);  
            //3.通过response获取ServletOutputStream对象(out)  
            out = response.getOutputStream();  
//            BufferedReader br=new BufferedReader(new FileReader(filePath),"UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"GBK"));  
            String line=null;
            while((line=br.readLine())!=null){
            	 //4.写到输出流(out)中  
                out.write(line.getBytes());  
            }
            inputStream.close();  
            out.close();  
            out.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static void  writeDataToFile(String filePath,String data,String charset) throws IOException{
    	File txt=new File(filePath);
    	if(!txt.exists()){
 		   txt.createNewFile();
 		}
 		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(txt),charset);      
         BufferedWriter writer=new BufferedWriter(write);          
         writer.write(data);  
         writer.close();
    }
 
  
}