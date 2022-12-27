package com.clankalliance.backbeta.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterData {

    @ExcelProperty("学工号")
    private String userNumber = "";//LoginName

    @ExcelProperty(value = "姓名",index = 1)
    private String name = "";

    @ExcelProperty(value = "密码",index = 2)
    private String password = "";

    @ExcelProperty(value = "电话",index = 3)
    private String phone = "";

    @ExcelProperty(value = "班级",index = 4)
    private String studentClass = "";

    @ExcelProperty(value = "身份证号",index = 5)
    private String idCardNumber = "";

    @ExcelProperty(value = "性别",index = 6)
    private String gender = "";

    @ExcelProperty(value = "民族",index = 7)
    private String ethnic = "";

    @ExcelProperty(value = "政治面貌",index = 8)
    private String politicalAffiliation = "";

    @ExcelProperty(value = "电子邮箱",index = 9)
    private String eMail = "";

    @ExcelProperty(value = "学生届次",index = 10)
    private String section = "";


}
