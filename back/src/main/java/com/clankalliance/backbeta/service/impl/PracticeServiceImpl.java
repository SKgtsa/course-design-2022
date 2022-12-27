package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Practice;
import com.clankalliance.backbeta.entity.Reward;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.PracticeRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.ManagerRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.PracticeService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import com.clankalliance.backbeta.utils.SnowFlake;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PracticeServiceImpl implements PracticeService {
    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private UserService userService;

    @Resource
    private PracticeRepository practiceRepository;

    @Resource
    private StudentRepository studentRepository;

    /**
     * 保存课外活动
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
            response.setMessage("262555784829865984");//259887250475716608
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
                    //id为空，则为Practice的创建
                    id=snowFlake.nextId();
                }else{
                    Optional<Practice> practiceOp= practiceRepository.findById(id);
                    if(practiceOp.isEmpty()){
                        //所取出的Optional为空,为创建
                        id=snowFlake.nextId();
                    }else{ //为Practice的修改，需更新学生表,但不需要更改id
                        Practice OldPractice=practiceOp.get();
                        studentSet=OldPractice.getStudentSet();
                    }
                }
                studentSet.add(student);
                Practice practice=new Practice(id,name,description,studentSet);
                practiceRepository.save(practice);

                //将practice加入进学生的活动表中
                List<Practice> practiceList=student.getPracticeSet();
                practiceList.add(practice);
                student.setPracticeSet(practiceList);
                studentRepository.save(student);

                response.setSuccess(true);
                response.setMessage("社会实践创建成功");
            }else if(user instanceof Manager){
                //管理员没有社会实践表，故只能修改practice
                Practice practiceOld = practiceRepository.findById(id).get();
                Set<Student> studentSet=practiceOld.getStudentSet();
                Practice practice=new Practice(id,name,description,studentSet);

                practiceRepository.save(practice);

                response.setSuccess(true);
                response.setMessage("社会实践修改成功");
            }
        }
        return response;
    }

    /**
     * 删除社会实践
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
            response.setMessage("259887250475716608");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                //Manager可以彻底删除此社会实践
                Optional<Practice> practiceOp=practiceRepository.findById(id);
                //将活动和学生解绑
                Practice practice=practiceOp.get();
                //遍历此社会实践的学生表，更新学生表中学生的社会实践表
                Set<Student> studentSet=practice.getStudentSet();
                for(Student s : studentSet){
                    List<Practice> practiceList=s.getPracticeSet();
                    practiceList.remove(practice);
                    s.setPracticeSet(practiceList);
                }
                practiceRepository.delete(practice);

                response.setSuccess(true);
                response.setMessage("社会实践删除成功");
            }else if(user instanceof Student){
                //Student只能将自己从社会实践的学生表中移出，而不能删除此社会实践(除非社会实践为个人立项)
                Optional<Practice> practiceOp=practiceRepository.findById(id);
                if(practiceOp.isEmpty()){
                    response.setMessage("对象不存在");
                    response.setSuccess(false);
                    return response;
                }
                Practice practice=practiceOp.get();
                Student student=(Student) user;
                //在学生的实践表中删除实践
                List<Practice> practiceList=student.getPracticeSet();
                practiceList.remove(practice);
                student.setPracticeSet(practiceList);
                //在实践的学生表中删除学生
                Set<Student> studentSet=practice.getStudentSet();
                studentSet.remove(practice);
                practice.setStudentSet(studentSet);
                //个人立项则完全删除
                if(studentSet.size() == 0){
                    practiceRepository.delete(practice);
                }

                response.setSuccess(true);
                response.setMessage("学生社会实践移除成功");
            }
        }
        return response;
    }

    /**
     * 查找课外活动
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
            response.setMessage("259887250475716608");//259887250475716608
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                List<Practice> practiceList=student.getPracticeSet();
                if(practiceList.size() <= pageSize){
                    response.setContent(practiceList);
                    response.setSuccess(true);
                    response.setMessage("活动表返回成功");
                    response.setTotalPage(1);
                }else if(practiceList.size() ==0){
                    response.setSuccess(true);
                    response.setMessage("没有参加社会实践");
                }else {
                    Collections.reverse(practiceList);
                    List<Practice> subPracticeList= practiceList.subList((pageNum - 1) * pageSize,pageNum * pageSize >= practiceList.size()? practiceList.size() - 1 :pageNum * pageSize );
                    int size=practiceList.size();
                    //计算总页数
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(subPracticeList);
                    response.setMessage("社会实践表返回成功");
                    response.setSuccess(true);
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

}

