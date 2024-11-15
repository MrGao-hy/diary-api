package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.MarkReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.system.entity.PageRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-11-14
 */
public interface IMarkReplyService extends IService<MarkReply> {

    Result<Page<MarkReply>> markReplyListService(PageRequest<MarkReply> param);

    Result<String> markReplyService(MarkReply markReply);
}
