package com.draw.drawMapOnline.method;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;

import net.sf.json.JSONObject;
/**
 * 处理properties文件的类，进行查询和修改
 */
public class HandlePropertiesInfo {
	public static  JSONObject getPropertyByParam(List<String> array,String address) throws Exception{
		Properties prop = new Properties();// 属性集合对象 
		FileInputStream fis = new FileInputStream(address);// 属性文件输入流 
		prop.load(fis);// 将属性文件流装载到Properties对象中 
		fis.close();// 关闭流 
		JSONObject result = new JSONObject();
		for (int i = 0; i <array.size()-1; i++) {
			String array_type = array.get(i);
			String value = prop.getProperty(array_type);
			result.put(array_type, value);
		}
		result.put("orginAddress",array.get(array.size()-1));
		return result;
	}
	
	public static JSONObject setPropertyByParam(String data,String fileAddress) throws IOException{
		
		JSONObject dataSource = JSONObject.fromObject(data);
//		System.out.println(dataSource);
		JSONObject result = new JSONObject();
		Properties prop = new Properties();// 属性集合对象 
		FileInputStream fis = new FileInputStream(fileAddress);// 属性文件输入流 
		prop.load(fis);// 将属性文件流装载到Properties对象中 
		fis.close();// 关闭流 
		// 修改属性值 
		prop.setProperty("EzManagerUrl", (String) dataSource.get("EzManagerUrl")); 
		prop.setProperty("pgisDriver", (String) dataSource.get("driver")); 
		prop.setProperty("pgisUrl", (String) dataSource.get("address")); 
		prop.setProperty("pgisUser", (String) dataSource.get("username")); 
		prop.setProperty("pgisPwd", (String) dataSource.get("password")); 
//		prop.setProperty("zzjgTable", (String) dataSource.get("zzjgTable")); 
//		prop.setProperty("zzjgIpTable", (String) dataSource.get("zzjgIpTable")); 
		// 文件输出流 
		FileOutputStream fos = new FileOutputStream(fileAddress); 
		// 将Properties集合保存到流中 
		prop.store(fos, "update for dataSource"); 
		fos.close();// 关闭流 
		result.put("state", "success");
		return result;

	}
	public static JSONObject setResourceByParam(String data,String fileAddress) throws IOException{
		
		JSONObject dataSource = JSONObject.fromObject(data);
		JSONObject result = new JSONObject();
		Properties prop = new Properties();// 属性集合对象 
		FileInputStream fis = new FileInputStream(fileAddress);// 属性文件输入流 
		prop.load(fis);// 将属性文件流装载到Properties对象中 
		fis.close();// 关闭流 
		// 修改属性值 
		prop.setProperty("driver", (String) dataSource.get("driver")); 
		prop.setProperty("url", (String) dataSource.get("url")); 
		prop.setProperty("username", (String) dataSource.get("username")); 
		prop.setProperty("password", (String) dataSource.get("password")); 
		prop.setProperty("layerTableDriver", (String) dataSource.get("layerTableDriver")); 
		prop.setProperty("layerTableUrl", (String) dataSource.get("layerTableUrl")); 
		prop.setProperty("layerTableUsername", (String) dataSource.get("layerTableUsername")); 
		prop.setProperty("layerTablePassword", (String) dataSource.get("layerTablePassword")); 
		prop.setProperty("def_id", (String) dataSource.get("def_id")); 
		// 文件输出流 
		FileOutputStream fos = new FileOutputStream(fileAddress); 
		// 将Properties集合保存到流中 
		prop.store(fos, "update for dataSource"); 
		fos.close();// 关闭流 
		result.put("state", "success");
		return result;

	}
}
