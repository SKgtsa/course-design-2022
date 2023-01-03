package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {

    //发帖
    CommonResponse handleSubmit(String token, String heading, String content, MultipartFile topImage);
    CommonResponse handleMine(String token, int length, int startIndex);
    //获取所有帖子 按时间顺序分页
    CommonResponse handleMainPage(String token, int length, int startIndex);
    //获取关注者的帖子 按时间顺序分页
    CommonResponse handleLikePost(String token, int length, int startIndex);
    //获取收藏的帖子 按时间顺序分页
    CommonResponse handleCollectPost(String token, int length, int startIndex);
    //查看个人主页 若登录状态失效则以游客模式
    CommonResponse handlePersonalPageData(String token, Long userId);
    //查看个人主页 博客更新方法
    CommonResponse handlePersonalPagePost(String token, int length, int startIndex,Long userId);
    //查看详细文章 前端传来token和文章id 获取全文
    CommonResponse handleDetailPage(String token,String blogId);
    //点赞
    CommonResponse handleLike(String token, String blogId);
    //收藏
    CommonResponse handleCollect(String token, String blogId);
    //删除 只能删除自己的以及管理员删除
    CommonResponse handleDelete(String token, String blogId);
    //评论
    CommonResponse handleComment(String token, String blogId, String content);
    //关注
    CommonResponse handleSubscribe(String token, Long userId);
    //搜索博客
    CommonResponse handleSearch(String token, String heading, int length,int startIndex);
    //删除评论
    CommonResponse handleDeleteComment(String token,String id);

}
