package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AnnouncementService {
    //获得前五个公告
    CommonResponse handleGet(String token);
    //获取正文
    CommonResponse handleGetDetail(String token, Long id);
    //新建公告
    CommonResponse handlePushAnnouncement(String token, MultipartFile picture, String heading, String content);


}
