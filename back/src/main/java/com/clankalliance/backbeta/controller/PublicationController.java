package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.blog.CommonBlogRequest;
import com.clankalliance.backbeta.request.blog.MainRequest;
import com.clankalliance.backbeta.request.publication.CommonPublicationRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.PublicationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/publication")
public class PublicationController {

    @Resource
    private PublicationService publicationService;

    @PostMapping("/submit")
    public CommonResponse submit(@RequestParam("topImage") MultipartFile topImage, @RequestParam("token") String token, @RequestParam("heading") String heading, @RequestParam("content") String content){
        return publicationService.handleSubmit(token, heading, content, topImage);
    }

    @PostMapping("/getPersonal")
    public CommonResponse getPersonal(@RequestBody MainRequest request){
        return publicationService.handlePersonalPageData(request.getToken(), request.getUserId());
    }

    @PostMapping("/getPersonalPublication")
    public CommonResponse getPersonalPublication(@RequestBody MainRequest request){
        return publicationService.handlePersonalPagePublication(request.getToken(), request.getLength(), request.getStartIndex(), request.getUserId());
    }

    @PostMapping("/getDetail")
    public CommonResponse getDetail(@RequestBody CommonPublicationRequest request){
        return publicationService.handleDetailPage(request.getToken(), request.getPublicationId());
    }

    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody CommonPublicationRequest request){
        return publicationService.handleDelete(request.getToken(), request.getPublicationId());
    }


}
