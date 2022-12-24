package com.clankalliance.backbeta.request.course;

import com.clankalliance.backbeta.entity.user.sub.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindRequest {

    private String token;

    private long courseId;

    private Student student;

}
