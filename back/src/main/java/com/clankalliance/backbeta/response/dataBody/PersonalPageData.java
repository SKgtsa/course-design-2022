package com.clankalliance.backbeta.response.dataBody;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.user.User;
import com.clankalliance.backbeta.entity.user.sub.Student;
import com.clankalliance.backbeta.entity.user.sub.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPageData {

    private String avatarURL;

    private String identity;

    private String nickName;

    private String name;

    private String gender;

    private String email;

    private Set<Achievement> achievementList;

    private boolean follow;

    public PersonalPageData(User user,boolean follow){
        avatarURL = user.getAvatarURL();
        identity = user instanceof Teacher? "老师":user instanceof Student? "学生":"管理员";
        nickName = user.getNickName();
        name = user.getName();
        gender = user.getGender() ? "女":"男";
        email = user.getEMail();
        achievementList = user instanceof Teacher? ((Teacher) user).getAchievementSet():user instanceof Student? ((Student) user).getAchievementSet():new HashSet<>();
        this.follow = follow;
    }

}
