package com.clankalliance.backbeta.utils;

import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.ManipulateUtil;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.StatusNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;



@Component
public class TokenUtil {

    @Resource
    private UserRepository userRepository;

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
            response.setUser(userRepository.findUserById(status.getUserId()).get());
        }else{
            response.setSuccess(false);
            response.setMessage("登录失效");
        }
        return response;
    }

}
