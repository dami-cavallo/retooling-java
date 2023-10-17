package com.retooling.accenture.msspringsecurity.model;

public class LoginCredentials {
    private String email;
    private String userPass;

    protected LoginCredentials(){

    }
    public LoginCredentials(String email, String userPass) {
        this.email = email;
        this.userPass = userPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
