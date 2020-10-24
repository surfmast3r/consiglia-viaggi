package com.surfmaster.consigliaviaggi.models;

public class AuthenticatedUser extends User {

    private String token;
    private Integer type;


    public AuthenticatedUser(User.Builder builder){
        super(builder);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
