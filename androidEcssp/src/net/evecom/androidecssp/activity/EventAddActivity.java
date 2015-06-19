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
import net.evecom.androidecssp.base.UploadPictureActivity;
import net.evecom.androidecssp.bean.EventInfo;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.gps.TDTLocation222;
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

/**
 * �¼��ϱ�
 * 
 * @author EVECOM-PC
 * 
 */
public class EventAddActivity extends BaseActivity {

	
	private EditText nameeditText;
	private EditText addresseditText;
	private EditText personeditText;
	private EditText phoneeditText;
	private EditText contenteditText;

	private TextView leveView;
	private TextView stateView;
	private TextView gpsView;
	
	private ListView imageListView; 

	private String[] levestr;
	private String[] statestr;
	private FinalDb db;
	/** ͼƬ�б� */
	private List<FileManageBean> fileList;
	private UploadPictureAdapter uploadPictureAdapter;

	private String saveResult = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_add_activity);
		/*Intent intent = getIntent(); 
		
		*/
		init();
		initdata();
	}

	private void init() {
		nameeditText = (EditText) findViewById(R.id.event_name_et);
		addresseditText = (EditText) findViewById(R.id.event_address_et);
		personeditText = (EditText) findViewById(R.id.event_person_et);
		phoneeditText = (EditText) findViewById(R.id.event_phone_et);
		contenteditText = (EditText) findViewById(R.id.event_content_et);

		leveView = (TextView) findViewById(R.id.event_leve_tv);
		stateView = (TextView) findViewById(R.id.event_status_tv);
		gpsView = (TextView) findViewById(R.id.event_gps_tv);
		
		imageListView = (ListView) findViewById(R.id.event_file_list);
	}

	private void initdata() {
		/** ������ݿ����� */
		db = FinalDb.create(this);
		db.deleteAll(FileManageBean.class);

		levestr = new String[] { "һ��", "�ϴ�", "�ش�" , "�ر��ش�" };
		statestr = new String[] { "������", "������", "������" };
		
		fileList = new ArrayList<FileManageBean>();
		uploadPictureAdapter = new UploadPictureAdapter(
				getApplicationContext(), fileList);
		imageListView.setAdapter(uploadPictureAdapter);
		
		updateGpsview();
		
	}
	
	private void updateGpsview(){
		gpsView.setText(
				" ( "+ShareUtil.getString(getApplicationContext(), "GPS", "latitude", "0.0")
				+" , "+ShareUtil.getString(getApplicationContext(), "GPS", "longitude", "0.0")
				+" )");
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
		case 3: // ��λ���� 
			updateGpsview();
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
			// �ж��Ƿ��ظ�����
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
		if(checkifstop()){
			return ;
		}  
		
		StringBuilder sb = new StringBuilder();
		sb.append("eventlever=" +leveView.getText().toString() );
		sb.append("&eventname=" + nameeditText.getText().toString());
		sb.append("&happenaddress=" + addresseditText.getText().toString());
		sb.append("&eventcontent=" + contenteditText.getText().toString());
		sb.append("&belongunitid="
				+ ShareUtil.getString(getApplicationContext(), "PASSNAME",
						"orgid", ""));
		sb.append("&reporterperson=" + personeditText.getText().toString());
		sb.append("&reportertel=" + phoneeditText.getText().toString());
		sb.append("&eventstatus=" + stateView.getText().toString());
		
		
		sb.append("&gisy="
				+ ShareUtil.getString(getApplicationContext(), "GPS",
						"latitude", "")); 
		sb.append("&gisx="
				+ ShareUtil.getString(getApplicationContext(), "GPS",
						"longitude", "")); 
		postdata(sb.toString());
	}

	private void postdata(final String entity) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				try {
					saveResult = connServerForResultPost(
							"jfs/mobile/androidIndex/EventAdd", entity);
				} catch (ClientProtocolException e) {
					message.what = MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				} catch (IOException e) {
					message.what = MESSAGETYPE_02;
					Log.e("mars", e.getMessage());
				}
				if (saveResult.length() > 0) {
					message.what = MESSAGETYPE_01;
					String eventId = "";
					try {
						EventInfo eventInfo = getEventInfo(saveResult);
						if (null != eventInfo) {
							eventId = eventInfo.getId();
						}
					} catch (JSONException e) {
						Log.e("mars", e.getMessage());
					}
					postImage(eventId);
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
	public static EventInfo getEventInfo(String jsonString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		EventInfo eventInfo = new EventInfo();
		eventInfo.setId(jsonObject.getString("id"));
		eventInfo.setEventname("eventname");
		return eventInfo;
	}

	/**
	 * �ϴ�ͼƬ
	 * 
	 * @param taskresponseId
	 */
	private void postImage(String eventId) {
		if (null == eventId || eventId.length() < 1) {
			return;
		}
		if(null==fileList||fileList.size()==0){
			return;
		}
		AjaxParams params = new AjaxParams();
		params.put("eventId", eventId);
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
		fh.post(HttpUtil.getPCURL(getApplicationContext())
				+ "jfs/mobile/androidIndex/eventFileSave", params,
				new AjaxCallBack<String>() {
					@Override
					public void onLoading(long count, long current) {
						Log.v("mars", current + "/" + count);
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Log.v("mars", "ͼƬ����ʧ�ܣ����������Ƿ����" );
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						Log.v("mars", "�¼��ļ��ϴ��ɹ�:"+t);
					}
				});
	}

	/**
	 * ��Ϣ��������
	 */
	private Handler saveHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGETYPE_01:// �ı�����ɹ� ��ת���¼��б� ���ύͼƬ��Դ
				Intent intent = new Intent(getApplicationContext(),
						EventListActivity.class); 
				startActivity(intent);
				finish();
				break;
			case MESSAGETYPE_02:
				toast("�������ϱ��¼���", 1);
				break;
			default:
				break;
			}
		};
	};
 
	public void sjjb(View view) {
		Dialog dialog = new AlertDialog.Builder(EventAddActivity.this)
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

	public void clzt(View view) {
		Dialog dialog = new AlertDialog.Builder(EventAddActivity.this)
				.setIcon(R.drawable.qq_dialog_default_icon).setTitle("��ѡ�񼶱�")
				.setItems(statestr, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						stateView.setText(statestr[which]);
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

	public void eventgpsbtn(View view){
		Intent intent = new Intent(getApplicationContext(), TDTLocation222.class);
        startActivityForResult(intent, 3);
	}
	
	public void bc(View view) {
		submit();
	}
	
	
	
	
	/**
	 * У��
	 * @return
	 */
	private Boolean checkifstop(){  
		if (nameeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("�������¼����ƣ�");
			return true;
		}
		if (addresseditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("�������·���ַ��");
			return true;
		}
		if (gpsView.getText().toString().trim().length() <16 ) {
			DialogToastNoCall("�����GPS���¶�λ��");
			return true;
		}
		if (leveView.getText().toString().trim().length() == 0) {
			DialogToastNoCall("��ѡ���¼�����");
			return true;
		}
		if (stateView.getText().toString().trim().length() == 0) {
			DialogToastNoCall("��ѡ����״̬��");
			return true;
		}
		if (personeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("�����뱨���ˣ�");
			return true;
		}
		if (phoneeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("�����뱨���˵绰��");
			return true;
		}
		if (contenteditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("�������¼�������");
			return true;
		}
		return false; 
		
		
	}
}