/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.TaskResponseInfo;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * 描述 任务反馈列表
 * @author Mars zhang
 * @created 2015-11-12 上午10:14:40
 */
public class ResponseListActivity extends BaseActivity {

	private ListView responseListView=null;
	private List<BaseModel> taskResponseInfos=null;
	private String resutArray="";
	private BaseModel eventInfo=null;
	private BaseModel projectInfo=null;
	private BaseModel taskInfo=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.response_list_at);
		Intent intent=getIntent();
		eventInfo=(BaseModel) getData("eventInfo", intent);
		projectInfo=(BaseModel) getData("projectInfo", intent);
		taskInfo=(BaseModel) getData("taskInfo", intent);
		init();
	}
	
	private void init() {
		responseListView=(ListView) findViewById(R.id.response_list_listView_1);
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
				HashMap<String, String> hashMap=new HashMap<String, String>();
                hashMap.put("taskId", taskInfo.get("id").toString());
				try {
					resutArray=connServerForResultPost("jfs/ecssp/mobile/taskresponseCtr/getTaskResponseByTaskId",
					        hashMap);
				} catch (ClientProtocolException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if(resutArray.length()>0){
					try {
						taskResponseInfos=getObjsInfo(resutArray);
						if(null==taskResponseInfos){
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
				ListAdapter eventAdapter=new MyListAdapter(getApplicationContext(), taskResponseInfos);
				responseListView.setAdapter(eventAdapter);
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
     * 解析事件json数据
     * 
     */
    public static List<TaskResponseInfo> getEvents(String jsonString) throws JSONException {
        List<TaskResponseInfo> list = new ArrayList<TaskResponseInfo>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TaskResponseInfo taskResponseInfo = new TaskResponseInfo();
            taskResponseInfo.setId(jsonObject.getString("id"));
            taskResponseInfo.setResponsetitle(jsonObject.getString("responsetitle"));
            taskResponseInfo.setResponsecon(jsonObject.getString("responsecon"));
            taskResponseInfo.setCreatetime(jsonObject.getString("createtime"));
            taskResponseInfo.setRemark(jsonObject.getString("remark"));
            taskResponseInfo.setName(jsonObject.getString("name"));
            taskResponseInfo.setDetailattach(jsonObject.getString("detailattach"));
            list.add(taskResponseInfo); 
        }
        return list;
    }

	/**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     */
    public class MyListAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<BaseModel> list;

        public MyListAdapter(Context context, List<BaseModel> list) {
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
            textViewEventName.setText("反馈标题：" + list.get(i).get("responsetitle"));
            textViewEventType.setText("反馈机构：" + list.get(i).get("name"));
            textViewEventArea.setText("反馈内容：" + list.get(i).get("responsecon"));
            textViewEventTime.setText("反馈时间：" + list.get(i).get("createtime")); 
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.v("mars", "点击了列表"+list.get(i).get("responsetitle"));
				}
			});
            return view;
        }
    }
}
