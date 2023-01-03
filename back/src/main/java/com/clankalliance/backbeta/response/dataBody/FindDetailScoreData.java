package com.clankalliance.backbeta.response.dataBody;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindDetailScoreData {
    private String studentName;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long studentNumber;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long studentId;

    private Integer dailyScore;

    private Integer endScore;

    private Double weight;

}
