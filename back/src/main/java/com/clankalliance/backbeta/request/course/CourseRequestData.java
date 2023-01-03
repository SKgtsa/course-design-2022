package com.clankalliance.backbeta.request.course;

import com.clankalliance.backbeta.entity.course.ClassTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestData {

    private long id;

    private String name;

    private Integer weekStart;

    private Integer weekEnd;

    private List<ClassTime> time;

    private Integer capacity;

    private List<String> studentClass;

    private List<String> studentSection;

    private String location;

    private Integer year;

    private String semester;

    private Double credit;

    private String description;

    private Double weight;

}
