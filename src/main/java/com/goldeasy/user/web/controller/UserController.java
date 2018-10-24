package com.goldeasy.user.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.goldeasy.common.response.CommonResponse;
import com.goldeasy.user.service.UserService;
import com.goldeasy.user.vo.UserInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: tianliya
 * @date: 2018/10/23
 * @description: 用户相关的controller层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference(timeout = 2000,version = "${dubbo.service.version}", loadbalance = "random")
    private UserService userService;

    /**
     * fetch 修改密码功能校验旧密码
     * @author: tianliya
     * @time: 2018/10/23
     * @param oldPassword
     * @param userId
     * @return
     */
    @GetMapping("/authOldPassword")
    public CommonResponse authOldPassword(String oldPassword, Long userId){
        Boolean flag = this.userService.validatePassword(oldPassword, userId);
        if (flag){
            return CommonResponse.success("密码正确正确");
        }else {
            return CommonResponse.fail("原密码错误");
        }
    }

    /**
     * fetch 修改密码功能设置新密码
     * @author: tianliya
     * @time: 2018/10/23
     * @param password
     * @param userId
     * @return
     */
    @GetMapping("/setPassword")
    public CommonResponse setPassword(String password, Long userId){
        try{
            Boolean flag = this.userService.modifyPassword(password, userId);
            if (flag){
                return CommonResponse.success("修改成功");
            }
        }catch (Exception e){
            throw e;
        }
        return CommonResponse.fail("修改失败");
    }

    /**
     * fetch 获取用户信息
     * @author: tianliya
     * @time: 2018/10/23
     * @param userId
     * @return
     */
    @GetMapping("/getUserInfo")
    public CommonResponse getUserInfo( Long userId){
        try{
            UserInfoVO userInfoVO = this.userService.getUserInfoById( 8L);
            if (userInfoVO != null){
                return CommonResponse.success("查询成功",userInfoVO);
            }
        }catch (Exception e){
            throw e;
        }
        return CommonResponse.fail("查询失败");
    }
}
