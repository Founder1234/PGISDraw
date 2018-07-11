package com.draw.drawMapOnline.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.draw.drawMapOnline.util.FileController;


public class FileDownLoadServlet extends HttpServlet {

	private static final long serialVersionUID = -7214797337192802349L;

	public FileDownLoadServlet() {
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
		FileController FC=new FileController();
		String path=this.getClass().getClassLoader().getResource("/").getPath();
		path=path.substring(1, path.lastIndexOf("WEB-INF"));
		String filePath=request.getParameter("filePath");
		FC.fileDownload(response, path+filePath);
	}
	public void init() throws ServletException {
	}

}
