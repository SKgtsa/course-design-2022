package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PublicationService {

    CommonResponse handleSubmit(String token, String heading, String content, MultipartFile topImage);

    CommonResponse handlePersonalPageData(String token, Long userId);

    CommonResponse handlePersonalPagePublication(String token, int length, int startIndex,Long userId);

    CommonResponse handleDetailPage(String token,String publicationId);

    CommonResponse handleDelete(String token, String publicationId);


}
