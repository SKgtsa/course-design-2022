package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.response.CommonResponse;

public interface UserService {

    CommonResponse handleLogin(long userNumber,String password);

    CommonResponse handleRegister(Integer identity, String code, long phone, long userNumber, String password, String name, String gradeClass,String idCardNumber, Boolean gender, String ethnic, String politicalAffiliation, String eMail);

    CommonResponse handlePhoneLogin(long phone);

    CommonResponse handleCodeLogin(long phone, String code);

    CommonResponse handlePhoneRegister(long phone);

    User findById(long id);

    User findByUserNumber(long userNumber);

    User findByPhone(long phone);
}
