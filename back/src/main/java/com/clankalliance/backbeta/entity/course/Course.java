package com.clankalliance.backbeta.entity.course;

import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "course")
public class Course {
    //课号 课程id
    //为避免long传向前台出现精度丢失，通过注释将long化为String
    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    //课程名
    private String name;

    //结束周
    private Integer weekStart;

    //开始周
    private Integer weekEnd;

    //上课时间 列表，可能会有多个时间
    @OneToMany
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

    @JsonIgnore
    @OneToMany
    private Set<Student> studentSet;

    @ManyToOne
    private Teacher teacher;

    @JsonIgnore
    @OneToMany
    private Set<Score> scoreSet;

}
