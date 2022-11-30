package com.clankalliance.backbeta.request.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDeleteRequest {

    private long activityId;

    private String token;
}
