package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
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

        public Set<Student> getFriendS() {
                return friendS;
        }

        public void setFriendS(Set<Student> friendS) {
                this.friendS = friendS;
        }

        public Set<Teacher> getFriendT() {
                return friendT;
        }

        public void setFriendT(Set<Teacher> friendT) {
                this.friendT = friendT;
        }

        public List<Post> getPostList() {
                return postList;
        }

        public void setPostList(List<Post> postList) {
                this.postList = postList;
        }

        //删除老师会一并删除他的课程
        @JsonIgnore
        @OneToMany
        private Set<Course> courseSet;

        //学生好友
        @JsonIgnore
        @ManyToMany
        private Set<Student> friendS;
        //教师好友
        @JsonIgnore
        @ManyToMany
        private Set<Teacher> friendT;

        @JsonIgnore
        @OneToMany
        private List<Post> postList;

}
