/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 
package net.evecom.androidecssp.gps;

import java.util.List;

import net.evecom.androidecssp.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapActivity;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;

*//**
 * 天 2014-9-3下午12:05:29 类MyLocationDemo
 *     
 * @author Mars zhang
 * 
 *//*
public class TDTLocation extends MapActivity {
    *//** MemberVariables *//*
    private MapView mMapView = null;
    *//** MemberVariables *//*
    protected View mViewLayout = null;
    *//** MemberVariables *//*
    protected Context mCon = null;
    *//** MemberVariables *//*
    int mCount = 0;
    *//** MemberVariables *//*
    MyLocationOverlay mMyLocation = null;
    *//** MemberVariables *//*
    private LocationManager m_locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        // 以上两行功能一样
        mViewLayout = inflater.inflate(R.layout.get_tdt_map2, null);
        setContentView(mViewLayout);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.displayZoomControls(true);
        mCon = this;
        List<Overlay> list = mMapView.getOverlays();
        mMyLocation = new MyOverlay(this, mMapView);

        mMyLocation.enableCompass();
        mMyLocation.enableMyLocation();
        mMyLocation.setGpsFollow(true);
        System.out.println("精确度：" + mMyLocation.getAccuracy());
        System.out.println("我的位置是否可用：" + mMyLocation.isMyLocationEnabled());
        list.add(mMyLocation);

        m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // if(m_locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        // m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
        // 5000, 0, mMyLocation);
        // }
        *//**
         * gps
         *//*
        if (m_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mMyLocation);
        }
        Button location = (Button) findViewById(R.id.map_btn_back);
        location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                // m_locationManager.removeUpdates(mMyLocation);
                // System.exit(0);
            }

        });
        Button fh = (Button) findViewById(R.id.contact_group_find);
        fh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPoint point = mMyLocation.getMyLocation();
                if (point != null)
                    mMapView.getController().animateTo(point);
                
            }

        });
    }

    // @Override
    // public void onClick(View v) {
    // switch (v.getId()) {
    // case R.id.map_btn_back: // 返回
    // Intent intent = new Intent();
    // GPSActivity.this.setResult(RESULT_OK, intent);
    // GPSActivity.this.finish();
    // break;
    // default:
    // break;
    // }
    //
    // }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        mMapView.getController().stopAnimation(false);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if (null != mMyLocation) {
                m_locationManager.removeUpdates(mMyLocation);
            }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (null != mMyLocation) {
            m_locationManager.removeUpdates(mMyLocation);
        }
        super.onPause();
    }
    *//**
     * 
     * 描述MyLocationOverlay
     * @author Mars zhang
     * @created 2014-11-5 上午11:19:51
     *//*
    class MyOverlay extends MyLocationOverlay {

        public MyOverlay(Context context, MapView mapView) {
            super(context, mapView);
            // TODO Auto-generated constructor stub
        }

        
         * 处理在"我的位置"上的点击事件
         
        protected boolean dispatchTap() {
            Toast.makeText(mCon, "您点击了我的位置", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            super.onLocationChanged(location);
            if (location != null) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // 存入数据
                Editor editor = sp.edit();
                editor.putString("GPS_latitude", "" + location.getLatitude());
                editor.putString("GPS_longitude", "" + location.getLongitude());
                editor.commit();
                String strLog = String.format("您当前的位置:\r\n" + "纬度:%f\r\n" + "经度:%f", location.getLongitude(),
                        location.getLatitude());
                Toast.makeText(mCon, strLog, Toast.LENGTH_SHORT).show();
            }
            GeoPoint point = mMyLocation.getMyLocation();
            if (point != null)
                mMapView.getController().animateTo(point);
        }
    }
}
*/