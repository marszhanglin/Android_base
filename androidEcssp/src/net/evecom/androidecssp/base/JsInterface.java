package net.evecom.androidecssp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.webkit.JavascriptInterface;
/**
 * 
 * 描述 android 4.2 之后版本提供给js调用的函数必须带有注释语句@JavascriptInterface 
 * @author Mars zhang
 * @created 2015-11-5 下午3:46:45
 */
public final class JsInterface {
    public Context context;
    
    public JsInterface(Context context) {
        this.context = context;
    }


    @JavascriptInterface
    public String show(final String strmsg){
        System.out.println(strmsg); 
        return strmsg;
    } 
    
    @JavascriptInterface
    public String finish(final String strmsg){
        System.out.println(strmsg);
        new Handler().post(new Runnable() { //UI线程
            @Override
            public void run() {
                BaseWebActivity.instance.finish();
            }
        });
        return strmsg;
    } 
    
    
    @JavascriptInterface
    public void saveToSP(final String fileName ,final String key ,final String value ){
         SharedPreferences sp = BaseWebActivity.instance.getSharedPreferences(fileName, Context.MODE_PRIVATE);
         Editor editor= sp.edit();
         editor.putString(key, value);
         editor.commit();
    } 

}
