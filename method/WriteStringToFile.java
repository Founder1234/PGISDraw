package com.draw.drawMapOnline.method;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * 将字符串流写入文件中
 */
public class WriteStringToFile {
	public static String writeToFile(String desc,String str){
//		FileWriter writer=new FileWriter("D:/Java/workspaces/PGIS_Portal_anhui/WebRoot/js/copyInitConfig.js");
		try {
			File f = new File(desc);
			if(!f.exists()){
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(str);
			writer.close();
			return "success";
		} catch (IOException e) {
				e.printStackTrace();
				return e.getCause().toString();
		}
		/*FileWriter writer;
		try {
			writer = new FileWriter(desc);
			writer.write(str);
			writer.flush();
			writer.close();
			return "success";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getCause().toString();
		}*/
	}
}
