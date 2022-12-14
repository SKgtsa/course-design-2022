package com.clankalliance.backbeta.controller;


import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.TencentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Resource
    private TencentService tencentService;

    @PostMapping("/send")
    public CommonResponse send(@RequestBody Map<String,String> request){
        System.out.println(request.get("phone") + " and code : " + request.get("code"));
        return tencentService.sendSms(request.get("phone"),request.get("code"));
    }

}
