package com.clankalliance.backbeta.controller;



import com.clankalliance.backbeta.entity.CourseSelectTime;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.CourseSelectTimeRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.request.course.*;
import com.clankalliance.backbeta.request.user.TokenCheckRequest;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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
    public CommonResponse selectCourse(@RequestBody StudentCourseSaveRequest request){
        CommonResponse tokenResponse = tokenUtil.tokenCheck(request.getToken());
        if(!tokenResponse.getSuccess())
            return tokenResponse;
        Student student = studentRepository.findUserById(Long.valueOf(tokenResponse.getMessage())).get();
        CommonResponse response = courseService.bind(request.getCourseId(),student);
        response.setToken(tokenResponse.getToken());
        return response;
    }

    //学生退课
    @PostMapping("/quit")
    public CommonResponse quitCourse(@RequestBody StudentCourseSaveRequest request){
        return courseService.handleQuit(request.getToken(),request.getCourseId());
    }

    //老师或管理员给课添加新学生
    @PostMapping("/addStudent")
    public CommonResponse addStudent(@RequestBody EditCourseStudentRequest request){
        CommonResponse tokenResponse = tokenUtil.tokenCheck(request.getToken());
        if(!tokenResponse.getSuccess())
            return tokenResponse;
        //通过request.getStudentNumber找出id
        Optional<Student> sop = studentRepository.findByUserNumber(request.getStudentNumber());
        if(sop.isEmpty()){
            tokenResponse.setSuccess(false);
            tokenResponse.setMessage("学生不存在");
            return tokenResponse;
        }
        Student student = sop.get();
        CommonResponse response = courseService.bind(request.getCourseId(),student);
        response.setToken(tokenResponse.getToken());
        return response;
    }


    //将学生剔出课程
    @PostMapping("/removeStudent")
    public CommonResponse removeStudent(@RequestBody EditCourseStudentRequest request){
        return courseService.handleRemoveStudent(request.getToken(),request.getCourseId(),request.getStudentNumber());
    }

    //保存课程
    @PostMapping("/save")
    public CommonResponse saveCourse(@RequestBody TeacherCourseSaveRequest request){
        return courseService.handleTeacherSave(request.getToken(),request.getCourseId(),request.getName(),request.getWeekStart(),request.getWeekEnd(),request.getTime(),request.getCapacity(),request.getStudentClass(),request.getStudentSection(),request.getLocation(), request.getYear(), request.getSemester(),request.getCredit(),request.getDescription(),request.getWeight());
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
        return courseService.handleFindAllCourse(request.getToken(),request.isFilterOpen(),request.getPageNum(),request.getPageSize());
    }

    //打分时返回该课程所有学生
    @PostMapping("/findCourseStudent")
    public CommonResponse findCourseStudent(@RequestBody FindStudentOfCourseRequest request){
        return courseService.handleFindCourseStudent(request.getToken(),request.getCourseId(),request.getPageNum(),request.getPageSize());
    }

    //教师获取自己所教的课程
    @PostMapping("/teacherFind")
    public CommonResponse teacherFindCourse(@RequestBody TeacherFindCourseRequest request){
        return courseService.handleTeacherFindCourse(request.getToken(), request.getYear(), request.getSemester());
    }

    @PostMapping("/managerFind")
    public CommonResponse managerFind(@RequestBody CourseManagerFindRequest request){
        return courseService.handleManagerFind(request.getToken(), request.getId());
    }

    @PostMapping("/managerSave")
    public CommonResponse managerSave(@RequestBody CourseManagerSaveRequest request){
        return courseService.handleManagerSave(request.getToken(), request.getCourse());
    }

    @PostMapping("/managerDelete")
    public CommonResponse managerDelete(@RequestBody CourseManagerDeleteRequest request){
        return courseService.handleManagerDelete(request.getToken(), request.getId());
    }

    @PostMapping("/managerFindStudent")
    public CommonResponse managerFindStudent(@RequestBody CourseManagerFindRequest request){
        return courseService.handleManagerFindStudent(request.getToken(), request.getId());
    }

    @PostMapping("/managerRemoveStudent")
    public CommonResponse managerRemoveStudent(@RequestBody ManagerRemoveStudentRequest request){
        return courseService.handleManagerRemoveStudent(request.getToken(), request.getStudentId(),request.getCourseId());
    }

    @PostMapping("/managerAddStudent")
    public CommonResponse managerAddStudent(@RequestBody EditCourseStudentRequest request){
        return courseService.handleManagerAddStudent(request.getToken(),request.getCourseId(),request.getStudentNumber());
    }

    @PostMapping("/teacherPersonalFind")
    public CommonResponse teacherPersonalFind(@RequestBody TeacherPersonalFindRequest request){
        return courseService.handlePersonalFind(request.getId());
    }

    @PostMapping("/saveCourseSelectTime")
    public CommonResponse saveCourseSelectTime(@RequestBody SetCourseSelectTimeRequest request){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startTime,endTime;
        try{
            startTime = dateFormat.parse(request.getStartTime());
            endTime = dateFormat.parse(request.getEndTime());
        }catch (Exception e){
            CommonResponse response = new CommonResponse<>();
            response.setSuccess(false);
            response.setMessage("格式错误");
            return response;
        }
        return courseService.handleEditCourseSelectTime(request.getToken(),startTime,endTime);
    }

}
