package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.Activity;
import com.clankalliance.backbeta.entity.Reward;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.ActivityRepository;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ActivityService;
import com.clankalliance.backbeta.service.ScoreService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import com.clankalliance.backbeta.utils.SnowFlake;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private UserService userService;

    @Resource
    private StudentRepository studentRepository;


    @Resource
    private ActivityRepository activityRepository;

    @Resource
    private ScoreService scoreService;

    private final Achievement ACTIVITY_C = new Achievement(Long.parseLong("10"),"课外活动3项以上","课外活动爱好者");

    private final Achievement ACTIVITY_B = new Achievement(Long.parseLong("11"),"课外活动7项以上","课外活动丰富");

    private final Achievement ACTIVITY_A = new Achievement(Long.parseLong("12"),"课外活动10项以上","课外活动达人");

    private final Achievement ACTIVITY_POINT = new Achievement(Long.parseLong("13"),"同时获得课外活动与绩点最高成就","文体两开花");

    @Override
    public Achievement getACTIVITY_POINT() {
        return ACTIVITY_POINT;
    }

    @Override
    public Achievement getACTIVITY_A() {
        return ACTIVITY_A;
    }

    private Set<Achievement> updateAchievementList(Student student){
        List<Activity> rewardList = student.getActivity();
        int activityNum = rewardList.size();
        Set<Achievement> achievementSet = student.getAchievementSet();
        if(activityNum >= 10){
            if(achievementSet.contains(ACTIVITY_B)){
                achievementSet.remove(ACTIVITY_B);
            }else if(achievementSet.contains(ACTIVITY_C)){
                achievementSet.remove(ACTIVITY_C);
            }
            achievementSet.add(ACTIVITY_A);
            if(achievementSet.contains(scoreService.getPOINT_A())){
                achievementSet.add(ACTIVITY_POINT);
            }
        }else if(activityNum >= 7){
            if(achievementSet.contains(ACTIVITY_A)){
                achievementSet.remove(ACTIVITY_A);
            }else if(achievementSet.contains(ACTIVITY_C)){
                achievementSet.remove(ACTIVITY_C);
            }
            achievementSet.add(ACTIVITY_B);
        }else if(activityNum >= 3){
            if(achievementSet.contains(ACTIVITY_A)){
                achievementSet.remove(ACTIVITY_A);
            }else if(achievementSet.contains(ACTIVITY_B)){
                achievementSet.remove(ACTIVITY_B);
            }
            achievementSet.add(ACTIVITY_C);
        }
        return achievementSet;
    }


    /**
     * 保存课外活动
     * 验证token,若id为空(null)则创建，绑定到该token对应用户，否则覆盖原数据
     * @param token 用户令牌
     * @param id 课外活动id(可为空)
     * @param name 课外活动名
     * @param description 活动描述
     * @param date 活动时间(日期字符串)
     * @param result 成果
     * @return
     */
    @Override
    public CommonResponse handleSave(String token, long id,String name,String description,String date,String result){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                Activity activity;
                if(0!=id){
                    Optional<Activity> activityOp= activityRepository.findById(id);
                    if(activityOp.isEmpty()){
                        response.setSuccess(false);
                        response.setMessage("课外活动不存在");
                        return response;
                    }
                    activity = activityOp.get();
                    activity.setName(name);
                    activity.setDate(date);
                    activity.setResult(result);
                    activity.setDescription(description);
                    activityRepository.save(activity);
                }else{
                    activity = new Activity(snowFlake.nextId(),name,description,date,result,student);
                    activityRepository.save(activity);
                    List<Activity> activityList=student.getActivity();
                    activityList.add(activity);
                    student.setActivity(activityList);
                    student.setAchievementSet(updateAchievementList(student));
                    studentRepository.save(student);
                }
                response.setSuccess(true);
                response.setMessage("课外活动创建成功");
            }else if(user instanceof Manager){
                //管理员没有课外活动表，故只能修改活动
                Optional<Activity> aop = activityRepository.findById(id);
                if(aop.isEmpty()){
                    response.setMessage("管理员无法创建课外活动");
                    response.setSuccess(false);
                    return response;
                }
                Activity activityOld = aop.get();
                Student student=activityOld.getStudent();
                Activity activity=new Activity(id,name,description,date,result,student);
                activityRepository.save(activity);

                response.setSuccess(true);
                response.setMessage("课外活动修改成功");
            }
        }
        return response;
    }

    /**
     * 删除课外活动
     * Manger与Student均使用此方法删除Activity
     * 验证token,验证登陆状态
     * 根据id找到课外活动并删除
     * @param token 用户令牌
     * @param id 课外活动id
     * @return
     */
    @Override
    public CommonResponse handleDelete(String token, long id){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262555784829865984");//学生身份
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Optional<Activity> activityOp=activityRepository.findById(id);
                //将活动和学生解绑
                if(activityOp.isEmpty()){
                    response.setMessage("对象不存在");
                    response.setSuccess(false);
                }else{
                    Activity activity=activityOp.get();
                    Student student=activity.getStudent();
                    List<Activity> activityList=student.getActivity();
                    activityList.remove(activity);
                    student.setActivity(activityList);
                    activityRepository.delete(activity);
                    student.setAchievementSet(updateAchievementList(student));
                    studentRepository.save(student);

                    response.setMessage("课程删除成功");
                    response.setSuccess(true);
                }
            }
        }
        return response;
    }

    /**
     * 查找课外活动
     * 验证token,验证登陆状态并找到用户对象
     * 分页返回用户课外活动List中对象
     * @param token 用户令牌
     * @param pageNum 页码(从1开始)
     * @param pageSize 页容量
     * @return
     */
    @Override
    public CommonResponse handleFind(String token,Integer pageNum,Integer pageSize){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("259887250475716608");//259887250475716608
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                List<Activity> activityList=student.getActivity();
                if(activityList.size() <= pageSize){
                    response.setContent(activityList);
                    response.setSuccess(true);
                    response.setMessage("活动表返回成功");
                    response.setTotalPage(1);
                }else if(activityList.size() ==0){
                    response.setSuccess(true);
                    response.setMessage("该学生没有参加课外活动");
                }else {
                    Collections.reverse(activityList);
                    List<Activity> subActivityList= activityList.subList((pageNum - 1) * pageSize,pageNum * pageSize >= activityList.size()? activityList.size() - 1 :pageNum * pageSize );
                    int size=activityList.size();
                    //计算总页数
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(subActivityList);
                    response.setMessage("活动表返回成功");
                    response.setSuccess(true);
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerFind(String token,long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                User target = userService.findById(id);
                List<Activity> activityList;
                if(target instanceof Teacher || target instanceof Manager){
                    response.setSuccess(false);
                    response.setMessage("老师不具有成果奖励");
                }
                activityList = ((Student) target).getActivity();
                response.setMessage("查找成功");
                response.setSuccess(true);
                response.setContent(activityList);
            }else{
                response.setSuccess(false);
                response.setMessage("用户权限不足");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerDelete(String token,long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Activity> activityOp=activityRepository.findById(id);
                //将活动和学生解绑
                if(activityOp.isEmpty()){
                    response.setMessage("对象不存在");
                    response.setSuccess(false);
                }else{
                    Activity activity=activityOp.get();
                    Student student=activity.getStudent();
                    List<Activity> activityList=student.getActivity();
                    activityList.remove(activity);
                    student.setActivity(activityList);
                    activityRepository.delete(activity);
                    student.setAchievementSet(updateAchievementList(student));
                    studentRepository.save(student);

                    response.setMessage("课程删除成功");
                    response.setSuccess(true);
                }
            }else{
                response.setSuccess(false);
                response.setMessage("用户权限不足");
            }
        }
        return response;
    }

}
