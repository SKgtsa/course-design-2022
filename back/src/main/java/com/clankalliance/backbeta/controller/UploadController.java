package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.GeneralUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Resource
    private GeneralUploadService generalUploadService;

    @PostMapping("/generalUpload")
    public CommonResponse handleUpload(HttpSession session,
                                       // 路径变量 解决前后端不一致
                                       @RequestParam("file") MultipartFile file, @RequestParam("token") String token) throws IOException{
        return generalUploadService.handleSave(file,token);
    }

}
