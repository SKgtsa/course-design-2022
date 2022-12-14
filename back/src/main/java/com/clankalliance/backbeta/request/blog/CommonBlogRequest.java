package com.clankalliance.backbeta.request.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonBlogRequest {

    private String token;
    private String blogId;
    private String content;
}
