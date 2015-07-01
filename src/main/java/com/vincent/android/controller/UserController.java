package com.vincent.android.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.vincent.android.model.UserModel;

/**
 * Created by HS on 2015/6/29.
 */
public class UserController extends Activity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.loginmodule_testlogin);

        String username = UserModel.getInstance().getUsername();
        byte[] avatar = UserModel.getInstance().getAvatar();

        ((TextView)findViewById(R.id.loginModule_user_name)).setText(username);
        Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
        ((ImageView)findViewById(R.id.loginModule_show_image)).setImageBitmap(bitmap);
    }
}
