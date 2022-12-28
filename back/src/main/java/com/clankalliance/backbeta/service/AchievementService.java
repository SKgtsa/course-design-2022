package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface AchievementService {

    CommonResponse handleEvaluate(String token, Long userId, String name, String description);

}
