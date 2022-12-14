package com.clankalliance.backbeta.request.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseSaveRequest {

    private String token;

    private long courseId;

    private long teacherId;

    private String studentSection;

    private String studentClass;

    private Integer year;

    private String semester;


}

