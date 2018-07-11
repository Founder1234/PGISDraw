package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.draw.drawMapOnline.util.CommonDao;
import com.draw.drawMapOnline.util.GeneralMarker;

public class QueryLayerDataServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QueryLayerDataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out= response.getWriter();
		CommonDao dao=new CommonDao();
		String params=request.getParameter("params");
		JSONObject jsonParams=JSONObject.fromObject(params);
		JSONObject data=null;
		
		if(jsonParams.getString("type").equals("getCols")){
			jsonParams.put("driver", GeneralMarker.LAYER_DRIVER);
			jsonParams.put("url", GeneralMarker.LAYER_URL);
			jsonParams.put("username", GeneralMarker.LAYER_USER);
			jsonParams.put("pwd", GeneralMarker.LAYER_PASSWORD);
			data=dao.getColumns(jsonParams);
		}
		out.write(data.toString());
		out.flush();
		out.close();
	}

	
	public void init() throws ServletException {
		// Put your code here
	}

}
