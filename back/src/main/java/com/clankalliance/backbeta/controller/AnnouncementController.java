package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.CommonAnnouncementRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @PostMapping("/getMain")
    public CommonResponse getMain(@RequestBody CommonAnnouncementRequest request){
        return announcementService.handleGet(request.getToken());
    }

    @PostMapping("/getDetail")
    public CommonResponse getDetail(@RequestBody CommonAnnouncementRequest request){
        return announcementService.handleGetDetail(request.getToken(), request.getId());
    }

    @PostMapping("/push")
    public CommonResponse push(@RequestBody CommonAnnouncementRequest request){
        return announcementService.handlePushAnnouncement(request.getToken(), request.getPicture(), request.getHeading(), request.getContent());
    }

}