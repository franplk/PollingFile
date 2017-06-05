/**
 * 
 */
package cn.fy.ftpupload;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author {康培亮/AB052634}
 *
 */
public class FtpConfig {
	
	private static String configPath = "config/ftpconfig.properties";
	
	private static Properties prop;
	
	//属性列表
	static {
        try {
        	prop = new Properties();
        	InputStream configInput = FTPUtil.class.getClassLoader().getResourceAsStream(configPath);
            prop.load(configInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static String getConfig (String keyName) {
		return prop.getProperty(keyName);
	}

	public static void main(String[] args) {
		String name = FtpConfig.getConfig("ftp.login.name");
		System.out.println("ftp.login.name = " + name);
	}

}
