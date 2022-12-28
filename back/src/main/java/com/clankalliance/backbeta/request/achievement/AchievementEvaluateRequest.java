package com.clankalliance.backbeta.request.achievement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEvaluateRequest {

    private String token;

    private Long userId;

    private String name;

    private String description;

}
