package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.user.sub.Student;

import java.util.Map;

public interface IntroduceService {
    Map getIntroduceDataMap(Student student);

    String getHtmlCount(Student student);

}
