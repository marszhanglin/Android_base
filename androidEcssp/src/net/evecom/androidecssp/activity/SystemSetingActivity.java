/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.bean.UpdateInfo;
import net.evecom.androidecssp.bean.xmlparser.XmlParser;
import net.evecom.androidecssp.util.AppInfoUtil;
import net.evecom.androidecssp.util.HttpUtil;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.view.wheel.OnWheelChangedListener;
import net.evecom.androidecssp.view.wheel.OnWheelScrollListener;
import net.evecom.androidecssp.view.wheel.WheelView;
import net.evecom.androidecssp.view.wheel.adapter.AbstractWheelTextAdapter;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 2014-7-22下午3:48:02 类SystemSetingActivity
 * 
 * @author Mars zhang
 * 
 */
public class SystemSetingActivity extends Activity {
    /** 成员变量 */
    protected static final int LOAD_MAIN = 1;
    /** 成员变量 */
    protected static final int SHOW_UPDATA_DIALOG = 2;
    /** 成员变量 */
    protected static final int SERVER_URL_ERROR = 3;
    /** 成员变量 */
    protected static final int NETWORK_ERROR = 4;
    /** 成员变量 */
    protected static final int XML_PARSE_ERROR = 5;
    /** 成员变量 */
    protected static final int SERVER_ERROR = 6;
    /** updateInfo */
    private UpdateInfo updateInfo;
    /** 进度提示框 */
    private AlertDialog dialogPress;
    /** 记住密码 */
    private CheckBox jzmmCheckBox;
    /** 自动登入 */
    private CheckBox zddrCheckBox;
    /** xml */
    private SharedPreferences spPhone;
    /** 保证滚动条的流畅 */
    private boolean scrolling = false;
    /** 列表长度temp */
    private String temp = "15";
    /** GPS时长temp */
    private String gps_time_temp = "120";
    /** flow_size */
    private TextView flow_size = null; //
    /** flow_size_android */
    private TextView flow_size_android = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_seting);
        spPhone = getApplicationContext().getSharedPreferences("Phone", MODE_PRIVATE);

        flow_size = (TextView) findViewById(R.id.flow_size);
        long LJWG_Rx = (TrafficStats.getUidRxBytes(android.os.Process.myUid()) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getUidRxBytes(android.os.Process.myUid()) / 1024));
        long LJWG_Tx = (TrafficStats.getUidTxBytes(android.os.Process.myUid()) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getUidTxBytes(android.os.Process.myUid()) / 1024));
        long all = LJWG_Rx + LJWG_Tx + ShareUtil.getLong(getApplicationContext(), "FLOW", "LJWG", 0L);
        String s = "" + all / 1024.00;
        flow_size.setText(s.substring(0, s.indexOf(".") + 4) + "M");

        flow_size_android = (TextView) findViewById(R.id.flow_size_android);
        long ALL_Rx = (TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes() / 1024));
        long ALL_Tx = (TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalTxBytes() / 1024));
        long all_android = ALL_Rx + ALL_Tx + ShareUtil.getLong(getApplicationContext(), "FLOW", "ALL_WIFI_GPRS", 0L);
        String s2 = "" + all_android / 1024.00;
        flow_size_android.setText(s2.substring(0, s2.indexOf(".") + 4) + "M");


        jzmmCheckBox = (CheckBox) findViewById(R.id.main_tab_setting_checkbox_jzmm);
        zddrCheckBox = (CheckBox) findViewById(R.id.main_tab_setting_checkbox_zddr);
        if ("1".equals(spPhone.getString("isJZMM", "0"))) {
            jzmmCheckBox.setChecked(true);
        }
        if ("1".equals(spPhone.getString("isZDDR", "0"))) {
            zddrCheckBox.setChecked(true);
        }

        // 记住密码
        jzmmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Editor editor = spPhone.edit();
                if (isChecked) {
                    editor.putString("isJZMM", "1");
                } else {
                    editor.putString("isJZMM", "0");
                }
                editor.commit();
            }
        });
        // 自动登录
        zddrCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Editor editor = spPhone.edit();
                if (isChecked) {
                    editor.putString("isZDDR", "1");
                } else {
                    editor.putString("isZDDR", "0");
                }
                editor.commit();
            }
        });

        // 更新dialog注册
        dialogPress = new AlertDialog.Builder(this).setTitle("正在下载apk文件").setMessage("下载进度:0/0")
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        /**
         * 分页设置
         */
        final WheelView country = (WheelView) findViewById(R.id.country);
        ArrayList list = new ArrayList();
        for (int i = 10; i < 100; i++) {
            list.add(i + "");
        }
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this, list));
        country.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                // System.out.println("addScrollingListener");
            }

            public void onScrollingFinished(WheelView wheel) {
                // System.out.println("onScrollingFinished");
                SharedPreferences sp = getSharedPreferences("PageSize", MODE_PRIVATE);
                Editor editor = sp.edit();
                editor.putString("pagesize", temp);
                editor.commit();
                // System.out.println(temp+"----");
            }
        });
        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                temp = "" + (newValue + 10);
            }
        });
        SharedPreferences sp = getSharedPreferences("PageSize", MODE_PRIVATE);
        country.setCurrentItem(Integer.parseInt(sp.getString("pagesize", "15")) - 10);

        // /**
        // * 定时时长设置（s） 默认120
        // */
        // final WheelView country_gps_time = (WheelView)
        // findViewById(R.id.country_gps_time);
        // ArrayList list_gps_time = new ArrayList();
        // for (int i = 10; i < 600; i++) {
        // list_gps_time.add(i + "");
        // }
        // country_gps_time.setVisibleItems(3);
        // country_gps_time.setViewAdapter(new CountryAdapter(this,
        // list_gps_time));
        // country_gps_time.addScrollingListener(new OnWheelScrollListener() {
        // public void onScrollingStarted(WheelView wheel) {
        // // System.out.println("addScrollingListener");
        // }
        //
        // public void onScrollingFinished(WheelView wheel) {
        // // System.out.println("onScrollingFinished");
        // SharedPreferences sp_gps_time = getSharedPreferences("GPS_TRACK",
        // MODE_PRIVATE);
        // Editor editor = sp_gps_time.edit();
        // editor.putString("TIME", gps_time_temp);
        // editor.commit();
        // }
        // });
        // country_gps_time.addChangingListener(new OnWheelChangedListener() {
        // public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // gps_time_temp = "" + (newValue + 120);
        // }
        // });
        // SharedPreferences sp_gps_time = getSharedPreferences("GPS_TRACK",
        // MODE_PRIVATE);
        // country_gps_time.setCurrentItem(Integer.parseInt(sp_gps_time.getString("TIME",
        // "120")) - 10);

    }

    /**
     * 返回按钮
     * 
     * @param v
     */
    public void main_tab_settings_back(View v) {
        finish();
    }

    /**
     * 检测更新
     * 
     * @param v
     */
    public void main_tab_setting_check_updata_apk(View v) {
        getVersionInfo();
    }

    /**
     * 获取软件版本信息
     */
    private void getVersionInfo() {
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain(); // 比new一个对象好 相当于创建消息对象 
                try {
                    // URL url=new
                    // URL(getResources().getString(R.string.url)+"update.xml");
                    URL url = new URL(HttpUtil.getPCURL() + HttpUtil.getVersionXML());
                    HttpURLConnection conn;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {// 请求成功
                        InputStream is = conn.getInputStream();// 成功就获取返回的输入流
                        updateInfo = XmlParser.getUpdateInfo(is);
                        if (new AppInfoUtil().getAppversion(SystemSetingActivity.this)
                                .equals(updateInfo.getVersion())) {
                            msg.what = LOAD_MAIN; // 版本一致
                        } else {
                            msg.what = SHOW_UPDATA_DIALOG;// 版本不一致
                        }
                    } else {
                        msg.what = SERVER_ERROR;// 服务器失败
                    }
                } catch (MalformedURLException e) {
                    msg.what = SERVER_URL_ERROR;
                    e.printStackTrace();
                } catch (NotFoundException e) {
                    msg.what = SERVER_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    msg.what = XML_PARSE_ERROR;
                    e.printStackTrace();
                } finally { // 不管如何都要执行的操作
                    System.out.println(msg.what);
                    handler2.sendMessage(msg);
                }
                super.run();
            }
        }.start();
    }

    /**
     * 新建一个消息处理器
     */
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("msg.what" + msg.what);
            switch (msg.what) {
                case LOAD_MAIN: // 版本已经是最新的了
                    toast("该版本已经是最新 ");
                    break;
                case SHOW_UPDATA_DIALOG: // 提示更新
                    newDialog();
                    break;
                case SERVER_URL_ERROR: // 网络路径错误
                    toast("请检查网络是否可用");
                    break;
                case NETWORK_ERROR: // 网络连接错误
                    toast("请检查网络是否可用");
                    break;
                case XML_PARSE_ERROR: // XML解析错误
                    toast("请检查网络是否可用");
                    break;
                case SERVER_ERROR: // 服务器出错
                    toast("请检查网络是否可用");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /** 弹出更新对话框 */
    private void newDialog() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示更新").setMessage(updateInfo.getDescription())
                .setPositiveButton("立即更新", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downLoadApk();
                        dialog.dismiss();
                    }
                }).setNegativeButton("下次再说", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /** 判断并下载apk安装文件 */
    private void downLoadApk() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
            FinalHttp fh = new FinalHttp();
            // 创建一个临时文件
            File clear_temp = new File(Environment.getExternalStorageDirectory(), "temp.apk");
            deleteFile(clear_temp);
            File temp = new File(Environment.getExternalStorageDirectory(), "temp.apk");
            dialogPress.show();
            // 调用download方法开始下载
            HttpHandler handler1 = fh.download(updateInfo.getApkurl(), // 这里是下载的路径
                    temp.getAbsolutePath(), // 这是保存到本地的路径
                    true, // true:断点续传 false:不断点续传（全新下载）
                    new AjaxCallBack<File>() {
                        @Override
                        public void onLoading(long count, long current) {// 每秒回调一次
                            System.out.println(current + "/" + count);
                            dialogPress.setMessage("下载进度:" + current / 1024 + "k/" + count / 1024 + "k");
                            super.onLoading(count, current);
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            dialogPress.setMessage("下载更新失败");
                            toast(strMsg + "下载更新失败");
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onSuccess(File t) {
                            System.out.println(t == null ? "null" : t.getAbsoluteFile().toString() + "下载成功");
                            dialogPress.dismiss();
                            toast("下载成功...替换安装...");
                            replaceAPK(t);
                        }

                        /**
                         * 替换安装APK
                         */
                        private void replaceAPK(File t) {
                            // <activity
                            // android:name=".PackageInstallerActivity"
                            // android:configChanges="orientation|keyboardHidden"
                            // android:theme="@style/TallTitleBarTheme">
                            // <intent-filter>
                            // <action android:name="android.intent.action.VIEW"
                            // />
                            // <category
                            // android:name="android.intent.category.DEFAULT" />
                            // <data android:scheme="content" />
                            // <data android:scheme="file" />
                            // <data
                            // android:mimeType="application/vnd.android.package-archive"
                            // />
                            // </intent-filter>
                            // </activity>
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            // intent.setType("application/vnd.android.package-archive");
                            // intent.setData(Uri.fromFile(t));
                            intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    });
        } else {
            toast("没有SD卡更新失败");
        }
    }

    /** 删除文件 */
    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
                System.out.println("file.delete();");
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
            System.out.println("file.delete();");
        } else {
            System.out.println("文件不存在。。");
        }
    }

    /** 土司 */
    private void toast(String strMsg) {
        Toast.makeText(getApplicationContext(), strMsg, 0).show();
    }

    /**
     * 分页适配器 2014-7-22下午5:56:27 类CountryAdapter
     * 
     * @author Mars zhang
     * 
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        /** MemberVariables */
        ArrayList list;
        /** MemberVariables */
        int i = 0;

        protected CountryAdapter(Context context, ArrayList list) {
            super(context, R.layout.tempitem, NO_RESOURCE);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        // Countries flags
        // private int flags =R.drawable.tem_unit_dialog;

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            // ImageView img = (ImageView) view.findViewById(R.id.tempImag);
            // img.setImageResource(flags);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }

    }

}
