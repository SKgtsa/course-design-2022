package com.clankalliance.backbeta.response;

import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.entity.course.Course;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseTarget {

    //课号 课程id
    //为避免long传向前台出现精度丢失，通过注释将long化为String
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    //课程名
    private String name;

    //结束周
    private Integer weekStart;

    //开始周
    private Integer weekEnd;

    //上课时间 列表，可能会有多个时间
    private List<ClassTime> time;

    //课容量
    private Integer capacity;

    //开课班级
    private String studentClass;

    //学生届次
    private String studentSection;

    //上课地址
    private String location;

    //上课年份
    private Integer year;

    //学期
    private String semester;

    //学分
    private Double credit;

    //课程描述
    private String description;

    private String TeacherName;

    private Integer studentNum;

    public CourseResponseTarget(Course course){
        id = course.getId();
        name = course.getName();
        weekStart = course.getWeekStart();
        weekEnd = course.getWeekEnd();
        time = course.getTime();
        capacity = course.getCapacity();
        studentClass = course.getStudentClass();
        studentSection = course.getStudentSection();
        location = course.getLocation();
        year = course.getYear();
        semester = course.getSemester();
        credit = course.getCredit();
        description = course.getDescription();
        TeacherName = course.getTeacher().getName();
        studentNum = course.getStudentSet().size();
    }

}
