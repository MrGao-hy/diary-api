package com.example.demo.system.service.impl;

import com.example.demo.system.entity.Headers;
import com.example.demo.system.service.IExcelExportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportServiceImpl implements IExcelExportService {

    public void exportToExcel(List<?> dataList, Headers[] headers, String fileName, HttpServletResponse response) {
        // 第一步，创建一个webbook，对应一个Excel文件
        Workbook workbook = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        Sheet sheet = workbook.createSheet("Sheet1");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        Row headerRow = sheet.createRow(0);


        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i].getLabel());
        }

        // 填充数据
        int rowNum = 1;
        for (Object data : dataList) {
            Row row = sheet.createRow(rowNum++);
            // 假设data是一个Map，包含了要导出的字段和值
            if (data instanceof Map) {
                Map<?, ?> dataMap = (Map<?, ?>) data;
                int cellNum = 0;
                for (Headers header : headers) {
                    Cell cell = row.createCell(cellNum++);
                    if (dataMap.containsKey(header.getValue())) {
                        cell.setCellValue(dataMap.get(header.getValue()) != null ? dataMap.get(header.getValue()).toString() : "");
                    }
                }
            }
        }

        // 写入文件
        try (OutputStream osOut = response.getOutputStream()) {
            // 设置响应类型与编码
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");  // .xlsx 用这个
//            response.setContentType("application/vnd.ms-excel;charset=utf-8"); // .xls 用这个
            response.setCharacterEncoding("utf-8");

            // 将指定的字节写入此输出流
            workbook.write(osOut);
            // 刷新此输出流并强制将所有缓冲的输出字节被写出
            osOut.flush();
            // 关闭流
            osOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


