package com.clankalliance.backbeta.utils.ExcelListener.ScoreListener;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchScoreData {

    @ExcelProperty("学号")
    private String studentNumber = "0";

    @ExcelProperty("平时分数")
    private String dailyScore = "0";

    @ExcelProperty("期末分数")
    private String endScore = "0";

}
