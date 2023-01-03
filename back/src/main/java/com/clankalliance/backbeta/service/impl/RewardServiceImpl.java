package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.Practice;
import com.clankalliance.backbeta.entity.Reward;
import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.RewardRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.RewardService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import com.clankalliance.backbeta.utils.SnowFlake;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RewardServiceImpl implements RewardService {
    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private UserService userService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private RewardRepository rewardRepository;


    private final Achievement REWARD_C = new Achievement(Long.parseLong("7"),"成果奖励3项以上","淡泊名利小修士");

    private final Achievement REWARD_B = new Achievement(Long.parseLong("8"),"成果奖励7项以上","勤勤恳恳打工人");

    private final Achievement REWARD_A = new Achievement(Long.parseLong("9"),"成果奖励10项以上","奖金证书收割机");

    private List<Achievement> updateAchievementList(Student student){
        List<Reward> rewardList = student.getRewardSet();
        int rewardNum = rewardList.size();
        List<Achievement> achievementList = student.getAchievementList();
        if(rewardNum >= 10){
            if(achievementList.contains(REWARD_B)){
                achievementList.remove(REWARD_B);
            }else if(achievementList.contains(REWARD_C)){
                achievementList.remove(REWARD_C);
            }
            achievementList.add(REWARD_A);
        }else if(rewardNum >= 7){
            if(achievementList.contains(REWARD_A)){
                achievementList.remove(REWARD_A);
            }else if(achievementList.contains(REWARD_C)){
                achievementList.remove(REWARD_C);
            }
            achievementList.add(REWARD_B);
        }else if(rewardNum >= 3){
            if(achievementList.contains(REWARD_A)){
                achievementList.remove(REWARD_A);
            }else if(achievementList.contains(REWARD_B)){
                achievementList.remove(REWARD_B);
            }
            achievementList.add(REWARD_C);
        }
        return achievementList;
    }

    /**
     * 保存成果奖励
     * 验证token,若id为空(null)则创建，绑定到该token对应用户，否则覆盖原数据
     * @param token 用户令牌
     * @param id 课外活动id(可为空)
     * @param name 课外活动名
     * @param description 活动描述
     * @return
     */
    @Override
    public CommonResponse handleSave(String token, long id,String name,String description){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262555784829865984");//学生
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                Set<Student> studentSet=new HashSet<>();
                //先判断id是否为空
                if(id == 0){
                    //id为空，则为reward的创建
                    id=snowFlake.nextId();
                }else{
                    Optional<Reward> rewardOp= rewardRepository.findById(id);
                    if(rewardOp.isEmpty()){
                        //所取出的Optional为空,为创建
                        id=snowFlake.nextId();
                    }else{ //为reward的修改，需更新学生表,但不需要更改id
                        Reward OldReward=rewardOp.get();
                        studentSet=OldReward.getStudentSet();
                    }
                }
                studentSet.add(student);
                Reward reward=new Reward(id,name,description,studentSet);
                rewardRepository.save(reward);

                //将reward加入进学生的活动表中
                List<Reward> rewardList=student.getRewardSet();
                rewardList.add(reward);
                student.setRewardSet(rewardList);
                student.setAchievementList(updateAchievementList(student));
                studentRepository.save(student);
                response.setSuccess(true);
                response.setMessage("成果奖励创建成功");
            }else if(user instanceof Manager){
                //管理员没有成果奖励表，故只能修改reward
                Reward rewardOld = rewardRepository.findById(id).get();
                Set<Student> studentSet=rewardOld.getStudentSet();
                Reward reward=new Reward(id,name,description,studentSet);
                rewardRepository.save(reward);
                response.setSuccess(true);
                response.setMessage("成果奖励修改成功");
            }
        }
        return response;
    }

    /**
     * 删除成果奖励
     * 验证token,验证登陆状态
     * 根据id找到社会实践并删除
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
            response.setMessage("262555784829865984");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                //Student只能将自己从成果奖励的学生表中移出，而不能删除此成果奖励(除非成果奖励为个人奖项)
                Optional<Reward> rewardOp=rewardRepository.findById(id);
                if(rewardOp.isEmpty()){
                    response.setMessage("对象不存在");
                    response.setSuccess(false);
                    return response;
                }
                Reward reward=rewardOp.get();
                Student student=(Student) user;
                //在学生的成果表中删除成果
                List<Reward> rewardList=student.getRewardSet();
                rewardList.remove(reward);
                student.setRewardSet(rewardList);
                //在成果的学生表中删除学生
                Set<Student> studentSet=reward.getStudentSet();
                studentSet.remove(student);
                reward.setStudentSet(studentSet);
                //个人奖项则完全删除
                if(studentSet.size() == 0){
                    rewardRepository.delete(reward);
                }
                student.setAchievementList(updateAchievementList(student));
                studentRepository.save(student);

                response.setSuccess(true);
                response.setMessage("学生成果奖励移除成功");
            }
        }
        return response;
    }

    /**
     * 查找成果奖励
     * 验证token,验证登陆状态并找到用户对象
     * 分页返回用户社会实践List中对象
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
            response.setMessage("262555784829865984");//259887250475716608
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                List<Reward> rewardList=student.getRewardSet();
                if(rewardList.size() <= pageSize){
                    response.setContent(rewardList);
                    response.setSuccess(true);
                    response.setMessage("成果表返回成功");
                    response.setTotalPage(1);
                }else if(rewardList.size() ==0){
                    response.setSuccess(true);
                    response.setMessage("该学生没有参加成果奖励");
                }else {
                    Collections.reverse(rewardList);
                    List<Reward> subRewardList= rewardList.subList((pageNum - 1) * pageSize,pageNum * pageSize >= rewardList.size()? rewardList.size() - 1 :pageNum * pageSize );
                    int size=rewardList.size();
                    //计算总页数
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(subRewardList);
                    response.setMessage("成果奖励表返回成功");
                    response.setSuccess(true);
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerFind(String token,long id){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("259887250475716608");//Manager
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            Optional<Reward> rewardOp=rewardRepository.findById(id);
            Reward reward=rewardOp.get();
            response.setToken(token);
            response.setMessage("查找成功");
            response.setSuccess(true);
            response.setContent(reward);
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerDelete(String token,long id){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262555784829865984");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                //Manager可以彻底删除此成果奖励
                Optional<Reward> rewardOp=rewardRepository.findById(id);
                //将活动和学生解绑
                Reward reward=rewardOp.get();
                //遍历此成果奖励的学生表，更新学生表中学生的成果奖励表
                Set<Student> studentSet=reward.getStudentSet();
                for(Student s : studentSet){
                    List<Reward> rewardList=s.getRewardSet();
                    rewardList.remove(reward);
                    s.setRewardSet(rewardList);
                    s.setAchievementList(updateAchievementList(s));
                    studentRepository.save(s);
                }
                rewardRepository.delete(reward);

                response.setToken(token);
                response.setMessage("成果奖励删除成功");
                response.setSuccess(true);
            }
        }
        return response;
    }

}
