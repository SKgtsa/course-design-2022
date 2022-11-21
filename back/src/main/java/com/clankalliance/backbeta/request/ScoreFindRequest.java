package com.clankalliance.backbeta.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreFindRequest {

    private String token;

    private long courseId;

    private long studentId;

    private long scoreId;

    private Integer Year;

    private String Semester;


}
