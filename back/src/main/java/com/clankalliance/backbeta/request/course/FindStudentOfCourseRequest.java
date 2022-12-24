package com.clankalliance.backbeta.request.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindStudentOfCourseRequest {

    private String token;

    private long courseId;

    private Integer pageNum;

    private Integer pageSize;
}
