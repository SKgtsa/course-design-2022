package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.TokenCheckRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
                                       @RequestParam("file") MultipartFile file,@RequestParam("token") String token) throws IOException {
        return avatarService.handleSave(file,token);
    }

    @RequestMapping("/getAvatar")
    public CommonResponse getAvatar(@RequestBody TokenCheckRequest request){
        return avatarService.handleGet(request.getToken());
    }


}
