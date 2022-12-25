package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.reward.RewardDeleteRequest;
import com.clankalliance.backbeta.request.reward.RewardFindRequest;
import com.clankalliance.backbeta.request.reward.RewardSaveRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.RewardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/reward")
public class RewardController {
    @Resource
    private RewardService rewardService;

    //保存成果奖励
    @PostMapping("/save")
    public CommonResponse save(@RequestBody RewardSaveRequest request){
        return rewardService.handleSave(request.getToken(),request.getId(), request.getRewardName(),request.getRewardDescription());
    }

    //删除成果奖励
    @PostMapping("/delete")
    public CommonResponse delete(@RequestBody RewardDeleteRequest request){
        return rewardService.handleDelete(request.getToken(),request.getRewardId());
    }

    //查找成果奖励
    @PostMapping("/find")
    public CommonResponse find(@RequestBody RewardFindRequest request){
        return rewardService.handleFind(request.getToken(),request.getPageNum(),request.getPageSize());
    }
}
