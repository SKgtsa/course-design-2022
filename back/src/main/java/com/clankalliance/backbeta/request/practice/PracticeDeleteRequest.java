package com.clankalliance.backbeta.request.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PracticeDeleteRequest {
    private long practiceId;

    private String token;
}
