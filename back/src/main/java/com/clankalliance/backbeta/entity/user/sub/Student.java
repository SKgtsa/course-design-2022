package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.Activity;
import com.clankalliance.backbeta.entity.Practice;
import com.clankalliance.backbeta.entity.Score;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@Table(	name = "student",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Student extends User {

        public Student(){
                super();
        }

        public Student(long id,long userNumber,String name,String password,long phone,String gradeClass,String idCardNumber,Boolean gender,String ethnic,String politicalAffiliation, String eMail, String avatarURL,String nickName,String photoURL){
                super(id,userNumber,nickName,name,password,phone,gradeClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,avatarURL,photoURL);
        }

        @JsonIgnore
        @OneToMany
        private Set<Course> courseSet;

        @JsonIgnore
        @OneToMany
        private Set<Score> scoreSet;

        @JsonIgnore
        @OneToOne
        private Activity activity;

        @JsonIgnore
        @ManyToMany(cascade = CascadeType.PERSIST)
        private Set<Practice> practiceSet;

        @JsonIgnore
        @ManyToMany(cascade = CascadeType.PERSIST)
        private Set<Practice> rewardSet;
}
