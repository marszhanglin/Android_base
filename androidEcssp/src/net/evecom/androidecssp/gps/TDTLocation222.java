package net.evecom.androidecssp.gps;

import java.util.List;

import net.evecom.androidecssp.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.tianditu.android.maps.MyLocationOverlay;
import com.tianditu.android.maps.Overlay;

public class TDTLocation222 extends Activity{
	private MapView mMapView = null;
	protected View   mViewLayout   = null;
	protected Context mCon   = null;
	int   mCount = 0;
	Handler handl;
	private Button locationBtn =null;
	private Button backButton  =null;
	MyLocationOverlay mMyLocation = null;
	private  LocationManager    locationManager; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		//以上两行功能一样
		mViewLayout = inflater.inflate(R.layout.get_tdt_map2, null);
		setContentView(mViewLayout);
		mMapView = (MapView)findViewById(R.id.mapview);
		mMapView.displayZoomControls(true);
		mCon = this;
		List<Overlay> list = mMapView.getOverlays();
		mMyLocation = new MyOverlay(this, mMapView);
		mMyLocation.enableCompass();
		mMyLocation.enableMyLocation();
		list.add(mMyLocation);

		backButton=(Button) findViewById(R.id.map_btn_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(); 
		        setResult(3, intent);
		        finish();
			}
		});
		/*handl = new Handler()
		{
			public void handleMessage(Message msg)
			{
				Location ll = new Location(LocationManager.GPS_PROVIDER);
				if(mCount%2==1)
				{
					ll.setLatitude(39.880000);
					ll.setLongitude(116.310000);
				}else
				{
					ll.setLatitude(39.890000);
					ll.setLongitude(116.310000);
				}
				mCount++;
				mMyLocation.onLocationChanged(ll);
			}
		};
		TimerTask MoveTask = new TimerTask() {  
	  		@Override public void run() {
	  			Message msg = handl.obtainMessage(2);
	  			handl.sendMessage(msg);
	  			
	  	    }
	  	};*/
	  	/*Timer 			mMoveTimer = new Timer();
	  	mMoveTimer.schedule(MoveTask,1000,2000);*/
		locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
			    	1000, 1, mMyLocation); 
		}else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,  
			    	1000, 1, mMyLocation); 
		}
		
		
		locationBtn = (Button)findViewById(R.id.contact_group_find);
		locationBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) { 
				GeoPoint point = mMyLocation.getMyLocation();
				if(point != null)
					mMapView.getController().animateTo(point); 
				 
			}
			
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mMapView.getController().stopAnimation(false);
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // back键 
	        Intent intent = new Intent(); 
	        setResult(3, intent);
	        finish();
        } 
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
			System.exit(0);
		return super.onKeyUp(keyCode, event);
	}

	class MyOverlay extends MyLocationOverlay 
	{
		public MyOverlay(Context context, MapView mapView) {
			super(context, mapView);
			// TODO Auto-generated constructor stub
		}
		/*
		 * 处理在"我的位置"上的点击事件
		 * */
		protected boolean dispatchTap()
		{
			Toast.makeText(mCon, "您点击了我的位置", Toast.LENGTH_SHORT).show();
			return true;
		}
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			super.onLocationChanged(location);
			if(location != null){
				SharedPreferences sp = getApplicationContext().getSharedPreferences("GPS", MODE_PRIVATE);
                // 存入数据
                Editor editor = sp.edit();
                editor.putString("latitude", "" + location.getLatitude());
                editor.putString("longitude", "" + location.getLongitude());
                editor.commit();
				String strLog = String.format("您当前的位置:\r\n" +
						"纬度:%f\r\n" +
						"经度:%f",
						location.getLongitude(), location.getLatitude());
				Toast.makeText(mCon, strLog, Toast.LENGTH_SHORT).show();
				backButton.setVisibility(View.VISIBLE);
			}
			GeoPoint point = mMyLocation.getMyLocation();
			if(point != null)
				mMapView.getController().animateTo(point);
		}
	}
	
}
