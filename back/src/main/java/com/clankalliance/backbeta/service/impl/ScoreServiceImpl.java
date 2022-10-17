package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.ScoreRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ScoreService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;


@Service
public class ScoreServiceImpl implements ScoreService {

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

    /**
     * 处理成绩保存的方法，先进行token验证，再存储。传入数据合乎规范
     * @param token 用户token
     * @param courseId 课程id
     * @param studentId 学生id
     * @param dailyScore 平时分
     * @param endScore 期末分
     * @param weight 平时分占比
     * @return
     */
    public CommonResponse handleSave(String token, long courseId, long studentId, Integer dailyScore, Integer endScore, Double weight){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token验证成功
            Optional<Score> scoreOp = scoreRepository.findByCourseStudentId(courseId, studentId);
            long id;
            if(scoreOp.isEmpty()){
                //该学生与该课程对应成绩已存在，为成绩的修改
                id = snowFlake.nextId();
            }else{
                //该学生与该课程对应成绩不存在，为成绩的创建
                id = scoreOp.get().getId();
            }
            Optional<Student> studentOp = studentRepository.findUserById(studentId);
            if(studentOp.isEmpty()){
                response.setSuccess(false);
                response.setMessage("学生不存在");
                return response;
            }
            Optional<Course> cop = courseRepository.findById(courseId);
            if(cop.isEmpty()){
                response.setSuccess(false);
                response.setMessage("课程不存在");
                return response;
            }
            Student student = studentOp.get();
            Course course = cop.get();
            Score score = new Score(id, dailyScore, endScore, weight, studentOp.get(), cop.get());
            //在学生的成绩表中加入成绩
            Set<Score> studentScoreSet = student.getScoreSet();
            studentScoreSet.add(score);
            student.setScoreSet(studentScoreSet);
            //在课程的成绩表中加入成绩
            Set<Score> courseScoreSet = course.getScoreSet();
            courseScoreSet.add(score);
            course.setScoreSet(courseScoreSet);
            //保存
            studentRepository.save(student);
            courseRepository.save(courseScoreSet);
            scoreRepository.save(score);
            response.setSuccess(true);
            response.setMessage("保存成功");
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }

}
