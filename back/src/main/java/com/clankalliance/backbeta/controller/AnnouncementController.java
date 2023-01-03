package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.CommonAnnouncementRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public CommonResponse push(@RequestParam("picture") MultipartFile picture,@RequestParam("token") String token, @RequestParam("heading") String heading, @RequestParam("content") String content){
        return announcementService.handlePushAnnouncement(token, picture, heading, content);
    }

    @PostMapping("/getAll")
    public CommonResponse getAll(@RequestBody CommonAnnouncementRequest request){
        return announcementService.handleGetAll(request.getToken());
    }

    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody CommonAnnouncementRequest request){
        return announcementService.handleDelete(request.getToken(), request.getId());
    }

}
