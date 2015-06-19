/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 2014-5-28 上午9:05:27 UiUtil工具类
 * 
 * @author Mars zhang
 * 
 */
public class UiUtil {
    /**
     * 
     * 动态设置ListView的高度 解决scrollView内嵌套listView不显示全部item
     * 一是Adapter中getView方法返回的View的必须由LinearLayout组成
     * ，因为只有LinearLayout才有measure()方法 在为ListView设置了Adapter之后使用 但是他的最大高度就是屏幕的高度
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    
    
    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWindowWidth(WindowManager wm){
    	Display display= wm.getDefaultDisplay();
    	
    	return display.getWidth();
    }
    
    /**
     * 获取屏幕宽度   通过矩阵的方式
     * @return
     */
    public static int getWindowWidthBymDisplayMetrics(WindowManager wm){
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(mDisplayMetrics); 
    	return mDisplayMetrics.widthPixels;
    }
    
    
    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWindowHeight(WindowManager wm){
    	Display display= wm.getDefaultDisplay();
    	
    	return display.getHeight();
    }
    
    /**
     * 获取屏幕宽度   通过矩阵的方式
     * @return
     */
    public static int getWindowHeightBymDisplayMetrics(WindowManager wm){
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(mDisplayMetrics); 
    	return mDisplayMetrics.heightPixels;
    }
    
    
    /***
     * 获取分辨率
     */
    public static  String getResolution(WindowManager wm){
    	return getWindowHeight(wm)+"-"+getWindowWidth(wm);
    }
}
