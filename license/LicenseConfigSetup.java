package com.draw.drawMapOnline.license;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LicenseConfigSetup implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		InputStream lic_is = sc.getResourceAsStream(sc.getInitParameter("LICENSE_FILE"));
		LicenseCheck check = new LicenseCheck();
		check.checkLic(lic_is);
		if(!check.ispass){
			try {
				Thread.currentThread().stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}