package com.example.greetingcards.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private Integer mID;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "email")
    private String mEmail;

    @NonNull
    @ColumnInfo(name = "login")
    private String mLogin;

    @NonNull
    @ColumnInfo(name = "password")
    private String mPassword;


    public User() {

    }

    public void setID(@NonNull Integer id) {this.mID = id;}
    public Integer getID(){return this.mID;}
    public void setName(@NonNull String name) {this.mName = name;}
    public String getName(){return this.mName;}
    public void setEmail(@NonNull String email) {this.mEmail = email;}
    public String getEmail(){return this.mEmail;}
    public void setLogin(@NonNull String login) {this.mLogin = login;}
    public String getLogin(){return this.mLogin;}
    public void setPassword(@NonNull String password) {this.mPassword = password;}
    public String getPassword(){return this.mPassword;}
}
