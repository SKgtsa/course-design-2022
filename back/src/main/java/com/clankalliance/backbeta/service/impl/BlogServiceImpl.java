package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.blog.Comment;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.PostRepository;
import com.clankalliance.backbeta.repository.userRepository.CommentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.PostResponseTarget;
import com.clankalliance.backbeta.service.BlogService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private UserService userService;

    @Resource
    private PostRepository postRepository;

    @Resource
    private CommentRepository commentRepository;

    //获取首页信息 好友帖子 按时间顺序分页
    @Override
    public CommonResponse handleMainPage(String token, int length, int startIndex){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = new ArrayList<>();
        Set<Student> friendS = new HashSet<>();
        Set<Teacher> friendT = new HashSet<>();
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            friendS = teacher.getFriendS();
            friendT = teacher.getFriendT();
        }else if(user instanceof Student){
            Student student = (Student) user;
            friendS = student.getFriendS();
            friendT = student.getFriendT();
        }
        for(Student s : friendS){
            totalPost.addAll(s.getPostList());
        }
        for(Teacher t : friendT){
            totalPost.addAll(t.getPostList());
        }
        totalPost = totalPost.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
        response.setMessage("查找成功");
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() - 1 : startIndex + length);
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setContent(tempList);
        return response;
    }
    //查看详细文章 前端传来token和文章id 获取全文
    @Override
    public CommonResponse handleDetailPage(String token,int blogId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        Optional<Post> pop = postRepository.findById(blogId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Post post = pop.get();
            Map result = new HashMap<String,String>();
            result.put("content",post.getContent());
            response.setContent(result);
            response.setMessage("查找成功");
            return response;
        }
    }
    //点赞
    @Override
    public CommonResponse handleLike(String token, int blogId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Optional<Post> pop = postRepository.findById(blogId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Post post = pop.get();
            if(user instanceof Student){
                List<Student> studentList = post.getLikeS();
                studentList.add((Student) user);
                post.setLikeS(studentList);
            }else if(user instanceof Teacher){
                List<Teacher> teacherList = post.getLikeT();
                teacherList.add((Teacher) user);
                post.setLikeT(teacherList);
            }else{
                response.setMessage("权限错误");
                response.setSuccess(false);
                return response;
            }
            postRepository.save(post);
            response.setMessage("操作成功");
            return response;
        }
    }
    //评论
    @Override
    public CommonResponse handleComment(String token, int blogId, String content){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Optional<Post> pop = postRepository.findById(blogId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Comment comment = new Comment(UUID.randomUUID().toString(),content,("" + user.getId()),user.getNickName(), user.getAvatarURL());
            Post post = pop.get();
            List<Comment> commentList = post.getCommentList();
            commentList.add(comment);
            post.setCommentList(commentList);
            commentRepository.save(comment);
            postRepository.save(post);
            response.setMessage("评论成功");
            return response;
        }
    }

}
