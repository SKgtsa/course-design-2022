package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.user.*;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import com.clankalliance.backbeta.service.ExcelService;
import com.clankalliance.backbeta.service.GeneralUploadService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Resource
    private GeneralUploadService generalUploadService;

    @Resource
    private AvatarService avatarService;

    @Resource
    private ExcelService excelService;


    /*
    每一个CommonResponse都要返回一个character
     */

    @PostMapping("/login")
    public CommonResponse login(@RequestBody UserLoginRequest request){
        return userService.handleLogin(request.getUserNumber(), request.getPassword());
    }

    @PostMapping("/register")
    public CommonResponse register(@RequestBody UserSaveRequest request){
        return userService.handleRegister(request.getIdentity(), request.getCode(), request.getPhone(), request.getUserNumber(), request.getPassword(),request.getName(),request.getStudentClass(),request.getIdCardNumber(),request.getGender(),request.getEthnic(),request.getPoliticalAffiliation(), request.getEMail(),request.getNickName());
    }

    @PostMapping("/tokenCheck")
    public CommonResponse tokenCheck(@RequestBody TokenCheckRequest request){
        return tokenUtil.tokenCheck(request.getToken());
    }

    @PostMapping("/loginPhone")
    public CommonResponse handlePhoneLogin(@RequestBody PhoneCheckRequest request){
        return userService.handlePhoneLogin(request.getPhone());
    }

    @PostMapping("/loginCode")
    public CommonResponse handleCodeLogin(@RequestBody PhoneCheckRequest request){
        return userService.handleCodeLogin(request.getPhone(), request.getCode());
    }

    @PostMapping("/registerPhone")
    public CommonResponse handlePhoneRegister(@RequestBody PhoneCheckRequest request){
        return userService.handlePhoneRegister(request.getPhone());
    }

    @PostMapping("/myInfo")
    public CommonResponse handleGetInfo(@RequestBody TokenCheckRequest request){
        return userService.handleGetInfo(request.getToken());
    }

    @PostMapping("/editInfo")
    public CommonResponse handleEditInfo(@RequestBody UserSaveRequest request){
        return userService.handleEditInfo(request.getToken(),request.getName(),request.getId(),request.getUserNumber(),request.getEthnic(),request.getEMail(),request.getPoliticalAffiliation());
    }

    @PostMapping("/editBlogInfo")
    public CommonResponse handleEditBlogInfo(@RequestBody BlogInfoSaveRequest request){
        return userService.handleSaveBlogInfo(request.getToken(),request.getNickName());
    }

    @PostMapping("/changeAvatar")
    public CommonResponse handleChangeAvatar(HttpSession session,
                                             // 路径变量 解决前后端不一致
                                             @RequestParam("avatar") MultipartFile avatar, @RequestParam("token") String token){
        return avatarService.handleSave(avatar,token);
    }

    @PostMapping("/changePhoto")
    public CommonResponse handleChangePhoto(HttpSession session,
                                             // 路径变量 解决前后端不一致
                                             @RequestParam("photo") MultipartFile photo, @RequestParam("token") String token){
        return avatarService.handleSavePhoto(photo,token);
    }

    //批量注册
    @PostMapping("/batchRegister")
    public CommonResponse handleBatchRegister(HttpSession session,
                                              // 路径变量 解决前后端不一致
                                              @RequestParam("file") MultipartFile file, @RequestParam("token") String token){
        return excelService.handleBatchRegister(file,token);
    }

}
