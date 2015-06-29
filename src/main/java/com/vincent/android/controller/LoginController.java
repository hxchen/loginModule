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

    public void login(View view) {
        UserService userService = new UserService(this);
        String login_name = ((EditText)findViewById(R.id.login_name)).getText().toString();
        String login_pass = ((EditText)findViewById(R.id.login_pass)).getText().toString();
        int flat = userService.login(login_name, login_pass);
        switch (flat) {
            case 1: {
                Intent intent = new Intent();
                intent.setClass(LoginController.this, UserController.class);
                startActivity(intent);
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