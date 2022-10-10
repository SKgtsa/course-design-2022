package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.User;
import com.clankalliance.backbeta.repository.UserRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private SnowFlake snowFlake;

    @Override
    public CommonResponse handleLogin(String loginName,String password) {
        CommonResponse  response = new CommonResponse<>();
        if(loginName.equals("")){
            response.setMessage("请输入登录名");
            return response;
        }
        response.setSuccess(false);
        if(password.equals("")){
            response.setMessage("请输入密码");
            return response;
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        Optional<User> uop = userRepository.findUserByLoginName(loginName);
        if(uop.isEmpty()){
            response.setMessage("用户不存在");
            return response;
        }
        User user = uop.get();
        if(!password.equals(user.getPassword())){
            response.setMessage("密码错误");
            return response;
        }
        response.setSuccess(true);
        response.setMessage("登陆成功");
        long currentTime = System.currentTimeMillis();
        String token = DigestUtils.md5DigestAsHex((currentTime + "" + user.getId()).getBytes());
        response.setToken(token);
        user.setToken(token);
        user.setLastLoginTime(currentTime);
        userRepository.save(user);
        response.setUser(user);
        return response;
    }

    @Override
    public CommonResponse handleRegister(String loginName, String password,String name) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        CommonResponse  response = new CommonResponse<User>();
        Optional<User> uop = userRepository.findUserByLoginName(loginName);
        if(uop.isPresent()){
            response.setSuccess(false);
            response.setMessage("账户已存在");
            return response;
        }
        long id = snowFlake.nextId();
        long loginTime = System.currentTimeMillis();
        String token = DigestUtils.md5DigestAsHex((loginTime + "" + id).getBytes());
        User user = new User(id,loginName,name, password,token,loginTime);
        try{
            userRepository.save(user);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage("请填写完整");
            return response;
        }
        response.setSuccess(true);
        response.setMessage("注册成功");
        response.setUser(user);
        response.setToken(token);
        System.out.println(response);
        return response;
    }
}
