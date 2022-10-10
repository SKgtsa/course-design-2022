package com.clankalliance.backbeta.response;

import com.clankalliance.backbeta.entity.User;

public class CommonResponse<T> {

    //业务成功与失败
    private Boolean success = true;
    //返回信息
    private String message;
    //返回用户对象
    private User user;
    //返回泛型数据 自定义类型
    private T content;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", user=" + user +
                ", content=" + content +
                ", token='" + token + '\'' +
                '}';
    }
}
