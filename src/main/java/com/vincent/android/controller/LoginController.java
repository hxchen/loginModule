package com.vincent.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;


public class LoginController extends Activity {
    private EditText etUsername,etPassword;

    private UserService userService;

    public static int flag = 0;

    //@TODO: 改造登陆控制，将页面的生成方式改成用java代码生成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }

    private void init(){
        userService = new UserService(this);
        etUsername = (EditText)findViewById(R.id.login_name);
        etPassword = (EditText)findViewById(R.id.login_pass);

        /** 注册成功后传过来用户名和密码,显示在登录界面 */
        if (flag == 1) {
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            etUsername.setText(username);
            etPassword.setText(password);
        }
    }

    //@TODO: 完成用户类型划分
    public void login(View view) {
        UserService userService = new UserService(this);
        String login_name = ((EditText)findViewById(R.id.login_name)).getText().toString();
        String login_pass = ((EditText)findViewById(R.id.login_pass)).getText().toString();
        int flat = userService.login(login_name, login_pass);
        switch (flat) {
            case 1: {
                Intent intent = new Intent();
                intent.setClass(LoginController.this, ManageController.class);
                startActivity(intent);
                this.onDestroy();
                break;
            }
            case 201: {
                Toast.makeText(this,"用户名或者密码错误", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void regist(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginController.this, RegistController.class);
        startActivity(intent);
    }
}
