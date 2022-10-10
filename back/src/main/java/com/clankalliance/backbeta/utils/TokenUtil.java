package com.clankalliance.backbeta.utils;

import com.clankalliance.backbeta.entity.User;
import com.clankalliance.backbeta.repository.UserRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class TokenUtil {


    @Resource
    UserRepository userRepository;

    //Token更新时间
    @Value("${clankToken.tokenUpdateTime}")
    private static int TOKEN_UPDATE_TIME;

    public String nextToken(long userId){
        return DigestUtils.md5DigestAsHex((System.currentTimeMillis() + "" + userId).getBytes());
    }

    //验证token是否合法
    public CommonResponse tokenCheck(String token){
        System.out.print("旧token: " + token);
        CommonResponse response = new CommonResponse();
        Optional<User> uop = userRepository.findUserByToken(token);
        if(uop.isPresent()){
            User user = uop.get();
            long newLoginTime = System.currentTimeMillis();
            if((newLoginTime - user.getLastLoginTime()) < TOKEN_UPDATE_TIME){
                String newToken = DigestUtils.md5DigestAsHex(("" + newLoginTime + user.getId()).getBytes());
                user.setToken(newToken);
                user.setLastLoginTime(newLoginTime);
                userRepository.save(user);
                response.setSuccess(true);
                response.setToken(newToken);
                response.setUser(user);
                System.out.print("一切正常，新token:" + newToken);
                System.out.println();
                return response;
            }else{
                response.setMessage("登录过期");
                System.out.print("因为token找到用户但登录过期，所以拒绝");
            }
        }else{
            System.out.print("因为该token查不到用户，所以拒绝");
            response.setMessage("您已在别处登录");
        }
        response.setSuccess(false);
        System.out.println("异常退出");
        return response;
    }
}
