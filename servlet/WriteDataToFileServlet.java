package com.draw.drawMapOnline.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.draw.drawMapOnline.util.CommonDao;
import com.draw.drawMapOnline.util.DBUtil;
import com.draw.drawMapOnline.util.FileController;
import com.draw.drawMapOnline.util.GeneralMarker;

import net.sf.json.JSONObject;


public class WriteDataToFileServlet extends HttpServlet {
	private static final long serialVersionUID = 6045570942137003734L;
	CommonDao commonDao=new CommonDao();
	
	public WriteDataToFileServlet() {
		super();
	}
	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String data = request.getParameter("data");
		JSONObject orginParams=new JSONObject();
		orginParams.put("fields", "");
		orginParams.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		orginParams.put("where", "pid='"+data+"'");
		JSONObject list=commonDao.getInfoByParam(orginParams);
		String jsonList=list.toString();
		String dataParam = jsonList.substring(jsonList.split(":")[0].length()+1+jsonList.split(":")[1].length()+1+jsonList.split(":")[2].length()+1);
		
		String path=this.getClass().getClassLoader().getResource("/").getPath();
		path=path.substring(1, path.lastIndexOf("WEB-INF"));
		String filePath=request.getParameter("filePath");
		String charset = request.getParameter("charset");
//				String filePath="upload/favJson.pgis";
		FileController.writeDataToFile(path+filePath,dataParam,charset);
		response.setContentType("text/html; charset=utf-8");
		JSONObject result = new JSONObject();
		result.put("satus", true);
		PrintWriter out= response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}
	
	public void init() throws ServletException {
	}
}
