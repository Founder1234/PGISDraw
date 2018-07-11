package com.draw.drawMapOnline.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.json.JSONObject;

public class HandleFileListByPath {
	public static String pickUpFileList(String fileDirec) throws IOException {
		JSONObject fileJson = new JSONObject();
		ArrayList<String> array = new ArrayList<String>();
		File real = new File(fileDirec);
		File[] fileNames = real.listFiles();//所有分类文件夹
		if(fileNames !=null){
			for (File file2 : fileNames) {//单个文件夹
				String type = file2.getName();
				if(file2.isDirectory()){
					array =new ArrayList<String>();
					File[] files = file2.listFiles();//文件
					for (File realFile : files) {
						array.add(type+"/"+realFile.getName());
					}
					fileJson.put(type,array );
				}
			}
		}
		return fileJson.toString();
	}
	public static void main(String[] args) throws IOException {
//		String file = "D:\\PGISOnline\\PGISMap\\WebRoot\\image\\fav";
		String file = "WebRoot\\image\\fav";
		String a = pickUpFileList(file);
		System.out.println(a);
	}
}
