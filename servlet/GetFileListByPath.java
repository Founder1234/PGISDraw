package com.draw.drawMapOnline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.draw.drawMapOnline.util.HandleFileListByPath;


public class GetFileListByPath extends HttpServlet {
	private static final long serialVersionUID = -6148646152183302287L;
	public GetFileListByPath() {
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
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out= response.getWriter();
		String filePath = request.getParameter("filePath");
		String realPath = this.getServletConfig().getServletContext().getRealPath("/")+filePath;
		String result = HandleFileListByPath.pickUpFileList(realPath);
		out.write(result.toString());
		out.flush();
		out.close();
	}
	public void init() throws ServletException {
	}

}
