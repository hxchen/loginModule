package com.vincent.android;

import android.app.Activity;
import android.content.Intent;
import com.vincent.android.controller.LoginController;
import com.vincent.android.controller.R;
import com.vincent.android.controller.UserController;
import com.vincent.android.model.UserModel;

/**
 * Created by HS on 2015/6/30.
 */
public class LoginModuleApi {
    public final static int FLAG_ERROR = -1;
    public final static int FLAG_OK = 1;
    private String token;
    private byte[] registDefaultImg;

    private static LoginModuleApi instance = new LoginModuleApi();
    public static LoginModuleApi getInstance() {
        return instance;
    }

    private Class manageLogoutClass;
    private Class loginClass;
    private int loginRequestCode;
    private Class logoutClass;

    // UI
    private int loginActivityUI;
    private int registActivityUI;

    private LoginModuleApi() {
        this.manageLogoutClass = LoginController.class;
        this.loginClass = UserController.class;
        this.loginRequestCode = 1;
        this.token = "sb token";
        this.registActivityUI = R.layout.loginmodule_regist_layout;
        this.loginActivityUI = R.layout.loginmodule_login_layout;
        this.registDefaultImg = null;
    }

    /*
     * 设置管理员退出时跳转页面，默认为登录页面
     */
    public LoginModuleApi setManageLogoutClass(Class _class) {
        this.manageLogoutClass = _class;
        return this;
    }

    /*
     * 获取登录后返回的页面的类
     */
    public Class getLoginClass() {
        return this.loginClass;
    }

    /*
     * 设置登录后跳转的页面
     */
    public LoginModuleApi setLoginClass(Class _class) {
        this.loginClass = _class;
        return this;
    }

    /*
     * 获取管理员退出时跳转的页面，默认为登录页面
     */
    public Class getManageLogoutClass() {
        return this.manageLogoutClass;
    }

    /*
     * 设置登录时的请求码，默认为1
     */
    public LoginModuleApi setLoginRequestCode(int _code){
        this.loginRequestCode = _code;
        return this;
    }

    /*
     * 获取登录时的请求码
     */
    public int getLoginRequestCode() {
        return this.loginRequestCode;
    }

    /*
     * 获取token
     */
    public String getToken() {
        return this.token;
    }

    /*
     * 设置token
     */
    public LoginModuleApi setToken(String _token){
        this.token  = _token;
        return this;
    }

    /*
     * 获取默认注册图片
     */
    public byte[] getRegistDefaultImg() {
        return this.registDefaultImg;
    }
    /*
     * 设置默认注册图片
     */
    public LoginModuleApi setRegistDefaultImg(byte[] _img) {
        this.registDefaultImg = _img;
        return this;
    }

    /*
     * 登录，参数为当前activity的this
     */
    public void login(Activity _this) {
        Intent intent = new Intent();
        intent.setClass(_this, LoginController.class);
        _this.startActivityForResult(intent, this.loginRequestCode);
    }

    /*
     * 退出，参数为当前activity的this
     */
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

//    public void regist (Activity _this) {
//        Intent intent = new Intent();
//        intent.setClass(_this, RegistController.class);
//        _this.startActivity(intent);
//        _this.finish();
//    }

    // 页面UI部分

    public LoginModuleApi setLoginActivityUI(int activityID) {
        this.loginActivityUI = activityID;
        return this;
    }

    public int getLoginActivityUI() {
        return this.loginActivityUI;
    }

    public LoginModuleApi setRegistActivityUI(int activityUI) {
        this.registActivityUI = activityUI;
        return this;
    }

    public int getRegistActivityUI() {
        return  this.registActivityUI;
    }
}
