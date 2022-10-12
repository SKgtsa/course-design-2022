package com.clankalliance.backbeta.request;

//用户登录请求对象

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    private long userNumber;

    private String password;
}
