package com.clankalliance.backbeta.controller;



import com.clankalliance.backbeta.request.*;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/studentSave")
    public CommonResponse saveStudent(@RequestBody StudentCourseEditRequest request){
        return courseService.handleStudentSave(request.getToken(), request.getCourseId(), request.getTeacherId(),request.getStudentSection(),request.getStudentClass(),request.getYear(),request.getSemester());
    }

    @PostMapping("/teacherSave")
    public CommonResponse saveTeacher(@RequestBody TeacherCourseEditRequest request){
        return courseService.handleTeacherSave(request.getToken(), request.getCourseId(),request.getName(),request.getWeekStart(),request.getWeekEnd(),request.getTime(),request.getCapacity(),request.getStudentClass(),request.getStudentSection(),request.getLocation(),request.getYear(),request.getSemester(),request.getCredit());
    }

}
