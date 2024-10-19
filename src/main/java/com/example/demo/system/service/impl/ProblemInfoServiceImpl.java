package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.ProblemInfo;
import com.example.demo.system.mapper.ProblemInfoMapper;
import com.example.demo.system.service.IProblemInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-04-05
 */
@Service
public class ProblemInfoServiceImpl extends ServiceImpl<ProblemInfoMapper, ProblemInfo> implements IProblemInfoService {

    /**
     * 反馈问题
     * @param problemInfo 集合
     * */
    @Override
    public Result<String> problemFeedbackService(ProblemInfo problemInfo) {
        boolean bool = save(problemInfo);
        if(bool) {
            return Result.success("提交成功");
        }
        return Result.fail(50000, "提交成功");
    }
}
