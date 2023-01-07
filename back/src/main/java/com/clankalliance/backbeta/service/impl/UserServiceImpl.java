package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.ManagerRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.request.user.UserRequestTarget;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.dataBody.UserBriefData;
import com.clankalliance.backbeta.service.TencentSmsService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.ManipulateUtil;
import com.clankalliance.backbeta.utils.StatusManipulateUtils.StatusNode;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final String DEFAULT_AVATAR_URL = "/static/inbuild/default.png";

    private final String DEFAULT_PHOTO_URL = "/static/inbuild/default.png";


    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

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

    public String getDEFAULT_AVATAR_URL() {
        return DEFAULT_AVATAR_URL;
    }

    public String getDEFAULT_PHOTO_URL() {
        return DEFAULT_PHOTO_URL;
    }

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
            response.setSuccess(false);
            response.setMessage("账户不存在");
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
        if(user instanceof Student){
            response.setCharacter(0);
            if(user.getNickName() == "默认昵称"){
                response.setNeedSupplement(true);
            }else{
                response.setNeedSupplement(false);
            }
            response.setNeedSupplement(false);
        }else if(user instanceof Teacher){
            response.setCharacter(1);
            if(((Teacher) user).getResearchDirection() == null){
                response.setNeedSupplement(true);
            }else{
                response.setNeedSupplement(false);
            }
        }else{
            response.setCharacter(2);
            response.setNeedSupplement(false);
        }

        return response;
    }

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
            case 1:
                user = new Teacher(id,userNumber,name,password,phone,idCardNumber,gender,ethnic,politicalAffiliation,eMail,DEFAULT_AVATAR_URL,nickName,DEFAULT_PHOTO_URL);
                break;
            case 2:
                user = new Manager(id,userNumber,name,password,phone,idCardNumber,gender,ethnic,politicalAffiliation,eMail,DEFAULT_AVATAR_URL,nickName,DEFAULT_PHOTO_URL);
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
        if(user instanceof Student){
            response.setCharacter(0);
            response.setNeedSupplement(false);
        }else if(user instanceof Teacher){
            response.setCharacter(1);
            response.setNeedSupplement(true);
        }else{
            response.setCharacter(2);
            response.setNeedSupplement(false);
        }
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
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = findById(Long.parseLong(response.getMessage()));
        if(user instanceof Student){
            response.setCharacter(0);
        }else if(user instanceof  Teacher){
            response.setCharacter(1);
        }else{
            response.setCharacter(2);
        }
        response.setUser(user);
        return response;
    }

    @Override
    public CommonResponse handleEditInfo(String token,String name,long id,long userNumber,String ethnic,String eMail,String politicalAffiliation, String researchDirection, String section){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = findById(Long.parseLong(response.getMessage()));
        if(user instanceof Manager){
            user = findById(id);
        }else if(user.getId() != id){
            response.setMessage("无权进行操作");
            response.setSuccess(false);
            return response;
        }
        user.setName(name);
        user.setUserNumber(userNumber);
        user.setEthnic(ethnic);
        user.setEMail(eMail);
        user.setPoliticalAffiliation(politicalAffiliation);
        try{
            userRepository.save(user);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage("保存失败");
            return response;
        }
        response.setMessage("保存成功");
        return response;
    }

    @Override
    public CommonResponse handleSaveNickName(String token,String nickName){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = findById(Long.parseLong(response.getMessage()));
        user.setNickName(nickName);
        try{
            userRepository.save(user);
        }catch (Exception e){
            response.setSuccess(false);
            response.setMessage("保存失败");
            return response;
        }
        response.setMessage("保存成功");
        return response;
    }


    @Override
    public boolean handleBatchRegisterStudent(Long userNumber, String name, String password, Long phone, String studentClass, String idCardNumber, boolean gender, String ethnic, String politicalAffiliation, String eMail,  String nickName, String section){
        password = DigestUtils.sha1Hex(password.getBytes());
        try{
            Student student = new Student(snowFlake.nextId(), userNumber, name, password, phone, studentClass, idCardNumber, gender, ethnic, politicalAffiliation, eMail, DEFAULT_AVATAR_URL, nickName, DEFAULT_PHOTO_URL, section);
            studentRepository.save(student);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public CommonResponse handleChangePassword(String token, String passWord){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = findById(Long.parseLong(response.getMessage()));
            String oldPassword=user.getPassword();
            if(oldPassword.equals(passWord)){
                response.setSuccess(false);
                response.setMessage("密码未进行更改");
            }else{
                user.setPassword(passWord);
                userRepository.save(user);
                response.setSuccess(true);
                response.setMessage("密码修改成功");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerFind(String token, long userNumber){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Student> studentOp=studentRepository.findByUserNumber(userNumber);
                if(studentOp.isEmpty()){
                    Optional<Teacher> teacherOp=teacherRepository.findByUserNumber(userNumber);
                    Teacher teacher=teacherOp.get();
                    response.setSuccess(true);
                    response.setMessage("查找成功");
                    response.setContent(teacher);
                }else{
                    Student student=studentOp.get();
                    response.setSuccess(true);
                    response.setMessage("查找成功");
                    response.setContent(student);
                }
            }else{
                response.setSuccess(false);
                response.setMessage("用户权限不足");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerSave(String token, UserRequestTarget user){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User currentUser = userService.findById(Long.parseLong(response.getMessage()));
            User targetUser = userService.findById(user.getId());
            if(currentUser instanceof Manager){
                if(targetUser instanceof Student){
                    Student student = (Student)targetUser;

                    studentRepository.save((Student) targetUser);
                }else if(targetUser instanceof Teacher){
                    Teacher teacher = (Teacher)targetUser;

                    teacherRepository.save((Teacher) targetUser);
                }
                response.setSuccess(true);
                response.setMessage("用户保存成功");
            }else{
                response.setSuccess(false);
                response.setMessage("用户权限不足");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerDelete(String token, long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Student> studentOp=studentRepository.findUserById(id);
                if(studentOp.isEmpty()){
                    Optional<Teacher> teacherOp=teacherRepository.findUserById(id);
                    Teacher teacher=teacherOp.get();
                    teacherRepository.delete(teacher);
                }else{
                    Student student=studentOp.get();
                    studentRepository.delete(student);
                }
                response.setSuccess(true);
                response.setMessage("用户删除成功");
            }else{
                response.setSuccess(false);
                response.setMessage("用户权限不足");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleSaveResearchDirection(String token,String researchDirection){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = findById(Long.parseLong(response.getMessage()));
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            teacher.setResearchDirection(researchDirection);
            teacherRepository.save(teacher);
            response.setMessage("保存成功");
        }else{
            response.setSuccess(false);
            response.setMessage("出现错误");
        }
        return response;
    }

    @Override
    public CommonResponse findPasswordPhone(Long userNumber, Long phone){
        CommonResponse response = new CommonResponse<>();
        User user = findByUserNumber(userNumber);
        if(user == null || user.getPhone() != phone){
            response.setSuccess(false);
            response.setMessage("用户不存在或用户与手机不符");
        }else{
            ManipulateUtil.updateStatus(user.getId());
            String code = ManipulateUtil.endNode.getVerifyCode();
            tencentSmsService.sendSms("" + phone,code);
            response.setMessage("发送成功");
            response.setSuccess(true);

        }
        return response;
    }

    @Override
    public CommonResponse findPasswordCode(Long phone ,String code, String password){
        CommonResponse<Object,User> response = tokenUtil.phoneCodeCheck(phone,code);
        if(!response.getSuccess())
            return response;
        User user = response.getUser();
        password = DigestUtils.sha1Hex(password.getBytes());
        user.setPassword(password);
        userRepository.save(user);
        response.setMessage("修改成功");
        return response;
    }

    @Override
    public CommonResponse findClassmate(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        if(user == null || !(user instanceof Student)){
            response.setMessage("用户不存在或用户不是学生");
            response.setSuccess(false);
        }else {
            Student student = (Student)user;
            List<Student> studentList = studentRepository.findUserByStudentClass(student.getStudentClass());
            List<UserBriefData> dataList = new ArrayList<>();
            for(Student s: studentList)
                dataList.add(new UserBriefData(s));
            response.setContent(dataList);
            response.setSuccess(true);
            response.setMessage("查找成功");
        }
        return response;
    }

}
