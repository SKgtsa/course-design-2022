package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ActivityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    /**
     * 保存课外活动
     * 验证token,若id为空(null)则创建，绑定到该token对应用户，否则覆盖原数据
     * @param token 用户令牌
     * @param id 课外活动id(可为空)
     * @param name 课外活动名
     * @param description 活动描述
     * @param date 活动时间(日期字符串)
     * @param result 成果
     * @return
     */
    @Override
    public CommonResponse handleSave(String token, long id,String name,String description,String date,String result){
        return null;
    }

    /**
     * 删除课外活动
     * 验证token,验证登陆状态
     * 根据id找到课外活动并删除
     * @param token 用户令牌
     * @param id 课外活动id
     * @return
     */
    @Override
    public CommonResponse handleDelete(String token, long id){
        return null;
    }

    /**
     * 查找课外活动
     * 验证token,验证登陆状态并找到用户对象
     * 分页返回用户课外活动List中对象
     * @param token 用户令牌
     * @param pageNum 页码(从1开始)
     * @param pageSize 页容量
     * @return
     */
    @Override
    public CommonResponse handleFind(String token,Integer pageNum,Integer pageSize){
        return null;
    }

}
