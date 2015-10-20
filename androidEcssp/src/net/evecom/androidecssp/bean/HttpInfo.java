package net.evecom.androidecssp.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.evecom.androidecssp.util.HttpUtil;
import android.util.Log;

public class HttpInfo { 
	/** 服务器端IP地址 */
	private String ip;
	/** 服务器端端口 */
	private int port=0;
	/** 服务器端项目名称 */
	private String projectName;
	/** 存在服务器端最新版本信息xml文件名 */
	private String versionxml;
	/** 自己单例 */
	private static HttpInfo me;
	
	public String getVersionxml() {
		return versionxml;
	}
	public void setVersionxml(String versionxml) {
		this.versionxml = versionxml;
	}
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
	 * 获取单例对象HttpInfo
	 * @return
	 */
	public static HttpInfo getInstance(){
		if(null==me){
			me=new HttpInfo();
		}
		return me;
	}
	/**
	 * 构造函数   往成员变量中赋值
	 */
	public HttpInfo() { 
		Properties properties=new Properties();
		InputStream is=HttpUtil.class.getClassLoader().getResourceAsStream("HttpInfo.properties");
		try {
			properties.load(is);  
			setIp(properties.getProperty("IP"));
			setPort(new Integer(properties.getProperty("PORT")));
			setProjectName(properties.getProperty("PROJECT"));
			setVersionxml(properties.getProperty("VERSIONXML"));
			Log.v("mars", "执行HttpInfo单例构造函数");
		} catch (IOException e) {
			Log.e("mars", "获取HttpInfo.properties出错："+e.getMessage());
		} 
	}
	
}
