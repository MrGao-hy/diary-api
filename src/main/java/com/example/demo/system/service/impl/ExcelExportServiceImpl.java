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

        // 第四步，填充数据
        int dataNum = 1;
        for (Object data : dataList) {
            Row row = sheet.createRow(dataNum++);
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

        // 第五步， 遍历所有列，计算最大宽度
        for (int colNum = 0; colNum < headers.length; colNum++) {
            // 设置单元格自适应宽度
            int maxColumnWidth = 0;

            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                Cell cell = row.getCell(colNum);
                if (cell == null) continue;
                // 使用Apache POI的自动宽度计算
                int cellWidth = sheet.getColumnWidth(colNum) / 256; // 默认宽度，这里只是为了演示如何计算，实际不需要

                // 假设我们使用单元格内容的字符串长度来估算宽度
                // 注意：这里直接使用toString()可能不够准确，特别是对于数字、日期等类型的单元格
                int length = cell.toString().length();
                if (length * 256 > cellWidth) { // 假设每个字符大约占用256个单位宽度（这只是一个粗略的估计）
                    cellWidth = (length) * 256;
                }

                // 但更好的做法是使用Apache POI的Sheet.autoSizeColumn(int column)方法
                // 这里我们仍然计算，但只是为了展示如何遍历和计算
                if (cellWidth > maxColumnWidth) {
                    maxColumnWidth = cellWidth;
                }
            }
            sheet.autoSizeColumn(colNum);
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


