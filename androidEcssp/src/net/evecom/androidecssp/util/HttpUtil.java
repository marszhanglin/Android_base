/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.util;

import net.evecom.androidecssp.bean.HttpInfo;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 2014-5-28 ����9:05:27 HttpUtil������
 * 
 * @author Mars zhang
 * 
 */
public class HttpUtil {  
	
    /**BASE_URL*/
    public static final String BASE_URL = "http://192.168.3.100:8080/gssms/"; 
    /** BASE_PC_URL ForWebView*/ 
    public static final String BASE_PC_URL = "http://192.168.3.100:8080/gssms/";
    /** UPDATE_VERSION_XML */
    public static final String UPDATE_VERSION_XML = "gssms_update_android_version.xml";
    /** �ָ��� */
    public static String DELIMITER = "@_2_";  
    
    /**
     * ��ȡ�б�����   Ĭ��Ϊ15
     * @param context
     * @return
     */
    public static String getPageSize(Context context) {
        if (null == context) {
            return "15";
        }
        SharedPreferences sp = context.getSharedPreferences("PageSize", context.MODE_PRIVATE);
        return sp.getString("pagesize", "15");
    } 
    
    /**
     * ��ȡwebip·��
     * @return
     */
    public static String getPCURL(Context context){
    	if(null==context){
    		return "";
    	} 
    	HttpInfo httpInfo= HttpInfo.getInstance();;
    	if(null!=httpInfo){
    		return "http://"+httpInfo.getIp().trim()+":"
    				+httpInfo.getPort()+"/"+httpInfo.getProjectName()+"/";
    	}else{
    		return "";
    	}
    	
    }
 
    /**
     * ��ȡip·��
     * @return
     */
    public static String getBaseURL(){
    	
    	
    	
    	return "";
    } 
    
/*    private static HttpInfo getHttpInfo(Context context){
    	Properties properties=new Properties();
    	HttpInfo httpInfo=new HttpInfo();
    	InputStream is=HttpUtil.class.getClassLoader().getResourceAsStream("HttpInfo.properties");
    	try {
			properties.load(is);
			httpInfo.setIp(properties.getProperty("IP"));
			httpInfo.setPort(new Integer(properties.getProperty("PORT")));
			httpInfo.setProjectName(properties.getProperty("PROJECT"));
		} catch (IOException e) {
			Log.e("mars", "��ȡHttpInfo.properties������"+e.getMessage());
		}
    	return httpInfo;
    }*/
    
    
}