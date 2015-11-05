package net.evecom.androidecssp.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.util.HttpUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
/**
 *  基础的Activity类
 * 2015-2-15下午7:21:37 类BaseActivity
 * <br>Log.e("mars:EventListActivity.initlist()", e.getMessage());
 * @author Mars zhang
 *
 */
public class BaseActivity extends Activity {
    /** 数据状态  */
    protected static final int MESSAGETYPE_01 = 0x0001; 
    /** 数据状态 */
    protected static final int MESSAGETYPE_02 = 0x0002;
    /** 数据状态 */
    protected static final int MESSAGETYPE_03 = 0x0003;
    /** 数据状态 */
    protected static final int MESSAGETYPE_04 = 0x0004;
    /** 数据状态 */
    protected static final int MESSAGETYPE_05 = 0x0005;
    /** 数据状态 */
    protected static final int MESSAGETYPE_06 = 0x0006;
    /** 自定义交互数据集 */
    public static HashMap<Long, Object> contextHashMap = new HashMap<Long, Object>();
    /** 实体对象 */
    public static BaseActivity instance = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//键盘总是隐藏
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//不要标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		instance=this;
	}
	
	
	
	
	
	/**
     * 错误填报提示信息
     * 
     * @param errorMsg
     */
    protected void DialogToast(String errorMsg,final ICallback callback) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("提示信息");
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// 图标
        builder1.setMessage("" + errorMsg);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
            	if(null!=callback){
            		callback.execute();
            	}
            }
        });
        builder1.show();
    }
    
	/**
     * 错误填报提示信息
     * 
     * @param errorMsg
     */
    protected void DialogToastNoCall(String errorMsg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("提示信息");
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// 图标
        builder1.setMessage("" + errorMsg);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        builder1.show();
    }
    
    
    /** 土司 */
    protected void toast(String strMsg, int L1S0) {
        Toast.makeText(getApplicationContext(), strMsg, L1S0).show();
    }
    
    
    
    
    /**
     * 
     * @param http  get请求
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */ 
    protected String connServerForResult(String strUrl) throws Exception {
        // HttpGet对象
        HttpGet httpRequest = new HttpGet(strUrl);
        String strResult = "";
        // HttpClient对象
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);// 设置超时时间
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 取得返回的数据
            strResult = EntityUtils.toString(httpResponse.getEntity());
        }

        return strResult;
    }
    
    /**
     * 
     * @param strUrl   jfs/mobile/androidIndex/login
     * @param entity_str   username=sysadmin&password=D860103725C09C63BFDFB0D6962EC1AB&imei=null
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    protected String connServerForResultPost(String strUrl, HashMap<String, String> entityMap) throws ClientProtocolException,
            IOException {
        String strResult = "";
        URL url = new URL(HttpUtil.getPCURL() +strUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        StringBuilder entitySb = new StringBuilder("");
        if(null==entityMap){
            entityMap = new HashMap<String, String>();
        } 
        Object[] entityKeys = entityMap.keySet().toArray();
        for(int i=0;i<entityKeys.length;i++){
            String key=(String) entityKeys[i];
            if(i==0){
                entitySb.append(key+"="+entityMap.get(key));
            }
            else{
                entitySb.append("&"+key+"="+entityMap.get(key));
            }
        }
        byte[] entity = entitySb.toString().getBytes();
        
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
        conn.getOutputStream().write(entity);
        if (conn.getResponseCode() == 200) {
            InputStream inputstream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = inputstream.read(b)) != -1;) {
                buffer.append(new String(b, 0, n));
            }
            strResult = buffer.toString();
        }
        return strResult;
    } 
    
    public void fh(View view){
    	this.finish();
    }
    
    
    /**
     * 解析json对象数据 成baseModel
     * 
     */
    public static BaseModel getObjInfo(String jsonString)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        BaseModel baseModel = new BaseModel();
        Iterator<String> iterators= jsonObject.keys();
        for (int j = 0; iterators.hasNext(); j++) {
          String key= iterators.next();
          baseModel.set(key, jsonObject.get(key));
         }
        return baseModel;
    }
    
    
     
    
    /**
     * 解析 json数组据 成baseModel
     * 
     */
    public static List<BaseModel> getObjsInfo(String jsonString) throws JSONException {
        List<BaseModel> list = new ArrayList<BaseModel>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            BaseModel baseModel = new BaseModel();
           Iterator<String> iterators= jsonObject.keys();
           for (int j = 0; iterators.hasNext(); j++) {
              String key= iterators.next();
              baseModel.set(key, jsonObject.get(key));
           }
            list.add(baseModel);
        }
        return list;
    }
    /**
     *  填充数据
     * 
     */
    public static   Intent pushData(String string,BaseModel baseModel,Intent intent){
        Long key = SystemClock.elapsedRealtime();
        intent.putExtra(string, key);
        contextHashMap.put(key, baseModel);
        return intent;
    }
    
    /**
     *  获取数据
     * 
     */
    public static Object getData(String string, Intent intent) {
        Long key = intent.getLongExtra(string, 0L);
        return contextHashMap.get(key);
    }
    
    /**
     * 要加动画过度动画  所以要重写finish方法
     */ 
    @Override
    public void finish(){
    	super.finish();
    	overridePendingTransition(R.anim.activity_in_heart , R.anim.activity_out_heart);
    } 
    
    /**
     * 要加动画过度动画  所以要重写startActivity方法
     */ 
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.activity_in_heart , R.anim.activity_out_heart);
	}  
}
