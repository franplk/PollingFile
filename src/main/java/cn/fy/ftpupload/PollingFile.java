/**
 * 
 */
package cn.fy.ftpupload;

import java.io.File;

/**
 * @author {康培亮/AB052634}
 *
 */
public class PollingFile {

	private long pollSpan; // 轮询时间间隔
	private String directoryName; // 轮询目录

	/**
	 * 构造函数初始化
	 */
	public PollingFile() {
		String timeSpan = FtpConfig.getConfig("polling.timespan");
		if(null == timeSpan) {
			this.pollSpan = 60;
		} else {
			this.pollSpan = Long.parseLong(timeSpan);
		}
		this.directoryName = FtpConfig.getConfig("polling.directory");
	}
	
	/**
	 * 运行
	 */
	public void run() throws Exception {
		File targetDirectory = new File(directoryName);
		if (!targetDirectory.isDirectory()) {
			throw new RuntimeException(directoryName + "不是一个目录");
		}
		while (true) {
			polling(targetDirectory);
			Thread.sleep(pollSpan * 1000);
		}
	}
	
	/**
	 * 扫描当前目录并上传文件，二级以及二级以下目录继续扫描
	 * @throws Exception 
	 */
	private void polling(File targetDirectory) throws Exception {
		FTPUtil ftpUtil = new FTPUtil();
		ftpUtil.connect();
		File directory = new File(directoryName);
		ftpUtil.uploadDirectory(directory);
	}

	public long getPollSpan() {
		return pollSpan;
	}

	public void setPollSpan(long pollSpan) {
		this.pollSpan = pollSpan;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
}
