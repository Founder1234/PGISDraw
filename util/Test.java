package com.draw.drawMapOnline.util;

import com.easymap.management.api.UserManager;

public class Test {

	public static void main(String[] args) {
		try {
			UserManager um = new UserManager("http://172.25.16.106:8080/EzManager6");
			System.out.println(um.validUser("admin", "admin123"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
