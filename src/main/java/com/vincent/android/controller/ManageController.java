package com.vincent.android.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.vincent.android.model.ManageViewModel;
import com.vincent.android.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Feng on 2015-06-28.
 */
public class ManageController extends Activity {
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private UserService userService;
    private  ArrayList<HashMap<String, Object>> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_layout);

        userService = new UserService(this);
        listView = (ListView)this.findViewById(R.id.manage_listview);
        items = getItems();
        //创建简单适配器
        simpleAdapter = new SimpleAdapter(this, items, R.layout.user_item,
                new String[]{"username","mail", "avatar"},
                new int[]{R.id.user_username,R.id.user_mail, R.id.user_avatar});

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

    public ArrayList<HashMap<String, Object>> getItems(){
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

    // 长按菜单响应函数
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
        else if(flag == 313){
            Toast.makeText(ManageController.this, "系统删除失败", Toast.LENGTH_SHORT)
                    .show();
        }
        return super.onContextItemSelected(item);
    }
}




