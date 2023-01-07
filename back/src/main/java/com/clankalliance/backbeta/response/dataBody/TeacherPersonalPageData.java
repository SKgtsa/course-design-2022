package com.clankalliance.backbeta.response.dataBody;

import com.clankalliance.backbeta.entity.user.sub.Teacher;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherPersonalPageData {

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userNumber;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long phone;

    private String name;

    private boolean gender;

    private String eMail;

    private String researchDirection;

    private String photoURL;

    public TeacherPersonalPageData(Teacher t){
        userNumber = t.getUserNumber();
        phone = t.getPhone();
        name = t.getName();
        gender = t.getGender();
        eMail = t.getEMail();
        researchDirection = t.getResearchDirection();
        photoURL = t.getPhotoURL();
    }

}
