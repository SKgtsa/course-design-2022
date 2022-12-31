package com.clankalliance.backbeta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {

    //成就系统 学生互评会起到创建成就的作用

    @Id
    @JsonIgnore
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    private String name;

    private String description;

}
