/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity;

import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.HandlerView;
import net.evecom.androidecssp.bean.Picture;
import net.evecom.androidecssp.util.HttpUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * MainOneActivity
 * 
 * @author Mars zhang
 * 
 */
public class MainMenuActivity extends BaseActivity {
    /** MemberVariables */
    private boolean menu_display = false;
    /** MemberVariables */
    private GridView gridView; 
    /** MemberVariables */
    private PopupWindow menuWindow; 
    /** MainMenuActivity实例 */
    public static MainMenuActivity mainMenuActivityInstance = null; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);  
        mainMenuActivityInstance = this; 
        // 换成gridView
        gridView = (GridView) findViewById(R.id.main_tab_weixin_gridview);
        int[] images = new int[] { R.drawable.wgh_main_ptxx2, R.drawable.wgh_main_sjdj2, R.drawable.wgh_main_rcbg2,
                R.drawable.wgh_main_jcsj2, R.drawable.wgh_main_zygz3, R.drawable.wgh_main_xtfx2,
                R.drawable.wgh_main_xtsz2, R.drawable.wgh_main_tcxt2 };
        // 平台消息、事件登记、日常办公、基础数据、专业工作、统计分析、系统设置、退出系统
        String[] itemtitles = new String[] { "消息公告", "预警提醒", "隐患排查", "执法检查"
                , "资源检索", "应急管理", "定位管理", "系统设置", "退出系统" };
        PictureAdapter pictureAdapter = new PictureAdapter(itemtitles, images, MainMenuActivity.this);
        gridView.setAdapter(pictureAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;
                switch (arg2) {
                    case 0: // 消息公告 
//                        intent = new Intent(MainMenuActivity.this, Web0Activity.class);
//                        startActivity(intent);
                    	intent=new Intent(getApplicationContext(), EventListActivity.class);
                    	startActivity(intent);
                        break;
                    case 1: // 事件登记
                    	Log.e("mars", "bb");
//                        intent = new Intent(MainMenuActivity.this, EventAddActivity.class);
//                        startActivity(intent);
                        break;
                    case 2: // 隐患排查
//                        intent = new Intent(MainMenuActivity.this, Web2Activity.class);
//                        startActivity(intent);
                        break;
                    case 3: //
//                        intent = new Intent(MainMenuActivity.this, Web3Activity.class);
//                        startActivity(intent);
                        break;
                    case 4:
//                        intent = new Intent(MainMenuActivity.this, DailyWorkActivity.class);
//                        startActivity(intent);
                        break;
                    case 5:
//                        intent = new Intent(MainMenuActivity.this, Web5Activity.class);
//                        startActivity(intent);
                        break;
                    case 6:// 系统设置
//                        intent = new Intent(MainMenuActivity.this, SystemSetingActivity.class);
//                        startActivity(intent);
                        break;
                    case 7:
                        if (menu_display) { // 如果 Menu已经打开 ，先关闭Menu
                            menuWindow.dismiss();
                            menu_display = false;
                        } else { 
//                            mainMenuActivityInstance = MainMenuActivity.this;
//                            intent = new Intent(MainMenuActivity.this, Exit.class);
//                            startActivity(intent);
                        }
                        break; 
                    default:
                        break;
                }
            }
        });  

    } 

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // back键 
            if (menu_display) { // 如果 Menu已经打开 ，先关闭Menu
                menuWindow.dismiss();
                menu_display = false;
            } else { 
//                Intent intent = new Intent();
//                intent.setClass(MainMenuActivity.this, Exit.class);
//                startActivity(intent);

            }
        }
        return false;
    }

     /**
     * 退出按钮
     * 
     * @param v
     */ 
    public void exit_settings(View v) { // 退出 伪“对话框”，其实是一个activity
//        Intent intent = new Intent(MainMenuActivity.this, ExitFromSettings.class);
//        startActivity(intent);
    }

     /**
     * 代办事件点击
     * 
     * @param v
     */ 
    public void toweb2(View v) {
//        Intent intent = new Intent(getApplicationContext(), WebdbActivity.class);
//        startActivity(intent);
    }

    /**
     * 返回按钮点击事件
     * 
     * @param v
     */ 
    public void login_back(View v) { // 标题栏 返回按钮

        this.finish();
    }

     /**
     * 注册按钮事件
     * 
     * @param v
     */ 
    public void login_pw(View v) {  

    }

     /**
     * 
     * 2014-7-22下午4:52:37 类PictureAdapter
     * 
     * @author Mars zhang
     * 
     */ 
    public class PictureAdapter extends BaseAdapter {
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<Picture> pictures; 
        /** MemberVariables */
        public PictureAdapter(String[] titles, int[] images, Context context) {
            super();
            pictures = new ArrayList<Picture>();
            inflater = LayoutInflater.from(context);
            for (int i = 0; i < images.length; i++) {
                Picture picture = new Picture(titles[i], images[i]);
                pictures.add(picture);
            }
        } 
        @Override
        public int getCount() {
            if (pictures != null) {
                return pictures.size();
            } else {
                return 0;
            }
        } 
        @Override
        public Object getItem(int position) {

            return pictures.get(position);
        } 
        @Override
        public long getItemId(int position) {
            return position;
        } 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HandlerView handlerView;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gvitem, null);
                handlerView = new HandlerView();
                handlerView.imageView = (ImageView) convertView.findViewById(R.id.gvitem_imageview);
                convertView.setTag(handlerView);
            } else {

                handlerView = (HandlerView) convertView.getTag();
            }
            handlerView.imageView.setImageResource(pictures.get(position).getImageld());
            return convertView;
        }
    }

     /**
     * 街道选择
     * 
     * @param v
     */ 
    @SuppressLint("NewApi")
    public void jd_click(View v) { //
        // String pid = "0";
        // getTreeOne("");
    	// getTree();
    	
    } 
}  