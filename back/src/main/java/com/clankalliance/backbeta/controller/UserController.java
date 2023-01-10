package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.request.user.*;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import com.clankalliance.backbeta.service.ExcelService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;


    @Resource
    private AvatarService avatarService;

    @Resource
    private ExcelService excelService;


    /*
    每一个CommonResponse都要返回一个character
     */

    @PostMapping("/login")
    public CommonResponse login(@RequestBody UserLoginRequest request){
        return userService.handleLogin(request.getUserNumber(), request.getPassword(),request.getTicket(),request.getRandstr());
    }

    @PostMapping("/register")
    public CommonResponse register(@RequestBody UserSaveRequest request){
        return userService.handleRegister(request.getIdentity(), request.getCode(), request.getPhone(), request.getUserNumber(), request.getPassword(),request.getName(),request.getIdCardNumber(),request.getGender(),request.getEthnic(),request.getPoliticalAffiliation(), request.getEmail(),request.getNickName());
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

    //aaa
    @PostMapping("/editInfo")
    public CommonResponse handleEditInfo(@RequestBody UserSaveRequest request){
        return userService.handleEditInfo(request.getToken(),request.getName(),request.getId(),request.getUserNumber(),request.getEthnic(),request.getEmail(),request.getPoliticalAffiliation(), request.getResearchDirection(), request.getSection(),request.getNickName());
    }

    @PostMapping("/editNickName")
    public CommonResponse handleEditNickName(@RequestBody BlogInfoSaveRequest request){
        return userService.handleSaveNickName(request.getToken(),request.getNickName());
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

    @PostMapping("/changeResearchDirection")
    public CommonResponse handleChangeResearchDirection(@RequestBody ChangeResearchDirectionRequest request){
        return userService.handleSaveResearchDirection(request.getToken(),request.getResearchDirection());
    }

    //批量注册
    @PostMapping("/batchRegister")
    public CommonResponse handleBatchRegister(HttpSession session,
                                              // 路径变量 解决前后端不一致
                                              @RequestParam("file") MultipartFile file, @RequestParam("token") String token){
        return excelService.handleBatchRegister(file,token);
    }

    @PostMapping("/changePassword")
    public CommonResponse handleChangePassword(@RequestBody PasswordChangeRequest request){
        return userService.handleChangePassword(request.getToken(),request.getPassword());
    }

    @PostMapping("/managerFind")
    public CommonResponse handleManagerFind(@RequestBody UserManagerFindRequest request){
        return userService.handleManagerFind(request.getToken(),request.getUserNumber());
    }
    //
    @PostMapping("/managerSave")
    public CommonResponse handleManagerSave(@RequestBody UserManagerSaveRequest request){
        return userService.handleManagerSave(request.getToken(), request.getId(), request.getUserNumber(), request.getNickName(), request.getName(), request.getPhone(), request.getStudentClass(), request.getIdCardNumber(), request.getGender(), request.getEthnic(), request.getPoliticalAffiliation(), request.getEmail(), request.getAvatarURL(), request.getPhotoURL(), request.getResearchDirection(), request.getSection());
    }

    @PostMapping("/managerDelete")
    public CommonResponse handleManagerDelete(@RequestBody UserManagerDeleteRequest request){
        return userService.handleManagerDelete(request.getToken(), request.getId());
    }

    @PostMapping("/findPasswordPhone")
    public CommonResponse findPasswordPhone(@RequestBody FindPasswordRequest request){
        return userService.findPasswordPhone(request.getUserNumber(),request.getPhone());
    }

    @PostMapping("/findPasswordCode")
    public CommonResponse findPasswordCode(@RequestBody FindPasswordRequest request){
        return userService.findPasswordCode(request.getPhone(), request.getCode(), request.getPassword());
    }

    @PostMapping("/findClassmate")
    public CommonResponse findClassmate(@RequestBody TokenCheckRequest request){
        return userService.findClassmate(request.getToken());
    }

}
