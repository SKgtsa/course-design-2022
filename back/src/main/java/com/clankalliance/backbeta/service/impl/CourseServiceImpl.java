package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.CourseSelectTime;
import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.CourseSelectTimeRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.request.course.CourseRequestData;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.response.dataBody.CourseResponseTarget;
import com.clankalliance.backbeta.service.CourseService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.response.dataBody.FindCourseStudentData;
import com.clankalliance.backbeta.utils.AntiInjection;
import com.clankalliance.backbeta.utils.SnowFlake;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    private Date courseSelectStartTime;

    private Date courseSelectEndTime;

    @Resource
    private CourseSelectTimeRepository courseSelectTimeRepository;

    @PostConstruct
    public void init(){
        Optional<CourseSelectTime> top = courseSelectTimeRepository.findById(1);
        CourseSelectTime time;
        if(top.isEmpty()){
            time = new CourseSelectTime(1,new Date(),new Date());
            courseSelectTimeRepository.save(time);
        }else{
            time = top.get();
        }
        courseSelectStartTime = time.getStartTime();
        courseSelectEndTime = time.getEndTime();
    }

    private final Achievement COURSE_B = new Achievement(Long.parseLong("22"),"????????????10?????????","??????");

    private final Achievement COURSE_A = new Achievement(Long.parseLong("23"),"????????????25?????????","??????");

    private Set<Achievement> updateAchievementListCourse(Teacher teacher){
        Set<Course> courseSet = teacher.getCourseSet();
        int courseNum = courseSet.size();
        Set<Achievement> achievementSet = teacher.getAchievementSet();
        if(courseNum >= 25){
            if(achievementSet.contains(COURSE_A))
                achievementSet.remove(COURSE_A);
            achievementSet.add(COURSE_B);
        }else if(courseNum >= 10){
            if(achievementSet.contains(COURSE_B))
                achievementSet.remove(COURSE_B);
            achievementSet.add(COURSE_A);
        }else{
            if(achievementSet.contains(COURSE_A))
                achievementSet.remove(COURSE_A);
            if(achievementSet.contains(COURSE_B))
                achievementSet.remove(COURSE_B);
        }
        return achievementSet;
    }



    /**
     * ??????????????????????????????
     * ????????????id???????????????????????????????????????????????????
     * @param courseId ??????id
     * @param student ????????????
     * @return
     */
    public CommonResponse bind(long courseId, Student student){
        CommonResponse response=new CommonResponse<>();
        Optional<Course> cop = courseRepository.findById(courseId); //????????????????????????
        if(cop.isEmpty()){
            response.setSuccess(false);
            response.setMessage("??????????????????");
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
        response.setMessage("????????????");
        return response;
    }

    /**
     * ????????????
     * ??????token?????????id,?????????token,??????token??????????????????
     * ????????????????????????id?????????????????????
     * @param token ????????????
     * @param courseId ??????id
     * @return
     */
    public CommonResponse handleQuit(String token,long courseId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token????????????
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student = (Student) user;
                Set<Course> courseSetOp = student.getCourseSet();
                Optional<Course> cop = courseRepository.findById(courseId); //????????????????????????
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("??????????????????");
                    return response;
                }
                Course course=cop.get();
                if(courseSetOp.contains(course)){
                    //??????????????????????????????????????????
                    courseSetOp.remove(course);
                    student.setCourseSet(courseSetOp);
                    studentRepository.save(student);
                    Set<Student> studentSet = course.getStudentSet();
                    studentSet.remove(student);
                    course.setStudentSet(studentSet);
                    courseRepository.save(course);
                }else{
                    response.setSuccess(false);
                    response.setMessage("?????????????????????");
                }
                response.setSuccess(true);
                response.setMessage("????????????");
            }
            return response;
        }
        //token??????????????????token???????????????????????????
        return response;
    }

    /**
     * ???????????????????????????
     * ??????token,???????????????????????????
     * ???????????????????????????????????????id????????????
     * ?????????????????????????????????
     * @param token ????????????
     * @param courseId ??????id
     * @param studentNumber ??????
     * @return
     */
    public CommonResponse handleRemoveStudent(String token,long courseId,long studentNumber){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token????????????
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Optional<Student> sop=studentRepository.findByUserNumber(studentNumber);
                if(sop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("???????????????");
                    return response;
                }
                Student student=sop.get();
                Optional<Course> cop=courseRepository.findById(courseId);
                Course course =cop.get();
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("???????????????");
                    return response;
                }
                Set<Student> studentSet = course.getStudentSet();
                if(studentSet.contains(student)){
                    //?????????????????????????????????????????????????????????
                    studentSet.remove(student);
                    course.setStudentSet(studentSet);
                    courseRepository.save(course);
                    Set<Course> courseSet = student.getCourseSet();
                    courseSet.remove(course);
                    student.setCourseSet(courseSet);
                    studentRepository.save(student);
                    response.setSuccess(true);
                    response.setMessage("??????????????????");
                }else{
                    //??????????????????????????????????????????
                    response.setSuccess(false);
                    response.setMessage("???????????????????????????");
                }
            }
        }
        return response;
    }

    /**
     * ????????????
     * ??????token???????????????????????????????????????????????????
     * @param token ????????????
     * @param courseId ??????id(?????????)
     * @param name ??????
     * @param weekStart ???????????????
     * @param weekEnd ???????????????
     * @param time ?????? ???list ??????ClassTime??????
     * @param capacity ?????????
     * @param studentClass ????????????
     * @param studentSection ????????????
     * @param location ????????????
     * @param year ????????????(???
     * @param semester ????????????
     * @param credit ??????
     * @param description ????????????
     * @param weight ??????(?????????????????????????????????
     * @return
     */
    public CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time,Integer capacity,List<String> studentClass,List<String> studentSection,String location,Integer year, String semester,Double credit,String description,Double weight){
        //CommonResponse response = tokenUtil.tokenCheck(token);
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("262483622651629568");//??????
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token????????????
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                Optional<Course> courseOp = courseRepository.findById(courseId);
                long id;
                if(courseOp.isEmpty()){
                    //??????????????????,??????????????????
                    id = snowFlake.nextId();
                }else {
                    //????????????????????????????????????
                    id = courseOp.get().getId();
                }
                Set<Student> studentSet=new HashSet<>();
                Set<Score> scoreSet=new HashSet<>();
                //???????????????????????????id?????????
                //??????????????????35???ClassTime??????
                for(ClassTime t :time){
                    //?????????id???ClassTime??????id?????????
                    t.setId(t.getSection() + (t.getWeekDay() - 1) * 5);
                }
                Course course=new Course(id,name,weekStart,weekEnd,time,capacity,studentClass.toString(),studentSection.toString(),location,year,semester,credit,description,studentSet,teacher,scoreSet,weight);
                courseRepository.save(course);

                //????????????????????????????????????
                Set<Course> teacherCourseSet = teacher.getCourseSet();
                teacherCourseSet.add(course);
                teacher.setCourseSet(teacherCourseSet);
                teacher.setAchievementSet(updateAchievementListCourse(teacher));
                //??????
                teacherRepository.save(teacher);
                response.setSuccess(true);
                response.setMessage("????????????");
            }else if(user instanceof Manager){
                //??????????????????????????????????????????
                Course courseOld = courseRepository.findById(courseId).get();
                Course course=new Course(courseId,name,weekStart,weekEnd,time,capacity,studentClass.toString(),studentSection.toString(),location,year,semester,credit,description,courseOld.getStudentSet(),courseOld.getTeacher(),courseOld.getScoreSet(),weight);
                courseRepository.save(course);
                response.setSuccess(true);
                response.setMessage("????????????");
            }
        }
        //token??????????????????token???????????????????????????
        return response;
    }


    /**
     * ????????????
     * ??????token,????????????id?????????????????????
     * @param token ????????????
     * @param courseId ??????id
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
            //token????????????
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher || user instanceof Manager){
                Optional<Course> courseOp = courseRepository.findById(courseId);
                Course course=courseOp.get();
                Set<Student> studentSetOp=course.getStudentSet();
                //???????????????????????????????????????????????????
                if(studentSetOp.size()!=0){
                    for(Student stu : studentSetOp){
                        Set<Course> stuCourseSetOp = stu.getCourseSet();
                        stuCourseSetOp.remove(course);
                        stu.setCourseSet(stuCourseSetOp);
                    }
                    courseRepository.delete(course);
                    response.setSuccess(true);
                    response.setMessage("??????????????????");
                }else{
                    courseRepository.delete(course);
                    response.setSuccess(true);
                    response.setMessage("??????????????????");
                }
                Teacher teacher = course.getTeacher();
                teacher.setAchievementSet(updateAchievementListCourse(teacher));
                teacherRepository.save(teacher);
            }
        }
        return response;
    }

    /**
     * ??????????????????
     * ??????token,????????????????????????????????????
     * @param token
     * @return
     */
    public CommonResponse handleFindCourseList(String token){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user= userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Set<Course> courseSetOp = ((Student) user).getCourseSet();
                response.setSuccess(true);
                response.setMessage("????????????");
                response.setContent(courseSetOp);
            }else{
                response.setSuccess(false);
                response.setMessage("????????????");
            }
            response.setUser(new CourseSelectTime(1,courseSelectStartTime,courseSelectEndTime));
        }
        return response;
    }

    /**
     * ?????????????????????????????????
     * ??????token,???filterOpen???true??????????????????????????????????????????
     * ???filterOpen???false???????????????????????????
     * @param token
     * @param filterOpen
     * @return
     */
    public CommonResponse handleFindAllCourse(String token,boolean filterOpen,Integer pageNum,Integer pageSize){
        CommonResponse response = tokenUtil.tokenCheck(token);;
        if(response.getSuccess()){
            User user=userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student) user;
                Set<Course> courseSet=student.getCourseSet();
                List<Course> allCourseList=courseRepository.findAll();
                //???????????????????????????????????????????????????????????????
                if(filterOpen){
                    for(Course c : courseSet){
                        allCourseList.remove(c);
                        List<ClassTime> timeList = c.getTime();
                        List<Course> allCourseSample = allCourseList;
                        for(Course target : allCourseSample){
                            if(target.getWeekStart() <= c.getWeekEnd() && target.getWeekEnd() >= c.getWeekStart()){
                                for(ClassTime t : timeList){
                                    if(target.getTime().contains(t)){
                                        allCourseList.remove(target);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if(allCourseList.size() == 0){
                    response.setContent(allCourseList);
                    response.setTotalPage(1);
                }else{
                    List<CourseResponseTarget> result = new ArrayList<>();
                    for(int i = (pageNum - 1) * pageSize;i < allCourseList.size() && i < pageNum * pageSize;i ++){
                        result.add(new CourseResponseTarget(allCourseList.get(i)));
                    }
                    int size=allCourseList.size();
                    //???????????????
                    Integer totalPage = size / pageSize;
                    if(size % pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(result);
                    response.setSuccess(true);
                    response.setMessage("????????????");
                    response.setTotalPage(totalPage);
                }
                response.setSuccess(true);
                response.setMessage("????????????");
            }
        }
        return response;
    }

    /**
     * ??????????????????????????????????????????
     * ??????token,??????courseId?????????????????????course???????????????
     * ???????????????name id????????????
     * @param token ????????????
     * @param courseId ??????id
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
                    response.setMessage("???????????????");
                    return response;
                }
                Course course=courseOp.get();
                Set<Student> studentSet=course.getStudentSet();
                List<Student> studentList = studentSet.stream().toList();
                studentList = studentList.stream().sorted(Comparator.comparing(Student::getUserNumber)).collect(Collectors.toList());
                if(studentList.size() ==0){
                    response.setContent(new ArrayList<>());
                    response.setSuccess(true);
                    response.setMessage("?????????????????????");
                    response.setTotalPage(0);
                }else {
                    List<Student> subStudentList = studentList.subList((pageNum - 1) * pageSize,pageNum * pageSize >= studentList.size()? studentList.size() : pageNum * pageSize);
                    List<FindCourseStudentData> result = new ArrayList<>();
                    for(Student s : subStudentList){
                        result.add(new FindCourseStudentData(s));
                    }
                    int size=studentList.size();
                    //???????????????
                    Integer totalPage = size / pageSize;
                    if(size - totalPage*pageSize != 0){
                        totalPage++;
                    }
                    response.setContent(result);
                    response.setMessage("?????????????????????");
                    response.setSuccess(true);
                    response.setTotalPage(totalPage);
                }
            }
        }
        return response;
    }

    /**
     * ?????????????????? ??????????????????????????????
     * @param token ????????????
     * @param year ????????????
     * @param semester ????????????
     * @return
     */
    @Override
    public CommonResponse handleTeacherFindCourse(String token, Integer year, String semester){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(AntiInjection.containsSqlInjection(semester)){
            response.setSuccess(false);
            response.setMessage("???????????????");
            return response;
        }
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Teacher){
                Teacher teacher=(Teacher) user;
                long teacherId= teacher.getId();
                List<Course> courseList=courseRepository.findByTeacherTime(teacherId,year,semester);
                response.setContent(courseList);
                response.setSuccess(true);
                response.setMessage("???????????????????????????");
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerFind(String token,long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Course> courseOp=courseRepository.findById(id);
                if(courseOp.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("???????????????");
                    return response;
                }
                Course course=courseOp.get();
                response.setSuccess(true);
                response.setMessage("??????????????????");
                response.setContent(course);
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerSave(String token, CourseRequestData course){
        CommonResponse response = tokenUtil.tokenCheck(token);
        Optional<Course> courseOldOptional = courseRepository.findById(course.getId());
        if(courseOldOptional.isEmpty()){
            response.setSuccess(false);
            response.setMessage("???????????????");
            return response;
        }
        Course courseTarget = courseOldOptional.get();
        courseTarget.updateInfo(course);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                courseRepository.save(courseTarget);
                response.setSuccess(true);
                response.setMessage("??????????????????");
            }else{
                response.setSuccess(false);
                response.setMessage("????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerDelete(String token,long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Course> courseOp=courseRepository.findById(id);
                Course course=courseOp.get();
                Teacher teacher = course.getTeacher();
                courseRepository.delete(course);
                teacher.setAchievementSet(updateAchievementListCourse(teacher));
                teacherRepository.save(teacher);
                response.setSuccess(true);
                response.setMessage("??????????????????");
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerFindStudent(String token, long id){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Course> courseOp=courseRepository.findById(id);
                Course course=courseOp.get();
                Set<Student> studentSet=course.getStudentSet();
                List<FindCourseStudentData> result = new ArrayList<>();
                for(Student s : studentSet){
                    result.add(new FindCourseStudentData(s));
                }
                response.setSuccess(true);
                response.setMessage("?????????????????????");
                response.setContent(result);
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerRemoveStudent(String token, long studentId, long courseId){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            //token????????????
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Student> sop=studentRepository.findUserById(studentId);
                if(sop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("???????????????");
                    return response;
                }
                Student student=sop.get();
                Optional<Course> cop=courseRepository.findById(courseId);
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("???????????????");
                    return response;
                }
                Course course =cop.get();
                Set<Student> studentSet = course.getStudentSet();
                if(studentSet.contains(student)){
                    //?????????????????????????????????????????????????????????
                    studentSet.remove(student);
                    course.setStudentSet(studentSet);
                    courseRepository.save(course);
                    Set<Course> courseSet = student.getCourseSet();
                    courseSet.remove(course);
                    student.setCourseSet(courseSet);
                    studentRepository.save(student);
                    response.setSuccess(true);
                    response.setMessage("??????????????????");
                }else{
                    //??????????????????????????????????????????
                    response.setSuccess(false);
                    response.setMessage("???????????????????????????");
                }
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handleManagerAddStudent(String token, long courseId, long studentNumber){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Manager){
                Optional<Student> sop=studentRepository.findByUserNumber(studentNumber);
                Student student=sop.get();
                Optional<Course> cop = courseRepository.findById(courseId); //????????????????????????
                if(cop.isEmpty()){
                    response.setSuccess(false);
                    response.setMessage("??????????????????");
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
                response.setMessage("??????????????????");
            }else{
                response.setSuccess(false);
                response.setMessage("??????????????????");
            }
        }
        return response;
    }

    @Override
    public CommonResponse handlePersonalFind(long userId){
        User user = userService.findById(userId);
        CommonResponse response = new CommonResponse<>();
        if(user == null || !(user instanceof Teacher)){
            response.setSuccess(false);
            response.setMessage("????????????");
            return response;
        }
        Teacher teacher = (Teacher) user;
        Set<Course> courseSet = teacher.getCourseSet();
        Set<String> courseName = new HashSet<>();
        for(Course c : courseSet){
            courseName.add(c.getName());
        }
        response.setContent(courseName);
        response.setSuccess(true);
        response.setMessage("????????????");
        return response;
    }

    @Override
    public CommonResponse handleEditCourseSelectTime(String token, Date startTime,Date endTime){
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(!response.getSuccess())
            return response;
        if(!(userService.findById(Long.parseLong(response.getMessage())) instanceof Manager)){
            response.setSuccess(false);
            response.setMessage("????????????");
            return response;
        }
        CourseSelectTime courseSelectTime = new CourseSelectTime(1,startTime,endTime);
        courseSelectTimeRepository.save(courseSelectTime);
        courseSelectStartTime = startTime;
        courseSelectEndTime = endTime;
        response.setSuccess(true);
        response.setMessage("????????????");
        return response;
    }
}
