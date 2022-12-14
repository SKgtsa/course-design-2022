package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.GeneralUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@CrossOrigin
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
