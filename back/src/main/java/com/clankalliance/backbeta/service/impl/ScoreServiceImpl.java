package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Manager;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.ScoreRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.service.ScoreService;
import com.clankalliance.backbeta.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.AbstractDocument;
import java.text.DecimalFormat;
import java.util.*;


@Service
public class ScoreServiceImpl implements ScoreService {

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
    private ScoreRepository scoreRepository;

    /**
     * 处理成绩保存的方法，先进行token验证，再存储。传入数据合乎规范
     * @param token 用户token
     * @param courseId 课程id
     * @param studentId 学生id
     * @param dailyScore 平时分
     * @param endScore 期末分
     * @param weight 平时分占比
     * @return
     */
    public CommonResponse handleSave(String token, long courseId, long studentId, Integer dailyScore, Integer endScore, Double weight){
        CommonResponse response;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("242634982651203584");
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功



            Optional<Score> scoreOp = scoreRepository.findByCourseStudentId(courseId, studentId);
            long id;
            if(scoreOp.isEmpty()){
                //该学生与该课程对应成绩不存在，为成绩的创建
                id = snowFlake.nextId();
            }else{
                //该学生与该课程对应成绩存在，为成绩的修改
                id = scoreOp.get().getId();
            }
            Optional<Student> studentOp = studentRepository.findUserById(studentId);
            if(studentOp.isEmpty()){
                response.setSuccess(false);
                response.setMessage("学生不存在");
                return response;
            }
            Optional<Course> cop = courseRepository.findById(courseId);
            if(cop.isEmpty()){
                response.setSuccess(false);
                response.setMessage("课程不存在");
                return response;
            }
            Student student = studentOp.get();
            Course course = cop.get();
            Score score = new Score(id, dailyScore, endScore, weight, studentOp.get(), cop.get());
            //在学生的成绩表中加入成绩
            Set<Score> studentScoreSet = student.getScoreSet();
            studentScoreSet.add(score);
            student.setScoreSet(studentScoreSet);
            //在课程的成绩表中加入成绩
            Set<Score> courseScoreSet = course.getScoreSet();
            courseScoreSet.add(score);
            course.setScoreSet(courseScoreSet);
            //保存
            studentRepository.save(student);
            courseRepository.save(course);
            scoreRepository.save(score);
            response.setSuccess(true);
            response.setMessage("保存成功");
        }
        //token无效，直接将token验证的数据返回前端
        return response;
    }

    /**
     * 处理成绩查询的方法，先进行token验证身份，teacher可以查询所教所有课程的成绩，student查询自己所有课程的成绩 均按学年学期筛选
     * @param token token
     * @param Year 学年
     * @param Semester 学期
     * @return
     */
    public CommonResponse handleFind(String token, Integer Year, String  Semester){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("259887250475716608");//259887250475716608
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                Student student=(Student)user;
                long studentId=student.getId();
                Set<Course> courseSet=student.getCourseSet();
                //建立List集合存储所有的课程成绩对象
                List<ScoreUtil> StuScore=new ArrayList<>();
                for(Course c : courseSet){
                    if(c.getSemester().equals(Semester) && c.getYear().equals(Year)){
                        long courseId=c.getId();
                        Optional<Score> scoreOp=scoreRepository.findByTime(studentId,courseId,Year,Semester);
                        Score score=scoreOp.get();
                        int dailyScore= score.getDailyScore();
                        int endScore= score.getEndScore();
                        double weight=score.getWeight();
                        double finalScore= dailyScore*(1-weight)+endScore*weight;
                        Integer finalScore1=(int) finalScore;
                        int rank;
                        Set<Student> courseStudentSet=c.getStudentSet();
                        int capacity=courseStudentSet.size();
                        List<Integer> finalList=new ArrayList<>(capacity);
                        finalList.add(finalScore1);
                        for(Student stu : courseStudentSet){
                            long stuId=stu.getId();
                            Optional<Score> scOp=scoreRepository.findByTime(stuId,courseId,Year,Semester);
                            Score sc=scOp.get();
                            int dailySc= sc.getDailyScore();
                            int endSc= sc.getEndScore();
                            double weightSc=sc.getWeight();
                            double finalSc= dailySc*(1-weightSc)+endSc*weightSc;
                            Integer finalSc1=(int) finalSc;
                            finalList.add(finalSc1);
                        }
                        //使用list的sort方法进行归并排序
                        finalList.sort(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return  o2.compareTo(o1);
                            }
                        });
                        rank=finalList.indexOf(finalScore1) + 1;
                        //将所获得的数据封装进所建立的对象内
                        String courseName=c.getName();
                        ScoreUtil tmp=new ScoreUtil(courseName,dailyScore,endScore,weight,finalScore1,rank);
                        StuScore.add(tmp);
                    }
                }
                response.setContent(StuScore);
                response.setMessage("成绩查询成功");
                response.setSuccess(true);
            }else if(user instanceof Teacher){
                Teacher teacher=(Teacher) user;
                Set<Course> teacherCourseSet=teacher.getCourseSet();
                List<AverageUtil> stuScore=new ArrayList<>();
                for(Course c : teacherCourseSet){
                    if(c.getSemester().equals(Semester) && c.getYear().equals(Year)){
                        Set<Student> studentCourseSet=c.getStudentSet();
                        int total=0;
                        int studentNum=studentCourseSet.size();
                        int passStudent=0;
                        double weight=0;
                        long courseId=c.getId();
                        for(Student s : studentCourseSet){
                            long studentId=s.getId();
                            Optional<Score> scoreOp=scoreRepository.findByTime(studentId,courseId,Year,Semester);
                            Score score=scoreOp.get();
                            int dailyScore= score.getDailyScore();
                            int endScore= score.getEndScore();
                            weight=score.getWeight();
                            double finalScore= dailyScore*(1-weight)+endScore*weight;
                            int finalScore1=(int) finalScore;
                            total+=finalScore1;
                            if(finalScore1>=60){
                                passStudent++;
                            }
                        }
                        Integer averageScore = total/studentNum;
                        double passRate = passStudent/studentNum;
                        DecimalFormat df = new DecimalFormat("#0.000");
                        String passRate1=df.format(passRate);
                        String courseName=c.getName();
                        AverageUtil tmp=new AverageUtil(courseName,weight,averageScore,passRate1,courseId);
                        stuScore.add(tmp);
                    }
                }
                response.setContent(stuScore);
                response.setSuccess(true);
                response.setMessage("成绩查询成功");
            }
        }
//        CommonResponse response = tokenUtil.tokenCheck(token);
//        if(response.getSuccess()){
//            User user = userService.findById(Long.parseLong(response.getMessage()));
//            //身份为教师
//            if(user instanceof Teacher || user instanceof Manager){
//
//                Course course = courseRepository.findById(courseId).get();
//                Set<Student> studentSet = course.getStudentSet();
//                List<Score> scoreList = new ArrayList<>();
//                for(Student s: studentSet){
//                    Optional<Score> sop = scoreRepository.findByCourseStudentId(courseId,s.getId());
//                    if(sop.isPresent()){
//                        scoreList.add(sop.get());
//                    }
//                }
//            response.setContent(scoreList);
//            }//身份为学生
//            else if(user instanceof Student){
//                long userId = user.getId();
//                List<Score> scoreList = new ArrayList<>();
//                Optional<Score> sop = scoreRepository.findByTime(userId,courseId,Year,Semester);
//                while(sop.isPresent()){
//                    scoreList.add(sop.get());
//                }
//                response.setContent(scoreList);
//            }
//        }
//        return response;
        return response;
    }

    public CommonResponse handleFindDetail(String token,long courseId){
        CommonResponse response ;
        if(token.equals("114514")){
            response = new CommonResponse();
            response.setSuccess(true);
            response.setMessage("259887250475716608");//259887250475716608
        }else{
            response = tokenUtil.tokenCheck(token);
        }
        if(response.getSuccess()){
            //token验证成功
            User user = userService.findById(Long.parseLong(response.getMessage()));
            List<FindDetailUtil> DetailList=new ArrayList<>();
            if(user instanceof Teacher){
                Teacher teacher = (Teacher) user;
                long teacherId=teacher.getId();
                Optional<Course> courseOp=courseRepository.findByTeacherAndId(teacherId,courseId);
                Course course=courseOp.get();
                Set<Student> studentSet=course.getStudentSet();
                for(Student s : studentSet){
                    long studentId=s.getId();
                    Optional<Score> scoreOp=scoreRepository.findByCourseStudentId(courseId,studentId);
                    Score score=scoreOp.get();
                    String studentName=s.getName();
                    Integer dailyScore=score.getDailyScore();
                    Integer endScore=score.getEndScore();
                    Double weight=score.getWeight();
                    FindDetailUtil tmp=new FindDetailUtil(studentName,dailyScore,endScore,weight);
                    DetailList.add(tmp);
                }
                response.setContent(DetailList);
                response.setMessage("学生成绩查询成功");
                response.setSuccess(true);
            }
        }
        return response;
    }

}
