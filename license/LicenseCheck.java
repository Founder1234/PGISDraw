package com.draw.drawMapOnline.license;

import java.io.InputStream;

import com.easymap.srvlic.client.ILicense;
import com.easymap.srvlic.client.LicenseClient;
import com.easymap.srvlic.client.LicenseLoadException;
import com.easymap.srvlic.client.LicenseUtil;
import com.easymap.srvlic.client.LicenseValidatingException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LicenseCheck {
	private static final Logger logger = LogManager.getLogger("com.draw.drawMapOnline.license.LicenseCheck");
	public boolean ispass;
	public String reason;
	
	public void checkLic(InputStream lic_is){
		if(lic_is==null){
			this.ispass = false;
			this.reason = "许可文件不存在";
			logger.warn("许可文件不存在, 许可认证失败");
			return;
		}
		try {
			LicenseClient licClient = new LicenseClient();
			ILicense lic = licClient.getLicense(lic_is);
			LicenseUtil lu = new LicenseUtil();
			this.ispass = lu.isValid(lic.getLicenseRequest(), "PGISDraw");
			logger.warn("许可认证成功");
		} catch (LicenseLoadException e) {
			this.ispass = false;
			this.reason = "许可认证错误";
			logger.warn("许可认证失败，请检查许可文件");
			e.printStackTrace();
		} catch (LicenseValidatingException e) {
			this.ispass = false;
			this.reason = "许可认证错误";
			logger.warn("许可认证失败，请检查许可文件");
			e.printStackTrace();
		}
	}
}
