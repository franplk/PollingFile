package cn.fy.ftpupload;

/**
 * @author {康培亮/AB052634}
 *
 */
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtil {

	private String serverPath;
	private String serverAddr;
	private int serverPort;
	private String username;
	private String password;
	
	private FTPClient ftpClient;

	public FTPUtil() {
		String portNum = FtpConfig.getConfig("polling.timespan");
		if(null == portNum) {
			this.serverPort = 21;
		} else {
			this.serverPort = Integer.parseInt(portNum);
		}
		this.serverPath = FtpConfig.getConfig("ftp.conn.path");
		this.serverAddr = FtpConfig.getConfig("ftp.conn.addr");
		this.username = FtpConfig.getConfig("ftp.login.name");
		this.password = FtpConfig.getConfig("ftp.login.pawd");
	}
	
	/**
	 * FTP服务器连接
	 */
	public void connect() {
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(serverAddr, serverPort);
			ftpClient.login(username, password);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}
			ftpClient.changeWorkingDirectory(serverPath);
		} catch (Exception e) {
			throw new RuntimeException("FTP 连接失败");
		}
	}

	/**
	 * @param file 上传文件夹
	 */
	public void uploadDirectory(File file) throws Exception {
		String dirPath = file.getPath();
		String dirName = file.getName();
		ftpClient.makeDirectory(dirName);
		ftpClient.changeWorkingDirectory(dirName);
		String[] fileNameList = file.list();
		for (String fileName : fileNameList) {
			File childFile = new File(dirPath + "\\" + fileName);
			if (childFile.isDirectory()) {
				uploadDirectory(childFile);
				ftpClient.changeToParentDirectory();
			} else {
				uploadFile(childFile);
			}
		}
	}

	/**
	 * @param file 上传的文件（不是文件夹）
	 */
	public void uploadFile(File file) throws Exception {
		FileInputStream input = new FileInputStream(file);
		ftpClient.storeFile(file.getName(), input);
		input.close();
	}

	/**
	 * 关闭FTP连接
	 */
	public void close() throws Exception {
		if (null != ftpClient) {
			ftpClient.disconnect();
		}
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
}
