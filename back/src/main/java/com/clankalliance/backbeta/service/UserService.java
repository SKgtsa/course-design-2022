package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface UserService {

    CommonResponse handleLogin(long userNumber,String password);

    CommonResponse handleRegister(long userNumber, String name, String password, long phone);

    CommonResponse handleAvatar(long userNumber,)
}
