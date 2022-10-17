package com.clankalliance.backbeta.entity;


import com.clankalliance.backbeta.entity.user.sub.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reward")
public class Reward {

    @Id
    @JsonIgnore
    private long id;

    private String name;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "rewardSet")
    private Set<Student> studentSet;
}
