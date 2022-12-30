package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.response.CommonResponse;

public interface ScoreService {

    Achievement getPOINT_A();

    CommonResponse handleSave(String token, long courseId, long studentId, Integer dailyScore, Integer endScore, Double weight);

    CommonResponse handleFind(String token, Integer Year, String Semester);

    CommonResponse handleFindDetail(String token,long courseId);

}
