package net.evecom.androidecssp.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.ICallback;
import net.evecom.androidecssp.base.UploadPictureActivity;
import net.evecom.androidecssp.bean.EventInfo;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.bean.ProjectInfo;
import net.evecom.androidecssp.bean.TaskInfo;
import net.evecom.androidecssp.bean.TaskResponseInfo;
import net.evecom.androidecssp.util.HttpUtil;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.util.UiUtil;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.client.ClientProtocolException;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���������
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

	// private TextView timeView;
	private TextView leveView;

	private ListView imageListView;

	private EventInfo eventInfo;
	private ProjectInfo projectInfo;
	private TaskInfo taskInfo;

	private String[] levestr;

	private FinalDb db;
	/** ͼƬ�б� */
	private List<FileManageBean> fileList;
	private UploadPictureAdapter uploadPictureAdapter;

	private String saveResult = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_response_add_activity);
		Intent intent = getIntent();
		eventInfo = (EventInfo) intent.getSerializableExtra("eventInfo");
		projectInfo = (ProjectInfo) intent.getSerializableExtra("projectInfo");
		taskInfo = (TaskInfo) intent.getSerializableExtra("taskInfo");
		init();
		initdata();
	}

	private void init() {
		titleeditText = (EditText) findViewById(R.id.task_response_title_et);
		contenteditText = (EditText) findViewById(R.id.task_response_content_et);
		keywordeditText = (EditText) findViewById(R.id.task_response_keyword_et);
		peopleeditText = (EditText) findViewById(R.id.task_response_people_et);
		remarkeditText = (EditText) findViewById(R.id.task_response_remark_et);

		// timeView=(TextView) findViewById(R.id.task_response_time_tv);
		leveView = (TextView) findViewById(R.id.task_response_leve_tv);

		imageListView = (ListView) findViewById(R.id.task_response_file_list);
	}

	private void initdata() {
		/** ������ݿ����� */
		db = FinalDb.create(this);
		db.deleteAll(FileManageBean.class);

		levestr = new String[] { "һ��", "����", "�ǳ�" };

		fileList = new ArrayList<FileManageBean>();
		uploadPictureAdapter = new UploadPictureAdapter(
				getApplicationContext(), fileList);
		imageListView.setAdapter(uploadPictureAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: // ͼƬ����
			String filePath = data.getStringExtra("filePath");
			Log.v("mars", filePath);
			manageFileDataAndListView(filePath);
			break;
		case 2: // Ԥ�� �п���ɾ����ͼƬ ���Ը���listView
			fileList.removeAll(fileList);
			List<FileManageBean> allPictures = db.findAll(FileManageBean.class);
			// �����б�listView
			for (FileManageBean item : allPictures) {
				fileList.add(item);
			}
			// ��������listView
			uploadPictureAdapter.notifyDataSetChanged();
			if (null != imageListView) {
				UiUtil.setListViewHeightBasedOnChildren(imageListView);
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ���ļ�bean�浽���ݿ� ���Ҹ���listView
	 * 
	 * @param filePath
	 */
	private void manageFileDataAndListView(String filePath) {
		if (null != filePath && !filePath.equals("none")) {
			FileManageBean filebean = new FileManageBean();
			filebean.setFile_URL(filePath);
			// �ж��Ƿ��ظ����
			List<FileManageBean> fileManageBeanstemp = db.findAllByWhere(
					FileManageBean.class, "File_URL=\"" + filePath + "\"");
			if (null == fileManageBeanstemp || fileManageBeanstemp.size() == 0) {
				db.save(filebean);
				fileList.removeAll(fileList);
				List<FileManageBean> allPictures = db
						.findAll(FileManageBean.class);
				// �����б�listView
				for (FileManageBean item : allPictures) {
					fileList.add(item);
				}
				// ��������listView
				uploadPictureAdapter.notifyDataSetChanged();
				if (null != imageListView) {
					UiUtil.setListViewHeightBasedOnChildren(imageListView);
				}
			} else {
				toast("��ͼƬ�Ѿ�ѡ��!", 1);
			}
		} else {
			Log.v("mars", "û��ѡ��ͼƬ");
		}
	}

	/**
	 * 
	 * ͼƬ�б�������
	 * 
	 * @author Mars zhang
	 * 
	 */
	public class UploadPictureAdapter extends BaseAdapter implements
			ListAdapter {
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
			TextView textViewTitle = (TextView) view
					.findViewById(R.id.file_list_item_tv);
			String s[] = Pattern.compile("/").split(list.get(i).getFile_URL());
			textViewTitle.setText("" + s[s.length - 1]);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getApplicationContext(),
							AfnailPictureActivity.class);
					intent.putExtra("URI", list.get(i).getFile_URL());
					intent.putExtra("File_Id", list.get(i).getFile_ID());
					startActivityForResult(intent, 2);
					/*
					 * if (FILE_TYPE_PIC_01.equals(list.get(i).getFile_Flag()))
					 * { Intent intent = new Intent(getApplicationContext(),
					 * AfnailPictureActivity.class); intent.putExtra("URI",
					 * list.get(i).getFile_URL()); intent.putExtra("File_Id",
					 * list.get(i).getFile_ID()); startActivityForResult(intent,
					 * 4); } else if
					 * (FILE_TYPE_VIDEO_02.equals(list.get(i).getFile_Flag())) {
					 * Intent intent = new Intent(getApplicationContext(),
					 * AfinalVideoActivity.class); intent.putExtra("URI",
					 * list.get(i).getFile_URL()); intent.putExtra("File_Id",
					 * list.get(i).getFile_ID()); startActivityForResult(intent,
					 * 4); } else if
					 * (FILE_TYPE_AUDIO_03.equals(list.get(i).getFile_Flag())) {
					 * Intent intent = new Intent(getApplicationContext(),
					 * AfinalAudioActivity.class); intent.putExtra("URI",
					 * list.get(i).getFile_URL()); intent.putExtra("File_Id",
					 * list.get(i).getFile_ID()); startActivityForResult(intent,
					 * 4); }
					 */

				}
			});
			return view;
		}
	}

	/**
	 * �����ύ
	 */
	private void submit() {
		int index = 0;
		// У��
		if (titleeditText.getText().toString().trim().length() == 0) {
			index += 1;
		}
		if (contenteditText.getText().toString().trim().length() == 0) {
			index += 2;
		}
		switch (index) {
		case 1:
			DialogToast("�����뷴������!", new ICallback() {
				@Override
				public Object execute() {
					return null;
				}
			});
			return;
		case 2:
			DialogToast("�����뷴������!", new ICallback() {
				@Override
				public Object execute() {
					return null;
				}
			});
			return;
		case 3:
			DialogToast("�����뷴�����⼰����!", new ICallback() {
				@Override
				public Object execute() {
					return null;
				}
			});
			return;

		default:
			break;
		}
		/*
		 * private EditText titleeditText; private EditText contenteditText;
		 * private EditText keywordeditText; private EditText peopleeditText;
		 * private EditText remarkeditText;
		 * 
		 * private TextView leveView;
		 */
		// eventInfo=(EventInfo) intent.getSerializableExtra("eventInfo");
		// projectInfo=(ProjectInfo) intent.getSerializableExtra("projectInfo");
		// taskInfo=(TaskInfo) intent.getSerializableExtra("taskInfo");
		StringBuilder sb = new StringBuilder();
		sb.append("eventid=" + eventInfo.getId());
		sb.append("&responselevel=" + leveView.getText().toString());
		sb.append("&responsetitle=" + titleeditText.getText().toString());
		sb.append("&remark=" + remarkeditText.getText().toString());
		sb.append("&responsedeptid="
				+ ShareUtil.getString(getApplicationContext(), "PASSNAME",
						"orgid", ""));
		sb.append("&responsename="
				+ ShareUtil.getString(getApplicationContext(), "PASSNAME",
						"usernameCN", ""));
		sb.append("&planid=" + projectInfo.getPlanid());
		sb.append("&taskid=" + taskInfo.getId());
		sb.append("&responsecon=" + contenteditText.getText().toString());
		sb.append("&responseid="
				+ ShareUtil.getString(getApplicationContext(), "PASSNAME",
						"userid", ""));

		postdata(sb.toString());
	}

	private void postdata(final String entity) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				try {
					saveResult = connServerForResultPost(
							"jfs/mobile/androidIndex/taskResponseAdd", entity);
				} catch (ClientProtocolException e) {
					message.what = MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what = MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if (saveResult.length() > 0) {
					message.what = MESSAGETYPE_01;
					String responseid = "";
					try {
						TaskResponseInfo taskResponseInfo = getTaskResponseInfo(saveResult);
						if (null != taskResponseInfo) {
							responseid = taskResponseInfo.getId();
						}
					} catch (JSONException e) {
						Log.e("mars", e.getMessage());
					}
					postImage(responseid);
				} else {
					message.what = MESSAGETYPE_02;
				}
				Log.v("mars", saveResult);
				saveHandler.sendMessage(message);
			}
		}).start();

	}

	/**
	 * ��������json����
	 * 
	 */
	public static TaskResponseInfo getTaskResponseInfo(String jsonString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		TaskResponseInfo taskResponseInfo = new TaskResponseInfo();
		taskResponseInfo.setId(jsonObject.getString("id"));
		taskResponseInfo.setResponsetitle("responsetitle");
		return taskResponseInfo;
	}

	/**
	 * �ϴ�ͼƬ
	 * 
	 * @param taskresponseId
	 */
	private void postImage(String taskresponseId) {
		if (null == taskresponseId || taskresponseId.length() < 1) {
			return;
		}
		if(null==fileList||fileList.size()==0){
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("taskresponseId", taskresponseId);
		for (int i = 0; i < fileList.size(); i++) {
			try {
				params.put("file" + i, new File(fileList.get(i).getFile_URL()));
			} catch (FileNotFoundException e) {
				if (null != e) {
					e.printStackTrace();
				}
			} // �ϴ��ļ�
		}
		FinalHttp fh = new FinalHttp();
		fh.post(HttpUtil.getPCURL()
				+ "jfs/mobile/androidIndex/taskResponseFileSave", params,
				new AjaxCallBack<String>() {
					@Override
					public void onLoading(long count, long current) {
						Log.v("mars", current + "/" + count);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(getApplicationContext(),
								"ͼƬ����ʧ�ܣ����������Ƿ����", 0).show();
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						Log.v("mars", "�����ļ��ϴ��ɹ�:"+t);
					}
				});
	}

	/**
	 * ��Ϣ�������
	 */
	private Handler saveHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGETYPE_01:// �ı�����ɹ� ��ת�������б� ���ύͼƬ��Դ
				Intent intent = new Intent(getApplicationContext(),
						ResponseListActivity.class);
				intent.putExtra("eventInfo", eventInfo);
				intent.putExtra("projectInfo", projectInfo);
				intent.putExtra("taskInfo", taskInfo);
				startActivity(intent);
				finish();
				break;
			case MESSAGETYPE_02:
				toast("�����±��淴��", 1);
				break;
			default:
				break;
			}
		};
	};

	public void fksj(View view) {

	}

	public void zyjb(View view) {
		Dialog dialog = new AlertDialog.Builder(TaskResponseAddActivity.this)
				.setIcon(R.drawable.qq_dialog_default_icon).setTitle("��ѡ�񼶱�")
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
		Intent intent = new Intent(getApplicationContext(),
				UploadPictureActivity.class);
		startActivityForResult(intent, 1);
	}

	public void bc(View view) {
		submit();
	}
}
