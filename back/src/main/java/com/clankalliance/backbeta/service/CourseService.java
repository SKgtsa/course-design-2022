package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.response.CommonResponse;

public interface CourseService {

    CommonResponse handleSave(String token, long courseId, long teacherId, String studentSection, String studentClass);



}
