package com.clankalliance.backbeta.request.score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherFindDetailedScoreRequest {

    private String token;

    private long courseId;

}
