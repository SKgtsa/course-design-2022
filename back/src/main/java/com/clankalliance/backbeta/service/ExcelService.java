package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    CommonResponse handleBatchRegister(MultipartFile file, String token);

    CommonResponse handleBatchScore(MultipartFile file, String token, Long courseId);
}
