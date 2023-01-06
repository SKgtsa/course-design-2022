package com.clankalliance.backbeta.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestTarget {

    //为避免long传向前台出现精度丢失，通过注释将long化为String
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

    private String eMail;

    private String avatarURL;

    private String photoURL;

    private String researchDirection;

    private String section;

}
