package com.clankalliance.backbeta.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //为避免long传向前台出现精度丢失，通过注释将long化为String
    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    private long userNumber;//LoginName

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String password;

    @JsonSerialize(using= ToStringSerializer.class)
    private long phone;

    private Integer power;

}
