package com.clankalliance.backbeta.entity.user.sub;

import com.clankalliance.backbeta.entity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(	name = "manager",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class Manager extends User {

        public Manager(){
                super();
        }

        public Manager(long id,long userNumber,String name,String password,long phone,String idCardNumber,Boolean gender,String ethnic,String politicalAffiliation, String eMail, String avatarURL,String nickName,String photoURL){
                super(id,userNumber,nickName,name,password,phone,idCardNumber,gender,ethnic,politicalAffiliation,eMail,avatarURL,photoURL);
        }


}

