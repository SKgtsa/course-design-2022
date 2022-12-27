package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.response.CommonResponse;

import java.util.List;

public interface CourseService {

    CommonResponse bind(long courseId, Student studentId);

    CommonResponse handleQuit(String token,long courseId);

    CommonResponse handleRemoveStudent(String token,long courseId,long studentNumber);

    CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time,Integer capacity,String studentClass,String studentSection,String location,Integer year, String semester,Double credit,String description);

    CommonResponse handleDelete(String token,long courseId);

    CommonResponse handleFindCourseList(String token);

    CommonResponse handleFindAllCourse(String token,boolean filterOpen,Integer pageNum,Integer pageSize);

    CommonResponse handleFindCourseStudent(String token, long courseId, Integer pageNum, Integer pageSize);

    CommonResponse handleTeacherFindCourse(String token, Integer year, String semester);
}
