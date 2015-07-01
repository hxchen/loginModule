package com.vincent.android;

import android.app.Activity;
import android.content.Intent;
import com.vincent.android.controller.LoginController;
import com.vincent.android.controller.UserController;

/**
 * Created by HS on 2015/6/30.
 */
public class LoginModuleApi {
    private static LoginModuleApi instance = new LoginModuleApi();
    public static LoginModuleApi getInstance() {
        return instance;
    }

    private Class manageLogoutClass;
    private Class loginClass;

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

    private LoginModuleApi() {
        this.manageLogoutClass = LoginController.class;
        this.loginClass = UserController.class;
    }

    public void login(Activity _this) {
        Intent intent = new Intent();
        intent.setClass(_this, LoginController.class);
        _this.startActivity(intent);
    }
}
