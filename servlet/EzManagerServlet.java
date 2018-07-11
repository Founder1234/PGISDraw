package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.draw.drawMapOnline.util.EntryForManager;

public class EzManagerServlet extends HttpServlet{
	private static final long serialVersionUID = -7556159057279919109L;

public EzManagerServlet() {
        super();
    }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) 	throws ServletException, IOException {
		this.doPost(request, response);
	}
   
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   EntryForManager entryForManager = new EntryForManager();
	   request.setCharacterEncoding("utf-8");
		String method = request.getParameter("method");
		String result = null;
		if(method.equals("getThemeListForPgis")){//初始化查询专题列表
			String params = request.getParameter("fields");
			JSONObject param = JSONObject.fromObject(params);
			result = EntryForManager.getThemeListForPgis(param.getString("userid"), param.getString("ezSpatialCode"), param.getString("systemName"),param.getString("moduleName"),param.getString("functionName"));
		}else if(method.equals("getTablesCloumns")){
			String params = request.getParameter("fields");
			JSONObject param = JSONObject.fromObject(params);
			result = entryForManager.getTablesCloumns(param.getString("ezSpatialCode"),param.getString("tablenames"));
		}else if(method.equals("validUser")){
			String params = request.getParameter("params");
			JSONObject param = JSONObject.fromObject(params);
				result = entryForManager.validUser(param.getString("userid"),param.getString("password"),param.getString("systemName"));
		}else if(method.equals("getDomainPrivilege")){
		   String params = request.getParameter("params");
		   JSONObject param = JSONObject.fromObject(params);
		   result = entryForManager.getDomainPrivilege(param.getString("userid"),param.getString("systemName"));
	   }else if(method.equals("getUser")){
		   String params = request.getParameter("params");
		   JSONObject param = JSONObject.fromObject(params);
		   result = entryForManager.getUser(param.getString("userid"));
	   }
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out= response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

}
