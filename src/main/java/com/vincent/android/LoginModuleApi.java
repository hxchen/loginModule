package com.vincent.android;

import android.app.Activity;
import android.content.Intent;
import com.vincent.android.controller.LoginController;
import com.vincent.android.controller.UserController;
import com.vincent.android.model.UserModel;

/**
 * Created by HS on 2015/6/30.
 */
public class LoginModuleApi {
    public final static int FLAG_ERROR = -1;
    public final static int FLAG_OK = 1;
    private String token;

    private static LoginModuleApi instance = new LoginModuleApi();
    public static LoginModuleApi getInstance() {
        return instance;
    }

    private Class manageLogoutClass;
    private Class loginClass;
    private int loginRequestCode;
    private Class logoutClass;

    public LoginModuleApi setManageLogoutClass(Class _class) {
        this.manageLogoutClass = _class;
        return this;
    }

    public Class getLoginClass() {
        return this.loginClass;
    }

    public LoginModuleApi setLoginClass(Class _class) {
        this.loginClass = _class;
        return this;
    }

    public Class getManageLogoutClass() {
        return this.manageLogoutClass;
    }

    public LoginModuleApi setLoginRequestCode(int _code){
        this.loginRequestCode = _code;
        return this;
    }

    public int getLoginRequestCode() {
        return this.loginRequestCode;
    }

    public String getToken() {
        return this.token;
    }

    private LoginModuleApi() {
        this.manageLogoutClass = LoginController.class;
        this.loginClass = UserController.class;
        this.loginRequestCode = 0;
        this.token = "sb token";
    }

    public void login(Activity _this) {
        Intent intent = new Intent();
        intent.setClass(_this, LoginController.class);
        _this.startActivityForResult(intent, this.loginRequestCode);
    }

    public boolean logout(Activity _this) {
        if (!UserModel.getInstance().destory()) {
            return false;
        }
        this.token = null;

        if (this.logoutClass == null) {
            return true;
        }
        Intent intent = new Intent();
        intent.setClass(_this, LoginController.class);
        _this.startActivity(intent);
        _this.finish();
        return true;
    }

    // 页面UI部分

}
