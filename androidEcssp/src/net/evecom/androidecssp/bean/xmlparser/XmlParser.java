/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.bean.xmlparser;

import java.io.IOException;
import java.io.InputStream;

import net.evecom.androidecssp.bean.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 
 * 2014-7-22下午4:22:38 类XmlParser
 * 
 * @author Mars zhang
 * 
 */
public class XmlParser {
    /**
     * 解析服务器返回的输入流获取xml文件里的版本内容
     * 
     * @throws XmlPullParserException
     * @throws IOException
     * */
    public static UpdateInfo getUpdateInfo(InputStream is) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        int type = parser.getEventType(); // 得到事件类型
        UpdateInfo updateInfo = new UpdateInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            type = parser.next();
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        updateInfo.setVersion(parser.nextText());
                    } else if ("description".equals(parser.getName())) {
                        updateInfo.setDescription(parser.nextText());
                    } else if ("apkurl".equals(parser.getName())) {
                        updateInfo.setApkurl(parser.nextText());
                    }
                    break;
                default:
                    break;
            }

        }
        is.close();
        return updateInfo;
    }
}
