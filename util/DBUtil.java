package com.draw.drawMapOnline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库连接工具类
 * 需要传入四个参数 
 * 返回数据连接
 */
public class DBUtil {
	// 连接池集合
	static List<Connection> list = new ArrayList<Connection>();
	
	/**
	 * 获取数据库连接
	 * 默认的数据库配置信息------parameters.properties
	 * @param driver 驱动
	 * @param url 链接
	 * @param user 用户名
	 * @param pwd 密码
	 * @return Connection
	 */
	private static Connection getConnection(String driver,String url,String username,String pwd) { 
		Connection conn = null;
		try {
			Properties pro=PropertiesUtil.getPro();
			String dbdriver = pro.getProperty(driver).trim();
			String dburl = pro.getProperty(url).trim();
			String dbusername = pro.getProperty(username).trim();
			String dbpassword = pro.getProperty(pwd).trim();
			Class.forName(dbdriver);
			conn = DriverManager.getConnection(dburl, dbusername, dbpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 将连接添加到连接池
	 * @return
	 */
	public static Connection getConn() {
		return getConnection(GeneralMarker.ORACLE_DRIVER,GeneralMarker.ORACLE_URL,GeneralMarker.ORACLE_USER,GeneralMarker.ORACLE_PASSWORD);
	}
	public static Connection getConn(String driver,String url,String username,String pwd) {
		return getConnection(driver,url,username,pwd);
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println("------------连接池异常------------------");
				e.printStackTrace();
			}
		}
	}
	
}
