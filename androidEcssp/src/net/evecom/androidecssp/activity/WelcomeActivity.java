package net.evecom.androidecssp.activity;

import java.io.IOException;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class WelcomeActivity extends BaseActivity {
    /** �û���EditText */
    private EditText userNmaeEditText;
    /** ����EditText */
    private EditText passwordEditText;
    /** ��ס���� */
    private CheckBox jzmmCheckBox;
    /** �Զ����� */
    private CheckBox zddrCheckBox;
    /** SharedPreferences */
    private SharedPreferences passnameSp;
    /** login������ */
    private ProgressDialog loginProgressDialog = null;
    /** loginResult  **/
    private String loginResult="";
    /** ����״̬  */
    private static final int MESSAGETYPE_01 = 0x0001; 
    /** ����״̬ */
    private static final int MESSAGETYPE_02 = 0x0002;
    /** ����״̬ */
    private static final int MESSAGETYPE_03 = 0x0003;
    /** ����״̬ */
    private static final int MESSAGETYPE_04 = 0x0004;
    /** ����״̬ */
    private static final int MESSAGETYPE_05 = 0x0005;
    /** ����״̬ */
    private static final int MESSAGETYPE_06 = 0x0006;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.welcome); 
		passnameSp=this.getSharedPreferences("PASSNAME", 0);
		initView();
	}
	
	/**
	 * �����ʼ�� <br>��ȡ�������ÿؼ�byid <br>�Ƿ��Զ���½����
	 */
	private void initView(){
		findbyId();
		iflogin();
		
		Log.v("mars", "�Զ�����");
	}  
	/**
	 * ��ȡ�������ÿؼ�byid
	 */
	private void findbyId() {
		userNmaeEditText=(EditText) findViewById(R.id.welcome_user_edit);
		passwordEditText=(EditText) findViewById(R.id.welcome_password_edit);
		jzmmCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_jzmm);
        zddrCheckBox = (CheckBox) findViewById(R.id.welcom_checkbox_zddr);
        listener();
	} 
	/**
	 * �ؼ�����
	 */
	private void listener() {
		//��ס������
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
		//�Զ���½���
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
		//��EditText��ֵ  
		userNmaeEditText.setText(username);
		passwordEditText.setText(password); 
		//�ж��Ƿ��
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
		//��ס�������Զ���½ʱ�ἰ��½����
		if(autologin.equals("1")&&rembernp.equals("1")){
			loginsubmit(username,password);
		} 
	}
	
	/**
	 * ��½����
	 */
	private void loginsubmit(final String username,final String password) {
		//�򿪽�����
		loginProgressDialog = ProgressDialog.show(this, "��ʾ", "���ڵ��룬���Ե�...");
		loginProgressDialog.setCancelable(true);
		Log.v("mars", EncryptUtil.getInstance().AESencode(password.trim()));
		//���߳�
		 new Thread(new Runnable() { 
			@Override
			public void run() {
				Log.v("mars", EncryptUtil.getInstance().AESencode(password.trim()));
				try {
					loginResult = connServerForResultPost(HttpUtil.getPCURL(getApplicationContext())
							+"jfs/mobile/androidIndex/login",
							"username="+username
							+"&password="+EncryptUtil.getInstance().AESencode(password.trim())
							+"&imei="+PhoneUtil.getInstance().getImei(getApplicationContext()));
					//������ܵ��������� ������Ϣ��ʾ
					if(loginResult.length()>5){
						//�����½�ɹ���ת  ������Ϣ��ʾ
						if(loginResult.substring(0, 5).equals("true@")){
							
						}else{
							
						}
					}else{
						
					}
					
					
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
	 * ��½���
	 * @param view
	 */
	public void welcomelogin(View view) { 
		String username=userNmaeEditText.getText().toString();
		String password=passwordEditText.getText().toString();
		loginsubmit(username,password);
		Intent intent = new Intent();
	    intent.setClass(this, MainMenuActivity.class);
	    startActivity(intent);
	    this.finish();
	}
	

	
	
	
}
