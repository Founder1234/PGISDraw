package com.draw.drawMapOnline.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.draw.drawMapOnline.util.CommonDao;
import com.draw.drawMapOnline.util.GeneralMarker;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1216108521060196337L;
	public FileUploadServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	
			throws ServletException, IOException {
		 doPost(request, response);  
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		 req.setCharacterEncoding("UTF-8");
		 
		 JSONObject root=new JSONObject();
		 String filePath = this.getServletConfig().getServletContext().getRealPath("/")+"upload/";
		 System.out.println(filePath);
		 try{  
	            DiskFileItemFactory diskFactory = new DiskFileItemFactory();  
	            diskFactory.setSizeThreshold(4 * 1024);  
	            ServletFileUpload upload = new ServletFileUpload(diskFactory);  
	            upload.setHeaderEncoding("UTF-8");
	            upload.setSizeMax(15 * 1024 * 1024);  
	            List<?> fileItems = upload.parseRequest(req);  
	            Iterator<?> iter = fileItems.iterator();
	            if(fileItems.size()<1){
        			root.put("state",false); 
        			root.put("result","文件名为空,或文件大小为空~");
	            }else{
	            	while(iter.hasNext()){  
	            		FileItem item = (FileItem)iter.next();  
	            		String filename = item.getName();   
	            		long fileSize = item.getSize();  
	            		if(("".equals(filename)|| null==filename) && fileSize == 0){             
	            			root.put("state",false); 
	            			root.put("result","文件名为空,或文件大小为空~");
	            		}else{
	            			//获取文件中的内容
	            			String fileContent = item.getString();
	            			fileContent=new String(fileContent.getBytes("iso-8859-1"));//解决中文乱码的问题
	            			fileContent = fileContent.trim();
	            			File uploadFile = new File(filePath + filename);  
	            			item.write(uploadFile);  
	            			root.put("state", true);
	            			root.put("result",fileContent);
	            		}  
	            	}
	            	
	            }
	        }catch(Exception e){  
            	root.put("state",false);
            	root.put("result","使用 fileupload 包时发生异常 ...");
	            System.out.println("使用 fileupload 包时发生异常 ...");  
	            e.printStackTrace();
	        }
		 resp.setContentType("text/html; charset=utf-8");
		 PrintWriter out= resp.getWriter();
		 out.write(root.toString());
		 out.flush();
		 out.close();
	}
	public void init() throws ServletException {
	}
}
