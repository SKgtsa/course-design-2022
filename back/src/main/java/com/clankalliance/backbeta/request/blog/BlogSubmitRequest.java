package com.clankalliance.backbeta.request.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSubmitRequest {

    private String token;

    private String heading;

    private String content;

}
