package com.clankalliance.backbeta.entity.blog;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Date;
import java.util.List;
//帖子
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;

    //标题
    private String heading;
    //内容
    @JsonIgnore
    private String content;
    //作者昵称
    private String nickName;
    //头像url
    private String avatarURL;

    private Long userId;
    //发帖时间
    private String time;
    //封面图
    private String topImageUrl;
    //评论
    @JsonIgnore
    @OneToMany
    private List<Comment> commentList;
    //点赞(学生/老师)
    @JsonIgnore
    @ManyToMany
    private List<Student> likeS;
    @JsonIgnore
    @ManyToMany
    private List<Teacher> likeT;


}
