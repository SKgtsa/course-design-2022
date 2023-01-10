package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.Achievement;
import com.clankalliance.backbeta.entity.blog.Post;
import com.clankalliance.backbeta.entity.course.Course;
import com.clankalliance.backbeta.entity.publications.Publication;
import com.clankalliance.backbeta.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
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
                collection = new ArrayList<>();
                postList = new ArrayList<>();
                publicationList = new ArrayList<>();
                achievementSet = new HashSet<>();
                researchDirection = null;
        }


        public Teacher(long id,long userNumber,String name,String password,long phone,String idCardNumber,Boolean gender,String ethnic,String politicalAffiliation, String eMail, String avatarURL,String nickName,String photoURL,String researchDirection){
                super(id,userNumber,nickName,name,password,phone,idCardNumber,gender,ethnic,politicalAffiliation,eMail,avatarURL,photoURL);
                courseSet = new HashSet<>();
                collection = new ArrayList<>();
                postList = new ArrayList<>();
                achievementSet = new HashSet<>();
                publicationList = new ArrayList<>();
                this.researchDirection = researchDirection;
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

        public List<Post> getCollection() {
                return collection;
        }

        public void setCollection(List<Post> collection) {
                this.collection = collection;
        }

        public Set<Achievement> getAchievementSet() {
                return achievementSet;
        }

        public void setAchievementSet(Set<Achievement> achievementSet) {
                this.achievementSet = achievementSet;
        }

        public List<Publication> getPublicationList() {
                return publicationList;
        }

        public void setPublicationList(List<Publication> publicationList) {
                this.publicationList = publicationList;
        }

        public String getResearchDirection() {
                return researchDirection;
        }

        public void setResearchDirection(String researchDirection) {
                this.researchDirection = researchDirection;
        }

        //删除老师会一并删除他的课程
        @JsonIgnore
        @OneToMany
        private Set<Course> courseSet;

        //关注学生
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

        @JsonIgnore
        @OneToMany
        private List<Post> collection;

        @JsonIgnore
        @OneToMany
        private Set<Achievement> achievementSet;

        @JsonIgnore
        @OneToMany
        private List<Publication> publicationList;

        private String researchDirection;

}
