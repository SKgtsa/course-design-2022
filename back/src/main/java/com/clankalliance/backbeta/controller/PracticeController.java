package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.practice.PracticeDeleteRequest;
import com.clankalliance.backbeta.request.practice.PracticeFindRequest;
import com.clankalliance.backbeta.request.practice.PracticeSaveRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.PracticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/practice")
public class PracticeController {
    @Resource
    private PracticeService practiceService;

    //保存社会实践
    @PostMapping("/save")
    public CommonResponse save(@RequestBody PracticeSaveRequest request){
        return practiceService.handleSave(request.getToken(),request.getId(), request.getPracticeName(),request.getPracticeDescription());
    }

    //删除社会实践
    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody PracticeDeleteRequest request){
        return practiceService.handleDelete(request.getToken(),request.getPracticeId());
    }

    //查找社会实践
    @PostMapping("/find")
    public CommonResponse find(@RequestBody PracticeFindRequest request){
        return practiceService.handleFind(request.getToken(),request.getPageNum(),request.getPageSize());
    }
}
