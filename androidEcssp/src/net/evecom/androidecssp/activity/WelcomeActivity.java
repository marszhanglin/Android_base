package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.ICallback;
import net.evecom.androidecssp.bean.SysDictBean;
import net.evecom.androidecssp.util.HttpUtil;
import net.evecom.androidecssp.util.PhoneUtil;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.util.UiUtil;
import net.evecom.androidecssp.util.entryption.EncryptUtil;
import net.tsz.afinal.FinalDb;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;

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
    /** dictResult  **/
    private String dictResult="";
    /** 是否真正登入 防止重复提交 **/
    private Boolean islogining=false;
    /** 是否需要调整定位设置 */
    private Boolean isNeedGpsSet=false;
    /** 定位客户端 */
    MyLocationOverlay mMyLocation = null;
    /** 当前坐标   */
    private Location  currentLocation= null;
    /** 定位管理器 */
    LocationManager locationManager=null;
    /** 数据库 */
    FinalDb db=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.welcome_at); 
		passnameSp=this.getSharedPreferences("PASSNAME", 0);
		
		initdata();
		
		askForOpenGPS();
		
		initView();
		
		
	}
	/**
	 * app数据初始化
	 */
	private void initdata() {
		db=FinalDb.create(this);
		List<SysDictBean> list=db.findAll(SysDictBean.class);
		//数据库有数据时不请求
		if(null!=list&&list.size()>0){
			return ;
		}
		db.deleteAll(SysDictBean.class);
		//获取数据字典数据
		
		getDictSave2SqlData();
	}
	
	/**
	 * 获取数据字典数据并保存至数据库
	 */
	private void getDictSave2SqlData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try { 
					dictResult=connServerForResultPost("jfs/mobile/androidIndex/gitSysDicts", null);
				} catch (ClientProtocolException e) {
					Log.e("mars", "数据字典获取："+e.getMessage());
				} catch (IOException e) {
					Log.e("mars", "数据字典获取："+e.getMessage());
				} finally{
					try {
						putString2Map(dictResult);
					} catch (JSONException e) {
						Log.e("mars", "数据字典json解析："+e.getMessage());
					}
				}
			}
		}).start();
	}
	
	
	/**
	 * 将数据字典str转成map
	 * @param val
	 * @return
	 * @throws JSONException 
	 */
	private void putString2Map(String val) throws JSONException{
		if(null==val||val.length()<1){
			return ;
		}
		long s=System.currentTimeMillis();
		JSONObject jsonObject=new JSONObject(val);
		Iterator< String> dickKeyIterator=jsonObject.keys();
		while(dickKeyIterator.hasNext()){
			String key=dickKeyIterator.next();
			SysDictBean dictBean=new SysDictBean();
			dictBean.setIdkey(key);
			dictBean.setValue(jsonObject.get(key).toString());
			db.save(dictBean);
//			dictBean=null;
		}
		Log.v("mars", (System.currentTimeMillis()-s)/1000+"秒");
	}
	
    /**
     * 请求打开gps
     */
    private void askForOpenGPS() {
        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(),
                LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) { 
        	isNeedGpsSet=true;
            toast("请点击定位服务,并打开GPS卫星选项,否则定位无法正常工作!", 1);
            Intent intent = new Intent(Settings.ACTION_SETTINGS); 
            startActivityForResult(intent, 0); 
        }
    }
    
	

	/**
	 * GIS操作
	 */
	private void manageGis() {
		//天地图描点   实现系统定位回调接口并处理了坐标数据
        mMyLocation = new MyOverlay(this, null); 
        //定位管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	toast("开始定位", 0);
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        			1000, 1, mMyLocation);
        }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,  
			    	1000, 1, mMyLocation); 
			toast("开始基站定位", 0);
		}else{
        	toast("定位未打开", 0);
        }
	} 
	 /**
     * 天地图
     */
    class MyOverlay extends MyLocationOverlay {
        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView); 
        } 
        /*
         * 处理在"我的位置"上的点击事件
         */
        protected boolean dispatchTap() { 
            return true;
        } 
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            super.onLocationChanged(location);
            if (location != null) {
                currentLocation = location;
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // 存入数据
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
                Log.v("", location.getLatitude()+"-----"+location.getLongitude());
                toast(location.getLatitude()+"-----"+location.getLongitude(), 0);
            } 
            GeoPoint point = mMyLocation.getMyLocation(); 
        }

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
		if(autologin.equals("1")&&rembernp.equals("1")&&!isNeedGpsSet){
			Log.v("mars", "自动登入");
			loginsubmit(username,password);
		} 
	}
	
	/**
	 * 登陆请求
	 */
	private void loginsubmit(final String username,final String password) {
		if(islogining){
			return ;
		}
		
		/** 保存登录名 */
		final Editor editor= passnameSp.edit();
        editor.putString("username", username);
        editor.putString("password", password);
		editor.commit();
		
		//打开进度条
		loginProgressDialog = ProgressDialog.show(this, "提示", "正在登入，请稍等...");
		loginProgressDialog.setCancelable(true);
		
		
		
		
		//打开线程
		 new Thread(new Runnable() {
			@Override
			public void run() {
				Log.v("mars", EncryptUtil.getInstance().AESencode(password.trim()));
				try {
					//登陆线程通信实体  在线程中创建
					Message loginMessage=new Message();
					HashMap<String, String> hashMap=new HashMap<String, String>();
			        hashMap.put("pwd",password.trim());
					loginResult = connServerForResultPost("jfs/ecssp/mobile/accessCtr/login",
							hashMap);
					
					//如果接受到返回数据 否则消息提示
					if(loginResult.length()>0){
					    try {
                            BaseModel resultObj=getObjInfo(loginResult);
                            Boolean success=resultObj.get("sys_success");
                            if(null!=success&&success){
                                loginMessage.what=MESSAGETYPE_01;//成功登陆 
                                BaseModel organization=getObjInfo(resultObj.get("organization").toString());
                                BaseModel userdata=getObjInfo(resultObj.get("userdata").toString());
                                BaseModel userInfo=getObjInfo(resultObj.get("userInfo").toString());
                                String code=resultObj.get("code").toString();
                                        //存储数据--用户名，密码
                                        String username=userNmaeEditText.getText().toString();
                                        editor.putString("username", userdata.getStr("loginname"));
                                        editor.putString("userid", userdata.getStr("id")); 
                                        editor.putString("usernameCN", userInfo.getStr("name")); 
                                        editor.putString("sex", userInfo.get("sex")+""); 
                                        editor.putString("mobile_In_clound", userInfo.get("mobile")+""); 
                                        editor.putString("orgid", organization.getStr("id")); 
                                        editor.putString("orgname",organization.getStr("name"));  
                                        editor.putString("code",code);
                                        editor.commit();  
                            } else{
                                loginMessage.what=MESSAGETYPE_02;//登陆失败 有接收到数据
                            }
                        } catch (JSONException e) {
                            loginMessage.what=MESSAGETYPE_02;//登陆失败 有接收到数据
                        } 
					}else{
						loginMessage.what=MESSAGETYPE_03;//登陆失败  没有接收到数据
					}
					loginRequestHandler.sendMessage(loginMessage);
					
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
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
			final Editor editor= passnameSp.edit();
			switch (msg.what) {
			case MESSAGETYPE_01:
				//关闭登录进度条
				if (null != loginProgressDialog) {
					loginProgressDialog.dismiss();
                }
				
				Intent intent = new Intent();
			    intent.setClass(WelcomeActivity.this, MainMenuActivity.class);
			    startActivity(intent);
			    WelcomeActivity.this.finish();
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
				DialogToast("登陆失败,请重新登录！", new ICallback() { 
					@Override
					public Object execute() { 
						loginResult="";
//						toast("ok", 1);
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
		
		/*Intent intent=new Intent(getApplicationContext(), TDTLocation222.class);
		startActivity(intent);*/
	}
	

	@Override
	protected void onPause() {
		if (null != mMyLocation) {
            locationManager.removeUpdates(mMyLocation);
        }
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case 0:
			manageGis();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	
	public void test(View view){
		toast(UiUtil.getResolution(getWindowManager()), 1);
	}
}
