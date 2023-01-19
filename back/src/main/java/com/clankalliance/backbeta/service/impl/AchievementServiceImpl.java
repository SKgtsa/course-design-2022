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
import com.clankalliance.backbeta.utils.StatusManipulateUtils.StatusNode;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Resource
    private UserService userService;

    @Resource
    private AchievementRepository achievementRepository;

    @Resource
    private ActivityServiceImpl activityService;

    @Resource
    private CourseServiceImpl courseService;

    @Resource
    private BlogServiceImpl blogService;

    @Resource
    private PracticeServiceImpl practiceService;

    @Resource
    private RewardServiceImpl rewardService;

    @Resource
    private ScoreServiceImpl scoreService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private TokenUtil tokenUtil;


    @PostConstruct
    private void init(){
        achievementRepository.save(activityService.ACTIVITY_A);
        achievementRepository.save(activityService.ACTIVITY_B);
        achievementRepository.save(activityService.ACTIVITY_C);
        achievementRepository.save(blogService.COLLECT_A);
        achievementRepository.save(blogService.COLLECT_B);
        achievementRepository.save(blogService.COLLECT_C);
        achievementRepository.save(blogService.WRITE_A);
        achievementRepository.save(blogService.WRITE_B);
        achievementRepository.save(blogService.WRITE_C);
        achievementRepository.save(blogService.LIKE);
        achievementRepository.save(courseService.COURSE_A);
        achievementRepository.save(courseService.COURSE_B);
        achievementRepository.save(practiceService.PRACTICE_A);
        achievementRepository.save(practiceService.PRACTICE_B);
        achievementRepository.save(practiceService.PRACTICE_C);
        achievementRepository.save(rewardService.REWARD_A);
        achievementRepository.save(rewardService.REWARD_B);
        achievementRepository.save(rewardService.REWARD_C);
        achievementRepository.save(scoreService.POINT_A);
        achievementRepository.save(scoreService.POINT_B);
        achievementRepository.save(scoreService.POINT_C);
    }

    /**
     * 学生互评 给学生留下评价
     * @param token 用户令牌
     * @param userId 目标用户id
     * @param name 评价名称
     * @param description 评价描述
     * @return
     */
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
            User target = userService.findById(userId);
            if(target instanceof Teacher || target instanceof Student){
                Achievement achievement = new Achievement(snowFlake.nextId(), name, description);
                achievementRepository.save(achievement);
                if(target instanceof Teacher){
                    Teacher teacher = (Teacher) target;
                    Set<Achievement> achievementSet = teacher.getAchievementSet();
                    achievementSet.add(achievement);
                    teacher.setAchievementSet(achievementSet);
                    teacherRepository.save(teacher);
                }else if(target instanceof Student){
                    Student student = (Student) target;
                    Set<Achievement> achievementSet = student.getAchievementSet();
                    achievementSet.add(achievement);
                    student.setAchievementSet(achievementSet);
                    studentRepository.save(student);
                }
                User user = userService.findById(Long.parseLong(response.getMessage()));
                if(user instanceof Student){
                    Student student = (Student) user;
                    student.setEvaluateNum(student.getEvaluateNum() + 1);
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
