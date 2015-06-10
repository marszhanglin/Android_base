package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.EventInfo;
import net.evecom.androidecssp.bean.ProjectInfo;
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
 * �����б�
 * @author EVECOM-PC
 *
 */
public class TaskListActivity extends BaseActivity {

	private ListView taskListView=null;
	private List<TaskInfo> taskInfos=null;
	private String resutArray="";
	private EventInfo eventInfo=null;
	private ProjectInfo projectInfo=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list_at);
		Intent intent=getIntent();
		eventInfo=(EventInfo) intent.getSerializableExtra("eventInfo");
		projectInfo=(ProjectInfo) intent.getSerializableExtra("projectInfo");
		init();
	}
	
	private void init() {
		taskListView=(ListView) findViewById(R.id.task_list_listView_1);
		initlist();
	}
	
	/**
	 * ��ʼ���б�
	 */
	private void initlist(){ 
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message= new Message();
				
				try {
					resutArray=connServerForResultPost("jfs/mobile/androidIndex/getTaskByEventIdAndProjectId",
							"eventId="+eventInfo.getId()+"&projectId="+projectInfo.getId());
				} catch (ClientProtocolException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if(resutArray.length()>0){
					try {
						taskInfos=getEvents(resutArray);
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
			case MESSAGETYPE_01://��������ɹ����б�
				ListAdapter eventAdapter=new MyListAdapter(getApplicationContext(), taskInfos);
				taskListView.setAdapter(eventAdapter);
				break;
			case MESSAGETYPE_02:
				toast("�����¼�����", 1);
				break;
			default:
				break;
			}
			
		};
	};
	
    /**
     * �����¼�json����
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
     * ������ListView������
     * 
     * @author Mars zhang
     */
    public class MyListAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<TaskInfo> list;

        public MyListAdapter(Context context, List<TaskInfo> list) {
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
            textViewEventName.setText("�������ƣ�" + list.get(i).getTaskname());
            textViewEventType.setText("ִ�е�λ��" + list.get(i).getTaskdept());
            textViewEventArea.setText("�ؼ�˵����" + list.get(i).getKeyword());
            textViewEventTime.setText("����ʱ�䣺" + list.get(i).getCreatetime()); 
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Dialog delDia = new AlertDialog.Builder(TaskListActivity.this)
                    .setIcon(R.drawable.qq_dialog_default_icon).setTitle("��ʾ").setMessage("����Ҫ�������񣬻��ǲ鿴��ʷ������¼��")
                    .setPositiveButton("���ӷ���", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) { 
                        	Intent intent=new Intent(getApplicationContext(), TaskResponseAddActivity.class);
                        	intent.putExtra("eventInfo", eventInfo);
        					intent.putExtra("projectInfo", projectInfo);
        					intent.putExtra("taskInfo", list.get(i));
        					startActivity(intent);
                            dia.dismiss();
                        }
                    }).setNegativeButton("��ʷ����", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	Intent intent=new Intent(getApplicationContext(), ResponseListActivity.class);
        					intent.putExtra("eventInfo", eventInfo);
        					intent.putExtra("projectInfo", projectInfo);
        					intent.putExtra("taskInfo", list.get(i));
        					startActivity(intent);
                            dialog.dismiss();
                        }
                    }).create();
            delDia.show();
            
					Log.v("mars", "������б�"+list.get(i).getTaskname());
				}
			});
            return view;
        }
    }
}