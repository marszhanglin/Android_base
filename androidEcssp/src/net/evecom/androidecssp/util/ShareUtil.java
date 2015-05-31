/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 2014-5-28 上午9:05:27 ShareUtil工具类
 * 
 * @author Mars zhang
 * 
 */
public class ShareUtil {
    /**
     * SharedPreferences getString
     * 
     * @return if context is null ""
     */
    public static String getString(Context context, String xmlFileName, String key, String defValue) {
        if (null == context) {
            return "";
        }
        SharedPreferences sp = context.getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * SharedPreferences getBoolean
     * 
     * @return if context is null false
     */
    public static Boolean getBoolean(Context context, String xmlFileName, String key, Boolean defValue) {
        if (null == context) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * SharedPreferences getFloat
     * 
     * @return if context is null 0F
     * 
     */
    public static Float getFloat(Context context, String xmlFileName, String key, Float defValue) {
        if (null == context) {
            return 0F;
        }
        SharedPreferences sp = context.getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    /**
     * SharedPreferences getLong
     * 
     * @return if context is null 0L
     * 
     */
    public static Long getLong(Context context, String xmlFileName, String key, Long defValue) {
        if (null == context) {
            return 0L;
        }
        SharedPreferences sp = context.getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    /**
     * SharedPreferences getString
     * 
     * @return if context is null return null
     * 
     */
    @SuppressLint("NewApi")
    public static Set<String> getStringSet(Context context, String xmlFileName, String key, Set<String> defValue) {
        if (null == context) {
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(xmlFileName, Context.MODE_PRIVATE);
        return sp.getStringSet(key, defValue);
    }
}




/*SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
// 存入数据
Editor editor = sp.edit();
editor.putString("GPS_latitude", "" + locData.latitude);
editor.putString("GPS_longitude", "" + locData.longitude);
editor.commit();*/

/*SharedPreferences sp = context.getSharedPreferences("GPS", Context.MODE_PRIVATE);
 // 取出数据
sp.getString("GPS_latitude", "defValue");
sp.getString("GPS_longitude", "defValue");*/


