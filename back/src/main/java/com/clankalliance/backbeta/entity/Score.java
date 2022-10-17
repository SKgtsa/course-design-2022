package com.clankalliance.backbeta.entity;


import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(	name = "score",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
public class Score {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;



}
