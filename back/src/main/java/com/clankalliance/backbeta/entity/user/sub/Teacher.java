package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(	name = "teacher",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Teacher extends User {

        public Teacher(){
                super();
                courseSet = new HashSet<>();
        }

        public Teacher(long id,long userNumber,String name,String password,long phone,String gradeClass,String idCardNumber,Boolean gender,String ethnic,String politicalAffiliation, String eMail, String avatarURL,String nickName,String photoURL){
                super(id,userNumber,nickName,name,password,phone,gradeClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,avatarURL,photoURL);
                courseSet = new HashSet<>();
        }

        public Set<Course> getCourseSet() {
                return courseSet;
        }

        public void setCourseSet(Set<Course> courseSet) {
                this.courseSet = courseSet;
        }

        //删除老师会一并删除他的课程
        @JsonIgnore
        @OneToMany
        private Set<Course> courseSet;

}