package com.clankalliance.backbeta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseEditRequest {

    private String token;

    private long courseId;

    private long teacherId;

    private String studentClass;

    private Integer weekStart;

    private Integer weekEnd;

    private Integer capacity;

    private String location;

    private Integer year;

    private String semester;

    private Double credit;
}

