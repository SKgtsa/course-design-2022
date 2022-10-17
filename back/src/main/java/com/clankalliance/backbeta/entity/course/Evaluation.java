package com.clankalliance.backbeta.entity.course;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//课程评价
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    private Integer grade;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Student student;
}
