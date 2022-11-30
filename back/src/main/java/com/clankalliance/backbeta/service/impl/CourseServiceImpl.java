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


//    /**
//     * 处理学生页面课程保存的方法，先进行token验证，再存储。传入数据合乎规范
//     *
//     * @param token          用户token
//     * @param courseId       课程id
//     * @param teacherId      学生id
//     * @param studentSection 学生所属届
//     * @param studentClass   学生所属班级
//     * @param year           所选课程开设学年
//     * @param semester       所选课程开设学期
//     * @return
//     */
//
//    public CommonResponse handleStudentSave(String token, long courseId, long teacherId, String studentSection, String studentClass, Integer year, String semester) {
//        CommonResponse response ;
//        if(token.equals("114514")){
//            response = new CommonResponse();
//            response.setSuccess(true);
//            response.setMessage("242634982651203584");
//        }else{
//            response = tokenUtil.tokenCheck(token);
//        }
//        if(response.getSuccess()){
//            //token验证成功
//            User user = userService.findById(Long.parseLong(response.getMessage()));
//            if(user instanceof Student ){
//                Set<Course> courseSetOp = ((Student) user).getCourseSet();
//                long id;
//                Optional<Course> cop = courseRepository.findById(courseId); //检查课程是否存在
//                Course course=cop.get();
//                if(cop.isEmpty()){
//                    response.setSuccess(false);
//                    response.setMessage("课程不存在");
//                    return response;
//                }
//                if(courseSetOp.contains(course)){
//                    //该学生的课程存在，为课程的修改
//                    id = course.getId();
//                }else {
//                    //该学生的课程不存在，为课程的添加
//                    id = snowFlake.nextId();
//                }
//                //在学生的课程表中加入所选课程
//                courseSetOp.add(course);
//                ((Student) user).setCourseSet(courseSetOp);
//                //保存
//                courseRepository.save(course);
//                response.setSuccess(true);
//                response.setMessage("保存成功");
//            }
//        }
//        //token无效，直接将token验证的数据返回前端
//        return response;
//    }
//
//    /**
//     * 处理学生页面课程保存的方法，先进行token验证，再存储。传入数据合乎规范
//     *
//     * @param token          用户token
//     * @param courseId       课程id
//     * @param weekStart      开始周
//     * @param weekEnd        结束周
//     * @param time           上课具体时间
//     * @param capacity       容量
//     * @param studentSection 课程面向的学生届
//     * @param studentClass   课程面向的学生班级
//     * @param location       上课地址
//     * @param credit         学分
//     * @param year           课程开设学年
//     * @param semester       课程开设学期
//     * @return
//     */
//    public CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time, Integer capacity, String studentClass, String studentSection, String location, Integer year, String semester, Double credit,String description){
//        CommonResponse response=tokenUtil.tokenCheck(token);
//        if(response.getSuccess()){
//            //token验证成功
//            User user = userService.findById(Long.parseLong(response.getMessage()));
//            if(user instanceof Teacher){
//                Optional<Course> courseOp = courseRepository.findById(courseId);
//                long id;
//                if(courseOp.isEmpty()){
//                    //该课程不存在,为课程的创建
//                    id = snowFlake.nextId();
//                }else {
//                    //该课程存在，为课程的修改
//                    id = courseOp.get().getId();
//                }
//                Set<Student> studentSet=new HashSet<>();
//                Set<Score> scoreSet=new HashSet<>();
//                Course course=new Course(id,name,weekStart,weekEnd,time,capacity,studentClass,studentSection,location,year,semester,credit,description,studentSet,(Teacher) user,scoreSet);
//                //在教师的课程表中加入课程
//                Set<Course> teacherCourseSet = ((Teacher) user).getCourseSet();
//                teacherCourseSet.add(course);
//                ((Teacher) user).setCourseSet(teacherCourseSet);
//                //保存
//                courseRepository.save(course);
//                response.setSuccess(true);
//                response.setMessage("保存成功");
//            }
//        }
//        //token无效，直接将token验证的数据返回前端
//        return response;
//    }

    /**
     * 绑定学生与课程的关系
     * 传入课程id和学生对象，将该课程与学生建立关系
     * @param courseId 课程id
     * @param student 学生对象
     * @return
     */
    public CommonResponse bind(long courseId, Student student){
        return null;
    }

    /**
     * 学生退课
     * 传入token和课程id,先验证token,后用token获得用户信息
     * 解除该用户与课程id对于课程的绑定
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleQuit(String token,long courseId){
        return null;
    }

    /**
     * 将学生从课程中踢出
     * 验证token,判断权限及登录状态
     * 根据学号找到学生，根据课程id找到课程
     * 解除该课程与学生的关联
     * @param token 用户令牌
     * @param courseId 课程id
     * @param studentNumber 学号
     * @return
     */
    public CommonResponse handleRemoveStudent(String token,long courseId,long studentNumber){
        return null;
    }

    /**
     * 保存课程
     * 进行token验证，存在课程则修改，不存在则创建
     * @param token 用户令牌
     * @param courseId 课程id(可为空)
     * @param name 课名
     * @param weekStart 教学开始周
     * @param weekEnd 教学结束周
     * @param time 时间 为list 存放ClassTime对象
     * @param capacity 课容量
     * @param studentClass 授课班级
     * @param studentSection 授课年级
     * @param location 授课地点
     * @param year 授课时间(年
     * @param semester 授课学期
     * @param credit 学分
     * @param description 课程描述
     * @return
     */
    public CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time,Integer capacity,String studentClass,String studentSection,String location,Integer year, String semester,Double credit,String description){
        return null;
    }


    /**
     * 删除课程
     * 验证token,根据课程id找到课程并删除
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleDelete(String token,long courseId){
        return null;
    }

    /**
     * 学生查看课表
     * 验证token,返回该学生当前正在上的课
     * @param token
     * @return
     */
    public CommonResponse handleFindCourseList(String token){
        return null;
    }

    //***************存在问题：分页*********************************************

    /**
     * 选课时用来返回所有课程
     * 验证token,若filterOpen为true则需过滤时间冲突的及已选的课
     * 若filterOpen为false则只需过滤已选的课
     * 该方法分页问题未解决，暂不实现
     * @param token
     * @param filterOpen
     * @return
     */
    public CommonResponse handleFindAllCourse(String token,boolean filterOpen){
        return null;
    }

    /**
     * 打分时返回所选课程的所有学生
     * 验证token,根据courseId找到课程，遍历course的学生列表
     * 只选取名字name id返回前端
     * 该方法分页问题未解决，暂不实现
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleFindCourseStudent(String token,long courseId){
        return null;
    }
}
