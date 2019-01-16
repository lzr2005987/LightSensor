package com.lightsensor.lzr.lightsensor.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by lizheren on 2018/2/10 0010.
 */

public class SaveUtil {
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/lightSensor/";

    public static void saveDataToExcel(ArrayList<String> timeList, ArrayList<String> aList,
                                       ArrayList<String> bList, ArrayList<String> xList,
                                       ArrayList<String> caliList,ArrayList<String> lightOrgList,
                                       ArrayList<String> lightList, ArrayList<String> conList) throws IOException, WriteException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatTime = df.format(new Date());

        File fileDir = new File(FILE_PATH);
        if (!fileDir.exists()) fileDir.mkdirs();
        File excelFile = new File(FILE_PATH, formatTime + ".xls");
        if (!excelFile.exists()) excelFile.createNewFile();

        Workbook readWorkbook = null;
        int startRows = 0;
        boolean isExist = false;
        try {
            readWorkbook = Workbook.getWorkbook(new File(FILE_PATH, formatTime + ".xls"));
            Sheet sheet0 = readWorkbook.getSheet(0);
            startRows = sheet0.getRows();
            if (startRows != 0) startRows++;
            isExist = true;
        } catch (BiffException e) {
            e.printStackTrace();
            isExist = false;
        }

        WritableWorkbook writeBook = null;
        WritableSheet sheet = null;
        // 1、创建工作簿(WritableWorkbook)对象，打开excel文件，若文件不存在，则创建文件
        // 2、新建工作表(sheet)对象，并声明其属于第几页
        if (!isExist) {
            writeBook = Workbook.createWorkbook(new File(FILE_PATH, formatTime + ".xls"));
            sheet = writeBook.createSheet("DataSheet", 0);// 第一个参数为工作簿的名称，第二个参数为页数
        } else {
            writeBook = Workbook.createWorkbook(new File(FILE_PATH, formatTime + ".xls"), readWorkbook);
            sheet = writeBook.getSheet(0);
        }

        // 3、创建单元格(Label)对象，
        //Label label1 = new Label(startRows + 1, 0, "name");// 第一个参数指定单元格的列数、第二个参数指定单元格的行数，第三个指定写的字符串内容

        sheet.addCell(new Label(0, startRows, "Time"));
        sheet.addCell(new Label(1, startRows, "a"));
        sheet.addCell(new Label(2, startRows, "b"));
        sheet.addCell(new Label(3, startRows, "x"));
        sheet.addCell(new Label(4, startRows, "ALS reading"));
        sheet.addCell(new Label(5, startRows, "Light calibration"));
        sheet.addCell(new Label(6, startRows, "Light intensity"));
        sheet.addCell(new Label(7, startRows, "Concentration"));

        for (int i = 0; i < timeList.size(); i++) {
            sheet.addCell(new Label(0, startRows + i + 1, timeList.get(i)));
            sheet.addCell(new Label(1, startRows + i + 1, aList.get(i)));
            sheet.addCell(new Label(2, startRows + i + 1, bList.get(i)));
            sheet.addCell(new Label(3, startRows + i + 1, xList.get(i)));
            sheet.addCell(new Label(4, startRows + i + 1, lightOrgList.get(i)));
            sheet.addCell(new Label(5, startRows + i + 1, caliList.get(i)));
            sheet.addCell(new Label(6, startRows + i + 1, lightList.get(i)));
            sheet.addCell(new Label(7, startRows + i + 1, conList.get(i)));
        }

        // 4、打开流，开始写文件
        writeBook.write();

        // 5、关闭流
        writeBook.close();
    }
}
