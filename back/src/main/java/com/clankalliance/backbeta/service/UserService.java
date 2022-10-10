package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface UserService {

    CommonResponse handleLogin(String loginName,String password);

    CommonResponse handleRegister(String loginName,String password,String name);
}
