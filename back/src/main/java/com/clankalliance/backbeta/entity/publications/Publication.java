package com.clankalliance.backbeta.entity.publications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publication {


    @Id
    private String id;

    //标题
    private String heading;
    //内容
    @JsonIgnore
    private String content;
    //封面图
    private String topImageURL;

    private Long userId;

}
