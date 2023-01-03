package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Announcement;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.repository.AnnouncementRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.AnnouncementService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {


    @Resource
    private SnowFlake snowFlake;

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Resource
    private AnnouncementRepository announcementRepository;


    //获得前五个公告
    public CommonResponse handleGet(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        List<Announcement> list = announcementRepository.findAll();
        response.setContent(list.subList( ((list.size() - 6) >= 0 ? (list.size() - 6): 0) , list.size()));
        response.setMessage("查找成功");
        return response;
    }
    //获取正文
    public CommonResponse handleGetDetail(String token, Long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        Optional<Announcement> aop = announcementRepository.findById(id);
        if(aop.isPresent()){
            Announcement announcement = aop.get();
            response.setContent(announcement.getContent());
            response.setMessage("查找成功");
        }else{
            response.setMessage("找不到文章");
            response.setSuccess(false);
        }
        return response;

    }
    //新建公告
    public CommonResponse handlePushAnnouncement(String token, MultipartFile picture, String heading, String content){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        if(user instanceof Manager){
            // 文件是否为空
            if (picture.isEmpty()) {
                System.out.println("文件为空");
            }
            // 上传的文件.../upload/file/文件
            String parent =
                    System.getProperty("user.dir") + "/static/file";
            // File对象指向这个路径，file是否存在
            File dir = new File(parent);
            if (!dir.exists()) { // 检测目录是否存在
                dir.mkdirs(); // 创建当前目录
            }
            // 获取到这个文件名称 uuid工具来将生成一个新的字符串作为文件名
            // 例如：avatar01.png
            String originalFilename = picture.getOriginalFilename();
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
                picture.transferTo(dest); //将file文件中的数据写入到dest文件中
            }
            catch (Exception e) {
                System.out.println("文件状态异常或文件读写异常");
            }

            // 返回文件的路径/upload/test.png
            String filePath = "/static/file/" + filename;

            Announcement announcement = new Announcement(snowFlake.nextId(),filePath,heading,content);
            announcementRepository.save(announcement);
            response.setSuccess(true);
            response.setMessage("保存成功");
            return response;
        }else{
            response.setSuccess(false);
            response.setMessage("权限不足");
            return response;
        }
    }

    @Override
    public CommonResponse handleGetAll(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        List<Announcement> list = announcementRepository.findAll();
        response.setContent(list);
        response.setMessage("查找成功");
        return response;

    }

    @Override
    public CommonResponse handleDelete(String token,Long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        if(user instanceof Manager){
            Optional<Announcement> aop = announcementRepository.findById(id);
            if(aop.isEmpty()){
                response.setMessage("通知不存在");
                response.setSuccess(false);
                return response;
            }
            Announcement announcement = aop.get();
            announcementRepository.delete(announcement);
            response.setMessage("删除成功");
        }else{
            response.setMessage("权限不足");
            response.setSuccess(false);
        }
        return response;
    }

}
