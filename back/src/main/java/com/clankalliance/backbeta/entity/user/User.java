package com.clankalliance.backbeta.entity.user;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;



@Data
@NoArgsConstructor
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

    //需要增加头像url属性
    private String avatarURL;

}
