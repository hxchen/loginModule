package com.vincent.android.model;

import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by HS on 2015/6/23.
 */
public class UserModel {
    private static UserModel instance = new UserModel();

    private int id;
    private String username;
    private String password;
    private String mail;
    private byte[] avatar;
    private char role;

    public int getId() {
        return id;
    }

    public UserModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public UserModel setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public UserModel setAvatar(byte[]  avatar) {
        this.avatar = avatar;
        return this;
    }

    public char getRole() {
        return role;
    }

    public void setRole(char role) {
        this.role = role;
    }

    public static UserModel getInstance() {
        return instance;
    }

    private UserModel() {

    }

    public UserModel login() {
        Log.i("UserModel", "test login function");
        return  this;
    }
}