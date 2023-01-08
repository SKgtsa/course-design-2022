package com.clankalliance.backbeta.request.user;

import com.clankalliance.backbeta.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagerSaveRequest {
    private String token;
    private UserRequestTarget user;
}
