package com.clankalliance.backbeta.controller;


import com.clankalliance.backbeta.request.activity.ActivityDeleteRequest;
import com.clankalliance.backbeta.request.activity.ActivityFindRequest;
import com.clankalliance.backbeta.request.activity.ActivityManagerRequest;
import com.clankalliance.backbeta.request.activity.ActivitySaveRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ActivityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Resource
    private ActivityService activityService;

    //保存课外活动
    @PostMapping("/save")
    public CommonResponse save(@RequestBody ActivitySaveRequest request){
        return activityService.handleSave(request.getToken(),request.getId(), request.getActivityName(),request.getActivityDescription(),request.getDate(),request.getResult());
    }

    //删除课外活动
    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody ActivityDeleteRequest request){
        return activityService.handleDelete(request.getToken(),request.getActivityId());
    }

    //查找课外活动
    @PostMapping("/find")
    public CommonResponse find(@RequestBody ActivityFindRequest request){
        return activityService.handleFind(request.getToken(),request.getPageNum(),request.getPageSize());
    }

    @PostMapping("/managerFind")
    public  CommonResponse ManagerFind(@RequestBody ActivityManagerRequest request){
        return activityService.handleManagerFind(request.getToken(), request.getId());
    }

    @PostMapping("/managerDelete")
    public CommonResponse MangerDelete(@RequestBody ActivityManagerRequest request){
        return activityService.handleManagerDelete(request.getToken(),request.getId());
    }
}
