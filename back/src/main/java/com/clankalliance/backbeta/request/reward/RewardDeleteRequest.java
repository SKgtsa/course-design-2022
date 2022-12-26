package com.clankalliance.backbeta.request.reward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardDeleteRequest {
    private long rewardId;

    private String token;
}
