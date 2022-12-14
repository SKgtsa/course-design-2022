package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.response.CommonResponse;

public interface UserService {

    String getDEFAULT_AVATAR_URL();

    String getDEFAULT_PHOTO_URL();

    CommonResponse handleLogin(long userNumber,String password,String ticket,String randstr);

    CommonResponse handleRegister(Integer identity, String code, long phone, long userNumber, String password, String name, String idCardNumber, Boolean gender, String ethnic, String politicalAffiliation, String eMail,String nickName);
    CommonResponse handlePhoneLogin(long phone);

    CommonResponse handleCodeLogin(long phone, String code);

    CommonResponse handlePhoneRegister(long phone);

    User findById(long id);

    User findByUserNumber(long userNumber);

    User findByPhone(long phone);

    CommonResponse handleGetInfo(String token);

    CommonResponse handleEditInfo(String token,String name,long id,long userNumber,String ethnic,String eMail,String politicalAffiliation, String researchDirection, String section, String nickName,Long phone);

    CommonResponse handleSaveNickName(String token,String nickName);

    boolean handleBatchRegisterStudent(Long userNumber, String name, String password, Long phone, String studentClass, String idCardNumber, boolean gender, String ethnic, String politicalAffiliation, String eMail,  String nickName, String section);

    CommonResponse handleChangePassword(String token, String oldPassword, String newPassword);

    CommonResponse handleManagerFind(String token, long userNumber);

    CommonResponse handleManagerSave(String token, Long id, Long userNumber, String nickName, String name, Long phone, String studentClass, String idCardNumber, boolean gender, String ethnic, String politicalAffiliation, String eMail, String avatarURL, String photoURL, String researchDirection, String section);

    CommonResponse handleManagerDelete(String token, long id);

    CommonResponse handleSaveResearchDirection(String token,String researchDirection);

    CommonResponse findPasswordPhone(Long userNumber, Long phone);

    CommonResponse findPasswordCode(Long phone ,String code, String password);

    CommonResponse findClassmate(String token);
}
