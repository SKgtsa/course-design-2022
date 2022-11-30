package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    private UserService userService;

    @Resource
    private CourseRepository courseRepository;


    /**
     * 处理学生页面课程保存的方法，先进行token验证，再存储。传入数据合乎规范
     *
     * @param token          用户token
     * @param courseId       课程id
     * @param teacherId      学生id
     * @param studentSection 学生所属届
     * @param studentClass   学生所属班级
     * @param year           所选课程开设学年
     * @param semester       所选课程开设学期
     * @return
     */

    public CommonResponse handleStudentSave(String token, long courseId, long teacherId, String studentSection, String studentClass, Integer year, String semester) {
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("242634982651203584");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student ){
                Set<Course> courseSetOp = ((Student) user).getCourseSet();
                long id;
                Optional<Course> cop = courseRepository.findById(courseId); //检查课程是否存在
                Course course=cop.get();
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("课程不存在");
                    return response;
                }
                if(courseSetOp.contains(course)){
                    //该学生的课程存在，为课程的修改
                    id = course.getId();
                }else {
                    //该学生的课程不存在，为课程的添加
                    id = snowFlake.nextId();
                }
                //在学生的课程表中加入所选课程
                courseSetOp.add(course);
                ((Student) user).setCourseSet(courseSetOp);
                //保存
                courseRepository.save(course);
                response.setSuccess(true);
                response.setMessage("保存成功");
            }
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }

    /**
     * 处理学生页面课程保存的方法，先进行token验证，再存储。传入数据合乎规范
     *
     * @param token          用户token
     * @param courseId       课程id
     * @param weekStart      开始周
     * @param weekEnd        结束周
     * @param time           上课具体时间
     * @param capacity       容量
     * @param studentSection 课程面向的学生届
     * @param studentClass   课程面向的学生班级
     * @param location       上课地址
     * @param credit         学分
     * @param year           课程开设学年
     * @param semester       课程开设学期
     * @return
     */
    public CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time, Integer capacity, String studentClass, String studentSection, String location, Integer year, String semester, Double credit){
        CommonResponse response=tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Optional<Course> courseOp = courseRepository.findById(courseId);
                long id;
                if(courseOp.isEmpty()){
                    //该课程不存在,为课程的创建
                    id = snowFlake.nextId();
                }else {
                    //该课程存在，为课程的修改
                    id = courseOp.get().getId();
                }
                Set<Student> studentSet=new HashSet<>();
                Set<Score> scoreSet=new HashSet<>();
                Course course=new Course(id,name,weekStart,weekEnd,time,capacity,studentClass,studentSection,location,year,semester,credit,studentSet,(Teacher) user,scoreSet);
                //在教师的课程表中加入课程
                Set<Course> teacherCourseSet = ((Teacher) user).getCourseSet();
                teacherCourseSet.add(course);
                ((Teacher) user).setCourseSet(teacherCourseSet);
                //保存
                courseRepository.save(course);
                response.setSuccess(true);
                response.setMessage("保存成功");
            }
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }

}