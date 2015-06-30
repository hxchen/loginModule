package com.vincent.android;

import com.vincent.android.controller.LoginController;

/**
 * Created by HS on 2015/6/30.
 */
public class LoginModuleApi {
    private static LoginModuleApi instance = new LoginModuleApi();
    public static LoginModuleApi getInstance() {
        return instance;
    }

    private Class manageLogoutClass;

    public LoginModuleApi setManageLogoutClass(Class _class) {
        this.manageLogoutClass = _class;
        return this;
    }

    public Class getManageLogoutClass() {
        return this.manageLogoutClass;
    }

    private LoginModuleApi() {
        this.manageLogoutClass = LoginController.class;
    }
}
