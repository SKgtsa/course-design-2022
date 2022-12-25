package com.clankalliance.backbeta.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T1,T2> {

    //业务成功与失败
    private Boolean success = true;
    //返回信息
    private String message;
    //返回用户对象
    private T2 user;
    //返回泛型数据 自定义类型
    private T1 content;

    private String token;

    private Integer character;

    private Integer startIndex;

    private Integer totalPage;

}
