package com.thinkland.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.json.JSONException;
import org.json.JSONObject;

import com.thinkland.base.LogHandle;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 运行时参数设置
 * 
 * @author murphy
 * @date 2015-8-19
 */
public class Util {

	/**
	 * 运行当前类
	 */
	public static String runClass = "runClass";
	/**
	 * 运行当前case
	 */
	public static String runCase = "runCase";
	/**
	 * 运行当前title
	 */
	public static String runTitle = "runTitle";
	/**
	 * 运行当前时间
	 */
	public static String runTime = formatDate(new Date(), "yyyyMMdd_HHmmss");

	/**
	 * LogHandle
	 */
	public static LogHandle log = LogHandle.getInstence();

	/**
	 * 获取当前时间, 格式为 "yyyyMMdd_HHmmss" getCurrentDateTime
	 * 
	 * @return runTime
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String getCurrentDateTime() {
		runTime = formatDate(new Date(), "yyyyMMdd_HHmmss");
		return runTime;
	}

	/**
	 * 根据运行时的method name 获取Title getRunTitle
	 * 
	 * @param index
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static void getRunTitle(int index) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[index];
		runClass = ste.getClassName().replaceAll("\\w+\\.+", "");
		runCase = ste.getMethodName();
		runTitle = runClass + "_" + runCase + "_" + getCurrentDateTime();
	}

	/**
	 * 等待（秒）
	 * 
	 * @param second
	 */
	public static void waitFor(long second) {
		try {
			Thread.sleep(second * 1000);
		} catch (Exception ex) {
			return;
		}
	}

	/**
	 * 根据运行时的method name 获取Title getRunTitle
	 * 
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static void getRunTitle(Method m) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		runClass = ste.getClassName().replaceAll("\\w+\\.+", "");
		if (!m.getName().equals(runCase)) {
			runCase = m.getName();
			runTitle = runClass + "_" + runCase + "_" + getCurrentDateTime();
			System.out.println(runTitle);
		}
	}

	public static void getRunTitle(Method m, String excelName) {
		runCase = m.getName();
		runTitle = runCase + "_" + excelName + "_" + getCurrentDateTime();
		System.out.println(runTitle);
	}

	/**
	 * 返回一个字符串，该值介于[0，max)区间， 长度为len， 不足在数值前用0补足。 比如随机数值为0，长度为6的话，返回就是000000
	 * getRandomNumber
	 * 
	 * @param max
	 * @param len
	 * @return randomNumber
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String getRandomNumber(int max, int len) {
		int a = new Random().nextInt(max);
		String s = String.valueOf(a);
		for (int j = s.length(); j < len; j++) {
			s = "0" + s;
		}
		return s;
	}

	/**
	 * 返回一个和expString不一样的字符串，该值介于[0，max)区间， 长度为len， 不足在数值前用0补足。
	 * 比如随机数值为0，长度为6的话，返回就是000000 getRandomNumber
	 * 
	 * @param max
	 * @param len
	 * @param expString
	 * @return randomNumber
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String getRandomNumber(int max, int len, String expString) {
		String returnStr = "";
		do {
			returnStr = getRandomNumber(max, len);
		} while (expString.equals(returnStr));

		return returnStr;
	}

	/**
	 * 转换时间为"yyyyMMddHHmm"格式的字符串 formatDate
	 * 
	 * @param date
	 * @return formatDate
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyyMMddHHmm");
	}

	/**
	 * 转换时间为指定格式的字符串 formatDate
	 * 
	 * @param date
	 * @param format
	 * @return formatDate
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String formatDate(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 根据长度产生一个随机的字符串 getRandomString
	 * 
	 * @param length
	 * @return RandomString
	 * @author murphy
	 * @date 2015-8-21
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}

	/**
	 * 获取数据,去除 字符串前后的"[" , "]";
	 * 
	 * getData
	 * 
	 * @param obj
	 * @return 去除"[" , "]"后的字符串
	 * @author murphy
	 * @date 2015-9-21
	 */
	public static String getData(Object obj) {
		return obj.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim();
	}

	/**
	 * 等待指定毫秒时间
	 * 
	 * sleep
	 * 
	 * @param timeoutInMilliseconds
	 * @author murphy
	 * @date 2015-9-21
	 */
	public static void sleep(long timeoutInMilliseconds) {
		try {
			Thread.sleep(timeoutInMilliseconds);
		} catch (InterruptedException e) {
			log.logException(e);
			e.printStackTrace();
		}
	}

	/**
	 * adb cmd 命令
	 * 
	 * @param cmd
	 * @return
	 */
	public static String adbCmd(String cmd) {
		String s = "\n";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				s += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cmd.contains("battery")) {
			int index = s.indexOf("level");
			return s.substring(index + 6, index + 9).trim();
		} else if (cmd.contains("screenrecord") && s.contains("screenrecord")) {
			int index = s.indexOf("shell");
			return s.substring(index + 6, index + 16).trim();
		} else {
			return s;
		}
	}

	/**
	 * 执行adbcmd
	 * 
	 * @param cmd
	 */
	public static void adbShellCmd(String cmd) {
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据map获取expectReponse的值
	 * 
	 * @param data
	 * @return
	 */
	public static String getExpectStatus(Map<String, String> data) {
		String status = "";
		status = Util.getData(data.get("expectreponse")).toLowerCase().trim();
		return status;
	}

	public static String getPercent(int x, int y) {
		double k = (double) x / y * 100;
		BigDecimal big = new BigDecimal(k);
		return big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%";
	}

	/**
	 * 获取JSON或者XML节点值
	 * 
	 * @param obj
	 * @param node
	 * @return
	 * @throws JSONException
	 * @throws DocumentException
	 */
	public static String getNodefromXMLOrJson(String obj, String node) throws JSONException, DocumentException {
		if (obj.equals("")) {
			return "";
		}
		// JSON解析
		else if (obj.startsWith("{")) {
			JSONObject jsonObject = new JSONObject(obj);
			return jsonObject.getString(node);
		}
		// XML解析
		else {
			Document doc = DocumentHelper.parseText(obj);
			Node nodes = doc.selectSingleNode("/root/" + node);
			return nodes.getText();
		}
	}

	public static List<Map<String, String>> getListFromExcel(String excelName) {
		Sheet sheet = null;
		int lineNum = 0;
		int columnNum = 0;
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		LogHandle log = LogHandle.getInstence();
		try {
			String excelPath = System.getProperty("user.dir") + "/" + "files/data/" + excelName + ".xls";
			System.out.println(excelPath);
			File file = new File(excelPath);
			if (!file.exists()) {
				return null;
			}
			Workbook book = Workbook.getWorkbook(file);
			String[] sheets = book.getSheetNames();
			for (int i = 0; i < sheets.length; i++) {
				sheet = book.getSheet(sheets[i]);
				lineNum = sheet.getRows();
				Cell[] cell = sheet.getRow(0);
				columnNum = cell.length;
				for (int j = 1; j < lineNum; j++) {
					map = new HashMap<String, String>();
					for (int k = 0; k < columnNum; k++) {
						String key = sheet.getCell(k, 0).getContents().toString().trim().toLowerCase();
						String context = sheet.getCell(k, j).getContents().trim().toString();
						map.put(key, context);
					}
					listMap.add(map);
				}
			}
			return listMap;
		} catch (Exception e) {
			log.logException(e);
			return null;
		}
	}

	public static Map<String, String> removeKeyfromMap(Map<String, String> map, String key1, String key2, String key3,
			String key4) {
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			if (key.equals(key1) || key.equals(key2) || key.equals(key3) || key.equals(key4)) {
				it.remove();
				map.remove(key);
			}
		}
		return map;
	}

	public static Map<String, String> addDataToMap(String desc, String url, String method, String req_param,
			String act_result, String exp_result, String comp_result) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("desc", desc);
		map.put("url", url);
		map.put("method", method);
		map.put("req_param", req_param);
		map.put("act_result", act_result);
		map.put("exp_result", exp_result);
		map.put("comp_result", comp_result);
		return map;
	}
}
