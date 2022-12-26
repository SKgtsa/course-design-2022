package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.blog.Comment;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.PostRepository;
import com.clankalliance.backbeta.repository.userRepository.CommentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.PostResponseTarget;
import com.clankalliance.backbeta.service.BlogService;
import com.clankalliance.backbeta.service.GeneralUploadService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource
    private GeneralUploadService generalUploadService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Override
    public CommonResponse handleSubmit(String token, String heading, String content, MultipartFile topImage){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Post post = new Post(UUID.randomUUID().toString(),heading,content, user.getNickName(), user.getAvatarURL(), user.getId(), new Date(), generalUploadService.upload(topImage), new ArrayList<>(),new ArrayList<>(), new ArrayList<>());
        try{
            postRepository.save(post);
        }catch (Exception e){
            response.setMessage("保存失败");
            response.setSuccess(false);
            return response;
        }
        response.setMessage("保存成功");
        return response;
    }

    @Override
    public CommonResponse handleMine(String token, int length, int startIndex){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = new ArrayList<>();
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            totalPost = teacher.getPostList();
        }else if(user instanceof Student){
            Student student = (Student) user;
            totalPost = student.getPostList();
        }
        Collections.reverse(totalPost);
        response.setMessage("查找成功");
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() - 1 : startIndex + length);
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setContent(tempList);
        response.setStartIndex(tempList.size() + startIndex);
        return response;
    }

    //获取首页信息 所有人的帖子
    @Override
    public CommonResponse handleMainPage(String token, int length, int startIndex){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = postRepository.findAll();
        totalPost = totalPost.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
        Collections.reverse(totalPost);
        response.setMessage("查找成功");
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() : startIndex + length);
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setContent(resultList);
        response.setStartIndex(tempList.size() + startIndex);
        return response;
    }

    //关注者的帖子 按时间顺序分页
    @Override
    public CommonResponse handleLikePost(String token, int length, int startIndex){
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
        Collections.reverse(totalPost);
        response.setMessage("查找成功");
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() - 1 : startIndex + length);
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setContent(tempList);
        response.setStartIndex(tempList.size() + startIndex);
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

    @Override
    public CommonResponse handleSubscribe(String token, Long userId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        User target = userService.findById(userId);
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            if(target instanceof Teacher){
                Teacher t = (Teacher) target;
                Set<Teacher> set = teacher.getFriendT();
                set.add(t);
                teacher.setFriendT(set);
            }else if(target instanceof Student){
                Student s = (Student) target;
                Set<Student> set = teacher.getFriendS();
                set.add(s);
                teacher.setFriendS(set);
            }
            teacherRepository.save(teacher);
        }else if(user instanceof  Student){
            Student student = (Student) user;
            if(target instanceof Teacher){
                Teacher t = (Teacher) target;
                Set<Teacher> set = student.getFriendT();
                set.add(t);
                student.setFriendT(set);
            }else if(target instanceof Student){
                Student s = (Student) target;
                Set<Student> set = student.getFriendS();
                set.add(s);
                student.setFriendS(set);
            }
            studentRepository.save(student);
        }
        response.setMessage("保存成功");
        return response;
    }

}
