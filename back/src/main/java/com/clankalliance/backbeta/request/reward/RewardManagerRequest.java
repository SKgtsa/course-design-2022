package com.clankalliance.backbeta.request.reward;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardManagerRequest {
    private long id;

    private String token;
}
