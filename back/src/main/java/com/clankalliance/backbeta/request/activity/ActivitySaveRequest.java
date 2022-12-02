package com.clankalliance.backbeta.request.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySaveRequest {

    private long id;

    private String token;

    private String activityName;

    private String activityDescription;

    private String date;

    private String result;
}
