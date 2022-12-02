package com.clankalliance.backbeta.request.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFindRequest {

    private String token;

    private Integer pageNum;

    private Integer pageSize;
}
