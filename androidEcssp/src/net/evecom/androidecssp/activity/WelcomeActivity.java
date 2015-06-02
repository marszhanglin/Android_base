package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.regex.Pattern;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.ICallback;
import net.evecom.androidecssp.util.HttpUtil;
import net.evecom.androidecssp.util.PhoneUtil;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.util.entryption.EncryptUtil;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class WelcomeActivity extends BaseActivity {
    /** 用户名EditText */
    private EditText userNmaeEditText;
    /** 密码EditText */
    private EditText passwordEditText;
    /** 记住密码 */
    private CheckBox jzmmCheckBox;
    /** 自动登入 */
    private CheckBox zddrCheckBox;
    /** SharedPreferences */
    private SharedPreferences passnameSp;
    /** login进度条 */
    private ProgressDialog loginProgressDialog = null;
    /** loginResult  **/
    private String loginResult="";
    /** 数据状态  */
    private static final int MESSAGETYPE_01 = 0x0001; 
    /** 数据状态 */
    private static final int MESSAGETYPE_02 = 0x0002;
    /** 数据状态 */
    private static final int MESSAGETYPE_03 = 0x0003;
    /** 数据状态 */
    private static final int MESSAGETYPE_04 = 0x0004;
    /** 数据状态 */
    private static final int MESSAGETYPE_05 = 0x0005;
    /** 数据状态 */
    private static final int MESSAGETYPE_06 = 0x0006;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.welcome); 
		passnameSp=this.getSharedPreferences("PASSNAME", 0);
		initView();
	}
	
	/**
	 * 界面初始化 <br>获取所有有用控件byid <br>是否自动登陆操作
	 */
	private void initView(){
		findbyId();
		iflogin(); 
	}  
	/**
	 * 获取所有有用控件byid
	 */
	private void findbyId() {
		userNmaeEditText=(EditText) findViewById(R.id.welcome_user_edit);
		passwordEditText=(EditText) findViewById(R.id.welcome_password_edit);
		jzmmCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_jzmm);
        zddrCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_zddr);
        listener();
	} 
	/**
	 * 控件监听
	 */
	private void listener() {
		//记住密码点击
		jzmmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				Editor editor=passnameSp.edit();
				editor.putString("rembernp", "0"); 
				if(isChecked){
					editor.putString("rembernp", "1");
				}else{
					editor.putString("rembernp", "0");
				}
				editor.commit();
			}
		});
		//自动登陆点击
		zddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				Editor editor=passnameSp.edit();
				editor.putString("autologin", "0"); 
				if(isChecked){
					editor.putString("autologin", "1");
				}else{
					editor.putString("autologin", "0");
				}
				editor.commit();
			}
		});
	}

	private void iflogin() {
		String autologin=ShareUtil.getString(getApplicationContext(), "PASSNAME", "autologin", "0");
		String rembernp=ShareUtil.getString(getApplicationContext(), "PASSNAME", "rembernp", "0");
		String username=ShareUtil.getString(getApplicationContext(), "PASSNAME", "username", "");
		String password=ShareUtil.getString(getApplicationContext(), "PASSNAME", "password", "");
		//给EditText赋值  
		userNmaeEditText.setText(username);
		passwordEditText.setText(password); 
		//判断是否打钩
		if(autologin.equals("1")){
			zddrCheckBox.setChecked(true);
		}else{
			zddrCheckBox.setChecked(false);
		}
		if(rembernp.equals("1")){
			jzmmCheckBox.setChecked(true);
		}else{
			jzmmCheckBox.setChecked(false);
		}
		//记住密码且自动登陆时提及登陆请求
		if(autologin.equals("1")&&rembernp.equals("1")){
			Log.v("mars", "自动登入");
			loginsubmit(username,password);
		} 
	}
	
	/**
	 * 登陆请求
	 */
	private void loginsubmit(final String username,final String password) {
		//打开进度条
		loginProgressDialog = ProgressDialog.show(this, "提示", "正在登入，请稍等...");
		loginProgressDialog.setCancelable(true);
//		Log.v("mars", EncryptUtil.getInstance().AESencode(password.trim()));//加密
		
		
		
		
		//打开线程
		 new Thread(new Runnable() {
			@Override
			public void run() {
				Log.v("mars", EncryptUtil.getInstance().AESencode(password.trim()));
				try {
					//登陆线程通信实体  在线程中创建
					Message loginMessage=new Message();
					
					loginResult = connServerForResultPost(HttpUtil.getPCURL(getApplicationContext())
							+"jfs/mobile/androidIndex/login",
							"username="+username
							+"&password="+password.trim()
							+"&imei="+PhoneUtil.getInstance().getImei(getApplicationContext()));
					//如果接受到返回数据 否则消息提示
					if(loginResult.length()>5){
						//如果登陆成功跳转  否则消息提示
						if(loginResult.substring(0, 5).equals("true@")){
							loginMessage.what=MESSAGETYPE_01;//成功登陆
						}else{
							loginMessage.what=MESSAGETYPE_02;//登陆失败 有接收到数据
						}
					}else{
						loginMessage.what=MESSAGETYPE_03;//登陆失败  没有接收到数据
					}
					loginRequestHandler.sendMessage(loginMessage);
					
					Log.v("mars", "WelcomeActivity@loginResult:"+loginResult+PhoneUtil.getInstance().getImei(getApplicationContext()));
				} catch (ClientProtocolException e) {
					Log.v("mars", "WelcomeActivity:"+e.getMessage());
				} catch (IOException e) {
					Log.v("mars", "WelcomeActivity:"+e.getMessage());
				}
			}
		}).start(); 
	}

	/**
	 * 异步登陆请求GUI更新
	 */
	private  Handler loginRequestHandler=new Handler(){
		//重写handlerMessage  
		@Override
		public void handleMessage(Message msg) {
			Editor editor= passnameSp.edit();
			switch (msg.what) {
			case MESSAGETYPE_01:
				//关闭登录进度条
				if (null != loginProgressDialog) {
					loginProgressDialog.dismiss();
                }
				//存储数据--用户名，密码
				String username=userNmaeEditText.getText().toString();
				String password=passwordEditText.getText().toString();
				editor.putString("username", username);
				editor.putString("password", password);
				
				//分割存储--user org
				String[] loginResults = Pattern.compile(HttpUtil.DELIMITER).split(loginResult);
				editor.putString("userid", loginResults[1]); 
				editor.putString("usernameCN", loginResults[2]); 
				editor.putString("sex", loginResults[3]); 
				editor.putString("mobile_In_clound", loginResults[4]); 
				editor.putString("orgid", loginResults[5]); 
				editor.putString("orgname", loginResults[6]);  
				
				
				Intent intent = new Intent();
			    intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
			    startActivity(intent);
			    WelcomeActivity.this.finish();
				editor.commit(); 
				Log.v("mars", "成功登陆");
				break;
			case MESSAGETYPE_02:
				//关闭登录进度条
				if (null != loginProgressDialog) {
					loginProgressDialog.dismiss();
                }
				DialogToast(loginResult, new ICallback() { 
					@Override
					public Object execute() { 
						loginResult="";
						toast("ok", 1);
						return null;
					}
				});
				Log.v("mars", "登陆失败 有接收到数据");
				break;
			case MESSAGETYPE_03:
				//关闭登录进度条
				if (null != loginProgressDialog) {
					loginProgressDialog.dismiss();
                }
				DialogToast(loginResult, new ICallback() { 
					@Override
					public Object execute() { 
						loginResult="";
						toast("ok", 1);
						return null;
					}
				});
				Log.v("mars", "登陆失败  没有接收到数据");
				break;
			default:
				break;
			}
		}
		
	} ;
	
	/**
	 * 登陆点击
	 * @param view
	 */
	public void welcomelogin(View view) {
		String username=userNmaeEditText.getText().toString();
		String password=passwordEditText.getText().toString();
		loginsubmit(username,password);
	}
	

	
	
	
}
