package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.request.user.UserRequestTarget;
import com.clankalliance.backbeta.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface UserService {

    String getDEFAULT_AVATAR_URL();

    String getDEFAULT_PHOTO_URL();

    CommonResponse handleLogin(long userNumber,String password);

    CommonResponse handleRegister(Integer identity, String code, long phone, long userNumber, String password, String name, String studentClass,String idCardNumber, Boolean gender, String ethnic, String politicalAffiliation, String eMail,String nickName);
    CommonResponse handlePhoneLogin(long phone);

    CommonResponse handleCodeLogin(long phone, String code);

    CommonResponse handlePhoneRegister(long phone);

    User findById(long id);

    User findByUserNumber(long userNumber);

    User findByPhone(long phone);

    CommonResponse handleGetInfo(String token);

    CommonResponse handleEditInfo(String token,String name,long id,long userNumber,String ethnic,String eMail,String politicalAffiliation, String researchDirection, String section);

    CommonResponse handleSaveNickName(String token,String nickName);

    boolean handleBatchRegisterStudent(Long userNumber, String name, String password, Long phone, String studentClass, String idCardNumber, boolean gender, String ethnic, String politicalAffiliation, String eMail,  String nickName, String section);

    CommonResponse handleChangePassword(String token,String password);

    CommonResponse handleManagerFind(String token, long userNumber);

    CommonResponse handleManagerSave(String token, UserRequestTarget user);

    CommonResponse handleManagerDelete(String token, long id);

    //后台准备个人简历所需要的各类数据组成的段落数据
    CommonResponse getStudentIntroduceData(String token);

    ResponseEntity<StreamingResponseBody> getStudentIntroducePdf(String token);

    CommonResponse handleSaveResearchDirection(String token,String researchDirection);

    CommonResponse findPasswordPhone(Long userNumber, Long phone);

    CommonResponse findPasswordCode(Long phone ,String code, String password);
}
