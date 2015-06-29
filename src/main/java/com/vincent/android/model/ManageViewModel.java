package com.vincent.android.model;

import android.graphics.Bitmap;

/**
 * Created by Feng on 2015-06-29.
 */
public class ManageViewModel {
    public  String username;
    public  String mail;
    public byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
