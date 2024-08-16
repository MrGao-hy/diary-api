package com.example.demo.system.service;

import com.example.demo.system.entity.Headers;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IExcelExportService {
    void exportToExcel(List<?> dataList, Headers[] headers, String fileName, HttpServletResponse response);
}
