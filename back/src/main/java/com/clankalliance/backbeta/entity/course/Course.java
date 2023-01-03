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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //课程名
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    //结束周
    private Integer weekStart;
    public Integer getWeekStart(){return weekStart;}
    public void setWeekStart(Integer weekStart){this.weekStart=weekStart;}


    //开始周
    private Integer weekEnd;
    public Integer getWeekEnd() {
        return weekEnd;
    }
    public void setWeekEnd(Integer weekEnd){
        this.weekEnd=weekEnd;
    }

    //上课时间 列表，可能会有多个时间
    @OneToMany
    private List<ClassTime> time;

    public List<ClassTime> getTime() {
        return time;
    }

    public void setTime(List<ClassTime> time) {
        this.time = time;
    }

    //课容量
    private Integer capacity;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    //开课班级
    private String studentClass;
    public String getStudentClass(){
        return studentClass;
    }

    public void SetStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    //学生届次
    private String studentSection;
    public String getStudentSection(){
        return studentSection;
    }
    public void setStudentSection(String studentSection){
        this.studentSection=studentSection;
    }

    //上课地址
    private String location;
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }

    //上课年份
    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    //学期
    private String semester;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    //学分
    private Double credit;

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    //课程描述
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    @OneToMany
    private Set<Student> studentSet;

    public Set<Student> getStudentSet() {
        return studentSet;
    }

    public void setStudentSet(Set<Student> studentSet) {
        this.studentSet = studentSet;
    }

    @JsonIgnore
    @ManyToOne
    private Teacher teacher;

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @JsonIgnore
    @OneToMany
    private Set<Score> scoreSet;

    public Set<Score> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<Score> scoreSet) {
        this.scoreSet = scoreSet;
    }

    //权重 最终分数里期末分数的占比
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
