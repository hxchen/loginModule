package com.vincent.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;

import java.io.ByteArrayOutputStream;

/**
 * Created by Feng on 2015-06-30.
 */
public class AddController extends ActionBarActivity{
    private UserService userService;
    private EditText etUsername, etPassword, etMail;
    private int RESULT_LOAD_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Actionbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.regist_menu, menu);
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
        etUsername = (EditText)findViewById(R.id.add_name);
        etPassword = (EditText)findViewById(R.id.add_pass);
        etMail = (EditText)findViewById(R.id.add_mail);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                UserModel.getInstance()
                        .setAvatar(byteArrayOutputStream.toByteArray());
                ImageView showImage = (ImageView)findViewById(R.id.loginModuleID_regist_image_view);
                showImage.setImageURI(imagePath);
            }
            catch (Exception e){
                Log.e("path", "exception: " + imagePath.toString());
            }
        }
    }


    // 调用系统api获取图片
    public void getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }


    public void add(View view) {
        String username = etUsername.getText().toString();
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String mail = etMail.getText().toString();
        UserModel userModel = UserModel.getInstance();
        userModel.setUsername(username)
                .setPassword(password)
                .setMail(mail);
        if (userModel.getAvatar() == null) {
            Toast.makeText(this, "没有设置图片", Toast.LENGTH_LONG).show();
            return;
        }
        int flag = userService.register(userModel);
        if(flag == 1){
            this.setResult(RESULT_OK);
            Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            switch(flag) {
                case 301: {
                    Toast.makeText(this,"用户已存在",Toast.LENGTH_LONG).show();
                    break;
                }
                case 311:
                case 312: {
                    Toast.makeText(this, "系统内部错误", Toast.LENGTH_LONG).show();
                    break;
                }
                default:{
                    Toast.makeText(this,"新增失败",Toast.LENGTH_LONG).show();
                    break;
                }
            }

            return;
        }

    }

}
