package com.clankalliance.backbeta.request.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseManagerFindRequest {
    private String token;
    private long id;
}
