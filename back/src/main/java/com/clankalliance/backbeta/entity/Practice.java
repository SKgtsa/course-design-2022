package com.clankalliance.backbeta.entity;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "practice")
public class Practice {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    private String name;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "practiceSet")
    private Set<Student> studentSet;

}
