package com.clankalliance.backbeta.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCheckRequest {

    private long phone;

    private String code;

}
