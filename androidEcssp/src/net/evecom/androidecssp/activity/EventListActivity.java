package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.EventInfo;

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
 * 事件列表
 * @author EVECOM-PC
 *
 */
public class EventListActivity extends BaseActivity {

	private ListView eventListView=null;
	private List<EventInfo> eventInfos=null;
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
					resutArray=connServerForResultPost("jfs/mobile/androidIndex/getEnentList", "");
				} catch (ClientProtocolException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if(resutArray.length()>0){
					try {
						eventInfos=getEvents(resutArray);
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
     * 解析事件json数据
     * 
     */
    public static List<EventInfo> getEvents(String jsonString) throws JSONException {
        List<EventInfo> list = new ArrayList<EventInfo>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            EventInfo eventInfo = new EventInfo();
            eventInfo.setId(jsonObject.getString("id"));
            eventInfo.setEventname(jsonObject.getString("eventname"));
            eventInfo.setHappendate(jsonObject.getString("happendate"));
            eventInfo.setTypename(jsonObject.getString("typename"));
            eventInfo.setGisx(jsonObject.getString("gisx"));
            eventInfo.setGisy(jsonObject.getString("gisy"));
            eventInfo.setAreaname(jsonObject.getString("areaname"));
            list.add(eventInfo);
        }
        return list;
    }

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
        private List<EventInfo> list;

        public EventAdapter(Context context, List<EventInfo> list) {
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
            textViewEventName.setText("事件名称：" + list.get(i).getEventname());
            textViewEventType.setText("事件类别：" + list.get(i).getTypename());
            textViewEventArea.setText("所属区域：" + list.get(i).getAreaname());
            textViewEventTime.setText("事发时间：" + list.get(i).getHappendate()); 
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getApplicationContext(), ProjectListActivity.class);
					intent.putExtra("eventInfo", list.get(i));
					startActivity(intent);
//					EventListActivity.this.finish();
					Log.v("mars", "点击了列表"+list.get(i).getEventname());
				}
			});
            return view;
        }
    }
}
