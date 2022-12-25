package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface PracticeService {
    CommonResponse handleSave(String token, long id, String name, String description);

    CommonResponse handleDelete(String token, long id);

    CommonResponse handleFind(String token,Integer pageNum,Integer pageSize);
}
