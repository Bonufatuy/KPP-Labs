package com.example.demo.excel;

import com.example.demo.controllers.EncryptController;
import com.example.demo.models.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

@Component
public class ExcelBuilder {

    private static final Logger logger = LogManager.getLogger(EncryptController.class);

    public void buildExcel(Collection<Encrypt> encryptList) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Encrypt List");
        sheet.setDefaultColumnWidth(20);
        HSSFRow header = sheet.createRow(0);
        int rowCount = 1;

        header.createCell(0).setCellValue("First parameter");
        header.createCell(1).setCellValue("Second parameter");

        for (Encrypt encrypt : encryptList) {
            HSSFRow row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(encrypt.getFirstString());
            row.createCell(1).setCellValue(encrypt.getSecondString());
        }
        try {
            FileOutputStream out = new FileOutputStream(ExcelFile.sheet);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            logger.error("Creating error");
            e.printStackTrace();
        }
    }
}