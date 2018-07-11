package com.draw.drawMapOnline.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class CResourceUtil {
	
	public static String doPost(String url, String params) {
		BufferedWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			out = new BufferedWriter(new OutputStreamWriter(
					conn.getOutputStream(), "utf-8"));
			if (params != null)
				out.write(params);
			out.flush();
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	
	public static InputStream getStream(String url, String params) {
		BufferedWriter out = null;
		InputStream is = null;
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();

			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			out = new BufferedWriter(new OutputStreamWriter(
					conn.getOutputStream(), "utf-8"));
			if (params != null)
				out.write(params);
			out.flush();
			is = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return is;
	}
	
	public static void main(String[] args) {
		String json = "{\"Request\":\"PrintImage\","
				+"\"PrintSet\":{\"Boundary\":\"115.48632,39.27734,117.08789,40.53515\",\"MapLevel\":\"11\",\"DPI\":\"300\",\"Format\":\"png\","
				+"\"MapService\":{\"Server\":\"172.18.70.124\",\"Port\":\"8080\",\"ServiceName\":\"EzServer66/Maps/slTDT\",\"WMTS\":\"1\",\"Layers\":\"vec,cva\"},"
			+"\"Graphic\":{\"Marks\":[{\"ID\":\"\",\"Name\":\"\",\"X\":\"116.37402\",\"Y\":\"39.9121\",\"Style\":{\"SymType\":\"PICTURE\",\"Symbolization\":\"PictureFill\",\"Picture\":{\"Name\":\"person_track.png\",\"Transparent\":\"0\"}}},"
			+"{\"ID\":\"\",\"Name\":\"\",\"X\":\"116.57402\",\"Y\":\"39.7121\",\"Style\":{\"SymType\":\"PICTURE\",\"Symbolization\":\"PictureFill\",\"Picture\":{\"Name\":\"spatialQuery_aj.png\",\"Transparent\":\"0\"}}}]}}}";
		String str1 = CResourceUtil.doPost("http://172.18.69.28:8002/on", json);
		System.out.println(str1);
	}
}
