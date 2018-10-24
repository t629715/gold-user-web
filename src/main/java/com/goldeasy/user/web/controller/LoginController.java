package com.goldeasy.user.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.goldeasy.common.response.CommonResponse;
import com.goldeasy.user.dto.UserLoginDTO;
import com.goldeasy.user.dto.UserRegisterDTO;
import com.goldeasy.user.service.UserService;
import com.goldeasy.user.web.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: tianliya
 * @date: 2018/10/23
 * @description: 用户注册、登陆controller层
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Value("dubbo.version")
    private String serverVersion;
    /**
     * 成功
     */
    private final String SUCCESS="success";
    /**
     * 失败
     */
    private final String FALSE="false";
    /**
     * 已存在
     */
    private final String REG_EXIST="exist";
    /**
     * 调用登陆服务返回的map中的状态key
     */
    private final String LOGIN_STATE = "state";
    /**
     * 调用登陆服务返回的map中的数据key
     */
    private final String LOGIN_DATA = "data";

    @Reference(timeout = 2000,version = "${dubbo.service.version}", loadbalance = "random")
    private UserService userService;

    /**
     * fetch
     * @author: tianliya
     * @time: 2018/10/23
     * @param userRegisterDTO
     * @return 用户注册接口
     */
    @PostMapping("/register")
    public CommonResponse register(UserRegisterDTO userRegisterDTO, HttpServletRequest request){
        userRegisterDTO.setRegisterDevice(HttpUtil.getDevice(request));
        userRegisterDTO.setRegisterIp(HttpUtil.getIp(request));
        String  flag  = this.userService.register(userRegisterDTO);

        if (this.SUCCESS.equals(flag)){
            return CommonResponse.success("注册成功");
        }else if (this.FALSE.equals(flag)){
            return CommonResponse.fail("注册失败");
        }else {
            return CommonResponse.fail("账号已存在");
        }
    }
    /**
     * fetch 用户登陆接口
     * @author: tianliya
     * @time: 2018/10/23
     * @param
     * @return
     */
    @PostMapping("/login")
    public CommonResponse login(UserLoginDTO userLoginDTO, HttpServletRequest request){
        try{
            userLoginDTO.setLoginFrom(HttpUtil.getDevice(request));
            userLoginDTO.setLoginIp(HttpUtil.getIp(request));
            Map map = this.userService.login(userLoginDTO);
            if (!this.SUCCESS.equals(map.get(this.LOGIN_STATE).toString())){
                return CommonResponse.fail(map.get(this.LOGIN_DATA).toString());
            }else {
                return CommonResponse.success("登陆成功",map.get(this.LOGIN_DATA).toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResponse.error("未知异常");
    }
}
