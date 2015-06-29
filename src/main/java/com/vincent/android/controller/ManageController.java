package com.vincent.android.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.vincent.android.model.ManageViewModel;
import com.vincent.android.model.UserModel;
import com.vincent.android.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Feng on 2015-06-28.
 */
public class ManageController extends Activity {
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_layout);

        userService = new UserService(this);
        listView = (ListView)this.findViewById(R.id.manage_listview);

        //创建简单适配器
        simpleAdapter = new SimpleAdapter(this, this.getItems(), R.layout.user_item,
                new String[]{"username","mail", "avatar"},
                new int[]{R.id.user_username,R.id.user_mail, R.id.user_avatar});

        //加载SimpleAdapter到ListView中
        listView.setAdapter(simpleAdapter);
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if (view instanceof ImageView && o instanceof Bitmap) {
                    Bitmap bitmap = (Bitmap)o;
                    ((ImageView)view).setImageBitmap(bitmap);
                    return true;
                }
                return false;
            }
        });
    }

    public ArrayList<HashMap<String, Object>> getItems(){
        List<ManageViewModel> list = userService.getItemList();
        if(list == null){
            return  null;
        }
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < list.size();i++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("username", list.get(i).getUsername());
            map.put("mail", list.get(i).getMail());
            //将字节数组转化为位图
            Bitmap imagebitmap= BitmapFactory.decodeByteArray( list.get(i).getAvatar(), 0,  list.get(i).getAvatar().length);
            map.put("avatar",imagebitmap );
            items.add(map);
        }
        return  items;
    }
}




