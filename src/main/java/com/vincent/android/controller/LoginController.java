package com.vincent.android.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.vincent.android.LoginModuleApi;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;


public class LoginController extends ActionBarActivity {
    private EditText etUsername,etPassword;

    private UserService userService;

    //@TODO: 改造登陆控制，将页面的生成方式改成用java代码生成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Actionbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(){
        userService = new UserService(this);
        etUsername = (EditText)findViewById(R.id.login_name);
        etPassword = (EditText)findViewById(R.id.login_pass);

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
                switch (UserModel.getInstance().getRole()) {
                    case 'n': {
                        intent.setClass(LoginController.this, LoginModuleApi.getInstance().getLoginClass());
                        intent.putExtra("flag", LoginModuleApi.FLAG_OK);
                        intent.putExtra("token", LoginModuleApi.getInstance().getToken());
                        this.setResult(RESULT_OK, intent);
                        break;
                    }
                    case 'y': {
                        intent.setClass(LoginController.this, ManageController.class);
                        LoginController.this.finish();
                        startActivity(intent);
                        break;
                    }
                }
                this.finish();
                break;
            }
            case 201: {
                Toast.makeText(this,"用户名或者密码错误", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (RESULT_OK != resCode) {
            return;
        }
        switch (reqCode) {
            case 1: {
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                etUsername.setText(username);
                etPassword.setText(password);
                break;
            }
        }
    }

    public void regist(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginController.this, RegistController.class);
        startActivityForResult(intent, 1);
    }
}
