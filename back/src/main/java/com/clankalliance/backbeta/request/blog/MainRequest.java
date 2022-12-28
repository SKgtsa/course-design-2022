package com.clankalliance.backbeta.request.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//主页请求
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainRequest {

    private String token;
    private int length;
    private int startIndex;
    private Long userId;

}
