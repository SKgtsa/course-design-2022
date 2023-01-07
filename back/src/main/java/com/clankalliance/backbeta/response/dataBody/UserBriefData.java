package com.clankalliance.backbeta.response.dataBody;

import com.clankalliance.backbeta.entity.user.sub.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefData {
    private String name;

    private Long id;

    private String photoURL;

    public UserBriefData(Student student){
        name = student.getName();
        id = student.getId();
        photoURL = student.getPhotoURL();
    }

}
