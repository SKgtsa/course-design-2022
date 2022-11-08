package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Override
    public CommonResponse handleGet(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess()){
            return response;
        }
        User user = userService.findById(Long.valueOf(response.getMessage()));
        response.setContent(user.getAvatarURL());
        response.setMessage("操作成功");
        return response;
    }


}
