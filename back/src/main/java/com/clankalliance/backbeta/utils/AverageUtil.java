package com.clankalliance.backbeta.utils;

public class AverageUtil {
    private String courseName;

    private Double weight;

    private Integer average;

    private String passRate;

    private Long courseId;


    public AverageUtil(String courseName,Double weight,Integer average,String passRate,Long courseId){
        this.courseName=courseName;
        this.weight=weight;
        this.average=average;
        this.passRate=passRate;
        this.courseId=courseId;
    }
}
