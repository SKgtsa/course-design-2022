package com.clankalliance.backbeta.response.dataBody;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AverageScoreData {
    private String courseName;

    private Double weight;

    private Integer average;

    private String passRate;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long courseId;


    public AverageScoreData(String courseName, Double weight, Integer average, String passRate, Long courseId){
        this.courseName=courseName;
        this.weight=weight;
        this.average=average;
        this.passRate=passRate;
        this.courseId=courseId;
    }

}
