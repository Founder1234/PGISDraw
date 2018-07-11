package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.draw.drawMapOnline.method.HandlePropertiesInfo;
import com.draw.drawMapOnline.method.WriteStringToFile;

import net.sf.json.JSONObject;
/**
 * 系统配置的servlet
 */
public class SysConfigServletDraw extends HttpServlet {
	private static final long serialVersionUID = -121573824485331807L;
	 public SysConfigServletDraw() {
	        super();
	    }
	   
	   public void doGet(HttpServletRequest request, HttpServletResponse response) 	throws ServletException, IOException {
			this.doPost(request, response);
		}
	   
	   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		   request.setCharacterEncoding("utf-8");
		   String method = request.getParameter("method");
		   JSONObject json = new JSONObject();
		  
		   if (method.equals("writeToFile")) {
			   String data = request.getParameter("fields");
			   String desc = request.getParameter("desc");
			   String result = WriteStringToFile.writeToFile(desc, data);
			   json.put("state",result);
		  }else if(method.equals("setPropertyByParam")){
			  String dataResult = request.getParameter("data");
//			  System.out.println(dataResult);
			  String fileAddress = this.getServletConfig().getServletContext().getRealPath("/")+"WEB-INF/conf/pgisConfig.properties";
			  json =  HandlePropertiesInfo.setPropertyByParam(dataResult,fileAddress);
		  }else if(method.equals("getPropertyByParam")){
			  String pgisDataSource =request.getParameter("pgisDataSource");
			  String driver = request.getParameter("driver");
			  String address = request.getParameter("address");
			  String username = request.getParameter("username");
			  String password = request.getParameter("password");
			  String EzManagerUrl = request.getParameter("EzManagerUrl");
			  String orginAddress = this.getServletConfig().getServletContext().getRealPath("/");
			  String fileAddress = orginAddress+"WEB-INF/conf/pgisConfig.properties";
			 
			  List<String> params = new ArrayList<String>();
			  params.add(pgisDataSource);
			  params.add(driver);
			  params.add(address);
			  params.add(username);
			  params.add(password);
			  params.add(EzManagerUrl);
			  params.add(orginAddress);
				try {
					json =  HandlePropertiesInfo.getPropertyByParam(params,fileAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
		  	}
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out= response.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();
		}
}
