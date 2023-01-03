package com.clankalliance.backbeta.request.course;

import com.clankalliance.backbeta.entity.course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseManagerSaveRequest {
    private String token;
    private CourseRequestData course;
}
