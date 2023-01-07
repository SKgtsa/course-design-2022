package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.blog.BlogDetail;
import com.clankalliance.backbeta.entity.blog.Comment;
import com.clankalliance.backbeta.response.dataBody.PersonalPageData;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.blogRepository.PostRepository;
import com.clankalliance.backbeta.repository.blogRepository.CommentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.dataBody.PostResponseTarget;
import com.clankalliance.backbeta.service.BlogService;
import com.clankalliance.backbeta.service.GeneralUploadService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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


    private Achievement COLLECT_C = new Achievement(Long.parseLong("4"),"收藏博客5篇以上","博客初心者");

    private Achievement COLLECT_B = new Achievement(Long.parseLong("5"),"收藏博客30篇以上","食量巨大的阅读者");

    private Achievement COLLECT_A = new Achievement(Long.parseLong("6"),"收藏博客50篇以上","识文解意的爱书人");

    private Achievement WRITE_C = new Achievement(Long.parseLong("17"),"撰写第一篇博客","初级作家");

    private Achievement WRITE_B = new Achievement(Long.parseLong("18"),"撰写博客5篇以上","生产力作家");

    private Achievement WRITE_A = new Achievement(Long.parseLong("19"),"撰写博客10篇以上","文豪");

    private Achievement LIKE = new Achievement(Long.parseLong("20"),"单篇博客收获点赞10个以上","高质量人类作家");


    private Set<Achievement> updateAchievementListCollect(User user){
        int collectNum;
        Set<Achievement> achievementSet;
        if(user instanceof Teacher){
            Teacher teacher = (Teacher)user;
            collectNum = teacher.getCollection().size();
            achievementSet = teacher.getAchievementSet();
        }else if(user instanceof Student){
            Student student = (Student)user;
            collectNum = student.getCollection().size();
            achievementSet = student.getAchievementSet();
        }else{
            return new HashSet<>();
        }
        if(collectNum >= 50){
            if(achievementSet.contains(COLLECT_B))
                achievementSet.remove(COLLECT_B);
            if(achievementSet.contains(COLLECT_C))
                achievementSet.remove(COLLECT_C);
            achievementSet.add(COLLECT_A);
        }else if(collectNum >= 30){
            if(achievementSet.contains(COLLECT_A))
                achievementSet.remove(COLLECT_A);
            if(achievementSet.contains(COLLECT_C))
                achievementSet.remove(COLLECT_C);
            achievementSet.add(COLLECT_B);
        }else if(collectNum >= 5){
            if(achievementSet.contains(COLLECT_A))
                achievementSet.remove(COLLECT_A);
            if(achievementSet.contains(COLLECT_B))
                achievementSet.remove(COLLECT_B);
            achievementSet.add(COLLECT_C);
        }
        return achievementSet;
    }

    private Set<Achievement> updateAchievementListWrite(User user){
        int writeNum;
        Set<Achievement> achievementSet;
        if(user instanceof Teacher){
            Teacher teacher = (Teacher)user;
            writeNum = teacher.getPostList().size();
            achievementSet = teacher.getAchievementSet();
        }else if(user instanceof Student){
            Student student = (Student)user;
            writeNum = student.getPostList().size();
            achievementSet = student.getAchievementSet();
        }else{
            return new HashSet<>();
        }
        if(writeNum >= 10){
            if(achievementSet.contains(WRITE_B))
                achievementSet.remove(WRITE_B);
            if(achievementSet.contains(WRITE_C))
                achievementSet.remove(WRITE_C);
            achievementSet.add(WRITE_A);
        }else if(writeNum >= 5){
            if(achievementSet.contains(WRITE_A))
                achievementSet.remove(WRITE_A);
            if(achievementSet.contains(WRITE_C))
                achievementSet.remove(WRITE_C);
            achievementSet.add(WRITE_B);
        }else if(writeNum >= 1){
            if(achievementSet.contains(WRITE_A))
                achievementSet.remove(WRITE_A);
            if(achievementSet.contains(WRITE_B))
                achievementSet.remove(WRITE_B);
            achievementSet.add(WRITE_C);
        }
        return achievementSet;
    }

    @Override
    public CommonResponse handleSubmit(String token, String heading, String content, MultipartFile topImage){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy年MM月dd日 hh时mm分");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));;
        Post post = new Post(UUID.randomUUID().toString(),heading,content, user.getNickName(), user.getAvatarURL(), user.getId(), dateFormat.format(date), generalUploadService.upload(topImage), new ArrayList<>(),new ArrayList<>(), new ArrayList<>());

        if(! (user instanceof Manager)){
            try{
                postRepository.save(post);
            }catch (Exception e){
                response.setMessage("保存失败");
                response.setSuccess(false);
                return response;
            }
        }else{
            response.setSuccess(false);
            response.setMessage("权限错误");
            return response;
        }
        updateAchievementListWrite(user);
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            List<Post> postList = teacher.getPostList();
            postList.add(post);
            teacher.setPostList(postList);
            teacherRepository.save(teacher);
        }else if(user instanceof Student){
            Student student = (Student) user;
            List<Post> postList = student.getPostList();
            postList.add(post);
            student.setPostList(postList);
            studentRepository.save(student);
        }
        response.setMessage("保存成功");
        return response;
    }

    @Override
    public CommonResponse handleMine(String token, int length, int startIndex, String keyWord){
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
        totalPost = totalPost.stream().filter(post -> post.getHeading().contains(keyWord))
                .collect(Collectors.toList());
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

    //获取首页信息 所有人的帖子
    @Override
    public CommonResponse handleMainPage(String token, int length, int startIndex, String keyWord){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = postRepository.findByHeading(keyWord);
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
    public CommonResponse handleLikePost(String token, int length, int startIndex, String keyWord){
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
        totalPost = totalPost.stream().filter(post -> post.getHeading().contains(keyWord)).collect(Collectors.toList());
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
    //收藏的帖子 按时间顺序分页
    @Override
    public CommonResponse handleCollectPost(String token, int length, int startIndex, String keyWord){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = new ArrayList<>();
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            totalPost = teacher.getCollection();
        }else if(user instanceof Student){
            Student student = (Student) user;
            totalPost = student.getCollection();
        }
        totalPost = totalPost.stream().filter(post -> post.getHeading().contains(keyWord)).collect(Collectors.toList());
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

    //查看个人主页 若登录状态失效则以游客模式
    @Override
    public CommonResponse handlePersonalPageData(String token, Long userId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        //success为false代表登录失效，但仍允许浏览
        User target = userService.findById(userId);
        boolean follow = false;
        if(target == null){
            response.setSuccess(false);
            response.setMessage("用户不存在");
            response.setContent(new PersonalPageData());
            return response;
        }
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student = (Student) user;
                if(target instanceof Student){
                    follow = student.getFriendS().contains(target);
                }else if(target instanceof Teacher){
                    follow = student.getFriendT().contains(target);
                }
            }else if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                if(target instanceof Student){
                    follow = teacher.getFriendS().contains(target);
                }else if(target instanceof Teacher){
                    follow = teacher.getFriendS().contains(target);
                }
            }
        }
        response.setContent(new PersonalPageData(target, follow));
        response.setMessage("查找成功");
        return response;
    }
    //查看个人主页 博客更新方法
    @Override
    public CommonResponse handlePersonalPagePost(String token, int length, int startIndex,Long userId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        //success为false代表登录失效，但仍允许浏览
        User target = userService.findById(userId);
        if(target == null){
            response.setSuccess(false);
            response.setMessage("用户不存在");
            response.setContent(new PersonalPageData());
            return response;
        }
        List<Post> totalPost = new ArrayList<>();
        if(target instanceof Teacher){
            Teacher teacher = (Teacher) target;
            totalPost = teacher.getPostList();
        }else if(target instanceof Student){
            Student student = (Student) target;
            totalPost = student.getPostList();
        }
        totalPost = totalPost.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
        Collections.reverse(totalPost);
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() : startIndex + length);
        User user = new Manager();
        if(response.getSuccess()){
            user = userService.findById(Long.parseLong(response.getMessage()));
        }
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setMessage("查找成功");
        response.setContent(resultList);
        response.setStartIndex(tempList.size() + startIndex);
        return response;
    }

    //查看详细文章 前端传来token和文章id 获取全文
    @Override
    public CommonResponse handleDetailPage(String token,String blogId){
        CommonResponse response = tokenUtil.tokenCheck(token);
//        if(!response.getSuccess())
//            return response;
        Optional<Post> pop = postRepository.findById(blogId);
        if(pop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("找不到文章");
            return response;
        }else{
            Post post = pop.get();
            response.setContent(new BlogDetail(post.getContent(), post.getCommentList()));
            response.setMessage("查找成功");
            return response;
        }
    }
    //点赞 若已点赞就取消
    @Override
    public CommonResponse handleLike(String token, String blogId){
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
                Student student = (Student) user;
                List<Student> studentList = post.getLikeS();
                if(studentList.contains(student)){
                    studentList.remove(student);
                }else{
                    studentList.add((Student) user);
                }
                post.setLikeS(studentList);
            }else if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                List<Teacher> teacherList = post.getLikeT();
                if(teacherList.contains(teacher)){
                    teacherList.remove(teacher);
                }else{
                    teacherList.add((Teacher) user);
                }
                post.setLikeT(teacherList);
            }else{
                response.setMessage("权限错误");
                response.setSuccess(false);
                return response;
            }

            if(post.getLikeS().size() + post.getLikeT().size() >= 10){
                User author = userService.findById(post.getUserId());
                if(author instanceof Teacher){
                    Teacher teacher = (Teacher)author;
                    Set<Achievement> achievementSet = teacher.getAchievementSet();
                    if(!achievementSet.contains(LIKE)){
                        achievementSet.add(LIKE);
                        teacher.setAchievementSet(achievementSet);
                        teacherRepository.save(teacher);
                    }
                }else if(author instanceof Student){
                    Student student = (Student) author;
                    Set<Achievement> achievementSet = student.getAchievementSet();
                    if(!achievementSet.contains(LIKE)){
                        achievementSet.add(LIKE);
                        student.setAchievementSet(achievementSet);
                        studentRepository.save(student);
                    }
                }
            }
            postRepository.save(post);
            response.setMessage("操作成功");
            return response;
        }
    }
    //收藏 若已收藏就取消
    @Override
    public CommonResponse handleCollect(String token, String blogId){
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
            updateAchievementListCollect(user);
            if(user instanceof Student){
                Student student = (Student) user;
                List<Post> postList = student.getCollection();
                if(postList.contains(post)){
                    postList.remove(post);
                }else{
                    postList.add(post);
                }
                student.setCollection(postList);
                studentRepository.save(student);
            }else if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                List<Post> postList = teacher.getCollection();
                if(postList.contains(post)){
                    postList.remove(post);
                }else{
                    postList.add(post);
                }
                teacher.setCollection(postList);
                teacherRepository.save(teacher);
            }else{
                response.setMessage("权限错误");
                response.setSuccess(false);
                return response;
            }
            response.setMessage("操作成功");
            return response;
        }
    }
    //管理员任意删除，其他角色删除自己的
    public CommonResponse handleDelete(String token, String blogId){
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
                Student student = (Student) user;
                List<Post> postList = student.getPostList();
                if(!postList.contains(post)){
                    response.setMessage("您无权进行此操作");
                    response.setSuccess(false);
                    return response;
                }
            }else if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                List<Post> postList = teacher.getPostList();
                if(!postList.contains(post)){
                    response.setMessage("您无权进行此操作");
                    response.setSuccess(false);
                    return response;
                }
            }
            User author = userService.findById(post.getUserId());
            if(author instanceof Student){
                Student student = (Student) author;
                Set<Achievement> achievementSet = student.getAchievementSet();
                if(post.getLikeT().size() + post.getLikeS().size() >= 10){
                    if(achievementSet.contains(LIKE)){
                        boolean valid = false;
                        for(Post p : student.getPostList()){
                            if(p.getLikeS().size() + p.getLikeT().size() >= 10){
                                valid = true;
                                break;
                            }
                        }
                        if(!valid){
                            achievementSet.remove(LIKE);
                            student.setAchievementSet(achievementSet);
                        }
                    }
                }
                student.setAchievementSet(updateAchievementListWrite(student));
                student.setAchievementSet(updateAchievementListCollect(student));
                student.setAchievementSet(updateAchievementListWrite(student));
                studentRepository.save(student);
            }else if(author instanceof Teacher){
                Teacher teacher = (Teacher) author;
                Set<Achievement> achievementSet = teacher.getAchievementSet();
                if(post.getLikeT().size() + post.getLikeS().size() >= 10){
                    if(achievementSet.contains(LIKE)){
                        boolean valid = false;
                        for(Post p : teacher.getPostList()){
                            if(p.getLikeS().size() + p.getLikeT().size() >= 10){
                                valid = true;
                                break;
                            }
                        }
                        if(!valid){
                            achievementSet.remove(LIKE);
                            teacher.setAchievementSet(achievementSet);
                        }
                    }
                }
                teacher.setAchievementSet(updateAchievementListWrite(teacher));
                teacher.setAchievementSet(updateAchievementListCollect(teacher));
                teacher.setAchievementSet(updateAchievementListWrite(teacher));
                teacherRepository.save(teacher);
            }
            //有权限 进行删除
            postRepository.delete(post);
            response.setMessage("操作成功");
            return response;
        }
    }
    //评论
    @Override
    public CommonResponse handleComment(String token, String blogId, String content){
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
        if(response.getMessage().equals("" + userId)){
            response.setSuccess(false);
            response.setMessage("不能关注自己");
            return response;
        }
        User user = userService.findById(Long.parseLong(response.getMessage()));
        User target = userService.findById(userId);
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            if(target instanceof Teacher){
                Teacher t = (Teacher) target;
                Set<Teacher> set = teacher.getFriendT();
                if(set.contains(t)){
                    set.remove(t);
                }else{
                    set.add(t);
                }
                set.add(t);
                teacher.setFriendT(set);
            }else if(target instanceof Student){
                Student s = (Student) target;
                Set<Student> set = teacher.getFriendS();
                if(set.contains(s)){
                    set.remove(s);
                }else{
                    set.add(s);
                }
                teacher.setFriendS(set);
            }
            teacherRepository.save(teacher);
        }else if(user instanceof  Student){
            Student student = (Student) user;
            if(target instanceof Teacher){
                Teacher t = (Teacher) target;
                Set<Teacher> set = student.getFriendT();
                if(set.contains(t)){
                    set.remove(t);
                }else{
                    set.add(t);
                }
                student.setFriendT(set);
            }else if(target instanceof Student){
                Student s = (Student) target;
                Set<Student> set = student.getFriendS();
                if(set.contains(s)){
                    set.remove(s);
                }else{
                    set.add(s);
                }
                student.setFriendS(set);
            }
            studentRepository.save(student);
        }
        response.setMessage("保存成功");
        return response;
    }

    //搜索博客
    public CommonResponse handleSearch(String token, String heading, int length,int startIndex){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        List<Post> totalPost = postRepository.findByHeading(heading);
        totalPost = totalPost.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
        Collections.reverse(totalPost);
        response.setMessage("查找成功");
        List<PostResponseTarget> resultList = new ArrayList<>();
        List<Post> tempList = totalPost.subList(startIndex,(startIndex + length) >= totalPost.size()? totalPost.size() : startIndex + length);
        for(Post p : tempList){
            resultList.add(new PostResponseTarget(user,p));
        }
        response.setContent(resultList);
        return response;
    }
    //删除评论
    public CommonResponse handleDeleteComment(String token,String id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        User user = userService.findById(Long.parseLong(response.getMessage()));
        if(user instanceof Manager){
            Optional<Comment> cop = commentRepository.findById(id);
            if(cop.isEmpty()){
                response.setSuccess(false);
                response.setMessage("评论不存在");
                return response;
            }
            Comment comment = cop.get();
            commentRepository.delete(comment);
            response.setMessage("删除成功");
        }else{
            response.setMessage("权限不足");
            response.setSuccess(false);
        }
        return response;
    }

}
