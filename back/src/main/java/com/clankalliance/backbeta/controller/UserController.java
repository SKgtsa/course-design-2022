package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.TokenCheckRequest;
import com.clankalliance.backbeta.request.UserLoginRequest;
import com.clankalliance.backbeta.request.UserSaveRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;


    @PostMapping("/login")
    public CommonResponse login(@RequestBody UserLoginRequest request){
        return userService.handleLogin(request.getUserNumber(), request.getPassword());
    }

    @PostMapping("/register")
    public CommonResponse register(@RequestBody UserSaveRequest request){
        return userService.handleRegister(request.getUserNumber(), request.getName(), request.getPassword(), request.getPhone());
    }

    @PostMapping("/tokenCheck")
    public CommonResponse tokenCheck(@RequestBody TokenCheckRequest request){
        return tokenUtil.tokenCheck(request.getToken());
    }



}
