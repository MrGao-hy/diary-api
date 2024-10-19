package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.*;
import com.example.demo.system.mapper.UsersMapper;
import com.example.demo.system.service.IExcelExportService;
import com.example.demo.system.service.IUserRoleService;
import com.example.demo.system.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-11-18
 */
@Component
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IExcelExportService excelExportService;


    /**
     * 注册用户
     * */
    @Override
    public Result registerService(Users users) {
        String username = users.getUserName();
        String phone = users.getPhone();
        // 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
        LambdaQueryWrapper<Users> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Users::getUserName, username).or().eq(Users::getPhone, phone).eq(Users::getIdCard, users.getIdCard()).eq(Users::getAddress, users.getAddress());
        Long count = usersMapper.selectCount(wrapper);
        // 判断查询结果是否大于0，大于零说明有重复的名字
        if (count > 0) {
            // 是：表示用户名已被占用，则抛出UsernameDuplicateException异常
            return Result.fail(StatusCode.REPETITION.getValue(), "尝试注册的用户名【" + username + "】或手机号【" + phone + "】已经被占用");
        }

        // 补全数据：加密后的密码
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(users.getPassword(), salt);
        users.setPassword(md5Password);
        users.setSalt(salt);
        LocalDateTime now = LocalDateTime.now();
        users.setCreateTime(now);

        // 表示用户名没有被占用，则允许注册
        // 调用持久层Integer insert(User user)方法，执行注册并获取返回值(受影响的行数)
        int row1 = usersMapper.insert(users);
        // 判断受影响的行数是否不为1
        if (row1 != 1) {
            // 是：插入数据时出现某种错误，则抛出InsertException异常
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
        }

        // 查询该用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUserName, users.getUserName());
        Users user = getOne(queryWrapper);
        // 绑定默认权限，默认权限：普通用户
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRoleService.bindRoleService(userRole);

        return Result.success("注册成功");
    }

    /**
     * 登录
     * */
    @Override
    public Result loginService(Users users) {
        String username = users.getUserName();
        String password = users.getPassword();
        Map<String, String> payload = new HashMap<>();
        LambdaQueryWrapper<Users> wrapper = Wrappers.<Users>lambdaQuery();
        wrapper.eq(Users::getUserName, username).or().eq(Users::getPhone, username);

        Users result = usersMapper.selectOne(wrapper);
        if (result == null) {
            return Result.fail(30001, "用户不存在");
        }


        // 从查询结果中获取盐值
        String salt = result.getSalt();
        // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
        String md5Password = getMd5Password(password, salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!result.getPassword().equals(md5Password)) {
            // 是：抛出异常
            return Result.fail(StatusCode.PASSWORD_ERR.getValue(), StatusCode.PASSWORD_ERR.getDescription());
        }

        // 生成token
        //用户登录成功后的信息放入payload
        payload.put("id", result.getId());
        payload.put("username", result.getUserName());
        payload.put("date", new Date().toString());
        String token = JWTUtils.getToken(payload);

        // 放在data中返回给前端
        Map<Object, String> data = new HashMap<>();
        data.put("token", token);
        data.put("salt", salt);


        return Result.success(data, "登录成功");
    }

    /**
     * 个人用户信息详情
     * */
    @Override
    public Result userInfoService(Users users) {
        LambdaQueryWrapper<Users>  wrapper = new LambdaQueryWrapper<>();

        if (users.getId().isEmpty()) {
            String userId = hostHolder.getUser().getId();
            wrapper.eq(Users::getId, userId);
        } else {
            wrapper.eq(Users::getId, users.getId());
        }

        Users data = getOne(wrapper);
        UserRole userRole = new UserRole();

        if (data != null) {
            userRole.setUserId(data.getId());
            List<Role> roleList = userRoleService.queryRoleService(userRole);
            data.setRoles(roleList);
            data.setSex(judgeSex(data.getSex()));
            return Result.success(data, "查询成功");
        }

        return Result.fail(StatusCode.NOT_DATA.getValue(), StatusCode.NOT_DATA.getDescription());
    }

    /**
     * 编辑个人用户信息
     * */
    @Override
    public Result editInfoService(Users users) {
        // 设置需要更新的字段
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<>();

        if (ObjectUtils.isEmpty(users.getId())) {
            String userId = hostHolder.getUser().getId();
            updateWrapper.eq(Users::getId, userId);
        } else {
            updateWrapper.eq(Users::getId, users.getId());
        }
        updateWrapper.set(Users::getUserName, users.getUserName())
                .set(Users::getIdCard, users.getIdCard())
                .set(Users::getAvatar, users.getAvatar())
                .set(Users::getEmit, users.getEmit())
                .set(Users::getConstellation, users.getConstellation())
                .set(Users::getBirthDate, users.getBirthDate())
                .set(Users::getMajor, users.getMajor())
                .set(Users::getSchool, users.getSchool())
                .set(Users::getEducation, users.getEducation())
                .set(Users::getSex, users.getSex())
                .set(Users::getAddress, users.getAddress())
                .set(Users::getPhone, users.getPhone())
                .set(Users::getSignature, users.getSignature());



        boolean updateData = update(updateWrapper);
        if (!updateData) {
            return Result.fail(StatusCode.NOT_DATA.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
        }

        return Result.success("修改成功");
    }

    /**
     * 注销用户，目前只能删用户信息
     * */
    @Override
    public Result unsubscribeService(Users users) {
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<Users> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Users::getId, userId);
        Users user = usersMapper.selectOne(wrapper);
        // 从查询结果中获取盐值
        String salt = user.getSalt();
        // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
        String md5Password = getMd5Password(users.getPassword(), salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!user.getPassword().equals((md5Password))) {
            return Result.success("密码错误");
        }
        int result = usersMapper.deleteById(userId);
        if (result != 1) {
            return Result.success("注销失败");
        }
        return Result.success("注销成功");
    }

    /**
     * 分页查询所有用户信息
     * */
    public Result getUserListService(Page<Users> page) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Users::getBirthDate);
        Page<Users> usersList = page(page, wrapper);
        for (Users item : usersList.getRecords()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(item.getId());
            List<Role> roleList = userRoleService.queryRoleService(userRole);
            item.setRoles(roleList);
            item.setSex(judgeSex(item.getSex()));
        }
        return Result.success(usersList, "查询成功");
    }

    /**
     * 搜索用户信息
     * */
    public Result searchUserService(Users users) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();

        // 搜索范围出生日期
        if(users.getBirthDate() != null) {
            LocalDate firstDayOfYear = users.getBirthDate().with(TemporalAdjusters.firstDayOfYear());
            LocalDate lastDayOfYear = users.getBirthDate().with(TemporalAdjusters.lastDayOfYear());
            wrapper.between(Users::getBirthDate, firstDayOfYear.getYear(),lastDayOfYear.atTime(23, 59, 59));
        }

        wrapper.like(!StringUtils.isEmpty(users.getUserName()), Users::getUserName, users.getUserName());
        wrapper.like(!StringUtils.isEmpty(users.getPhone()), Users::getPhone, users.getPhone());
        wrapper.like(!StringUtils.isEmpty(users.getEmit()), Users::getEmit, users.getEmit());
        wrapper.like(!StringUtils.isEmpty(users.getIdCard()), Users::getIdCard, users.getIdCard());
        wrapper.eq(!StringUtils.isEmpty(users.getSex()), Users::getSex, users.getSex());
        wrapper.eq(!StringUtils.isEmpty(users.getConstellation()), Users::getConstellation, users.getConstellation());

        List<Users> list = wrapper.isEmptyOfWhere() ? list(new QueryWrapper<>()) : list(wrapper);

        for (Users item : list) {
            UserRole userRole = new UserRole();
            userRole.setUserId(item.getId());
            List<Role> roleList = userRoleService.queryRoleService(userRole);
            item.setRoles(roleList);
            item.setSex(judgeSex(item.getSex()));
        }
        return Result.success(list, "查询成功");
    }

    /**
     * 导出所有用户数据
     * */
    public Result exportUserListService(HttpServletResponse response) {

        List<Users> lists = list();
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (Users data : lists) {
            HashMap<String, Object> obj = new HashMap<>();
            obj.put("userName", data.getUserName());
            obj.put("idCard", data.getIdCard());
            obj.put("phone", data.getPhone());
            obj.put("emit", data.getEmit());
            obj.put("signature", data.getSignature());
            obj.put("birthDate", data.getBirthDate());
            obj.put("avatar", data.getAvatar());
            obj.put("sex", judgeSex(data.getSex()));
            obj.put("school", data.getSchool());
            obj.put("education", data.getEducation());
            obj.put("major", data.getMajor());
            obj.put("constellation", data.getConstellation());
            obj.put("address", data.getAddress());
            obj.put("createTime", data.getCreateTime());

            dataList.add(obj);
        };
        // 表头
        Headers[] headers = {
                new Headers("名称", "userName"),
                new Headers("身份证号", "idCard"),
                new Headers("手机号", "phone"),
                new Headers("邮箱", "emit"),
                new Headers("签名", "signature"),
                new Headers("生日", "birthDate"),
                new Headers("头像", "avatar"),
                new Headers("性别", "sex"),
                new Headers("学校", "school"),
                new Headers("学历", "education"),
                new Headers("专业", "major"),
                new Headers("星座", "constellation"),
                new Headers("地址", "address"),
                new Headers("注册时间", "createTime")
        };
        // 导出文件路径
        String fileName = "user.xlsx";
        // 调用服务导出数据
        excelExportService.exportToExcel(dataList, headers, fileName, response);

        return Result.success("导出成功");
    }

    /**
     * 修改用户密码
     * */
    public Result changePasswordService(Users users) {
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<Users> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Users::getId, userId);
        Users user = getOne(wrapper);

        // 从查询结果中获取盐值
        String salt = user.getSalt();
        // 调用getMd5Password()方法，将参数password和salt结合起来进行加密
        String md5Password = getMd5Password(users.getOldPassword(), salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!user.getPassword().equals(md5Password)) {
            // 是：抛出异常
            return Result.fail(StatusCode.PASSWORD_ERR.getValue(), StatusCode.PASSWORD_ERR.getDescription());
        }

        // 新密码加密存起来
        String newPwd = getMd5Password(users.getPassword(), salt);
        LambdaUpdateWrapper<Users> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Users::getId, userId);
        updateWrapper.set(Users::getPassword, newPwd);
        boolean result = update(updateWrapper);


        // 更新token
        Map<String, String> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username", user.getUserName());
        payload.put("date", new Date().toString());
        String token = JWTUtils.getToken(payload);

        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        if (result) {
            return Result.success(data, "修改密码成功");
        } else {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
        }
    }

    /**
     * md5算法加密
     */
    private String getMd5Password(String password, String salt) {
        for (int i = 0; i < 3; i++) {
            //三次加密
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        //返回加密的密码
        return password;
    }

    /**
     * 判断男女
     * */
    private String judgeSex(String sex) {
        String gender = null;
        if (sex != null) {
            if (sex.equals("0")) {
                gender = "女生";
            } else if (sex.equals("1")) { // 注意：这里假设"1"也是有效的字符串输入，但在实际性别编码中很少见
                gender = "男生";
            } else if (sex.equals("-1")) {
                gender = "保密";
            } else {
                gender = "未知";
            }
        } else {
            gender = "未知"; // 或者你可以决定在这种情况下抛出异常或进行其他处理
        }

        return gender;
    }

}
