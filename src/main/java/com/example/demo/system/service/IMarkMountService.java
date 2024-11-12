package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.CollectMount;
import com.example.demo.system.entity.MarkMount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-22
 */
public interface IMarkMountService extends IService<MarkMount> {

    /**
     * 评语接口
     * */
    Result<String> markMountService(MarkMount markMount);

    /**
     * 评语列表接口
     * */
    Result<List<MarkMount>> commentListService(MarkMount markMount);

    /**
     * 删除评语接口
     * */
    Result<Boolean> deleteCommentService(MarkMount markMount);
}
