package com.clankalliance.backbeta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//公告
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Announcement {

    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    private String pictureUrl;

    private String heading;

    @JsonIgnore
    private String content;

}
