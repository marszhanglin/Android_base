package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.bean.TaskInfo;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * 任务列表
 * @author EVECOM-PC
 *
 */
public class TaskListActivity extends BaseActivity {

	private ListView taskListView=null;
	private List<BaseModel> taskInfos=null;
	private String resutArray="";
	private BaseModel eventInfo=null;
	private BaseModel projectInfo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list_at);
		Intent intent=getIntent();
		eventInfo=(BaseModel) getData("eventInfo", intent);
		projectInfo=(BaseModel) getData("projectInfo", intent);
		
		init();
	}
	
	private void init() {
		taskListView=(ListView) findViewById(R.id.task_list_listView_1);
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
				    HashMap<String, String> hashMap=new HashMap<String, String>();
                    hashMap.put("eventId", eventInfo.get("id").toString());
                    hashMap.put("projectId", projectInfo.get("id").toString());
                    System.out.println(hashMap.values().toArray().toString());
					resutArray=connServerForResultPost("jfs/ecssp/mobile/taskresponseCtr/getTaskByEventIdAndProjectId",
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
						taskInfos=getObjsInfo(resutArray);
						if(null==taskInfos){
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
				ListAdapter eventAdapter=new MyListAdapter(getApplicationContext(), taskInfos);
				taskListView.setAdapter(eventAdapter);
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
    public static List<TaskInfo> getEvents(String jsonString) throws JSONException {
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setId(jsonObject.getString("id"));
            taskInfo.setTaskname(jsonObject.getString("taskname"));
            taskInfo.setTaskcontern(jsonObject.getString("taskcontern"));
            taskInfo.setCreatetime(jsonObject.getString("createtime"));
            taskInfo.setKeyword(jsonObject.getString("keyword"));
            taskInfo.setTaskdept(jsonObject.getString("taskdept"));
            taskInfo.setTaskdeptid(jsonObject.getString("taskdeptid"));
            list.add(taskInfo); 
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
            textViewEventName.setText("任务名称：" + list.get(i).get("taskname"));
            textViewEventType.setText("执行单位：" + list.get(i).get("taskdept"));
            textViewEventArea.setText("关键说明：" + list.get(i).get("keyword"));
            textViewEventTime.setText("创建时间：" + list.get(i).get("createtime")); 
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Dialog delDia = new AlertDialog.Builder(TaskListActivity.this)
                    .setIcon(R.drawable.qq_dialog_default_icon).setTitle("提示").setMessage("您是要反馈任务，还是查看历史反馈记录？")
                    .setPositiveButton("添加反馈", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) { 
                        	Intent intent=new Intent(getApplicationContext(), TaskResponseAddActivity.class);
                        	TaskResponseAddActivity.pushData("eventInfo", eventInfo, intent);
                        	TaskResponseAddActivity.pushData("projectInfo", projectInfo, intent);
                        	TaskResponseAddActivity.pushData("taskInfo", list.get(i), intent);
        					startActivity(intent);
                            dia.dismiss();
                        }
                    }).setNegativeButton("历史反馈", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	Intent intent=new Intent(getApplicationContext(), ResponseListActivity.class);
                        	ResponseListActivity.pushData("eventInfo", eventInfo, intent);
                        	ResponseListActivity.pushData("projectInfo", projectInfo, intent);
                        	ResponseListActivity.pushData("taskInfo", list.get(i), intent);
        					startActivity(intent);
                            dialog.dismiss();
                        }
                    }).create();
            delDia.show();
            
					Log.v("mars", "点击了列表"+list.get(i).get("Taskname"));
				}
			});
            return view;
        }
    }
}
