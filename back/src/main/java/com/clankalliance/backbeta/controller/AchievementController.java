package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.achievement.AchievementEvaluateRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AchievementService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/achievement")
public class AchievementController {

    @Resource
    private AchievementService achievementService;


    @RequestMapping("/evaluate")
    public CommonResponse evaluate(@RequestBody AchievementEvaluateRequest request){
        return achievementService.handleEvaluate(request.getToken(), request.getUserId(), request.getName(), request.getDescription());
    }

}
