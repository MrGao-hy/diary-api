package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.AppVersion;
import com.example.demo.system.mapper.AppVersionMapper;
import com.example.demo.system.service.IExcelExportService;
import com.example.demo.system.service.IAppVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-08
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    /**
     * 检查app版本
     * */
    @Override
    public Result<AppVersion> queryAppVersionService() {

        LambdaQueryWrapper<AppVersion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AppVersion::getUpdateTime);

        try {
            AppVersion data = getOne(wrapper);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    };
}
