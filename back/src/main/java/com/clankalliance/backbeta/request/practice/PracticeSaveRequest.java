package com.clankalliance.backbeta.request.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeSaveRequest {
    private long id;

    private String token;

    private String practiceName;

    private String practiceDescription;

}
