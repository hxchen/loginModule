package com.vincent.android.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.vincent.android.LoginModuleApi;
import com.vincent.android.model.ManageViewModel;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Feng on 2015-06-28.
 */
public class ManageController extends ActionBarActivity {
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private UserService userService;
    private  ArrayList<HashMap<String, Object>> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginmodule_manage_layout);

        userService = new UserService(this);
        listView = (ListView)this.findViewById(R.id.loginModule_manage_listview);
        items = getItems();
        //创建简单适配器
        simpleAdapter = new SimpleAdapter(this, items, R.layout.loginmodule_user_item,
                new String[]{"username","mail", "avatar"},
                new int[]{R.id.loginModule_user_username,R.id.loginModule_user_mail, R.id.loginModule_user_avatar});

        //加载SimpleAdapter到ListView中
        listView.setAdapter(simpleAdapter);

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if (view instanceof ImageView && o instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap) o;
                    ((ImageView) view).setImageBitmap(bitmap);
                    return true;
                } else if (o == null) {
                    return true;
                }
                return false;
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
            }
        });
    }

    protected ArrayList<HashMap<String, Object>> getItems(){
        List<ManageViewModel> list = userService.getItemList();
        if(list == null){
            ArrayList<HashMap<String, Object>> nullArr = new ArrayList<HashMap<String, Object>>();
            return  nullArr;
        }
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < list.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("username", list.get(i).getUsername());
            map.put("mail", list.get(i).getMail());
            if (list.get(i).getAvatar() == null) {
                map.put("avatar", null);
            } else {
                //将字节数组转化为位图
                Bitmap imagebitmap= BitmapFactory.decodeByteArray( list.get(i).getAvatar(), 0,  list.get(i).getAvatar().length);
                map.put("avatar",imagebitmap );
            }

            items.add(map);
        }
        return  items;
    }

    // Actionbar 菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmodule_menu_main, menu);
        return true;
    }

    // 增加用户
    public void LoginModule_addUser(MenuItem item) {
        Intent intent = new Intent();
        intent.setClass(ManageController.this, AddController.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1: {
                restart();
                break;
            }
        }
    }

    private void restart() {
        Intent intent = new Intent();
        intent.setClass(this, ManageController.class);
        startActivity(intent);
        finish();
    }

    public void jump(MenuItem item) {
        Intent intent = new Intent();
        intent.setClass(ManageController.this, EditController.class);
        startActivity(intent);
    }

    // 退出登录
    public void LoginModule_logout(MenuItem item) {
        UserModel.getInstance().setToken(null);
        Intent intent = new Intent();
        intent.setClass(ManageController.this, LoginModuleApi.getInstance().getManageLogoutClass());
        startActivity(intent);
        this.finish();
        Toast.makeText(ManageController.this, "退出登录", Toast.LENGTH_SHORT);
    }

    // 长按菜单响应函数
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int pos = (int) info.position;// 这里的info.id对应的就是数据库中_id的值
        HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(pos);
        String username = map.get("username");
        int flag = 0;
        switch (item.getItemId()) {
            case 0:
                //删除操作
                flag = userService.delete(username);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        if (flag == 1){//删除成功
            items.remove(pos);
            if (!simpleAdapter.isEmpty()) {
                simpleAdapter.notifyDataSetChanged(); // 实现数据的实时刷新
            }
            Toast.makeText(ManageController.this,"删除成功", Toast.LENGTH_SHORT)
                    .show();
        }
        else if(flag == 411){
            Toast.makeText(ManageController.this, "系统删除失败", Toast.LENGTH_SHORT)
                    .show();
        }
        return super.onContextItemSelected(item);
    }
}




