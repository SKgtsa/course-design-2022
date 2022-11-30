package com.clankalliance.backbeta.entity.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(	name = "classTime",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
public class ClassTime {

    @Id
    @JsonIgnore
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    //周几上课
    private Integer weekDay;

    //第几节课
    private Integer section;

}
