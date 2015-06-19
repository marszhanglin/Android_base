package net.evecom.androidecssp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.bean.EventInfo;
import net.evecom.androidecssp.bean.ProjectInfo;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 处置项目列表
 * @author EVECOM-PC
 *
 */
public class ProjectListActivity extends BaseActivity {

	private ListView projectListView=null;
	private List<ProjectInfo> projectInfos=null;
	private String resutArray="";
	
	private EventInfo eventInfo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_at);
		Intent intent= getIntent();
		eventInfo=(EventInfo) intent.getSerializableExtra("eventInfo");
		init();
		
	}
	
	private void init() {
		projectListView=(ListView) findViewById(R.id.project_list_listView_1);
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
					resutArray=connServerForResultPost("jfs/mobile/androidIndex/getAllProjectByeventId", "eventId="+eventInfo.getId());
				} catch (ClientProtocolException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what=MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if(resutArray.length()>0){
					try {
						projectInfos=getjsons(resutArray);
						if(null==projectInfos){
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
				projectListHandler.sendMessage(message);
				
			}
		}).start();
		
	}

	/**
	 * eventListHandler
	 */
	private Handler projectListHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGETYPE_01://数据请求成功画列表
				ProjectAdapter eventAdapter=new ProjectAdapter(getApplicationContext(), projectInfos);
				projectListView.setAdapter(eventAdapter);
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
    public static List<ProjectInfo> getjsons(String jsonString) throws JSONException {
        List<ProjectInfo> list = new ArrayList<ProjectInfo>();
        JSONArray jsonArray = null;
        jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setId(jsonObject.getString("id"));
            projectInfo.setEventid(jsonObject.getString("eventid"));
            projectInfo.setPlanid(jsonObject.getString("planid"));
            projectInfo.setProjectname(jsonObject.getString("projectname"));
            projectInfo.setCreatetime(jsonObject.getString("createtime"));
            projectInfo.setProjectcode(jsonObject.getString("projectcode"));
            list.add(projectInfo); 
        }
        return list;
    }

	/**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     */
    public class ProjectAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<ProjectInfo> list;

        public ProjectAdapter(Context context, List<ProjectInfo> list) {
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
                view = inflater.inflate(R.layout.list_item_img_tv_img, null);
            }
            ImageView imageViewProjectCode = (ImageView) view.findViewById(R.id.list_item_img);
            TextView textViewProjectName = (TextView) view.findViewById(R.id.list_item_tv); 
            if(list.get(i).getProjectcode().equals("F42")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_aqjj);
            }else if(list.get(i).getProjectcode().equals("F43")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_ryjz);
            }else if(list.get(i).getProjectcode().equals("F44")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_xcqx);
            }else if(list.get(i).getProjectcode().equals("F45")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_yzps);
            }else if(list.get(i).getProjectcode().equals("F46")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_ryss);
            }else if(list.get(i).getProjectcode().equals("F47")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_xcjk);
            }else if(list.get(i).getProjectcode().equals("F48")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_zjzc);
            }else if(list.get(i).getProjectcode().equals("F49")){
            	imageViewProjectCode.setImageResource(R.drawable.ljwg_dw_gzrz_aqjj);
            } 
            textViewProjectName.setText(list.get(i).getProjectname());
            view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getApplicationContext(), TaskListActivity.class);
					intent.putExtra("eventInfo", eventInfo);
					intent.putExtra("projectInfo", list.get(i));
					startActivity(intent);
					Log.v("mars", "点击了列表"+list.get(i).getProjectname());
				}
			});
            return view;
        }
    }
}
