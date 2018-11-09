package com.goldeasy.user.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.goldeasy.common.response.CommonResponse;
import com.goldeasy.user.dto.UserBankCardDTO;
import com.goldeasy.user.dto.UserRealNameDTO;
import com.goldeasy.user.entity.UserAddress;
import com.goldeasy.user.entity.UserAlipay;
import com.goldeasy.user.service.UserAddressService;
import com.goldeasy.user.service.UserAlipayService;
import com.goldeasy.user.service.UserBankCardService;
import com.goldeasy.user.service.UserService;
import com.goldeasy.user.vo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: tianliya
 * @date: 2018/10/23
 * @description: 用户相关的controller层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference(timeout = 2000, version = "${dubbo.service.version}", loadbalance = "random")
    private UserService userService;
    @Reference(timeout = 2000, version = "${dubbo.service.version}", loadbalance = "random")
    private UserBankCardService userBankCardService;

    @Reference(timeout = 2000, version = "${dubbo.service.version}", loadbalance = "random")
    private UserAlipayService userAlipayService;

    @Reference(timeout = 2000, version = "${dubbo.service.version}", loadbalance = "random")
    private UserAddressService userAddressService;


    /**
     * fetch 修改密码功能校验旧密码
     *
     * @param oldPassword
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/authOldPassword")
    public CommonResponse authOldPassword(String oldPassword, Long userId) {
        Boolean flag = this.userService.validatePassword(oldPassword, userId);
        if (flag) {
            return CommonResponse.success("密码正确正确");
        } else {
            return CommonResponse.fail("原密码错误");
        }
    }

    /**
     * fetch 修改密码功能设置新密码
     *
     * @param password
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/setPassword")
    public CommonResponse setPassword(String password, Long userId) {
        try {
            Boolean flag = this.userService.modifyPassword(password, userId);
            if (flag) {
                return CommonResponse.success("修改成功");
            }
        } catch (Exception e) {
            throw e;
        }
        return CommonResponse.fail("修改失败");
    }

    /**
     * fetch 获取用户信息
     *
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/getUserInfo")
    public CommonResponse getUserInfo(Long userId) {
        try {
            UserInfoVO userInfoVO = this.userService.getUserInfoById(userId);
            if (userInfoVO != null) {
                return CommonResponse.success("查询成功", userInfoVO);
            }
        } catch (Exception e) {
            throw e;
        }
        return CommonResponse.fail("查询失败");
    }

    /**
     * fetch 获取个人信息
     *
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/getPersonalInfo")
    public CommonResponse getPersonalInfo(Long userId) {
        try {
            UserPersonalVO userPersonalVO = this.userService.getUserHeadImage(userId);
            return CommonResponse.success("查询成功", userPersonalVO);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }


    /**
     * fetch 获取用户昵称
     *
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/getUserNickName")
    public CommonResponse getUserNickName(Long userId) {
        try {
            UserNickNameVO userNickNameVO = this.userService.getUserNickName(userId);
            return CommonResponse.success("查询成功", userNickNameVO);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 修改用户昵称
     *
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/modifyUserNickName")
    public CommonResponse modifyUserNickName(Long userId, String userNickName) {
        try {
            boolean flag = this.userService.updateUserNickName(userId, userNickName);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.success("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 修改用头像
     *
     * @param userId
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/modifyUserHeadImage")
    public CommonResponse modifyUserHeadImage(Long userId, String userHeadImage) {
        try {
            boolean flag = this.userService.updateUserHeadImage(userId, userHeadImage);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.success("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 获取开户行列表
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/getBankList")
    public CommonResponse getBankList() {
        try {
            List<SysBankVO> sysBankVOList = this.userBankCardService.listSysBank();
            return CommonResponse.success("查询成功", sysBankVOList);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 获取用户银行卡列表
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @GetMapping("/getUserBankList")
    public CommonResponse getUserBankList(Long userId) {
        try {
            List<UserBankVO> userBankVOList = this.userBankCardService.listUserBank(userId);
            return CommonResponse.success("查询成功", userBankVOList);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 绑定用户银行卡
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/bindUserBank")
    public CommonResponse bindUserBank(UserBankCardDTO userBankCardDTO, Long userId) {
        try {
            boolean flag = this.userBankCardService.addUserBankCard(userBankCardDTO, userId);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }

    }

    /**
     * fetch 删除用户银行卡
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/deleteUserBank")
    public CommonResponse deleteUserBank(Long id) {
        try {
            boolean flag = this.userBankCardService.deleteUserBankCard(id);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }


    /**
     * fetch 绑定用户支付宝
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/bindAliPay")
    public CommonResponse bindAliPay(UserAlipay userAlipay, Long userId) {
        try {
            boolean flag = this.userAlipayService.bindUserAlipay(userAlipay, userId);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 绑定用户支付宝
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */

    @GetMapping("/getUserAliPay")
    public CommonResponse getUserAliPay(Long userId) {
        try {
            UserAlipayVO userAlipayVO = this.userAlipayService.getUserAlipay(userId);
            return CommonResponse.success("操作成功", userAlipayVO);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 修改支付宝信息
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/modifyAliPay")
    public CommonResponse modifyAliPay(UserAlipay userAlipay) {
        try {
            boolean flag = this.userAlipayService.modifyUserAlipay(userAlipay);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }


    /**
     * fetch 用户实名认证信息
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */

    @GetMapping("/realNameAuthInfo")
    public CommonResponse realNameAuthInfo(Long userId) {
        try {
            UserRealNameAuthVO userRealNameAuthVO = this.userService.getUserRealNameInfo(userId);
            return CommonResponse.success("操作成功", userRealNameAuthVO);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 用户实名认证申请
     *
     * @return
     * @author: tianliya
     * @time: 2018/10/23
     */
    @PostMapping("/realNameAuthApply")
    public CommonResponse realNameAuthApply(UserRealNameDTO userRealNameDTO, Long userId) {
        try {
            boolean flag = this.userService.realNameAuth(userRealNameDTO, userId);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 添加收货地址
     *
     * @return
     * @author:
     * @time: 2018/10/23
     */
    @PostMapping("/addUserAddress")
    public CommonResponse addUserAddress(UserAddress userAddress, Long userId) {
        try {
            boolean flag = this.userAddressService.addUserAddress(userAddress, userId);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 添加收货地址
     *
     * @return
     * @author:
     * @time: 2018/10/23
     */
    @PostMapping("/deleteUserAddress")
    public CommonResponse deleteUserAddress(Long id) {
        try {
            boolean flag = this.userAddressService.deleteUserAddressById(id);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 修改收货地址
     *
     * @return
     * @author:
     * @time: 2018/10/23
     */
    @PostMapping("/modifyUserAddress")
    public CommonResponse modifyUserAddress(UserAddress userAddress) {
        try {
            boolean flag = this.userAddressService.modifyUserAddressById(userAddress);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

    /**
     * fetch 设置收货地址
     *
     * @return
     * @author:
     * @time: 2018/10/23
     */
    @PostMapping("/setDefaultAddress")
    public CommonResponse setDefaultAddress(Long id, Long userId) {
        try {
            boolean flag = this.userAddressService.setDefaultAddress(id, userId);
            if (flag) {
                return CommonResponse.success("操作成功");
            } else {
                return CommonResponse.fail("操作失败");
            }
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }


    /**
     * fetch 设置收货地址
     *
     * @return
     * @author:
     * @time: 2018/10/23
     */
    @PostMapping("/listUserAddress")
    public CommonResponse listUserAddress(Long userId) {
        try {
            List<UserAddressVO> userAddressList = this.userAddressService.listUserAddress(userId);
           return CommonResponse.success("成功",userAddressList);
        } catch (Exception e) {
            return CommonResponse.error("系统异常");
        }
    }

}
