package com.clankalliance.backbeta.response.dataBody;

import com.clankalliance.backbeta.entity.user.sub.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefData {

    private String nickName;

    private String name;

    private Long id;

    public UserBriefData(Student student){
        nickName = student.getNickName();
        name = student.getName();
        id = student.getId();
    }

}
