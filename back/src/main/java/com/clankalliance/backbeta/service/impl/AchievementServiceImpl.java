package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.AchievementRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AchievementService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Resource
    private UserService userService;

    @Resource
    private AchievementRepository achievementRepository;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private TokenUtil tokenUtil;


    @Override
    public CommonResponse handleEvaluate(String token, Long userId, String name, String description){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        Optional<Achievement> aop = achievementRepository.findDuplicate(name);
        if(aop.isPresent()){
            //与预置成就重复
            response.setSuccess(false);
            response.setMessage("该成就已内置");
        }else{
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher || user instanceof Student){
                Achievement achievement = new Achievement(snowFlake.nextId(), name, description);
                achievementRepository.save(achievement);
                if(user instanceof Teacher){
                    Teacher teacher = (Teacher) user;
                    List<Achievement> achievementList = teacher.getAchievementList();
                    achievementList.add(achievement);
                    teacher.setAchievementList(achievementList);
                    teacherRepository.save(teacher);
                }else if(user instanceof Student){
                    Student student = (Student) user;
                    List<Achievement> achievementList = student.getAchievementList();
                    achievementList.add(achievement);
                    student.setAchievementList(achievementList);
                    studentRepository.save(student);
                }
                response.setMessage("评价成功");
            }else{
                response.setSuccess(false);
                response.setMessage("权限错误");
            }
        }
        return response;
    }

}
