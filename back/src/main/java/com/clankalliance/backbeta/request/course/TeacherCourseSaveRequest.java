package com.clankalliance.backbeta.request.course;

import com.clankalliance.backbeta.entity.course.ClassTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseSaveRequest {

    private String token;

    private long courseId;

    private String name;

    private List<String> studentSection;

    private List<String> studentClass;

    private Integer weekStart;

    private Integer weekEnd;

    private List<ClassTime> time;

    private Integer capacity;

    private String location;

    private Integer year;

    private String semester;

    private Double credit;

    private String description;

    private Double weight;
}

