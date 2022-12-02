package com.clankalliance.backbeta.request.score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindScoreRequest {

    private String token;

    private Integer year;

    private String semester;


}
