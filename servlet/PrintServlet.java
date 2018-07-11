package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.draw.drawMapOnline.util.CResourceUtil;

public class PrintServlet extends HttpServlet {
	private static final long serialVersionUID = 6963179223648308024L;
	public PrintServlet() {
		super();
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		CResourceUtil cru=new CResourceUtil();
		String url=request.getParameter("url");
		String params=request.getParameter("params");
		System.out.println(url+"======"+params);
		String pngName = CResourceUtil.doPost(url,params);
		PrintWriter out;
		try
		{
			out = response.getWriter();
			System.out.println("json格式为："+pngName);
			out.print(pngName);
			out.flush();
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 doPost(request, response);  
	}
	public void init() throws ServletException {
	}

}
