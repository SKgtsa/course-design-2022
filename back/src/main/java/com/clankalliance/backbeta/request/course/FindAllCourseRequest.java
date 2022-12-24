package com.clankalliance.backbeta.request.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllCourseRequest {

    private String token;

    private boolean filterOpen;

    private Integer pageNum;

    private Integer pageSize;

}
