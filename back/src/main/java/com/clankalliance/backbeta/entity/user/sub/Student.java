package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.*;
import com.clankalliance.backbeta.entity.blog.Post;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "student",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Student extends User {

        public Student(){
                super();
                courseSet = new HashSet<>();
                scoreSet = new HashSet<>();
                activity = new ArrayList<>();
                practiceSet = new ArrayList<>();
                rewardSet = new ArrayList<>();
                collection = new ArrayList<>();
                achievementSet = new HashSet<>();
                section = null;
        }

        public Student(long id,long userNumber,String name,String password,long phone,String gradeClass,String idCardNumber,Boolean gender,String ethnic,String politicalAffiliation, String eMail, String avatarURL,String nickName,String photoURL,String section){
                super(id,userNumber,nickName,name,password,phone,gradeClass,idCardNumber,gender,ethnic,politicalAffiliation,eMail,avatarURL,photoURL);
                courseSet = new HashSet<>();
                scoreSet = new HashSet<>();
                activity = new ArrayList<>();
                practiceSet = new ArrayList<>();
                rewardSet = new ArrayList<>();
                collection = new ArrayList<>();
                achievementSet = new HashSet<>();
                this.section = section;
        }

        public String getSection() {
                return section;
        }

        public void setSection(String section) {
                this.section = section;
        }

        public Set<Course> getCourseSet() {
                return courseSet;
        }

        public void setCourseSet(Set<Course> courseSet) {
                this.courseSet = courseSet;
        }

        public Set<Score> getScoreSet() {
                return scoreSet;
        }

        public void setScoreSet(Set<Score> scoreSet) {
                this.scoreSet = scoreSet;
        }

        public List<Activity> getActivity() {
                return activity;
        }

        public void setActivity(List<Activity> activity) {
                this.activity = activity;
        }

        public List<Practice> getPracticeSet() {
                return practiceSet;
        }

        public void setPracticeSet(List<Practice> practiceSet) {
                this.practiceSet = practiceSet;
        }

        public List<Reward> getRewardSet() {
                return rewardSet;
        }

        public void setRewardSet(List<Reward> rewardSet) {
                this.rewardSet = rewardSet;
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

        //学生届次
        private String section;

        @JsonIgnore
        @OneToMany
        private Set<Course> courseSet;

        @JsonIgnore
        @OneToMany
        private Set<Score> scoreSet;

        @JsonIgnore
        @OneToMany
        private List<Activity> activity;

        @JsonIgnore
        @ManyToMany(cascade = CascadeType.PERSIST)
        private List<Practice> practiceSet;

        @JsonIgnore
        @ManyToMany(cascade = CascadeType.PERSIST)
        private List<Reward> rewardSet;

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

        @JsonIgnore
        @OneToMany
        private List<Post> collection;

        @JsonIgnore
        @OneToMany
        private Set<Achievement> achievementSet;

}
