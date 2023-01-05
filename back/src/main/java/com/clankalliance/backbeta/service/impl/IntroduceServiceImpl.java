package com.clankalliance.backbeta.service.impl;

import com.clankalliance.backbeta.entity.*;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.repository.*;
import com.clankalliance.backbeta.repository.userRepository.UserRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.ManagerRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.StudentRepository;
import com.clankalliance.backbeta.repository.userRepository.sub.TeacherRepository;
import com.clankalliance.backbeta.service.IntroduceService;
import com.clankalliance.backbeta.service.UserService;
import com.clankalliance.backbeta.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.*;

@Service
public class IntroduceServiceImpl implements IntroduceService {

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private IntroduceService introduceService;

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private TeacherRepository teacherRepository;

    @Resource
    private ManagerRepository managerRepository;

    @Resource
    private UserRepository userRepository;
    @Resource
    private ScoreRepository scoreRepository;

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private ActivityRepository activityRepository;

    @Resource
    private PracticeRepository practiceRepository;

    @Resource
    private RewardRepository rewardRepository;

    @Resource
    private AchievementRepository achievementRepository;


    //绩点获取方法
    //用于内部计算
    public Double getPointByStudentIdReturnDouble(long studentId,ScoreRepository scoreRepository){
        Optional<Student> studentOp=studentRepository.findUserById(studentId);
        Student student=studentOp.get();
        List<Score> scoreList = scoreRepository.findByStudentId(student.getId());
        if(scoreList.isEmpty()){
            return 0.0;
        }else{
            Double point = 0.0;
            Double totalCredit = 0.0;
            for(Score s : scoreList){
                point += s.getCourse().getCredit().equals(0)? 0:  ((s.getEndScore() * s.getCourse().getWeight() + s.getDailyScore() * (1 - s.getCourse().getWeight())) - 50) / 10 * s.getCourse().getCredit();
                totalCredit += s.getCourse().getCredit();
            }
            Double pointResult = totalCredit.equals(0.0)? 0.0: point/totalCredit;
            if(pointResult != 0.0){
                return pointResult;
            }else{
                return 0.0;
            }
        }
    }

    public String getHtmlCount(long id){
        Optional<Student> studentOptional=studentRepository.findUserById(id);
        Student student=studentOptional.get();
        String studentName =student.getName();
        long telephoneNum =student.getPhone();
        String birthday=student.getIdCardNumber().substring(6,13);
        Boolean gender=student.getGender();
        String sex;
        if(gender){
            sex="女";
        } else {
            sex="男";
        }
        long studentNum=student.getUserNumber();
        String politicalAppearance=student.getPoliticalAffiliation();
        Double creditSum=0.0;
        Double doublePoint=getPointByStudentIdReturnDouble(id,scoreRepository);
        String point = doublePoint.toString();
        List<Score> scoreList=scoreRepository.findByStudentId(id);
        List<Activity> activityList=activityRepository.findByStudentId(id);
        List<Practice> practiceList=student.getPracticeSet();
        List<Reward> rewardList=student.getRewardSet();
        Set<Achievement> achievementSet=student.getAchievementSet();
        List<String> achievementNameList=new ArrayList<>();
        for(Achievement a : achievementSet){
            String achievementName=a.getName();
            achievementNameList.add(achievementName);
        }
        Set<Course> courseSet = student.getCourseSet();
        String scoreContent="<p>学期总绩点: " + point + "</p>";
        String courseContent="";
        String activityContent="";
        String practiceContent="";
        String rewardContent="";
        String achievementContent="";
        if(!achievementNameList.isEmpty()){
            for(String name : achievementNameList){
                achievementContent+="<p>学期总绩点: " + name + "</p>";
            }
        }
        if(achievementContent.equals("")){
            achievementContent+="暂无称号";
        }

        for(int i=0;i<scoreList.size();i++){
            Score score=scoreList.get(i);
            double weight=0.0;
            int dailyScore= score.getDailyScore();
            int endScore= score.getEndScore();
            try{
                weight=score.getCourse().getWeight();
            }catch (Exception e){
                weight = 0.0;
            }
            double finalScore= dailyScore*(1-weight)+endScore*weight;
            int finalScore1=(int) finalScore;
            double scorePoint=0.0;
            if(finalScore1 >= 70){
                scorePoint = 5 - 0.1 * (100 - finalScore1);
            }else{
                scorePoint = ((int)(10*(2.0 - 2 / 70 * (2 - finalScore1))))/ 10.0;
            }
            scoreContent+="<p>课程--"+(scoreList.get(i).getCourse().getName())+":"+scorePoint+"</p>";
        }

        if(activityList.isEmpty()){
            activityContent+="暂无数据";
        }
        else{
            for(int i=0;i<activityList.size();i++){
                Activity activity=activityList.get(i);
                activityContent+="<p>参加"+activity.getName()+"取得成果"+activity.getResult()+"</p>";
            }
        }

        if(practiceList.isEmpty()){
            practiceContent+="暂无数据";
        }
        else {
            for(int i=0;i<practiceList.size();i++){
                Practice practice=practiceList.get(i);
                practiceContent+="<p>参加"+practice.getName()+"实践内容"+practice.getDescription()+"</p>";
            }
        }

        if(rewardList.isEmpty()){
            rewardContent+="暂无数据";
        }
        else{
            for(int i=0;i<rewardList.size();i++){
                Reward reward=rewardList.get(i);
                rewardContent+="<p>在"+reward.getName()+"中获得"+reward.getDescription()+"</p>";
            }
        }

        for(Course course:courseSet){
            Optional<Score> scoreOptional=scoreRepository.findByCourseStudentId(student.getId(),course.getId());
            creditSum+=course.getCredit();
            courseContent+="<tr align=\"center\">";
            courseContent+="<td height='50px' width='120px' bgcolor=\"#c1cbd7\">"+course.getName()+"</td>";
            courseContent+="<td width='120px' bgcolor=\"#fffaf4\">"+course.getDescription()+"</td>";
            courseContent+="<td width='120px' bgcolor=\"#fffaf4\">"+course.getCredit()+"</td>";
            if(scoreOptional.isPresent()){
                double weight=0.0;
                Score score = scoreOptional.get();
                int dailyScore= score.getDailyScore();
                int endScore= score.getEndScore();
                try{
                    weight=score.getCourse().getWeight();
                }catch (Exception e){
                    weight = 0.0;
                }
                double finalScore= dailyScore*(1-weight)+endScore*weight;
                int finalScore1=(int) finalScore;
                double scorePoint=0.0;
                if(finalScore1 >= 70){
                    scorePoint = 5 - 0.1 * (100 - finalScore1);
                }else{
                    scorePoint = ((int)(10*(2.0 - 2 / 70 * (2 - finalScore1))))/ 10.0;
                }
                courseContent+="<td width='120px' bgcolor=\"#c1cbd7\">"+score.getDailyScore()+"</td>";
                courseContent+="<td width='120px' bgcolor=\"#c1cbd7\">"+score.getEndScore()+"</td>";
                courseContent+="<td width='120px' bgcolor=\"#fffaf4\">"+scorePoint+"</td>";
            }
            else{
                courseContent+="<td width='120px' bgcolor=\"#c1cbd7\">暂无成绩</td>";
                courseContent+="<td width='120px' bgcolor=\"#fffaf4\">暂无绩点</td>";
            }
            courseContent+="</tr>";
        }
        //个人简历
        String content= "<!DOCTYPE html>";
        content += "<html>";
        content += "<head>";
        content += "<style>";
        content += "html { font-family: \"SourceHanSansSC\", \"Open Sans\";}";
        content += "</style>";
        content += "<meta charset='UTF-8' />";
        content += "<title>个人简历</title>";
        content += "</head>";
        content += "<body bgcolor=\"#000000\">";
        content +="<h1 style=\"color:rgb(0, 0, 0); text-align: center;font-size:30px;\">个人简历</h1>";
        content +="<table style=\"border-collapse:separate;color: rgb(0, 0, 0);font-size:26px;\" color=\"#000000\" bgcolor=\"#000000\"  border=\"0.5\" cellspacing=\"4\" width='670px' height='700px' align=\"center\">";
        content +="<tr align=\"left\">";
        content += "<td height='50px' style=\"color:rgb(0, 0, 0);font-size:29px;\" width='100px' bgcolor=\"#d55e5e\"><b>姓名</b></td>";
        content += "<td width='100px' style=\"color:rgb(0, 0, 0);font-size:30px;\" bgcolor=\"#E8E8E8\"><b>"+studentName+"</b></td>";
        content += "<td width='100px' style=\"color:rgb(0, 0, 0);font-size:29px;\" bgcolor=\"#d55e5e\"><b>性别</b></td>";
        content += "<td width='150px' style=\"color:rgb(0, 0, 0);font-size:30px;\" bgcolor=\"#E8E8E8\"><b>"+sex+"</b></td>";
        content += "<td rowspan=\"3\" ><img width=\"101%\" length=\"40\" src=\"" + ClassUtils.getDefaultClassLoader().getResource("").getPath() +"image/" + student.getUserNumber() + ".jpg\" alt=\"请检查学生照片是否存在\"/></td>";
        content += "</tr>";

        content +="<tr align=\"left\">";
        content += "<td height='50px' style=\"color:rgb(0, 0, 0);font-size:29px;\" bgcolor=\"#d55e5e\"><b>年龄</b></td>";
        content += "<td bgcolor=\"#d55e5e\" style=\"color:rgb(0, 0, 0);font-size:23px;\"><b>政治面貌</b></td>";
        content += "<td bgcolor=\"#E8E8E8\" style=\"color:rgb(0, 0, 0);font-size:30px;\"><b>"+politicalAppearance+"</b></td>";
        content += "</tr>";

        content += "<tr align=\"left\">";
        content += "<td height='30px' bgcolor=\"#D4D4D4\" style=\"color:rgb(0, 0, 0);font-size:22px;\">电话<b></b></td>";
        content += "<td colspan=\"4\" style = \"color:rgb(232, 232, 232);font-size:24px; \" bgcolor=\"#d55e5e\">"+telephoneNum+"</td>";
        content += "</tr>";

        content += "<tr>";
        content += "<td bgcolor=\"#d55e5e\"  style=\"color:rgb(0, 0, 0);font-size:30px;\" align=\"left\" ><b>曾获荣誉</b></td>";
        content += "<td colspan=\"4\" style=\"color:rgb(0, 0, 0);font-size:18px;\" align=\"left\" bgcolor=\"#ffffff\">"+rewardContent+"</td>";
        content += "</tr>";

        content += "<tr>";
        content += "<td bgcolor=\"#d55e5e\" style=\"color:rgb(0, 0, 0);font-size:22px;\" align=\"left\"><b>社会实践</b></td>";
        content += "<td colspan=\"4\" style=\"color:rgb(0, 0, 0);font-size:18px;\" align=\"left\" bgcolor=\"#ffffff\">"+practiceContent+"</td>";
        content += "</tr>";

        content += "<tr>";
        content += "<td bgcolor=\"#d55e5e\" style=\"color:rgb(0, 0, 0);font-size:22px;\" align=\"left\"><b>课外活动</b></td>";
        content += "<td colspan=\"4\" style=\"color:rgb(0, 0, 0);font-size:18px;\" align=\"left\" bgcolor=\"#ffffff\">"+activityContent+"</td>";
        content += "</tr>";

        content += "<tr>";
        content += "<td bgcolor=\"#d55e5e\" style=\"color:rgb(0, 0, 0);font-size:30px;\" align=\"left\"><b>成绩</b></td>";
        content += "<td colspan=\"4\" style=\"color:rgb(0, 0, 0);font-size:18px;\" align=\"left\" bgcolor=\"#ffffff\">"+scoreContent+"</td>";
        content += "</tr>";

        content += "</table>";
        //------------------个人画像-----------------------
        content+="<h1 style=\"color:rgb(134, 150, 167); text-align: center;font-size:30px;\">个人画像</h1>";
        content+="<table border=\"1\"  cellspacing=\"0\" width='660px' align=\"center\"  style=\"color: rgb(0, 0, 0);font-size:20px;\">";
        content+="<tr align=\"center\">";
        content+="<td height='50px' width='85px' bgcolor=\"#c1cbd7\">姓名</td>";
        content+="<td width='85px' bgcolor=\"#fffaf4\">"+studentName+"</td>";
        content+="<td width='85px' bgcolor=\"#c1cbd7\">性别</td>";
        content+="<td width='85px' bgcolor=\"#fffaf4\">"+sex+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td height='50px' width='120px' bgcolor=\"#c1cbd7\">生日</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\" colspan=\"2\">"+birthday+"</td>";
        content+="<td width='120px' bgcolor=\"#c1cbd7\">学号</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\" colspan=\"2\">"+studentNum+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td height='50px' width='120px' bgcolor=\"#c1cbd7\">政治面貌</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\" colspan=\"2\">"+politicalAppearance+"</td>";
        content+="<td width='120px' bgcolor=\"#c1cbd7\">电话</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\" colspan=\"2\">"+telephoneNum+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#c1cbd7\">家庭关系</td>";
        content+="</tr>";


        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#c1cbd7\">荣誉称号</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#fffaf4\">"+achievementContent+"</td>";
        content+="</tr>";

        content+="<tr align=\"center\">";
        content+="<td height='50px' width='120px' bgcolor=\"#c1cbd7\">课程</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\">课程号</td>";
        content+="<td width='120px' bgcolor=\"#c1cbd7\">出勤率</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\">学分</td>";
        content+="<td width='120px' bgcolor=\"#c1cbd7\">成绩</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\">绩点</td>";
        content+="</tr>";
        content+=courseContent;
        content+="<tr align=\"center\">";
        content+="<td height='50px' width='120px' bgcolor=\"#c1cbd7\" colspan=\"3\">总学分</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\">"+creditSum+"</td>";
        content+="<td width='120px' bgcolor=\"#c1cbd7\">均绩</td>";
        content+="<td width='120px' bgcolor=\"#fffaf4\">"+point+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#c1cbd7\">课外活动</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#fffaf4\">"+activityContent+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#c1cbd7\">社会实践</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#fffaf4\">"+practiceContent+"</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#c1cbd7\">曾获荣誉</td>";
        content+="</tr>";
        content+="<tr align=\"center\">";
        content+="<td colspan=\"6\" bgcolor=\"#fffaf4\">"+rewardContent+"</td>";
        content+="</tr>";
        content+="</table>";

        content += "</body>";
        content += "</html>";


        return content;
    }
    //个人简历信息数据准备方法  根据自己的数据的希望展示的内容拼接成字符串，放在Map对象里， attachList可以方多段内容，具体内容由个人决定
    public Map getIntroduceDataMap(long studentId){
        if (studentId == 0) {//这里好像没起到什么作用但是至少后台不发生空指针异常了
            Map data = new HashMap();
            data.put("myName","请前往学生管理选择学生");
            data.put("overview","暂无学生信息");
            return data;
        }
        Map data = new HashMap();
        String html = "";
        html = getHtmlCount(studentId);
        if(html.length()> 0) {
            data.put("html", html);
            return data;
        }
        return data;
    }

}


