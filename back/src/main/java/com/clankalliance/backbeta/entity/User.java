package com.clankalliance.backbeta.entity;


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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(	name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "loginName"),
        })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    //为避免long传向前台出现精度丢失，通过注释将long化为String
    @Id
    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    @NotBlank
    @Size(max = 50)
    private long userNumber;//LoginName

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String password;

    @NotBlank
    @JsonSerialize(using= ToStringSerializer.class)
    private long phone;

    @NotBlank
    private Integer power;

}
