package com.clankalliance.backbeta.response;


import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseTarget {

    private String id;

    //标题
    private String heading;

    //作者昵称
    private String nickName;
    //头像url
    private String avatarURL;
    //发帖时间
    private Date time;

    private Integer likeNum;

    private boolean like;

    public PostResponseTarget(User user, Post post){
        id = post.getId();
        heading = post.getHeading();
        nickName = post.getNickName();
        avatarURL = post.getAvatarURL();
        time = post.getTime();
        likeNum = post.getLikeS().size() + post.getLikeT().size();
        like = post.getLikeS().contains(user) || post.getLikeT().contains(user);
    }

}