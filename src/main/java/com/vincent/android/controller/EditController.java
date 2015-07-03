package com.vincent.android.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;

import java.io.ByteArrayOutputStream;

/**
 * Created by Feng on 2015-07-02.
 */
public class EditController extends ActionBarActivity {
    private  UserService userService;
    private TextView tvUsername;
    private EditText  etMail;
    private ImageView imageView;
    private byte[] imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginmodule_edit_layout);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init(){
        userService = new UserService(this);
        tvUsername = (TextView)findViewById(R.id.loginModule_edit_name);
        etMail = (EditText)findViewById(R.id.loginModule_edit_mail);
        imageView = (ImageView)findViewById(R.id.loginModule_edit_image_view);
        tvUsername.setText(UserModel.getInstance().getUsername());
        etMail.setText(UserModel.getInstance().getMail());
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(UserModel.getInstance().getAvatar(), 0, UserModel.getInstance().getAvatar().length));
    }

    // Actionbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmodule_login_menu, menu);
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

    public void  loginModule_save(View view){
        if (etMail.getText().toString() != UserModel.getInstance().getMail()) {
            int code = userService.updateMail(etMail.getText().toString());
            if (code == 1) {
                UserModel.getInstance().setMail(etMail.getText().toString());
                Toast.makeText(this, "修改邮箱成功", Toast.LENGTH_SHORT).show();
            } else  if (code == 511) {
                Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "系统内部出错", Toast.LENGTH_LONG).show();
            }
        }
        if (imageData != null) {
            int code = userService.updateAvatar(imageData);
            if (code == 1) {
                UserModel.getInstance().setAvatar(imageData);
                Toast.makeText(this, "修改头像成功", Toast.LENGTH_SHORT).show();
            } else  if (code == 511) {
                Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                imageData = byteArrayOutputStream.toByteArray();
                ImageView showImage = (ImageView)findViewById(R.id.loginModule_edit_image_view);
                showImage.setImageURI(imagePath);
                Log.i("path", imagePath.toString());
            }
            catch (Exception e){
                Log.e("path", "exception: " + imagePath.toString());
            }
        }
    }

    // 调用系统api获取图片
    public void loginModule_getImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
}
