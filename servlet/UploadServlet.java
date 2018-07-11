package com.draw.drawMapOnline.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.draw.drawMapOnline.xls.CsvMain;
import com.draw.drawMapOnline.xls.XlsMain;
import com.draw.drawMapOnline.xls.XlsxMain;

 
// Servlet 文件上传  
public class UploadServlet extends HttpServlet  
{  
	private static final long serialVersionUID = 5093828051209066860L;
	private String filePath;    // 文件存放目录  
    private String tempPath;    // 临时文件目录  
 
    // 初始化  
    public void init(ServletConfig config) throws ServletException  
    {  
        super.init(config);  
        // 从配置文件中获得初始化参数  
        filePath = config.getInitParameter("filepath");  
        tempPath = config.getInitParameter("temppath");  
 
        ServletContext context = getServletContext();  
 
        filePath = context.getRealPath(filePath);  
        tempPath = context.getRealPath(tempPath);  
        System.out.println("文件存放目录、临时文件目录准备完毕 ...");  
    }  
      
    // doPost  
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException { 
        res.setContentType("text/plain;charset=gbk");  
        PrintWriter pw = res.getWriter();  
        try{  
            DiskFileItemFactory diskFactory = new DiskFileItemFactory();  
            // threshold 极限、临界值，即硬盘缓存 1M  
            diskFactory.setSizeThreshold(4 * 1024);  
            // repository 贮藏室，即临时文件目录  
            diskFactory.setRepository(new File(tempPath));  
          
            ServletFileUpload upload = new ServletFileUpload(diskFactory);  
            // 设置允许上传的最大文件大小 350k
            upload.setSizeMax(350 * 1024);  
            // 解析HTTP请求消息头  
            List fileItems = upload.parseRequest(req);  
            Iterator iter = fileItems.iterator();  
            while(iter.hasNext())  
            {  
                FileItem item = (FileItem)iter.next();  
                if(item.isFormField())  
                {  
                    System.out.println("处理表单内容 ...");  
                    processFormField(item, pw);  
                }else{  
                    System.out.println("处理上传的文件 ...");  
                    String sheetJson = processUploadFile(item, pw);  
                }  
            }// end while() 
            pw.flush();
            pw.close();  
        }catch(Exception e){  
            System.out.println("使用 fileupload 包时发生异常 ...");  
            e.printStackTrace();  
        }// end try ... catch ...  
    }// end doPost()  
 
 
    // 处理表单内容  
    private void processFormField(FileItem item, PrintWriter pw) throws Exception {  
        String name = item.getFieldName();  
        String value = item.getString();          
        pw.println(name + " : " + value + "\r\n");  
    }  
      
    // 处理上传的文件  
    private String processUploadFile(FileItem item, PrintWriter pw) throws Exception{  
        // 此时的文件名包含了完整的路径，得注意加工一下  
        String filename = item.getName();         
        System.out.println("完整的文件名：" + filename);  
        int index = filename.lastIndexOf("\\");  
        filename = filename.substring(index + 1, filename.length());  
 
        long fileSize = item.getSize();  
 
        if("".equals(filename) && fileSize == 0)  
        {             
            System.out.println("文件名为空 ...");  
            return "文件名为空 ...";  
        }  
 
        File uploadFile = new File(filePath + "/" + filename);  
        item.write(uploadFile);  
//        pw.println(filename + " 文件保存完毕 ...");  
//        pw.println("文件大小为 ：" + fileSize + "\r\n"); 
        String sheetJson = "";
        String fileType = filename.substring(filename.lastIndexOf(".")+1,filename.length()).toLowerCase();
        if("xls".equals(fileType)){
        	sheetJson = XlsMain.readXls(filePath+"/"+filename);
        }else if("xlsx".equals(fileType)){
        	sheetJson = XlsxMain.readXlsx(filePath+"/"+filename);
        }else if("csv".equals(fileType)){
        	sheetJson =CsvMain.csvReader(filePath+"/"+filename);
        }
        pw.println(pickupResInfo("0",sheetJson));
        return sheetJson;
    }  
      
    // doGet  
    public void doGet(HttpServletRequest req, HttpServletResponse res)  
        throws IOException, ServletException  
    {  
        doPost(req, res);  
    }
     
    private String pickupResInfo(String state ,String message) {  
    	ObjectMapper objectMapper = new ObjectMapper();
    	ObjectNode rootNode = objectMapper.createObjectNode();
    	rootNode.put("error", state);  
    	rootNode.put("sheetData", message);  
        return rootNode.toString();  
    }  
} 