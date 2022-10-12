package com.clankalliance.backbeta.request;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用户信息保存请求

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {
    private long id;

    private long userNumber;

    private String name;

    private String password;

    private long phone;
}
