package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface BlogService {

    //发帖
    CommonResponse handleSubmit(String token, String heading, String content);
    CommonResponse handleMine(String token, int length, int startIndex);
    //获取首页信息 好友帖子 按时间顺序分页
    CommonResponse handleMainPage(String token, int length, int startIndex);
    //查看详细文章 前端传来token和文章id 获取全文
    CommonResponse handleDetailPage(String token,int blogId);
    //点赞
    CommonResponse handleLike(String token, int blogId);
    //评论
    CommonResponse handleComment(String token, int blogId, String content);

}
