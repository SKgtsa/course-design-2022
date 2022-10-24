package com.clankalliance.backbeta.controller;

import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileUploadIOException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.security.auth.callback.ConfirmationCallback.OK;

@RestController
@RequestMapping("/api/upload")
public class AvatarTestController {


    @Resource
    private TokenUtil tokenUtil;


    // 设置上传文件的最大值
    public static final int AVATAR_MAX_SIZE = 10*1024*1024;

    // 限制上传文件的类型
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("images/jpeg");
        AVATAR_TYPE.add("images/png");
        AVATAR_TYPE.add("images/bmp");
        AVATAR_TYPE.add("images/gif");
    }

    @RequestMapping("/change_avatar")
    public CommonResponse changeAvatar(HttpSession session,
                                       // 路径变量 解决前后端不一致
                                       @RequestParam("file") MultipartFile file) throws IOException {




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
                session.getServletContext().
                        getRealPath("static");
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

        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        // 返回头像的路径/upload/test.png
        String avatar = "/static" + filename;
        userService.changeAvatar(uid, avatar, username);
        // 返回用户头像的路径给前端，将来用于头像的展示使用
        CommonResponse response = new CommonResponse();
        response.setSuccess(true);
        response.setContent(avatar);
        return response;

    }


}
