package com.draw.drawMapOnline.xls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.csvreader.CsvReader;

public class CsvMain {

	public static String csvReader(String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		String fileCharset=getFilecharset(filePath);
		CsvReader cr = new CsvReader(is, Charset.forName(fileCharset));
		int index = 1;
		ArrayList<String> heardList = new ArrayList<String>();
		JSONObject root = new JSONObject();
		JSONArray arr = new JSONArray();
		while (cr.readRecord()) {
			int tabLen = cr.getColumnCount() > 12 ?  11 :cr.getColumnCount();
			String serialLetter = "A", serialLetter2 = "A";
			JSONObject ele = new JSONObject();
			for (int i = 0; i < tabLen; i++) {
				String str = cr.get(i);
				if (index == 1) {
					if(!StringUtils.isEmpty(str)){
						heardList.add(str);
						if (i > 5) {
							root.put(serialLetter + "cname", str);
							serialLetter = aTob(serialLetter);
						}
					}
					
				} else {
					if(i>heardList.size()-1)break;
					String titleName=heardList.get(i);
					if (i > 5) {
						if(!StringUtils.isEmpty(titleName)){
							ele.put(serialLetter2 + "value", str);
							serialLetter2 = aTob(serialLetter2);
						}
					} else {
						ele.put(titleName, str);
					}
				}
			}
			if(index!=1){
				arr.add(ele);
			}
			index++;
		}
		System.out.println("总共："+(index-2)+"条数据！");
		root.put("objectInfo", arr);
		return root.toString();
	}

	private static String aTob(String a) {// 将大写字母加1 返回。如A>B
		char b = a.charAt(0);
		String bStr = String.valueOf((char) ((int) b + 1));// a,b,c,d
		return bStr;
	}

	private static String getFilecharset(String filePath) {
		File sourceFile=new File(filePath);
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	public static void main(String[] args) {
		try {
			String a = CsvMain.csvReader("C:\\Users\\Founder-YC\\Desktop\\56789.csv");
			System.out.println(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
