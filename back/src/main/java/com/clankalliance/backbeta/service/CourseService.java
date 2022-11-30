package com.clankalliance.backbeta.service;

import com.clankalliance.backbeta.entity.course.ClassTime;
import com.clankalliance.backbeta.response.CommonResponse;

import java.util.List;

public interface CourseService {
    CommonResponse handleStudentSave(String token, long courseId, long teacherId, String studentSection, String studentClass, Integer year, String semester);

    CommonResponse handleTeacherSave(String token, long courseId, String name, Integer weekStart, Integer weekEnd, List<ClassTime> time, Integer capacity, String studentClass, String studentSection, String location, Integer year, String semester, Double credit);
}
