package com.clankalliance.backbeta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseEditRequest {

    private String token;

    private long courseId;

    private long teacherId;

    private String studentSection;

    private String studentClass;


}

