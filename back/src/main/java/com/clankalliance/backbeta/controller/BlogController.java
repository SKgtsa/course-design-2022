package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.blog.BlogSubmitRequest;
import com.clankalliance.backbeta.request.blog.CommonBlogRequest;
import com.clankalliance.backbeta.request.blog.MainRequest;
import com.clankalliance.backbeta.request.score.ScoreEditRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @PostMapping("/submit")
    public CommonResponse submit(@RequestBody BlogSubmitRequest request){
        return blogService.handleSubmit(request.getToken(), request.getHeading(), request.getContent(), request.getTopImage());
    }

    @PostMapping("/getMine")
    public CommonResponse getMine(@RequestBody MainRequest request){
        return blogService.handleMine(request.getToken(), request.getLength(), request.getStartIndex());
    }

    @PostMapping("/getMain")
    public CommonResponse getMain(@RequestBody MainRequest request){
        return blogService.handleMainPage(request.getToken(), request.getLength(), request.getStartIndex());
    }

    @PostMapping("/getDetail")
    public CommonResponse getDetail(@RequestBody CommonBlogRequest request){
        return blogService.handleDetailPage(request.getToken(), request.getBlogId());
    }

    @PostMapping("/like")
    public CommonResponse like(@RequestBody CommonBlogRequest request){
        return blogService.handleLike(request.getToken(), request.getBlogId());
    }

    @PostMapping("/comment")
    public CommonResponse comment(@RequestBody CommonBlogRequest request){
        return blogService.handleComment(request.getToken(), request.getBlogId(), request.getContent());
    }

    @PostMapping("/test")
    public CommonResponse save(@RequestBody ScoreEditRequest request){
        return new CommonResponse<>();
    }

}
