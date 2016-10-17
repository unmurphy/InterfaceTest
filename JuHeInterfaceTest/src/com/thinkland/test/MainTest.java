package com.thinkland.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.thinkland.util.*;

import com.thinkland.base.BaseTest;
import com.thinkland.base.ExcelReportHandle;

public class MainTest extends BaseTest {

	private String excelName;
	private String actualReponse;
	private String actualStatus;
	private String repectStatus;
	private String desc;
	private String reqUrl;
	private String method;
	private String comp_result;

	@BeforeClass
	@Parameters({ "excelName" })
	public void beforeClass(String excelName) {
		this.excelName = excelName;
	}

	@BeforeMethod(alwaysRun = true)
	public void setup(Method m) throws Exception {
		Util.getRunTitle(m, excelName);
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() throws Exception {
	}

	@Test
	public void commonInterface() {
		ExcelReportHandle excelReport = new ExcelReportHandle(excelName);
		List<Map<String, String>> resultData = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listData = Util.getListFromExcel(excelName);
		for (Map<String, String> data : listData) {
			repectStatus = Util.getExpectStatus(data);
			reqUrl = data.get("url");
			desc = data.get("desc");
			method = data.get("method");
			actualReponse = "";
			Map<String, String> reqData = Util.removeKeyfromMap(data, "expectreponse", "url", "desc", "method");
			try {
				if (method.contains("get")) {
					actualReponse = httpGetClient(reqUrl, reqData);
				} else {
					actualReponse = httpPostClient(reqUrl, reqData);
				}
				System.out.println(actualReponse);
				actualStatus = Util.getNodefromXMLOrJson(actualReponse, "error_code");
				System.out.println("jiexi:" + actualStatus);
				if (actualStatus.equals("")) {
					comp_result = "fail";
				} else {
					if (repectStatus.toLowerCase().equals("pass")) {
						comp_result = actualStatus.equals("0") ? "pass" : "fail";
					} else if (repectStatus.toLowerCase().equals("fail")) {
						comp_result = actualStatus.equals("0") ? "fail" : "pass";
					}
				}
				Map<String, String> resultMap = Util.addDataToMap(desc, reqUrl, method, reqData.toString(),
						actualReponse, repectStatus, comp_result);
				resultData.add(resultMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.logException(e);
				e.printStackTrace();
			}
		}
		excelReport.createExcelReport(resultData);
	}

	@Test
	public void postInterface() {
		ExcelReportHandle excelReport = new ExcelReportHandle(excelName);
		List<Map<String, String>> resultData = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listData = Util.getListFromExcel(excelName);
		for (Map<String, String> data : listData) {
			repectStatus = Util.getExpectStatus(data);
			reqUrl = data.get("url");
			desc = data.get("desc");
			method = data.get("method");
			actualReponse = "";
			Map<String, String> reqData = Util.removeKeyfromMap(data, "expectreponse", "url", "desc", "method");
			try {
				actualReponse = httpPostClient(reqUrl, reqData);
				System.out.println(actualReponse);
				actualStatus = Util.getNodefromXMLOrJson(actualReponse, "error_code");
				System.out.println("jiexi:" + actualStatus);
				if (repectStatus.equals("pass")) {
					comp_result = actualStatus.equals("0") ? "pass" : "fail";
				} else if (repectStatus.equals("fail")) {
					comp_result = actualStatus.equals("0") ? "fail" : "pass";
				}
				Map<String, String> resultMap = Util.addDataToMap(desc, reqUrl, method, reqData.toString(),
						actualReponse, repectStatus, comp_result);
				resultData.add(resultMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.logException(e);
				e.printStackTrace();
			}
		}
		excelReport.createExcelReport(resultData);
	}
}
