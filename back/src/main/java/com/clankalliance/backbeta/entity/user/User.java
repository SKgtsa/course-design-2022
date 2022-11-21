package com.clankalliance.backbeta.entity.user;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;



@Data
@AllArgsConstructor
@MappedSuperclass
public abstract class User {
    //为避免long传向前台出现精度丢失，通过注释将long化为String
    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    @JsonSerialize(using= ToStringSerializer.class)
    private long userNumber;//LoginName

    @NotBlank
    @Size(max = 50)
    private String name;


    @NotBlank
    @Size(max = 50)
    private String password;

    @JsonSerialize(using= ToStringSerializer.class)
    private long phone;

    private String studentClass;

    private String idCardNumber;


    //男：false 女：true
    private Boolean gender;

    //民族
    private String ethnic;

    //政治面貌
    private String politicalAffiliation;

    //电子邮箱
    private String eMail;

    //头像url
    private String avatarURL;

    //对默认头像进行一个设置
    public User(){
        this.avatarURL = "/static/default.jpg";
    }


}
