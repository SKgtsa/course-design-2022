package com.clankalliance.backbeta.response;


import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
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

    private Long userId;
    //发帖时间
    private String time;

    private Integer likeNum;

    private String topImageURL;

    private boolean like;

    private boolean collect;

    public PostResponseTarget(User user, Post post){
        id = post.getId();
        heading = post.getHeading();
        nickName = post.getNickName();
        avatarURL = post.getAvatarURL();
        userId = post.getUserId();
        time = post.getTime();
        likeNum = post.getLikeS().size() + post.getLikeT().size();
        topImageURL = post.getTopImageUrl();
        like = post.getLikeS().contains(user) || post.getLikeT().contains(user);
        if(user instanceof Teacher){
            collect = ((Teacher) user).getCollection().contains(post);
        }else if(user instanceof Student){
            collect = ((Student) user).getCollection().contains(post);
        }

    }

}
