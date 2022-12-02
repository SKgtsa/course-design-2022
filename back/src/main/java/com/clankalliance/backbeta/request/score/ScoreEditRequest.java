package com.clankalliance.backbeta.request.score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreEditRequest {

    private String token;

    private long courseId;

    private long studentId;

    private long scoreId;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

}
