package com.clankalliance.backbeta.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindDetailUtil {
    private String studentName;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

    public FindDetailUtil(String studentName,Integer dailyScore,Integer endScore,Double weight){
        this.studentName=studentName;
        this.dailyScore=dailyScore;
        this.endScore=endScore;
        this.weight=weight;
    }
}
