package com.clankalliance.backbeta.request.user;

import com.clankalliance.backbeta.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagerSaveRequest {
    private String token;

    private long id;

    private long userNumber;//LoginName

    private String nickName;

    private String name;

    private long phone;

    private String studentClass;

    private String idCardNumber;

    private Boolean gender;

    private String ethnic;

    private String politicalAffiliation;

    private String email;

    private String avatarURL;

    private String photoURL;

    private String researchDirection;

    private String section;


}
