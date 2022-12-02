package com.clankalliance.backbeta.controller;



import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.request.*;
import com.clankalliance.backbeta.request.course.CourseDeleteRequest;
import com.clankalliance.backbeta.request.course.EditCourseStudentRequest;
import com.clankalliance.backbeta.request.course.FindStudentOfCourseRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TokenUtil tokenUtil;

    //学生选课
    @PostMapping("/select")
    public CommonResponse selectCourse(@RequestBody StudentCourseEditRequest request){
        CommonResponse response = tokenUtil.tokenCheck(request.getToken());
        if(!response.getSuccess())
            return response;
        Student student = studentRepository.findUserById(Long.valueOf(response.getMessage())).get();
        return courseService.bind(request.getCourseId(),student);
    }

    //学生退课
    @PostMapping("/quit")
    public CommonResponse quitCourse(@RequestBody StudentCourseEditRequest request){
        return courseService.handleQuit(request.getToken(),request.getCourseId());
    }

    //老师或管理员给课添加新学生
    @PostMapping("/addStudent")
    public CommonResponse addStudent(@RequestBody EditCourseStudentRequest request){
        CommonResponse response = tokenUtil.tokenCheck(request.getToken());
        if(!response.getSuccess())
            return response;
        //通过request.getStudentNumber找出id
        Student student = studentRepository.findByUserNumber(request.getStudentNumber()).get();
        return courseService.bind(request.getCourseId(),student);
    }


    //将学生剔出课程
    @PostMapping("/removeStudent")
    public CommonResponse removeStudent(@RequestBody EditCourseStudentRequest request){
        return courseService.handleRemoveStudent(request.getToken(),request.getCourseId(),request.getStudentNumber());
    }

    //保存课程
    @PostMapping("/save")
    public CommonResponse saveCourse(@RequestBody TeacherCourseEditRequest request){
        return courseService.handleTeacherSave(request.getToken(),request.getCourseId(),request.getName(),request.getWeekStart(),request.getWeekEnd(),request.getTime(),request.getCapacity(),request.getStudentClass(),request.getStudentSection(),request.getLocation(), request.getYear(), request.getSemester(),request.getCredit(),request.getDescription());
    }

    //删除课程
    @PostMapping("/delete")
    public CommonResponse deleteCourse(@RequestBody CourseDeleteRequest request){
        return courseService.handleDelete(request.getToken(),request.getCourseId());
    }

    //查看课表
    @PostMapping("/findCourseList")
    public CommonResponse findCourseList(@RequestBody TokenCheckRequest request){
        return courseService.handleFindCourseList(request.getToken());
    }

    //选课时候用来返回所有课程
    @PostMapping("/findAllCourse")
    public CommonResponse findAllCourse(@RequestBody FindAllCourseRequest request){
        return courseService.handleFindAllCourse(request.getToken(),request.isFilterOpen());
    }

    //打分时返回该课程所有学生
    @PostMapping("/findCourseStudent")
    public CommonResponse findCourseStudent(@RequestBody FindStudentOfCourseRequest request){
        return courseService.handleFindCourseStudent(request.getToken(),request.getCourseId());
    }


}
