package com.clankalliance.backbeta.request.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogSubmitRequest {

    private String token;

    private String heading;

    private String content;

    private MultipartFile topImage;

}
