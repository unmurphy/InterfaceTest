package com.thinkland.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.thinkland.util.Util;


/**
 * Log 处理器
 * 
 * @author murphy
 * @date 2015-8-19
 */
public class LogHandle {

	private static final String LOG_INFO = "Info      >>>: ";

	private static final String LOG_ENDINFO = "End      >>>: ";

	private static final String LOG_PASS = "Pass      >>>: ";

	private static final String LOG_FAIL = "Fail      >>>: ";

	private static final String LOG_ERROR = "Error     >>>: ";

	private static final String LOG_EXCEPTION = "Exception >>>: ";

	private static final String LOG_WARNING = "Warning   >>>: ";

	public String currentLogName = null;

	public static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public SimpleDateFormat format = new SimpleDateFormat(dateFormat);

	private static LogHandle logHandle;

	public static LogHandle getInstence() {
		if (logHandle == null) {
			logHandle = new LogHandle();
		}
		return logHandle;
	}

	/**
	 * 创建新的文件夹 createLogFile
	 * 
	 * @return
	 * @author murphy
	 * @date 2015-8-21
	 */
	private File createLogFile() {
		File logFile;
		String path = System.getProperty("user.dir") + "/files/log/"
				+ Util.runTitle;
		File p = new File(path);
		if (!p.exists()) {
			p.mkdirs();
		}
		String fileName = "/log_" + Util.runTitle + ".log";
		currentLogName = path + fileName;
		logFile = new File(currentLogName);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logException(e);
			}
		}
		return logFile;
	}

	/**
	 * 创建新的的文件夹并返回 getLogFile
	 * 
	 * @return new log file
	 * @author murphy
	 * @date 2015-8-21
	 */
	public File getLogFile() {
		return createLogFile();
	}

	/**
	 * 获取当前日志名称 getCurrentLogName
	 * 
	 * @return current log name
	 * @author murphy
	 * @date 2015-8-21
	 */
	public String getCurrentLogName() {
		return currentLogName;
	}

	/**
	 * 把字符串写入指定的文件中 writeLog
	 * 
	 * @param s
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void writeLog(String s, File logFile) {
		try {
			FileWriter fw = new FileWriter(logFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s + "\n");
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (Exception e) {
			logException(e);
		}
	}

	/**
	 * 把指定info写入log中 logInfo
	 * 
	 * @param info
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logInfo(String info) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_INFO + info;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的info写入指定的路径下面 logInfo
	 * 
	 * @param info
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logInfo(String info, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_INFO + info;
		writeLog(s, logFile);
	}

	/**
	 * 把指定的info写入指定的testcase logInfo
	 * 
	 * @param testCase
	 * @param info
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logInfo(String testCase, String info) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_INFO + "[" + testCase + "] "
				+ info;
		writeLog(s, createLogFile());
	}

	/**
	 * 把成功结果写入文件 logResult
	 * 
	 * @param result
	 * @param pass
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logResult(String result, boolean pass) {
		String logTime = format.format(new java.util.Date());
		String s = pass ? ("(" + logTime + ")" + " " + LOG_PASS + result)
				: ("(" + logTime + ")" + " " + LOG_FAIL + result);
		writeLog(s, createLogFile());
	}

	/**
	 * 把成功结果写入指定目录下的文件 logResult
	 * 
	 * @param result
	 * @param pass
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logResult(String result, boolean pass, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = pass ? ("(" + logTime + ")" + " " + LOG_PASS + result)
				: ("(" + logTime + ")" + " " + LOG_FAIL + result);
		writeLog(s, logFile);
	}

	/**
	 * 把指定信息写入error log logError
	 * 
	 * @param info
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logError(String info) {
		// AppiumDriverBase.saveScreenShot();
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_ERROR + info;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的error info写入指定的testcase logError
	 * 
	 * @param testCase
	 * @param error
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logError(String testCase, String error) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_ERROR + "[" + testCase
				+ "] " + error;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的error info写入指定的文件中 logError
	 * 
	 * @param error
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logError(String error, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_ERROR + error;
		writeLog(s, logFile);
	}

	/**
	 * 把指定的exception info写入指定的logFile logException
	 * 
	 * @param exception
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logException(String exception, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_EXCEPTION + exception;
		writeLog(s, logFile);
	}

	/**
	 * 把指定的exception info 写入文件 logException
	 * 
	 * @param e
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logException(Exception e) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_EXCEPTION + e.getMessage();
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的exception.Message info 写入文件 logException
	 * 
	 * @param exception
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logException(String exception) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_EXCEPTION + exception;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的exception info写入指定的testcase logException
	 * 
	 * @param testCase
	 * @param exception
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logException(String testCase, String exception) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_EXCEPTION + "[" + testCase
				+ "] " + exception;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的warning info写入指定的logFile logWarning
	 * 
	 * @param warning
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logWarning(String warning, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_WARNING + warning;
		writeLog(s, logFile);
	}

	/**
	 * 把指定的warning info写入指定的testcase logWarning
	 * 
	 * @param testCase
	 * @param warning
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logWarning(String testCase, String warning) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_WARNING + "[" + testCase
				+ "] " + warning;
		writeLog(s, createLogFile());
	}

	/**
	 * 把指定的logEndInfo写入指定的logFile logEndInfo
	 * 
	 * @param info
	 * @param logFile
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logEndInfo(String info, File logFile) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_ENDINFO + info;
		writeLog(s, logFile);
	}

	/**
	 * 把指定的logEndInfo写入指定的testCase logEndInfo
	 * 
	 * @param testCase
	 * @param info
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void logEndInfo(String testCase, String info) {
		String logTime = format.format(new java.util.Date());
		String s = "(" + logTime + ")" + " " + LOG_ENDINFO + "[" + testCase
				+ "] " + info;
		writeLog(s, createLogFile());
	}

	/**
	 * 创建指定文件路径
	 * 
	 * @param path
	 *            : String file path you want create, such as "C:\test"
	 * @return 是否创建路径成功
	 * @author murphy
	 * @date 2015-8-21
	 */
	public boolean createDir(String path) {
		boolean created = false;
		try {
			/*** Find directory,if not find,create *****/
			File dirFile = new File(path);
			if (!dirFile.exists()) {
				dirFile.mkdir();
				created = true;
			} else {
				created = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			created = false;
		}
		return created;
	}

	/**
	 * 创建指定文件
	 * 
	 * @param path
	 *            : String file name you want create, such as "C:\test\File.txt"
	 * @return 是否创建文件成功 * @author murphy
	 * @date 2015-8-21
	 */
	public boolean createFile(String fileName) {
		boolean created = false;
		try {
			/*** Find file,if not find,create *****/
			File file = new File(fileName);
			file.createNewFile();
			created = true;
		} catch (Exception e) {
			created = false;
			e.printStackTrace();
		}
		return created;
	}

	/**
	 * 打印指定的信息到指定文件中 printInfo
	 * 
	 * @param s
	 * @param f
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void printInfo(String s, File f) {
		String date = format.format(new java.util.Date());
		String ling = date + " <Info>: " + s;
		System.out.println(ling);
		logInfo(s, f);
	}

	/**
	 * 打印指定的错误信息到指定文件中 printError
	 * 
	 * @param s
	 * @param f
	 * @author murphy
	 * @date 2015-8-21
	 */
	public void printError(String s, File f) {
		String date = format.format(new java.util.Date());
		String ling = date + " >>>>> <Error>: " + s;
		System.out.println(ling);
		logInfo(s, f);
		logError(s, f);
	}

}
