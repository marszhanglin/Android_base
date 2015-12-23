/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * 描述  事件列表
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:27
 */
public class EventListActivity extends BaseActivity {

	private ListView eventListView=null;
	private List<BaseModel> eventInfos=null;
	private String resutArray="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list_at);
		init();
		
	}
	
	
	
	private void init() {
		eventListView=(ListView) findViewById(R.id.event_list_listView_1);
		initlist();
	}
	
	/**
	 * 初始化列表
	 */
	private void initlist(){ 
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message= new Message();
				
				try {
					resutArray=connServerForResultPost("jfs/ecssp/mobile/eventCtr/getEnentList", null);
				} catch (ClientProtocolException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if(resutArray.length()>0){
					try {
						eventInfos=getObjsInfo(resutArray);
						if(null==eventInfos){
							message.what=MESSAGETYPE_02;
						}else{
							message.what=MESSAGETYPE_01;
						}
					} catch (JSONException e) {
						message.what=MESSAGETYPE_02;
						Log.e("mars", e.getMessage());
					}
				}else{
					message.what=MESSAGETYPE_02;
				}
				Log.v("mars", resutArray);
				eventListHandler.sendMessage(message);
				
			}
		}).start();
		
	}

	/**
	 * eventListHandler
	 */
	private Handler eventListHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGETYPE_01://数据请求成功画列表
				EventAdapter eventAdapter=new EventAdapter(getApplicationContext(), eventInfos);
				eventListView.setAdapter(eventAdapter);
				break;
			case MESSAGETYPE_02:
				toast("暂无事件发生", 1);
				break;
			default:
				break;
			}
			
		};
	};
	
    

	/**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     */
    public class EventAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public EventAdapter(Context context, List<BaseModel> list) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();

        }

        @Override
        public Object getItem(int item) {
            return this.list.get(item);
        }

        @Override
        public long getItemId(int itemId) {
            return itemId;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = inflater.inflate(R.layout.list_item13, null);
            }
            TextView textViewEventName = (TextView) view.findViewById(R.id.list_item13_tv_1);
            TextView textViewEventType = (TextView) view.findViewById(R.id.list_item13_tv_2);
            TextView textViewEventArea = (TextView) view.findViewById(R.id.list_item13_tv_3);
            TextView textViewEventTime = (TextView) view.findViewById(R.id.list_item13_tv_4);
            textViewEventName.setText("事件名称：" + list.get(i).get("eventname"));
            textViewEventType.setText("事件类别：" + list.get(i).get("typename"));
            textViewEventArea.setText("所属区域：" + list.get(i).get("areaname"));
            textViewEventTime.setText("事发时间：" + list.get(i).get("happendate")); 
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getApplicationContext(), ProjectListActivity.class);
					ProjectListActivity.pushData("eventInfo",list.get(i),intent);
					startActivity(intent);
					Log.v("mars", "点击了列表"+list.get(i).get("eventname"));
				}
			});
            return view;
        }
    }
}
