package com.clankalliance.backbeta.utils;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class ScoreUtil {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;

    private String courseName;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

    private Integer finalScore;

    private Integer rank;

    public ScoreUtil(String courseName,Integer dailyScore,Integer endScore,Double weight,Integer finalScore,Integer rank){
        this.courseName=courseName;
        this.dailyScore=dailyScore;
        this.endScore=endScore;
        this.weight=weight;
        this.finalScore=finalScore;
        this.rank=rank;
    }
}
