package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.draw.drawMapOnline.util.CommonDao;
import com.draw.drawMapOnline.util.GeneralMarker;

public class DataHandleServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4708112898916884617L;

	public DataHandleServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@SuppressWarnings("null")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out= response.getWriter();
		CommonDao dao=new CommonDao();
		String params=request.getParameter("params");
		JSONObject jsonParams=JSONObject.fromObject(params);
		JSONObject data=null;
		
		if(jsonParams.getString("type").equals(GeneralMarker.QUERY_TYPE)){
			data=toQueryDataResult(dao,jsonParams);
		}else if(jsonParams.getString("type").equals(GeneralMarker.ADD_TYPE)){
			data=toAddDataResult(dao,jsonParams);
		}else if(jsonParams.getString("type").equals(GeneralMarker.DELETE_TYPE)){
			data=toDeleteDataResult(dao,jsonParams);
		}else if(jsonParams.getString("type").equals(GeneralMarker.UPDATE_TYPE)){
			data=toUpdateDataResult(dao,jsonParams);
		}else if(jsonParams.getString("type").equals(GeneralMarker.SHARE_TYPE)){
			data=toShareDataResult(dao,jsonParams);
		}
		out.write(data.toString());
		out.flush();
		out.close();
	}

	private JSONObject toUpdateDataResult(CommonDao dao, JSONObject jsonParams) {
		JSONObject list=new JSONObject();
		JSONObject resultList=new JSONObject();
		ArrayList<?> fieldList=dao.getColumns(GeneralMarker.TABLE_NAME_ZXZT);
		String fields="";
		String value="";
		Date thisTime=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat(GeneralMarker.DATE_TYPE);
		String times=sdf.format(thisTime);
		for(int i=0;i<fieldList.size();i++){
			String colName=(String) fieldList.get(i);
			fields+=","+colName+"=?";
		}
		jsonParams.put("CREATETIME", times);
		fields=fields.replaceFirst(",", "");
		value="PID='"+jsonParams.getString("PID")+"'";
		list.put("fields", fields);
		list.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		list.put("where", value);
		list.put("jsonParams", jsonParams);
		resultList=dao.updateInfoByClobParam(list);
		return resultList;
	}
	private JSONObject toShareDataResult(CommonDao dao, JSONObject jsonParams) {
		JSONObject list=new JSONObject();
		JSONObject resultList=new JSONObject();
		String fields="";
		String value="";
		fields="shared='"+jsonParams.getString("SHARED")+"'";
		value="PID='"+jsonParams.getString("PID")+"'";
		list.put("fields", fields);
		list.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		list.put("where", value);
		resultList=dao.updateInfoByParam(list);
		return resultList;
	}

	private JSONObject toDeleteDataResult(CommonDao dao, JSONObject jsonParams) {
		JSONObject list=new JSONObject();
		JSONObject resultList=new JSONObject();
		list.put("fields", "");
		list.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		list.put("where", jsonParams.getString("where"));
		resultList=dao.deleteInfoByParam(list);
		return resultList;
	}

	private JSONObject toAddDataResult(CommonDao dao, JSONObject jsonParams) {
		JSONObject list=new JSONObject();
		JSONObject resultList=new JSONObject();
		ArrayList fieldList=dao.getColumns(GeneralMarker.TABLE_NAME_ZXZT);
		String fields="";
		String value="";
		Date thisTime=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat(GeneralMarker.DATE_TYPE);
		String times=sdf.format(thisTime);
		for(int i=0;i<fieldList.size();i++){
			String colName=(String) fieldList.get(i);
			fields+=","+colName;
			value+=",?";
		}
		jsonParams.put("CREATETIME", times);
		fields=fields.replaceFirst(",", "");
		value=value.replaceFirst(",", "");
		list.put("fields", fields);
		list.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		list.put("where", value);
		list.put("jsonParams", jsonParams);
		resultList=dao.insertInfoByClobParam(list);
		return resultList;
	}

	private JSONObject toQueryDataResult(CommonDao dao, JSONObject jsonParams) {
		JSONObject list=new JSONObject();
		JSONObject resultList=new JSONObject();
		ArrayList fieldList=dao.getColumns(GeneralMarker.TABLE_NAME_ZXZT);
		String fields="";
		for(int i=0;i<fieldList.size();i++){
			fields+=","+fieldList.get(i);
		}
		fields=fields.replaceFirst(",", "");
		list.put("fields", fields);
		list.put("tableName", GeneralMarker.TABLE_NAME_ZXZT);
		list.put("where", jsonParams.getString("where"));
		try {
			resultList=dao.getInfoByParam(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public void init() throws ServletException {
		
	}

}
