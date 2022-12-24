package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.user.TokenCheckRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 头像控制器
 * 用于控制头像的删改查
 */
@CrossOrigin
@RestController
@RequestMapping("/api/upload")
public class AvatarController {


    @Resource
    private AvatarService avatarService;




    @RequestMapping("/changeAvatar")
    public CommonResponse changeAvatar(HttpSession session,
                                       // 路径变量 解决前后端不一致
                                       @RequestParam("avatar") MultipartFile file,@RequestParam("token") String token) throws IOException {
        return avatarService.handleSave(file,token);
    }

    @RequestMapping("/getAvatar")
    public CommonResponse getAvatar(@RequestBody TokenCheckRequest request){
        return avatarService.handleGet(request.getToken());
    }


}
