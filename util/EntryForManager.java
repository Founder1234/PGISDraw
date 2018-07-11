package com.draw.drawMapOnline.util;

import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.easymap.management.api.DataManager;
import com.easymap.management.api.UserManager;
import com.easymap.management.data.FieldProperty;
import com.easymap.management.data.ThemeTree;
import com.easymap.management.user.FunctionPrivilege;
import com.easymap.management.user.User;

public class EntryForManager {
	private static Logger logger = Logger.getLogger(EntryForManager.class);
	public static UserManager userManager =initEzmanager();
	public static DataManager dataManager  = initEzDataManager();
	public static String serviceUrl = null;
	public static UserManager initEzmanager(){
		Properties pro=PropertiesUtil.getPro();
		serviceUrl = pro.getProperty("EzManagerUrl").trim();
		UserManager userManagerInstance = new UserManager(serviceUrl);
		dataManager = new DataManager(serviceUrl);
		return userManagerInstance;
	}
	public static DataManager initEzDataManager(){
		DataManager dataManagerInstance = new DataManager(serviceUrl);
		dataManager = new DataManager(serviceUrl);
		return  dataManagerInstance;
	}
	/**
	 *   通过传入如下的参数，返回专题列表
	 * @param userId
	 * @param ezSpatialCode
	 * @param systemName
	 * @param moduleName
	 * @param functionName
	 * @return  专题列表
	 */
	public static String getThemeListForPgis(String userId ,String ezSpatialCode,String systemName,String moduleName,String functionName ){
		List<ThemeTree> list = null;
		String resultStr = null;
		try {
			list = userManager.getThemePrivilege(userId ,ezSpatialCode,systemName,moduleName,functionName );
		} catch (Exception e) {
			logger.error("运维系统异常：运维系统路径配置不对 或专题权限未分配~");
			e.printStackTrace();
		}
		if(list ==null){
			resultStr = "运维系统异常：运维系统路径配置不对 或专题权限未分配~";
		}else{
			JSONArray jsonArray = JSONArray.fromObject(list);
			resultStr = jsonArray.toString(); 
		}
		return resultStr;
	}
	/**
	 * 通过传入的表名获取ezspatial中的字段信息
	 * @param ezSpatialCode
	 * @param tableNames  可以是单个表，可以是多个表
	 * @return 字段信息
	 */
	public String getTablesCloumns(String ezSpatialCode,String tableNames){
		String resultStr = null;
		JSONObject jsonObject = new JSONObject();
		String[] tablesArray = tableNames.split(",");
		for (int i = 0; i < tablesArray.length; i++) {
			List<FieldProperty> list = null;
			String tempTable = tablesArray[i].toUpperCase();
			try {
				list = dataManager.getFieldProperty(tempTable,ezSpatialCode);
				jsonObject.put(tempTable,list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultStr = jsonObject.toString();
//		System.out.println(resultStr);
		return resultStr;
	}
	/**
	 * 根据用户名，系统名称，获取系统下的功能更权限
	 * @param userId
	 * @param systemName
	 * @return  JSONObject
	 */
	public String getDomainPrivilege(String userId,String systemName){
		JSONObject result = new JSONObject();
		List<FunctionPrivilege> list =null;
		 JSONArray funcitonList = null ;
		 JSONObject userObject =null;
		try {
			 list = userManager.getFunctionPrivilege(userId, systemName);
			 User user= userManager.getUser(userId);
			 userObject = JSONObject.fromObject(user);
			 funcitonList = JSONArray.fromObject(list);
		} catch (Exception e) {
			result.put("state", "运维配置出错或查询出错!");
			e.printStackTrace();
		}
		
		result.put("state","true");
		result.put("data",funcitonList);
		result.put("user",userObject.toString());
		return result.toString();
	}
	
	public String validUser(String userId,String Pwd,String sysName) {
		JSONObject result = new JSONObject();
		try {
			if(userManager.validUser(userId, Pwd)){
				if(userManager.hasFunctionPrivilege(userId, sysName)){
					 User user = userManager.getUser(userId);
					 JSONObject userObject = JSONObject.fromObject(user);
					result.put("state", "succeses");
					result.put("user", userObject.toString());
				}else{
					result.put("state", false);
					result.put("message", "此用户没有访问"+sysName+"的权限");
				}
			}else{
				result.put("state", false);
				result.put("message", "用户名或密码错误！");
			}
		} catch (Exception e) {
			result.put("state", false);
			result.put("message", "查询运维出错！请稍后重试！");
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public String getUser(String userid){
		JSONObject result = new JSONObject();
		try {
			 User user = userManager.getUser(userid);
			 JSONObject userObject = JSONObject.fromObject(user);
			result.put("state", "succeses");
			result.put("user", userObject.toString());
		} catch (Exception e) {
			result.put("state", false);
			result.put("message", "查询运维出错！请稍后重试！");
			e.printStackTrace();
		}
		return result.toString();
	}
	public static void main(String[] args) {
		EntryForManager entryForManager = new EntryForManager();
		//entryForManager.getThemeListForPgis("admin","GIS340000000000","PGIS_Portal","综合查询","综合查询");
		//entryForManager.getTablesCloumns("GIS340000000000","CS_MT_FWCS_PT".toUpperCase());
//		JSONObject result = entryForManager.getDomainPrivilege("admin","PGIS2.0");
//		System.out.println(result.toString());
	}
}
