package com.clankalliance.backbeta.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

//博客文章
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    //文章id
    @Id
    private long id;

    //学工号
    private String userNumber;

    //文章标题
    private String title;

    //文章内容
    private String content;


}
