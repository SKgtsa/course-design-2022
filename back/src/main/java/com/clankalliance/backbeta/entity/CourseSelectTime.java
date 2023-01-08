package com.clankalliance.backbeta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseSelectTime {

    @Id
    @JsonIgnore
    private Integer id;

    //选课开始时间
    private Date startTime;

    //选课截止时间
    private Date endTime;
}
