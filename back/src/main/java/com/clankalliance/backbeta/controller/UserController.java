package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.*;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@CrossOrigin
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
        return userService.handleRegister(request.getIdentity(), request.getCode(), request.getPhone(), request.getUserNumber(), request.getPassword(),request.getName(),request.getStudentClass(),request.getIdCardNumber(),request.getGender(),request.getEthnic(),request.getPoliticalAffiliation(), request.getEMail());
    }

    @PostMapping("/tokenCheck")
    public CommonResponse tokenCheck(@RequestBody TokenCheckRequest request){
        return tokenUtil.tokenCheck(request.getToken());
    }

    @PostMapping("/loginPhone")
    public CommonResponse handlePhoneLogin(@RequestBody PhoneCheckRequest request){
        return userService.handlePhoneLogin(request.getPhone());
    }

    @PostMapping("/loginCode")
    public CommonResponse handleCodeLogin(@RequestBody PhoneCheckRequest request){
        return userService.handleCodeLogin(request.getPhone(), request.getCode());
    }

    @PostMapping("/registerPhone")
    public CommonResponse handlePhoneRegister(@RequestBody PhoneCheckRequest request){
        return userService.handlePhoneRegister(request.getPhone());
    }




}
