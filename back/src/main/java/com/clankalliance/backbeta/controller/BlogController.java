package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.CommonDeleteRequest;
import com.clankalliance.backbeta.request.blog.BlogSearchRequest;
import com.clankalliance.backbeta.request.blog.CommonBlogRequest;
import com.clankalliance.backbeta.request.blog.MainRequest;
import com.clankalliance.backbeta.request.blog.UserRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.BlogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @PostMapping("/submit")
    public CommonResponse submit(@RequestParam("topImage") MultipartFile topImage,@RequestParam("token") String token, @RequestParam("heading") String heading, @RequestParam("content") String content){
        return blogService.handleSubmit(token, heading, content, topImage);
    }

    @PostMapping("/getMine")
    public CommonResponse getMine(@RequestBody MainRequest request){
        return blogService.handleMine(request.getToken(), request.getLength(), request.getStartIndex(), request.getKeyWord());
    }

    @PostMapping("/getMain")
    public CommonResponse getMain(@RequestBody MainRequest request){
        return blogService.handleMainPage(request.getToken(), request.getLength(), request.getStartIndex(), request.getKeyWord());
    }

    @PostMapping("/getPersonal")
    public CommonResponse getPersonal(@RequestBody MainRequest request){
        return blogService.handlePersonalPageData(request.getToken(), request.getUserId());
    }

    @PostMapping("/getPersonalPost")
    public CommonResponse getPersonalPost(@RequestBody MainRequest request){
        return blogService.handlePersonalPagePost(request.getToken(), request.getLength(), request.getStartIndex(), request.getUserId());
    }

    @PostMapping("/getLike")
    public CommonResponse getLike(@RequestBody MainRequest request){
        return blogService.handleLikePost(request.getToken(), request.getLength(), request.getStartIndex(), request.getKeyWord());
    }

    @PostMapping("/getCollect")
    public CommonResponse getCollect(@RequestBody MainRequest request){
        return blogService.handleCollectPost(request.getToken(), request.getLength(), request.getStartIndex(), request.getKeyWord());
    }

    @PostMapping("/getDetail")
    public CommonResponse getDetail(@RequestBody CommonBlogRequest request){
        return blogService.handleDetailPage(request.getToken(), request.getBlogId());
    }

    @PostMapping("/like")
    public CommonResponse like(@RequestBody CommonBlogRequest request){
        return blogService.handleLike(request.getToken(), request.getBlogId());
    }

    @PostMapping("/collect")
    public CommonResponse collect(@RequestBody CommonBlogRequest request){
        return blogService.handleCollect(request.getToken(), request.getBlogId());
    }

    @PostMapping("/comment")
    public CommonResponse comment(@RequestBody CommonBlogRequest request){
        return blogService.handleComment(request.getToken(), request.getBlogId(), request.getContent());
    }

    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody CommonBlogRequest request){
        return blogService.handleDelete(request.getToken(), request.getBlogId());
    }

    @PostMapping("/subscribe")
    public CommonResponse subscribe(@RequestBody UserRequest request){
        return blogService.handleSubscribe(request.getToken(), request.getUserId());
    }

    @PostMapping("/search")
    public CommonResponse search(@RequestBody BlogSearchRequest request){
        return blogService.handleSearch(request.getToken(),request.getHeading(), request.getLength(), request.getStartIndex());
    }

    //还需要一个删评论的
    @PostMapping("/deleteComment")
    public CommonResponse search(@RequestBody CommonDeleteRequest request){
        return blogService.handleDeleteComment(request.getToken(), request.getId());
    }

}
