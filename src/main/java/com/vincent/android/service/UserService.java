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
            UserModel.getInstance()
                    .setId(cursor.getInt(cursor.getColumnIndex("id")))
                    .setMail(cursor.getString(cursor.getColumnIndex("mail")))
                    .setUsername(cursor.getString(cursor.getColumnIndex("username")))
                    .setRole(role.toCharArray()[0]);
            if(role.equals("y")){ //属于管理员
                byte nullArr[] = {};
                UserModel.getInstance().setAvatar(nullArr);
            }
            else if(role.equals("n")){ //属于普通用户
                UserModel.getInstance().setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
            }

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
        String sql = "select * from user where role=?";
        try{
            Cursor cursor = sdb.rawQuery(sql, new String[]{"n"});
            List<ManageViewModel> list = new ArrayList<ManageViewModel>();
            if (cursor.moveToFirst()){ //说明有数据

                for(int i=0;i< cursor.getCount();i++){
                    cursor.moveToPosition(i);//移动到指定记录
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String username = cursor.getString(cursor.getColumnIndex("username"));
                    String mail = cursor.getString(cursor.getColumnIndex("mail"));
                    byte[] avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
                    ManageViewModel viewModel = new ManageViewModel();
                    viewModel.setUsername(username);
                    viewModel.setMail(mail);
                    viewModel.setAvatar(avatar);
                    list.add(viewModel);
                }
                cursor.close();
                return  list;
            }
            //没有数据返回
            return  list;
        }
        catch (Exception e){
            List<ManageViewModel> nullList = new ArrayList<ManageViewModel>();
            return  nullList;
        }
    }

    //删除用户
    public int delete(String username){
        SQLiteDatabase sdb = databaseHelper.getReadableDatabase();
        String sql = "delete from user where username=?";
        Object[] obj = {username};
        try {
            sdb.execSQL(sql, obj);
            return  1;
        }
        catch (Exception e){
            return  411;
        }
    }
}
