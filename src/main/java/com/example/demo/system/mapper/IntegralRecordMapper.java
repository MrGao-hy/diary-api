package com.example.demo.system.mapper;

import com.example.demo.system.entity.IntegralRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-12-28
 */
public interface IntegralRecordMapper extends BaseMapper<IntegralRecord> {
    @Select("SELECT COALESCE(SUM(CASE WHEN type = 1 THEN -integral WHEN type = 0 THEN integral ELSE 0 END), 0) FROM d_integral_record WHERE user_id = #{userId}")
    int calculateTotal(String userId);

    @Select("SELECT COALESCE(SUM(integral)) FROM d_integral_record WHERE type = #{type} AND user_id = #{userId} AND DATE(create_time) = CURDATE()")
    int getTodayValueSum(String userId, Integer type);

}
