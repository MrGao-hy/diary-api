package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.ApplyList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-05-07
 */
public interface IApplyListService extends IService<ApplyList> {

    /**
     * 获取工具应用列表
     * */
    Result getApplyListService();

    /**
     * 添加工具应用
     * */
    Result addApplyService(ApplyList applyList);

    /**
     * 更新工具应用
     * */
    Result changeApplyService(ApplyList applyList);

    /**
     * 删除工具应用
     * */
    Result deleteApplyService(List<String> ids);
}
