package com.thinkland.base;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thinkland.util.Util;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelReportHandle {
	private String excelName;

	public ExcelReportHandle(String excelName) {
		this.excelName = excelName;
	}

	public void createExcelReport(List<Map<String, String>> listData) {
		int i = 3;
		int errorNum = 0;
		String basePath = System.getProperty("user.dir") + "/" + "files/report/";
		File file = new File(basePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String excelPath = basePath + excelName + "_Report" + "_" + Util.runTime + ".xls";
		File excelFile = new File(excelPath);
		if (!excelFile.exists()) {
			try {
				excelFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			WritableWorkbook book = Workbook.createWorkbook(excelFile);
			WritableSheet sheet = book.createSheet("测试报告", 0);

			// 设置字体格式
			WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont normalFontred = new WritableFont(WritableFont.ARIAL, 10);
			// 标题
			WritableFont boldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			// 表名
			WritableFont titileFont = new WritableFont(WritableFont.ARIAL, 20, WritableFont.BOLD);

			// 表名格式
			WritableCellFormat title_center = new WritableCellFormat(titileFont);
			title_center.setBorder(Border.ALL, BorderLineStyle.THIN);
			title_center.setBackground(Colour.BLUE);
			title_center.setVerticalAlignment(VerticalAlignment.CENTRE);
			title_center.setAlignment(Alignment.CENTRE);
			title_center.setWrap(false);

			// 标题格式
			WritableCellFormat wcf_center = new WritableCellFormat(boldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_center.setBackground(Colour.GREY_40_PERCENT);
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_center.setAlignment(Alignment.CENTRE);
			wcf_center.setWrap(false);

			// 正文格式
			WritableCellFormat wcf_left = new WritableCellFormat(normalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN);
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_left.setAlignment(Alignment.LEFT);
			wcf_left.setWrap(false);

			WritableCellFormat wcf_left_red = new WritableCellFormat(normalFontred);
			wcf_left_red.setBorder(Border.NONE, BorderLineStyle.THIN);
			wcf_left_red.setBackground(Colour.DARK_RED);
			wcf_left_red.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_left_red.setAlignment(Alignment.LEFT);
			wcf_left_red.setWrap(false);
			
			WritableCellFormat wcf_summary = new WritableCellFormat(boldFont);
			wcf_summary.setBorder(Border.ALL, BorderLineStyle.THIN);
			wcf_summary.setBackground(Colour.GREEN);
			wcf_summary.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_summary.setAlignment(Alignment.CENTRE);
			wcf_summary.setWrap(false);

			// 正文标题
			Label lb0 = new Label(0, 0, "测试详情", title_center);
			// 列名
			Label lb1 = new Label(0, 2, "接口名称", wcf_center);
			Label lb2 = new Label(1, 2, "请求地址", wcf_center);
			Label lb3 = new Label(2, 2, "请求方式", wcf_center);
			Label lb4 = new Label(3, 2, "请求参数", wcf_center);
			Label lb5 = new Label(4, 2, "实际结果", wcf_center);
			Label lb6 = new Label(5, 2, "期望结果", wcf_center);
			Label lb7 = new Label(6, 2, "对比结果", wcf_center);

			// 设置列宽
			sheet.setColumnView(0, 30);
			sheet.setColumnView(1, 30);
			sheet.setColumnView(2, 30);
			sheet.setColumnView(3, 30);
			sheet.setColumnView(4, 30);
			sheet.setColumnView(5, 30);

			sheet.addCell(lb0);
			sheet.addCell(lb1);
			sheet.addCell(lb2);
			sheet.addCell(lb3);
			sheet.addCell(lb4);
			sheet.addCell(lb5);
			sheet.addCell(lb6);
			sheet.addCell(lb7);

			// 合并标题
			sheet.mergeCells(0, 0, 6, 0);
			// 插入概要统计
			sheet.addCell(new Label(0, 1, "用例总计", wcf_summary));
			sheet.addCell(new Label(2, 1, "用例失败", wcf_summary));
			sheet.addCell(new Label(4, 1, "失败率", wcf_summary));

			// 添加正文数据
			Iterator it = listData.iterator();
			while (it.hasNext()) {
				Map data = (Map) it.next();
				sheet.addCell(new Label(0, i, data.get("desc").toString(), wcf_left));
				sheet.addCell(new Label(1, i, data.get("url").toString(), wcf_left));
				sheet.addCell(new Label(2, i, data.get("method").toString(), wcf_left));
				sheet.addCell(new Label(3, i, data.get("req_param").toString(), wcf_left));
				sheet.addCell(new Label(4, i, data.get("act_result").toString(), wcf_left));
				sheet.addCell(new Label(5, i, data.get("exp_result").toString(), wcf_left));
				if (data.get("comp_result").toString().trim().equals("pass")) {
					sheet.addCell(new Label(6, i, data.get("comp_result").toString(), wcf_left));
				} else {
					sheet.addCell(new Label(6, i, data.get("comp_result").toString(), wcf_left_red));
					errorNum ++;
				}
				i++;
			}
			System.out.println("测试用例总数: " + listData.size() + ", 失败次数: " + errorNum);
			// 添加统计数据
			sheet.addCell(new Label(1, 1, String.valueOf(listData.size()), wcf_left));
			sheet.addCell(new Label(3, 1, String.valueOf(errorNum), wcf_left));
			sheet.addCell(new Label(5, 1, Util.getPercent(errorNum, listData.size()), wcf_left));

			book.write();
			book.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
