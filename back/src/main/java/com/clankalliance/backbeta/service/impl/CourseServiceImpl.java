package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.ScoreRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.service.ScoreService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private ScoreRepository scoreRepository;

    public CommonResponse handleSave( String token, long courseId,long teacherId,String studentSection,String studentClass){
    return null;
    }
}
