package com.vincent.android.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.vincent.android.controller.LoginController;
import com.vincent.android.model.ManageViewModel;
import com.vincent.android.model.UserModel;
import com.vincent.android.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Feng on 2015-06-28.
 */
public class UserService {
    private  DatabaseHelper databaseHelper;

    public UserService(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    //登录
    public  int login(String username, String password){
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        String sql = "select * from user where username = ? " +
                "and password =?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{username, password});
        if(cursor.moveToFirst() == true){
            String role = cursor.getString(cursor.getColumnIndex("role"));
            if(role.equals("y")){ //属于管理员

            }
            else if(role.equals("n")){ //属于普通用户

            }
            UserModel.getInstance()
                    .setId(cursor.getInt(cursor.getColumnIndex("id")))
                    .setMail(cursor.getString(cursor.getColumnIndex("mail")))
                    .setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")))
                    .setUsername(cursor.getString(cursor.getColumnIndex("username")))
                    .setRole(role.toCharArray()[0]);
            cursor.close();
            return  1;
        }
        cursor.close();
        return  201;
    }

    //注册
    public int register(UserModel userModel){
        SQLiteDatabase sdb = databaseHelper.getWritableDatabase();
        int flat = exist(userModel.getUsername());
        Log.w("图片长度:", "" + userModel.getAvatar().length);
        if (flat == 301){ //已经存在该用户
            return 301;
        }
        if (flat == 312){
            return 312;
        }
        //默认为普通用户注册
        String sql = "insert into user(username,password,mail,avatar,role) values(?,?,?,?,?)";
        Object obj[] = {userModel.getUsername(), userModel.getPassword(),
                userModel.getMail(), userModel.getAvatar(), 'n'};
        try {
            sdb.execSQL(sql, obj);
            return  1;
        }
        catch (Exception err) {
            Log.e("sql error", err.toString());
            return 311;
        }

    }

    //判断用户是否存在
    public int exist(String username){
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        String sql = "select * from user where username = ? ";
        try {
            Cursor cursor = sdb.rawQuery(sql, new String[]{username});
            if(cursor.getCount() > 0){
                cursor.close();
                return  301;
            }
            cursor.close();
            return  1;
        }
        catch (Exception err) {
            Log.e("sql error", err.toString());
            return 312;
        }

    }


    //获取用户列表
    public List<ManageViewModel> getItemList(){
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        Log.w("info","123!!!!!!!!!!!!!!到达这里了！！！！！！！！！！！！！！！");
        String sql = "select * from user";
        try{
            Cursor cursor = sdb.rawQuery(sql, new String[]{"n"});
            Log.w("info","1234!!!!!!!!!!!!!!到达这里了！！！！！！！！！！！！！！！");
            List<ManageViewModel> list;
            if (cursor.moveToFirst()){ //说明有数据
                list = new ArrayList<ManageViewModel>();
                for(int i=0;i<cursor.getCount();i++){
                    cursor.move(i);//移动到指定记录
                    String username = cursor.getString(cursor.getColumnIndex("username"));
                    String mail = cursor.getString(cursor.getColumnIndex("mail"));
                    byte[] avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
                    ManageViewModel viewModel = new ManageViewModel();
                    viewModel.setUsername(username);
                    viewModel.setMail(mail);
                    viewModel.setAvatar(avatar);
                    Log.d("info","!!!!!!!!!!!!!!"+list.get(i).getUsername());
                    Log.d("info","!!!!!!!!!!!!!!!!!"+list.get(i).getAvatar().length);
                    list.add(viewModel);
                }
                return  list;
            }
            Log.w("info","12345!!!!!!!!!!!!!!到达这里了！！！！！！！！！！！！！！！");
            //没有数据返回
            return  null;
        }
        catch (Exception e){
            Log.e("sql error", e.toString());
            return  null;
        }
    }
}
