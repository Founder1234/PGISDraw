package com.draw.drawMapOnline.listener;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.draw.drawMapOnline.util.GeneralMarker;
import com.draw.drawMapOnline.util.PropertiesUtil;

/**
 * Application Lifecycle Listener implementation class Initialize
 *
 */
public class Initialize implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public Initialize() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

//    	initLayerName(arg0);
    	//从数据库读取数据保存在AreaMap中
    	initStructureMap(arg0);

    }
		
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
    public void initStructureMap(ServletContextEvent sce){
    	//从数据库读取数据保存在AreaMap中
    	String SystemConfig = GeneralMarker.PROPERTIES_URL;
		ServletContext sc = sce.getServletContext();
    	Properties propDBConfig = new Properties();
    	try {
			propDBConfig.load(sc.getResourceAsStream(SystemConfig));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	PropertiesUtil.setPro(propDBConfig);
    }
}
