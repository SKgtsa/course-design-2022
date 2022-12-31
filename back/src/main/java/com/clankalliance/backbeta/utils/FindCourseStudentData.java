package com.clankalliance.backbeta.utils;

import com.clankalliance.backbeta.entity.user.sub.Student;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindCourseStudentData {

    @JsonSerialize(using= ToStringSerializer.class)
    private long id;

    @JsonSerialize(using= ToStringSerializer.class)
    private long userNumber;

    private String name;

    private String studentClass;

    private String section;

    public FindCourseStudentData(Student student){
        id = student.getId();
        userNumber = student.getUserNumber();
        name = student.getName();
        studentClass = student.getStudentClass();
        section = student.getSection();
    }

}
