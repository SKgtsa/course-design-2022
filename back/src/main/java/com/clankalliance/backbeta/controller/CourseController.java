package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.request.StudentCourseEditRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/save")
    public CommonResponse save(@RequestBody StudentCourseEditRequest request){
        return courseService.handleSave(request.getToken(), request.getCourseId(), request.getTeacherId(),request.getStudentSection(),request.getStudentClass());
    }

}
