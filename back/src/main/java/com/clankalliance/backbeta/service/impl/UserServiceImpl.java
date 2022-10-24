package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.ManipulateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private SnowFlake snowFlake;

    @Override
    public CommonResponse handleLogin(long userNumber,String password) {
        CommonResponse  response = new CommonResponse<>();
        response.setSuccess(false);
        password = DigestUtils.sha1Hex(password);
        Optional<User> uop = userRepository.findByUserNumber(userNumber);
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
        ManipulateUtil.updateStatus(user.getId());
        response.setToken(ManipulateUtil.endNode.getToken());
        userRepository.save(user);
        response.setUser(user);
        return response;
    }

    @Override
    public CommonResponse handleRegister(long userNumber, String name, String password, long phone) {
        password = DigestUtils.sha1Hex(password.getBytes());
        CommonResponse  response = new CommonResponse();
        Optional<User> uop = userRepository.findByUserNumber(userNumber);
        if(uop.isPresent()){
            //该用户已存在 进行判定是统一注册的空号还是用户重复注册
            response.setSuccess(false);
            response.setMessage("账户已存在");
            return response;
        }
        long id = snowFlake.nextId();
        User user = new Student(id, userNumber,name,password,phone);
        try{
            userRepository.save(user);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage("请填写完整");
            return response;
        }
        ManipulateUtil.updateStatus(id);
        response.setSuccess(true);
        response.setMessage("注册成功");
        response.setUser(user);
        response.setToken(ManipulateUtil.endNode.getToken());
        System.out.println(response);
        return response;
    }
}
