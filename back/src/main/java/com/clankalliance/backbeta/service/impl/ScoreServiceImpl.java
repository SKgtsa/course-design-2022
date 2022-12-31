package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.clankalliance.backbeta.repository.CourseRepository;
import com.clankalliance.backbeta.repository.ScoreRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.response.CommonResponse;
import com.clankalliance.backbeta.service.ActivityService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.service.ScoreService;
import com.clankalliance.backbeta.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private ActivityService activityService;

    private Achievement POINT_C = new Achievement(Long.parseLong("1"),"绩点3.5以上","勤奋好学");

    private Achievement POINT_B = new Achievement(Long.parseLong("2"),"绩点3.8以上","博学多闻");

    private Achievement POINT_A = new Achievement(Long.parseLong("3"),"绩点4.0以上","百科字典");

    @Override
    public Achievement getPOINT_A() {
        return POINT_A;
    }

    private List<Achievement> updateAchievementList(Student student){
        List<Score> scoreList = scoreRepository.findByStudentId(student.getId());
        Double point = 0.0;
        Double totalCredit = 0.0;
        for(Score s : scoreList){
            point += ((s.getEndScore() * s.getWeight() + s.getDailyScore() * (1 - s.getWeight())) - 50) / 10 * s.getCourse().getCredit();
            totalCredit += s.getCourse().getCredit();
        }
        Double pointResult = totalCredit.equals(0.0)? 0.0: point/totalCredit;
        List<Achievement> achievementList = student.getAchievementList();
        if(pointResult >= 4.0){
            if(achievementList.contains(POINT_B)){
                achievementList.remove(POINT_B);
            }else if(achievementList.contains(POINT_C)){
                achievementList.remove(POINT_C);
            }
            achievementList.add(POINT_A);
            if(achievementList.contains(activityService.getACTIVITY_A())){
                achievementList.add(activityService.getACTIVITY_POINT());
            }
        }else if(pointResult >= 3.8){
            if(achievementList.contains(POINT_A)){
                achievementList.remove(POINT_A);
            }else if(achievementList.contains(POINT_C)){
                achievementList.remove(POINT_C);
            }
            achievementList.add(POINT_B);
        }else if(pointResult >= 3.5){
            if(achievementList.contains(POINT_A)){
                achievementList.remove(POINT_A);
            }else if(achievementList.contains(POINT_B)){
                achievementList.remove(POINT_B);
            }
            achievementList.add(POINT_C);
        }
        return achievementList;
    }

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
        CommonResponse response = tokenUtil.tokenCheck(token);
        if(response.getSuccess()){
            User user = userService.findById(Long.parseLong(response.getMessage()));
            if(user instanceof Student){
                response.setSuccess(false);
                response.setMessage("学生不能修改自己的信息");
            }else{
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
            Score score = new Score(id, dailyScore, endScore, weight, student, course);
            scoreRepository.save(score);
            //在学生的成绩表中加入成绩
            Set<Score> studentScoreSet = student.getScoreSet();
            studentScoreSet.add(score);
            student.setScoreSet(studentScoreSet);
            student.setAchievementList(updateAchievementList(student));
            studentRepository.save(student);
            //在课程的成绩表中加入成绩
            Set<Score> courseScoreSet = course.getScoreSet();
            courseScoreSet.add(score);
            course.setScoreSet(courseScoreSet);
            courseRepository.save(course);
            response.setSuccess(true);
            response.setMessage("成绩保存成功");
            }
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
        CommonResponse response = tokenUtil.tokenCheck(token);
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
                        if(scoreOp.isPresent()){
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
                                if(scOp.isPresent()){
                                    Score sc=scOp.get();
                                    int dailySc= sc.getDailyScore();
                                    int endSc= sc.getEndScore();
                                    double weightSc=sc.getWeight();
                                    double finalSc= dailySc*(1-weightSc)+endSc*weightSc;
                                    Integer finalSc1=(int) finalSc;
                                    finalList.add(finalSc1);
                                }
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
                            if(scoreOp.isPresent()){
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
                        }
                        Integer averageScore = studentNum == 0? 0 : total/studentNum;
                        double passRate = studentNum == 0? 0 : passStudent/studentNum;
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
        return response;
    }

    public CommonResponse handleFindDetail(String token,long courseId){
        CommonResponse response = tokenUtil.tokenCheck(token);
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
                    if(scoreOp.isPresent()){
                        Score score=scoreOp.get();
                        Integer dailyScore=score.getDailyScore();
                        Integer endScore=score.getEndScore();
                        Double weight=score.getWeight();
                        String studentName=s.getName();
                        FindDetailUtil tmp=new FindDetailUtil(studentName,dailyScore,endScore,weight);
                        DetailList.add(tmp);
                    }
                }
                response.setContent(DetailList);
                response.setMessage("学生成绩查询成功");
                response.setSuccess(true);
            }
        }
        return response;
    }

}
