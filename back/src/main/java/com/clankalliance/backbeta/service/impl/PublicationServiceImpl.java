package com.clankalliance.backbeta.service.impl;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.publications.Publication;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.PublicationRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.dataBody.TeacherPersonalPageData;
import com.clankalliance.backbeta.service.GeneralUploadService;
import com.clankalliance.backbeta.service.PublicationService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.AntiInjection;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Resource
    private PublicationRepository publicationRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Resource
    private GeneralUploadService generalUploadService;


    /**
     * 老师提交新的论文
     * @param token 用户令牌
     * @param heading 论文标题
     * @param content 论文内容
     * @param topImage 论文封面
     * @return
     */
    @Override
    public CommonResponse handleSubmit(String token, String heading, String content, MultipartFile topImage){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Publication publication = new Publication(UUID.randomUUID().toString(),heading,content, generalUploadService.upload(topImage),Long.parseLong(response.getMessage()));
        if(user instanceof Teacher){
            try{
                publicationRepository.save(publication);
            }catch (Exception e){
                response.setMessage("保存失败");
                response.setSuccess(false);
                return response;
            }
            Teacher teacher = (Teacher) user;
            List<Publication> publicationList = teacher.getPublicationList();
            publicationList.add(publication);
            teacher.setPublicationList(publicationList);
            teacherRepository.save(teacher);
        }else{
            response.setSuccess(false);
            response.setMessage("权限错误");
            return response;
        }
        response.setMessage("保存成功");
        return response;
    }


    /**
     * 查看个人主页 若登录状态失效则以游客模式
     * @param token
     * @param userId
     * @return
     */
    @Override
    public CommonResponse handlePersonalPageData(String token, Long userId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        //success为false代表登录失效，但仍允许浏览
        User target = userService.findById(userId);
        if(target == null){
            response.setSuccess(false);
            response.setMessage("用户不存在");
            response.setContent(new TeacherPersonalPageData());
            return response;
        }
        if(target == null ||  !(target instanceof Teacher)){
            response.setSuccess(false);
            response.setMessage("用户不存在或不是老师");
            response.setContent(new TeacherPersonalPageData());
            return response;
        }
        response.setContent(new TeacherPersonalPageData((Teacher) target));
        response.setMessage("查找成功");
        return response;
    }


    /**
     * 查看个人主页 论文更新方法
     * 前端为无限列表，所以没有严格页码
     * @param token 用户令牌
     * @param length 页长
     * @param startIndex 列表中本页开始的索引
     * @param userId 用户id
     * @return
     */
    @Override
    public CommonResponse handlePersonalPagePublication(String token, int length, int startIndex,Long userId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        //success为false代表登录失效，但仍允许浏览
        User target = userService.findById(userId);
        if(target == null){
            response.setSuccess(false);
            response.setMessage("用户不存在");
            response.setContent(new TeacherPersonalPageData());
            return response;
        }
        List<Publication> totalPublications = new ArrayList<>();
        if(target instanceof Teacher){
            Teacher teacher = (Teacher) target;
            totalPublications = teacher.getPublicationList();
        }else{
            response.setSuccess(false);
            response.setMessage("用户不是老师");
            return response;
        }
        Collections.reverse(totalPublications);
        List<Publication> tempList = totalPublications.subList(startIndex,(startIndex + length) >= totalPublications.size()? totalPublications.size() : startIndex + length);
        response.setMessage("查找成功");
        response.setContent(tempList);
        response.setStartIndex(tempList.size() + startIndex);
        return response;
    }


    /**
     * 查看详细文章 获取全文
     * @param token 用户令牌
     * @param publicationId 论文id
     * @return
     */
    @Override
    public CommonResponse handleDetailPage(String token,String publicationId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(AntiInjection.containsSqlInjection(publicationId)){
            response.setSuccess(false);
            response.setMessage("参数不合法");
            return response;
        }
        Optional<Publication> pop = publicationRepository.findById(publicationId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Publication post = pop.get();
            response.setContent(post.getContent());
            response.setMessage("查找成功");
            return response;
        }
    }

    /**
     * 删除论文
     * @param token 用户令牌
     * @param publicationId 论文id
     * @return
     */
    @Override
    //管理员任意删除，其他角色删除自己的
    public CommonResponse handleDelete(String token, String publicationId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        if(AntiInjection.containsSqlInjection(publicationId)){
            response.setSuccess(false);
            response.setMessage("参数不合法");
            return response;
        }
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Optional<Publication> pop = publicationRepository.findById(publicationId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Publication publication = pop.get();
            if(user instanceof Student){
                response.setMessage("您无权进行此操作");
                response.setSuccess(false);
                return response;
            }else if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                List<Publication> publicationList = teacher.getPublicationList();
                if(!publicationList.contains(publication)){
                    response.setMessage("您无权进行此操作");
                    response.setSuccess(false);
                    return response;
                }
            }//有权限 进行删除
            try{
                File topImage = new File(publication.getTopImageURL());
                topImage.delete();
            }catch (Exception e){
                System.out.println("删除出错 删除对象：" + publication.getTopImageURL());
            }
            publicationRepository.delete(publication);
            response.setMessage("操作成功");
            return response;
        }
    }

}
