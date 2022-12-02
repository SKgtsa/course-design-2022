package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.ManagerRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.TencentSmsService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.ManipulateUtil;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.StatusNode;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final String DEFAULT_AVATAR_URL = "default";

    private final String DEFAULT_PHOTO_URL = "default";


    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Resource
    private ManagerRepository managerRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private TencentSmsService tencentSmsService;

    @Resource
    private SnowFlake snowFlake;


    @Override
    public User findById(long id){
        Optional<Student> sop = studentRepository.findUserById(id);
        Optional<Teacher> top = teacherRepository.findUserById(id);
        Optional<Manager> mop = managerRepository.findUserById(id);
        if(sop.isPresent()){
            return sop.get();
        }else if(top.isPresent()){
            return top.get();
        }else if(mop.isPresent()){
            return mop.get();
        }else{
            return null;
        }
    }

    @Override
    public User findByUserNumber(long userNumber){
        Optional<Student> sop = studentRepository.findByUserNumber(userNumber);
        Optional<Teacher> top = teacherRepository.findByUserNumber(userNumber);
        Optional<Manager> mop = managerRepository.findByUserNumber(userNumber);
        if(sop.isPresent()){
            return sop.get();
        }else if(top.isPresent()){
            return top.get();
        }else if(mop.isPresent()){
            return mop.get();
        }else{
            return null;
        }
    }

    @Override
    public User findByPhone(long phone){
        Optional<Student> sop = studentRepository.findByPhone(phone);
        Optional<Teacher> top = teacherRepository.findByPhone(phone);
        Optional<Manager> mop = managerRepository.findByPhone(phone);
        if(sop.isPresent()){
            return sop.get();
        }else if(top.isPresent()){
            return top.get();
        }else if(mop.isPresent()){
            return mop.get();
        }else{
            return null;
        }
    }

    @Override
    public CommonResponse handleLogin(long userNumber,String password) {
        /*
        增加nickName,avatarURL,character 0Teacher 1Student 2Manager
         */
        CommonResponse  response = new CommonResponse<>();
        response.setSuccess(false);
        password = DigestUtils.sha1Hex(password);
        User user = findByUserNumber(userNumber);
        if(user == null){
            //该用户已存在 进行判定是统一注册的空号还是用户重复注册
            response.setSuccess(false);
            response.setMessage("账户已存在");
            return response;
        }
        if(!password.equals(user.getPassword())){
            response.setMessage("密码错误");
            return response;
        }
        response.setSuccess(true);
        response.setMessage("登陆成功");
        ManipulateUtil.updateStatus(user.getId());
        response.setToken(ManipulateUtil.endNode.getToken());
        response.setUser(user);
        return response;
    }

    //存在bug: 保存时数据库条目不全 email接收有问题

    @Override
    public CommonResponse handleRegister(Integer identity, String code, long phone, long userNumber, String password, String name, String studentClass,String idCardNumber, Boolean gender, String ethnic, String politicalAffiliation, String eMail,String nickName) {
        password = DigestUtils.sha1Hex(password.getBytes());
        CommonResponse  response = new CommonResponse();
        User user = findByUserNumber(userNumber);
        if(user != null){
            //该用户已存在 进行判定是统一注册的空号还是用户重复注册
            response.setSuccess(false);
            response.setMessage("账户已存在");
            return response;
        }

        StatusNode status = ManipulateUtil.findStatusByUserId(phone);
        if(status.getToken() == null){
            //状态不存在
            response.setSuccess(false);
            response.setMessage("请先发送验证码");
            return response;
        }
        if(!status.getVerifyCode().equals(code)){
            //验证码错误
            response.setSuccess(false);
            response.setMessage("短信验证码错误");
            return response;
        }
        long id = snowFlake.nextId();
        switch (identity){
            case 0:
                user = new Student(id,userNumber,name,password,phone,studentClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,DEFAULT_AVATAR_URL,nickName,DEFAULT_PHOTO_URL);
                break;
            case 1:
                user = new Teacher(id,userNumber,name,password,phone,studentClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,DEFAULT_AVATAR_URL,nickName,DEFAULT_PHOTO_URL);
                break;
            case 2:
                user = new Manager(id,userNumber,name,password,phone,studentClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,DEFAULT_AVATAR_URL,nickName,DEFAULT_PHOTO_URL);
                break;
        }
        try{
            userRepository.save(user);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage("请填写完整");
            return response;
        }
        ManipulateUtil.updateStatus(id);
        response.setSuccess(true);
        response.setMessage("注册成功");
        response.setUser(user);
        response.setToken(ManipulateUtil.endNode.getToken());
        System.out.println(response);
        return response;
    }

    @Override
    public CommonResponse handlePhoneLogin(long phone){
        CommonResponse response = new CommonResponse();
        User user = findByPhone(phone);
        if(user != null){
            ManipulateUtil.updateStatus(user.getId());
            String code = ManipulateUtil.endNode.getVerifyCode();
            tencentSmsService.sendSms("" + phone,code);
            response.setSuccess(true);
            response.setMessage("已发送验证码");
        }else{
            response.setMessage("用户不存在");
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public CommonResponse handleCodeLogin(long phone, String code){
        return tokenUtil.phoneCodeCheck(phone,code);
    }

    @Override
    public CommonResponse handlePhoneRegister(long phone){
        CommonResponse response = new CommonResponse();
        User user = findByPhone(phone);
        if(user != null){
            //手机已被绑定
            response.setSuccess(false);
            response.setMessage("手机已被绑定");
        }else{
            //手机号一定比普通id短,作为临时id
            long tempId = phone;
            ManipulateUtil.updateStatus(tempId);
            String code = ManipulateUtil.endNode.getVerifyCode();
            tencentSmsService.sendSms("" + phone,code);
            response.setSuccess(true);
            response.setMessage("已发送验证码");
        }
        return response;
    }

    @Override
    public CommonResponse handleGetInfo(String token){
        return null;
    }

    @Override
    public CommonResponse handleEditInfo(String token,String name,long id,long userNumber,String ethnic,String eMail,String politicalAffiliation){
        return null;
    }

    @Override
    public CommonResponse handleSaveBlogInfo(String token,String nickName){
        return null;
    }


}
