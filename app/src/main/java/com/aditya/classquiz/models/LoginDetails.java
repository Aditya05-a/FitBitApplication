package com.aditya.classquiz.models;

public class LoginDetails {
    private long user_id;
    private String user_name;
    private String password;
    public LoginDetails(){

    }
    public LoginDetails(long user_id,String user_name,String password){
        this.user_id=user_id;
        this.user_name=user_name;
        this.password=password;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
