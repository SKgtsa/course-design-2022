package com.clankalliance.backbeta.request.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeFindRequest {
    private String token;

    private Integer pageNum;

    private Integer pageSize;
}
