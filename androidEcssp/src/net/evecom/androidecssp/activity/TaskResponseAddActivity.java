package net.evecom.androidecssp.activity;

import java.util.List;
import java.util.regex.Pattern;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.UploadPictureActivity;
import net.evecom.androidecssp.bean.EventInfo;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.bean.ProjectInfo;
import net.evecom.androidecssp.bean.TaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 添加任务反馈
 * 
 * @author EVECOM-PC
 * 
 */
public class TaskResponseAddActivity extends BaseActivity {

	private EditText titleeditText;
	private EditText contenteditText;
	private EditText keywordeditText;
	private EditText peopleeditText;
	private EditText remarkeditText;

//	private TextView timeView;
	private TextView leveView;
	
	private ListView imageListView;
	
	
	private EventInfo eventInfo;
	private ProjectInfo projectInfo;
	private TaskInfo taskInfo;
	
	private String[] levestr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_response_add_activity);
		Intent intent=getIntent(); 
		eventInfo=(EventInfo) intent.getSerializableExtra("eventInfo");
		projectInfo=(ProjectInfo) intent.getSerializableExtra("projectInfo");
		taskInfo=(TaskInfo) intent.getSerializableExtra("taskInfo");
		init();
		initdata();
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:  //图片界面
			String filePath = data.getStringExtra("filePath");
			Log.v("mars", filePath);
			break;
		case 2:
			
			break;
		default:
			break;
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}



	private void initdata() {
		levestr=new String[]{"一般","较重","非常"}; 		
	}


	private void init() { 
		titleeditText=(EditText) findViewById(R.id.task_response_title_et);
		contenteditText=(EditText) findViewById(R.id.task_response_content_et);
		keywordeditText=(EditText) findViewById(R.id.task_response_keyword_et);
		peopleeditText=(EditText) findViewById(R.id.task_response_people_et);
		remarkeditText=(EditText) findViewById(R.id.task_response_remark_et);

//		timeView=(TextView) findViewById(R.id.task_response_time_tv);
		leveView=(TextView) findViewById(R.id.task_response_leve_tv);
		
		imageListView=(ListView) findViewById(R.id.task_response_file_list); 
	}
	
	
    /**
     * 
     * 图片列表适配器
     * 
     * @author Mars zhang
     * 
     */
    public class UploadPictureAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<FileManageBean> list;

        public UploadPictureAdapter(Context context, List<FileManageBean> list) {
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
                view = inflater.inflate(R.layout.file_list_item, null);
            }
            TextView textViewTitle = (TextView) view.findViewById(R.id.file_list_item_tv);
            String s[] = Pattern.compile("/").split(list.get(i).getFile_URL());
            textViewTitle.setText("" + s[s.length - 1]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) { 
                	Intent intent = new Intent(getApplicationContext(), AfnailPictureActivity.class);
                    intent.putExtra("URI", list.get(i).getFile_URL());
                    intent.putExtra("File_Id", list.get(i).getFile_ID());
                    startActivityForResult(intent, 2);
                    /*if (FILE_TYPE_PIC_01.equals(list.get(i).getFile_Flag())) {
                        Intent intent = new Intent(getApplicationContext(), AfnailPictureActivity.class);
                        intent.putExtra("URI", list.get(i).getFile_URL());
                        intent.putExtra("File_Id", list.get(i).getFile_ID());
                        startActivityForResult(intent, 4);
                    } else if (FILE_TYPE_VIDEO_02.equals(list.get(i).getFile_Flag())) {
                        Intent intent = new Intent(getApplicationContext(), AfinalVideoActivity.class);
                        intent.putExtra("URI", list.get(i).getFile_URL());
                        intent.putExtra("File_Id", list.get(i).getFile_ID());
                        startActivityForResult(intent, 4);
                    } else if (FILE_TYPE_AUDIO_03.equals(list.get(i).getFile_Flag())) {
                        Intent intent = new Intent(getApplicationContext(), AfinalAudioActivity.class);
                        intent.putExtra("URI", list.get(i).getFile_URL());
                        intent.putExtra("File_Id", list.get(i).getFile_ID());
                        startActivityForResult(intent, 4);
                    }*/

                }
            });
            return view;
        }
    }
	
	
	private void  submit(){
		
	}

	
	public void fksj(View view) {

	}

	public void zyjb(View view) { 
		Dialog dialog=new AlertDialog.Builder(TaskResponseAddActivity.this)
		.setIcon(R.drawable.qq_dialog_default_icon)
		.setTitle("请选择级别")
		.setItems(levestr, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				leveView.setText(levestr[which]);
				dialog.dismiss();
			}
		}).create(); 
		dialog.show();
	}

	public void findpictureonclick(View view) {
		Intent intent = new Intent(getApplicationContext(), UploadPictureActivity.class);
        startActivityForResult(intent, 1);
	}

	public void bc(View view) {
		submit();
	}
}
