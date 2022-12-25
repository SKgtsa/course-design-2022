package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    CommonResponse handleGet(String token);

    CommonResponse handleSave(MultipartFile file, String token);

    CommonResponse handleSavePhoto(MultipartFile file, String token);

}
