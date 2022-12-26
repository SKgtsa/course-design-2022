package com.clankalliance.backbeta.request.reward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardFindRequest {
    private String token;

    private Integer pageNum;

    private Integer pageSize;
}
