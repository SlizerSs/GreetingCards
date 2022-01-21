package com.example.greetingcards.Models;

public class User {

    private String mID;
    private String mName;
    private String mEmail;
    private String mLogin;
    private String mPassword;

    public User() {

    }

    public void setID(String id) {this.mID = id;}
    public String getID(){return this.mID;}
    public void setName(String name) {this.mName = name;}
    public String getName(){return this.mName;}
    public void setEmail(String email) {this.mEmail = email;}
    public String getEmail(){return this.mEmail;}
    public void setLogin(String login) {this.mLogin = login;}
    public String getLogin(){return this.mLogin;}
    public void setPassword(String password) {this.mPassword = password;}
    public String getPassword(){return this.mPassword;}
}
