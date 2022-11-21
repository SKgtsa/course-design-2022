package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.ScoreEditRequest;
import com.clankalliance.backbeta.request.ScoreFindRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ScoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Resource
    private ScoreService scoreService;

    @PostMapping("/save")
    public CommonResponse save(@RequestBody ScoreEditRequest request){
        return scoreService.handleSave(request.getToken(), request.getCourseId(), request.getStudentId(), request.getDailyScore(), request.getEndScore(), request.getWeight());
    }

    @PostMapping("/find")
    public CommonResponse save(@RequestBody ScoreFindRequest request){
        return scoreService.handleFind(request.getToken(), request.getCourseId(), request.getYear(), request.getSemester());
    }
/*
    @PostMapping("/test")
    public CommonResponse test(@RequestBody Map<String,String> request){
        String stringDataA = request.get("stringDataA");
        System.out.println("stringDataA: " + stringDataA);
        Integer intDataA = Integer.parseInt(request.get("intDataA"));
        System.out.println("intDataA: " + intDataA);
        return null;
    }
*/
}
