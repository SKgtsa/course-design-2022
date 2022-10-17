package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface ScoreService {

    CommonResponse handleSave(String token, long courseId, long studentId, Integer dailyScore, Integer endScore, Double weight);

}
