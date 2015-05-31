package net.evecom.androidecssp.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

import net.evecom.androidecssp.util.HttpUtil;

public class HttpInfo { 
	/** ��������IP��ַ */
	private String ip;
	/** �������˶˿� */
	private int port;
	/** ����������Ŀ���� */
	private String projectName;
	/** �Լ����� */
	private static HttpInfo me;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * ��ȡ��������HttpInfo
	 * @return
	 */
	public static HttpInfo getInstance(){
		if(null==me){
			me=new HttpInfo();
		}
		return me;
	}
	/**
	 * ���캯��   ����Ա�����и�ֵ
	 */
	public HttpInfo() { 
		Properties properties=new Properties();
		InputStream is=HttpUtil.class.getClassLoader().getResourceAsStream("HttpInfo.properties");
		try {
			properties.load(is);  
			setIp(properties.getProperty("IP"));
			setPort(new Integer(properties.getProperty("PORT")));
			setProjectName(properties.getProperty("PROJECT"));
			Log.v("mars", "ִ��HttpInfo�������캯��");
		} catch (IOException e) {
			Log.e("mars", "��ȡHttpInfo.properties����"+e.getMessage());
		} 
	}
	
}
