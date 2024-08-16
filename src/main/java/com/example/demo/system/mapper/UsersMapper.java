package com.example.demo.system.mapper;

import com.example.demo.system.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-11-18
 */

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    @Insert("INSERT INTO d_users(`user_name`, `password`, `createTime`, `phone`, `emit`, `salt`, `birth_date`, `avatar`, `age`, `sex`, `constellation`, `signature`, `integral`) " +
            "VALUES(#{userName},#{password},#{createTime},#{phone},#{emit},#{salt},#{birthDate},#{avatar},#{age},#{sex},#{constellation},#{signature},#{integral})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Long insertReturnId(Users users);

}
