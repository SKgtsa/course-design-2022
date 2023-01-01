package com.clankalliance.backbeta.response.dataBody;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long id;

    private String courseName;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

    private Integer finalScore;

    private Integer rank;

    public ScoreData(String courseName, Integer dailyScore, Integer endScore, Double weight, Integer finalScore, Integer rank){
        this.courseName=courseName;
        this.dailyScore=dailyScore;
        this.endScore=endScore;
        this.weight=weight;
        this.finalScore=finalScore;
        this.rank=rank;
    }
}
