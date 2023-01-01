package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.score.ScoreEditRequest;
import com.clankalliance.backbeta.request.score.FindScoreRequest;
import com.clankalliance.backbeta.request.score.TeacherFindDetailedScoreRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ExcelService;
import com.clankalliance.backbeta.service.ScoreService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/api/score")
public class ScoreController {

    @Resource
    private ScoreService scoreService;

    @Resource
    private ExcelService excelService;

    //创建及修改成绩
    //weight可为空 为空时，为修改 修改成绩无法修改weight
    @PostMapping("/save")
    public CommonResponse save(@RequestBody ScoreEditRequest request){
        return scoreService.handleSave(request.getToken(), request.getCourseId(), request.getStudentId(), request.getDailyScore(), request.getEndScore());
    }

    //学生返回所有课程的分数 需返回排名rank
    @PostMapping("/studentFind")
    public CommonResponse studentFind(@RequestBody FindScoreRequest request){
        return scoreService.handleFind(request.getToken(), request.getYear(), request.getSemester());
    }
    //老师返回所有课程各自的平均分
    @PostMapping("/teacherFind")
    public CommonResponse teacherFind(@RequestBody FindScoreRequest request){
        return scoreService.handleFind(request.getToken(), request.getYear(), request.getSemester());
    }

    //老师查看自己某一门课的所有同学的分数
    @PostMapping("/teacherDetail")
    public CommonResponse teacherDetail(@RequestBody TeacherFindDetailedScoreRequest request){
        return scoreService.handleFindDetail(request.getToken(),request.getCourseId());
    }

    @PostMapping("/batchScore")
    public CommonResponse batchScore(HttpSession session,
                                     // 路径变量 解决前后端不一致
                                     @RequestParam("file") MultipartFile file, @RequestParam("token") String token,@RequestParam("courseId") Long courseId){
        return excelService.handleBatchScore(file,token,courseId);
    }

}
