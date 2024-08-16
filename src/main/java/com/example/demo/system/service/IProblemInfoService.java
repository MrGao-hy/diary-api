package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.ProblemInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-04-05
 */
public interface IProblemInfoService extends IService<ProblemInfo> {

    /**
     * 反馈问题
     * */
    Result problemFeedbackService(ProblemInfo problemInfo);
}
