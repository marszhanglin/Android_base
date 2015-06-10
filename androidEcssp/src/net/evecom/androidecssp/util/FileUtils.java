/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 
 * 2014-7-22下午3:22:35 类FileUtils
 * 
 * @author Mars zhang
 * 
 */
public class FileUtils {
    /**
     * 2014-4-1
     */
    public static String getPath(Context context, Uri uri) {
        // uri的格式是： content://contacts/people
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
