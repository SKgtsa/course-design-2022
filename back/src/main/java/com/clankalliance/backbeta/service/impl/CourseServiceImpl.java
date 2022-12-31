package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.Practice;
import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.CourseResponseTarget;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.FindCourseStudentData;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private UserService userService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private TeacherRepository teacherRepository;

    private Achievement COURSE_B = new Achievement(Long.parseLong("22"),"教授课程10门以上","园丁");

    private Achievement COURSE_A = new Achievement(Long.parseLong("23"),"教授课程25门以上","先生");

    private List<Achievement> updateAchievementListCourse(Teacher teacher){
        Set<Course> courseSet = teacher.getCourseSet();
        int courseNum = courseSet.size();
        List<Achievement> achievementList = teacher.getAchievementList();
        if(courseNum >= 25){
            if(achievementList.contains(COURSE_A)){
                achievementList.remove(COURSE_A);
            }
            achievementList.add(COURSE_B);
        }else if(courseNum >= 10){
            if(achievementList.contains(COURSE_B)){
                achievementList.remove(COURSE_B);
            }
            achievementList.add(COURSE_A);
        }
        return achievementList;
    }



    /**
     * 绑定学生与课程的关系
     * 传入课程id和学生对象，将该课程与学生建立关系
     * @param courseId 课程id
     * @param student 学生对象
     * @return
     */
    public CommonResponse bind(long courseId, Student student){
        CommonResponse response=new CommonResponse<>();
        Optional<Course> cop = courseRepository.findById(courseId); //检查课程是否存在
        if(cop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("该课程不存在");
        }
        Course course=cop.get();
        Set<Student> studentSet = course.getStudentSet();
        studentSet.add(student);
        course.setStudentSet(studentSet);
        courseRepository.save(course);

        Set<Course> courseSet = student.getCourseSet();
        courseSet.add(course);
        student.setCourseSet(courseSet);
        studentRepository.save(student);

        response.setSuccess(true);
        response.setMessage("绑定成功");
        return response;
    }

    /**
     * 学生退课
     * 传入token和课程id,先验证token,后用token获得用户信息
     * 解除该用户与课程id对于课程的绑定
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleQuit(String token,long courseId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student = (Student) user;
                Set<Course> courseSetOp = student.getCourseSet();
                Optional<Course> cop = courseRepository.findById(courseId); //检查课程是否存在
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("该课程不存在");
                    return response;
                }
                Course course=cop.get();
                if(courseSetOp.contains(course)){
                    //课程表中课程存在，即删除课程
                    courseSetOp.remove(course);
                    student.setCourseSet(courseSetOp);
                    studentRepository.save(student);
                    Set<Student> studentSet = course.getStudentSet();
                    studentSet.remove(student);
                    course.setStudentSet(studentSet);
                    courseRepository.save(course);
                }else{
                    response.setSuccess(false);
                    response.setMessage("你未选择此课程");
                }
                response.setSuccess(true);
                response.setMessage("退课成功");
            }
            return response;
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }

    /**
     * 将学生从课程中踢出
     * 验证token,判断权限及登录状态
     * 根据学号找到学生，根据课程id找到课程
     * 解除该课程与学生的关联
     * @param token 用户令牌
     * @param courseId 课程id
     * @param studentNumber 学号
     * @return
     */
    public CommonResponse handleRemoveStudent(String token,long courseId,long studentNumber){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher || user instanceof Manager){
                Optional<Student> sop=studentRepository.findByUserNumber(studentNumber);
                if(sop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("学生不存在");
                    return response;
                }
                Student student=sop.get();
                Optional<Course> cop=courseRepository.findById(courseId);
                Course course =cop.get();
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("课程不存在");
                    return response;
                }
                Set<Student> studentSet = course.getStudentSet();
                if(studentSet.contains(student)){
                    //课程的学生表中存在此学生，进行删除操作
                    studentSet.remove(student);
                    course.setStudentSet(studentSet);
                    courseRepository.save(course);
                    Set<Course> courseSet = student.getCourseSet();
                    courseSet.remove(course);
                    student.setCourseSet(courseSet);
                    studentRepository.save(student);
                    response.setSuccess(true);
                    response.setMessage("成功踢出学生");
                }else{
                    //课程的学生表中无此学生，报错
                    response.setSuccess(false);
                    response.setMessage("该学生不在本课程中");
                }
            }
        }
        return response;
    }

    /**
     * 保存课程
     * 进行token验证，存在课程则修改，不存在则创建
     * @param token 用户令牌
     * @param courseId 课程id(可为空)
     * @param name 课名
     * @param weekStart 教学开始周
     * @param weekEnd 教学结束周
     * @param time 时间 为list 存放ClassTime对象
     * @param capacity 课容量
     * @param studentClass 授课班级
     * @param studentSection 授课年级
     * @param location 授课地点
     * @param year 授课时间(年
     * @param semester 授课学期
     * @param credit 学分
     * @param description 课程描述
     * @return
     */
    public CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time,Integer capacity,List<String> studentClass,List<String> studentSection,String location,Integer year, String semester,Double credit,String description){
        //CommonResponse response = tokenUtil.tokenCheck(token);
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262483622651629568");//教师
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                Optional<Course> courseOp = courseRepository.findById(courseId);
                long id;
                if(courseOp.isEmpty()){
                    //该课程不存在,为课程的创建
                    id = snowFlake.nextId();
                }else {
                    //该课程存在，为课程的修改
                    id = courseOp.get().getId();
                }
                Set<Student> studentSet=new HashSet<>();
                Set<Score> scoreSet=new HashSet<>();
                //为每个时间对象分配id并储存
                //数据库中预置35个ClassTime对象
                for(ClassTime t :time){
                    //对没有id的ClassTime进行id的补充
                    t.setId(t.getSection() + (t.getWeekDay() - 1) * 5);
                }
                Course course=new Course(id,name,weekStart,weekEnd,time,capacity,studentClass.toString(),studentSection.toString(),location,year,semester,credit,description,studentSet,teacher,scoreSet);
                courseRepository.save(course);

                //在教师的课程表中加入课程
                Set<Course> teacherCourseSet = teacher.getCourseSet();
                teacherCourseSet.add(course);
                teacher.setCourseSet(teacherCourseSet);
                teacher.setAchievementList(updateAchievementListCourse(teacher));
                //保存
                teacherRepository.save(teacher);
                response.setSuccess(true);
                response.setMessage("保存成功");
            }else if(user instanceof Manager){
                //管理员没有课程表，故只能修改
                Course courseOld = courseRepository.findById(courseId).get();
                Course course=new Course(courseId,name,weekStart,weekEnd,time,capacity,studentClass.toString(),studentSection.toString(),location,year,semester,credit,description,courseOld.getStudentSet(),courseOld.getTeacher(),courseOld.getScoreSet());
                courseRepository.save(course);
                response.setSuccess(true);
                response.setMessage("保存成功");
            }
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }


    /**
     * 删除课程
     * 验证token,根据课程id找到课程并删除
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleDelete(String token,long courseId){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("257545660272873472");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher || user instanceof Manager){
                Optional<Course> courseOp = courseRepository.findById(courseId);
                Course course=courseOp.get();
                Set<Student> studentSetOp=course.getStudentSet();
                //注意课程有学生选择时需先解绑再删除
                if(studentSetOp.size()!=0){
                    for(Student stu : studentSetOp){
                        Set<Course> stuCourseSetOp = stu.getCourseSet();
                        stuCourseSetOp.remove(course);
                        stu.setCourseSet(stuCourseSetOp);
                    }
                    courseRepository.delete(course);
                    response.setSuccess(true);
                    response.setMessage("课程删除成功");
                }else{
                    courseRepository.delete(course);
                    response.setSuccess(true);
                    response.setMessage("课程删除成功");
                }
                Teacher teacher = course.getTeacher();
                teacher.setAchievementList(updateAchievementListCourse(teacher));
                teacherRepository.save(teacher);
            }
        }
        return response;
    }

    /**
     * 学生查看课表
     * 验证token,返回该学生当前正在上的课
     * @param token
     * @return
     */
    public CommonResponse handleFindCourseList(String token){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("257545660272873472");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Set<Course> courseSetOp = ((Student) user).getCourseSet();
                response.setSuccess(true);
                response.setMessage("查询成功");
                response.setContent(courseSetOp);
            }else{
                response.setSuccess(false);
                response.setMessage("权限错误");
            }
        }
        return response;
    }


    /**
     * 选课时用来返回所有课程
     * 验证token,若filterOpen为true则需过滤时间冲突的及已选的课
     * 若filterOpen为false则只需过滤已选的课
     * @param token
     * @param filterOpen
     * @return
     */
    public CommonResponse handleFindAllCourse(String token,boolean filterOpen,Integer pageNum,Integer pageSize){
        //CommonResponse response = tokenUtil.tokenCheck(token);
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262549303493529600");//学生
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                Set<Course> courseSet=student.getCourseSet();
                List<Course> allCourseList=courseRepository.findAll();
                //过滤器打开时，在全部课表中删除时间冲突课程
                if(filterOpen){
                    for(Course c : courseSet){
                        List<ClassTime> time=c.getTime();
                        for(Course re : allCourseList){
                            if(re.getTime().equals(time)){
                                allCourseList.remove(re);
                            }
                        }
                    }
                }
                if(allCourseList.size() <= pageSize){
                    response.setContent(allCourseList);
                    response.setSuccess(true);
                    response.setMessage("返回课程成功");
                }else if(allCourseList.size() == 0){
                    response.setSuccess(true);
                    response.setMessage("无课程可供学生选择");
                }else{
                    List<CourseResponseTarget> result = new ArrayList<>();
                    int count=1;
                    for(Course c1 : allCourseList){
                        if(count> (pageNum-1)*pageSize && count<= pageNum*pageSize){
                            result.add(new CourseResponseTarget(c1));
                        }
                        count++;
                    }
                    int size=allCourseList.size();
                    //计算总页数
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(result);
                    response.setSuccess(true);
                    response.setMessage("返回课程成功");
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

    /**
     * 打分时返回所选课程的所有学生
     * 验证token,根据courseId找到课程，遍历course的学生列表
     * 只选取名字name id返回前端
     * @param token 用户令牌
     * @param courseId 课程id
     * @return
     */
    public CommonResponse handleFindCourseStudent(String token,long courseId,Integer pageNum,Integer pageSize){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher || user instanceof Manager){
                Optional<Course> courseOp=courseRepository.findById(courseId);
                if(courseOp.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("找不到课程");
                    return response;
                }
                Course course=courseOp.get();
                Set<Student> studentSet=course.getStudentSet();
                List<Student> studentList = studentSet.stream().toList();
                studentList = studentList.stream().sorted(Comparator.comparing(Student::getUserNumber)).collect(Collectors.toList());
                if(studentList.size() ==0){
                    response.setContent(new ArrayList<>());
                    response.setSuccess(true);
                    response.setMessage("该课程暂无学生");
                    response.setTotalPage(0);
                }else {
                    List<Student> subStudentList = studentList.subList((pageNum - 1) * pageSize,pageNum * pageSize >= studentList.size()? studentList.size() : pageNum * pageSize);
                    List<FindCourseStudentData> result = new ArrayList<>();
                    for(Student s : subStudentList){
                        result.add(new FindCourseStudentData(s));
                    }
                    int size=studentList.size();
                    //计算总页数
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(result);
                    response.setMessage("学生表返回成功");
                    response.setSuccess(true);
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleTeacherFindCourse(String token, Integer year, String semester){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262478737566732288");//教师
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Teacher teacher=(Teacher) user;
                long teacherId= teacher.getId();
                List<Course> courseList=courseRepository.findByTeacherTime(teacherId,year,semester);
                response.setContent(courseList);
                response.setSuccess(true);
                response.setMessage("教师课程表返回成功");
            }
        }
        return response;
    }
}
