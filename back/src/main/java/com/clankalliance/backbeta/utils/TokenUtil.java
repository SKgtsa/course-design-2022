package com.clankalliance.backbeta.utils;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.ManipulateUtil;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.StatusNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class TokenUtil {


    @Resource
    private UserService userService;

    /**
     * 验证token是否有效，同时返回新token及用户对象
     * @param token 前台传来的token
     * @return
     */
    public CommonResponse tokenCheck(String token){
        CommonResponse response = new CommonResponse();
        StatusNode status =  ManipulateUtil.findStatusByToken(token);
        if(status.getToken() != null){
            //正常查找到状态
            ManipulateUtil.appendStatus(status.getUserId());
            response.setSuccess(true);
            response.setToken(ManipulateUtil.endNode.getToken());
            response.setUser(userService.findById(status.getUserId()));
        }else{
            response.setSuccess(false);
            response.setMessage("登录失效");
        }
        return response;
    }

    public CommonResponse phoneCodeCheck(long phone, String code){
        CommonResponse response = new CommonResponse();
        User user = userService.findByPhone(phone);
        if(user != null){
            long userId = user.getId();
            StatusNode status = ManipulateUtil.findStatusByUserId(userId);
            if(status.getVerifyCode().equals(code)){
                ManipulateUtil.updateStatus(userId);
                response.setSuccess(true);
                response.setToken(ManipulateUtil.endNode.getToken());
                response.setMessage("登陆成功");
                return response;
            }
        }
        response.setSuccess(false);
        response.setMessage("登陆失败");
        return response;
    }

}
