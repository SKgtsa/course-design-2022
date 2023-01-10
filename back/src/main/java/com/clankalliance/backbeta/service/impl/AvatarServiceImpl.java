package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.blogRepository.PostRepository;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AvatarService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private PostRepository postRepository;

    // 设置上传文件的最大值
    public static final int AVATAR_MAX_SIZE = 10*1024*1024;

    // 限制上传文件的类型
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("images/jpeg");
        AVATAR_TYPE.add("images/png");
        AVATAR_TYPE.add("images/bmp");
        AVATAR_TYPE.add("images/gif");
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");

    }

    @Override
    public CommonResponse handleGet(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess()){
            return response;
        }
        User user = userService.findById(Long.valueOf(response.getMessage()));
        response.setContent(user.getAvatarURL());
        response.setMessage("操作成功");
        return response;
    }

    @Override
    public CommonResponse handleSave(MultipartFile file, String token){
        System.out.println("intoAvatarProcess");
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess()){
            return response;
        }
        // 文件是否为空
        if (file.isEmpty()) {
            System.out.println("文件为空");
        }
        if (file.getSize() > AVATAR_MAX_SIZE) {
            System.out.println("文件大小超出限制");
        }
        // 文件类型是否符合
        String contentType = file.getContentType();
        // 如果集合包含某元素则返回ture
        if (!AVATAR_TYPE.contains(contentType)) {
            System.out.println("文件类型不支持");
        }
        // 上传的文件.../upload/文件.png
        String parent =
                System.getProperty("user.dir") + "/static/avatar";
        // File对象指向这个路径，file是否存在
        File dir = new File(parent);
        if (!dir.exists()) { // 检测目录是否存在
            dir.mkdirs(); // 创建当前目录
        }
        // 获取到这个文件名称 uuid工具来将生成一个新的字符串作为文件名
        // 例如：avatar01.png
        String originalFilename = file.getOriginalFilename();
        System.out.println("Originalfilename" + originalFilename);
        // 截取文件后缀
        String suffix = "";
        int index = originalFilename.lastIndexOf(".");
        suffix = originalFilename.substring(index);
        // 随机生成前缀名并拼接
        String filename = UUID.randomUUID().toString().toUpperCase() + suffix;

        File dest = new File(dir, filename); // 是一个空文件
        // 参数file中数据写入到这个空文件中
        try {
            file.transferTo(dest); //将file文件中的数据写入到dest文件中
        }
        catch (Exception e) {
            System.out.println("文件状态异常或文件读写异常");
        }


        // 返回头像的路径/upload/test.png
        String avatar = "/static/avatar/" + filename;

        User user = userService.findById(Long.valueOf(response.getMessage()));
        File oldAvatar = new File(System.getProperty("user.dir") + user.getAvatarURL());
        try {
            oldAvatar.delete();
        }
        catch (Exception e) {
            System.out.println("文件状态异常或文件读写异常");
        }
        user.setAvatarURL(avatar);
        userRepository.save(user);
        if(!(user instanceof Manager)){
            List<Post> postList = new ArrayList<>();
            if(user instanceof Teacher)
                postList = ((Teacher) user).getPostList();
            if(user instanceof Student)
                postList = ((Student) user).getPostList();
            for(Post p : postList){
                p.setAvatarUrl(avatar);
                postRepository.save(p);
            }
        }
        // 返回用户头像的路径给前端，将来用于头像的展示使用
        System.out.println("内容保存至" + avatar);
        response.setSuccess(true);
        response.setContent(avatar);
        return response;
    }

    @Override
    public CommonResponse handleSavePhoto(MultipartFile file, String token){
        System.out.println("intoPhotoProcess");
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess()){
            return response;
        }
        // 文件是否为空
        if (file.isEmpty()) {
            System.out.println("文件为空");
        }
        if (file.getSize() > AVATAR_MAX_SIZE) {
            System.out.println("文件大小超出限制");
        }
        // 文件类型是否符合
        String contentType = file.getContentType();
        // 如果集合包含某元素则返回ture
        if (!AVATAR_TYPE.contains(contentType)) {
            System.out.println("文件类型不支持");
        }
        // 上传的文件.../upload/文件.png
        String parent =
                System.getProperty("user.dir") + "/static/photo";
        // File对象指向这个路径，file是否存在
        File dir = new File(parent);
        if (!dir.exists()) { // 检测目录是否存在
            dir.mkdirs(); // 创建当前目录
        }
        // 获取到这个文件名称 uuid工具来将生成一个新的字符串作为文件名
        // 例如：avatar01.png
        String originalFilename = file.getOriginalFilename();
        System.out.println("Originalfilename" + originalFilename);
        // 截取文件后缀
        String suffix = "";
        int index = originalFilename.lastIndexOf(".");
        suffix = originalFilename.substring(index);
        // 随机生成前缀名并拼接
        String filename = UUID.randomUUID().toString().toUpperCase() + suffix;

        File dest = new File(dir, filename); // 是一个空文件
        // 参数file中数据写入到这个空文件中
        try {
            file.transferTo(dest); //将file文件中的数据写入到dest文件中
        }
        catch (Exception e) {
            System.out.println("文件状态异常或文件读写异常");
        }
        // 返回头像的路径/upload/test.png
        String photo = "/static/photo/" + filename;
        User user = userService.findById(Long.valueOf(response.getMessage()));
        File oldPhoto = new File(System.getProperty("user.dir") + user.getAvatarURL());
        try {
            oldPhoto.delete();
        }
        catch (Exception e) {
            System.out.println("文件状态异常或文件读写异常");
        }
        user.setPhotoURL(photo);
        userRepository.save(user);


        // 返回用户头像的路径给前端，将来用于头像的展示使用
        System.out.println("内容保存至" + photo);
        response.setSuccess(true);
        response.setContent(photo);
        return response;
    }

}
