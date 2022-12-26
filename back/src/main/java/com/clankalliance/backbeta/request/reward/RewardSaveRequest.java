package com.clankalliance.backbeta.request.reward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardSaveRequest {
    private long id;

    private String token;

    private String rewardName;

    private String rewardDescription;
}
