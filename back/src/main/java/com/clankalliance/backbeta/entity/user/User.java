package com.clankalliance.backbeta.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


/*
增加tag
每次数据更新时，统计变更数据并更新tag表
学生互评可直接增加tag
 */


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


    private String nickName;

    @NotBlank
    @Size(max = 50)
    private String name;


    @JsonIgnore
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

    //个人照片
    private String photoURL;



    //对默认头像进行一个设置
    public User(){
        this.avatarURL = "/static/default.jpg";
    }


}
