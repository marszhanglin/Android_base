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
import java.util.regex.Pattern;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.AfnailPictureActivity;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseModel;
import net.evecom.androidecssp.base.UploadPictureActivity;
import net.evecom.androidecssp.bean.FileManageBean;
import net.evecom.androidecssp.gps.TDTLocation222;
import net.evecom.androidecssp.util.ShareUtil;
import net.evecom.androidecssp.util.UiUtil;
import net.tsz.afinal.FinalDb;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

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
 * 
 * 描述
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:17
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
	/** 图片列表 */
	private List<FileManageBean> fileList;
	private UploadPictureAdapter uploadPictureAdapter;

	private String saveResult = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_add_activity);
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
		/** 清空数据库数据 */
		db = FinalDb.create(this);
		db.deleteAll(FileManageBean.class);

		levestr = new String[] { "一般", "较大", "重大" , "特别重大" };
		statestr = new String[] { "待处理", "处理中", "处理完" };
		
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
		case 1: // 图片界面
			String filePath = data.getStringExtra("filePath");
			Log.v("mars", filePath);
			manageFileDataAndListView(filePath);
			break;
		case 2: // 预览 有可能删除了图片 所以更新listView
			fileList.removeAll(fileList);
			List<FileManageBean> allPictures = db.findAll(FileManageBean.class);
			// 更新列表listView
			for (FileManageBean item : allPictures) {
				fileList.add(item);
			}
			// 重新适配listView
			uploadPictureAdapter.notifyDataSetChanged();
			if (null != imageListView) {
				UiUtil.setListViewHeightBasedOnChildren(imageListView);
			}
			break;
		case 3: // 定位界面 
			updateGpsview();
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 把文件bean存到数据库 并且更新listView
	 * 
	 * @param filePath
	 */
	private void manageFileDataAndListView(String filePath) {
		if (null != filePath && !filePath.equals("none")) {
			FileManageBean filebean = new FileManageBean();
			filebean.setFile_URL(filePath);
			// 判断是否重复添加
			List<FileManageBean> fileManageBeanstemp = db.findAllByWhere(
					FileManageBean.class, "File_URL=\"" + filePath + "\"");
			if (null == fileManageBeanstemp || fileManageBeanstemp.size() == 0) {
				db.save(filebean);
				fileList.removeAll(fileList);
				List<FileManageBean> allPictures = db
						.findAll(FileManageBean.class);
				// 更新列表listView
				for (FileManageBean item : allPictures) {
					fileList.add(item);
				}
				// 重新适配listView
				uploadPictureAdapter.notifyDataSetChanged();
				if (null != imageListView) {
					UiUtil.setListViewHeightBasedOnChildren(imageListView);
				}
			} else {
				toast("该图片已经选择!", 1);
			}
		} else {
			Log.v("mars", "没有选择图片");
		}
	}

	/**
	 * 
	 * 图片列表适配器
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

				}
			});
			return view;
		}
	}

	/**
	 * 保存提交
	 */
	private void submit() {
		if(checkifstop()){
			return ;
		}  
		HashMap<String, String> hashMap=new HashMap<String, String>();
		hashMap.put("infoReception.eventlever", leveView.getText().toString());
		hashMap.put("infoReception.eventname", nameeditText.getText().toString());
		hashMap.put("infoReception.happenaddress", addresseditText.getText().toString());
		hashMap.put("infoReception.eventcontent", contenteditText.getText().toString());
		hashMap.put("infoReception.belongunitid", ShareUtil.getString(getApplicationContext(), "PASSNAME",
                "orgid", ""));
		hashMap.put("infoReception.reporterperson", personeditText.getText().toString());
		hashMap.put("infoReception.reportertel", phoneeditText.getText().toString());
		hashMap.put("infoReception.eventstatus", stateView.getText().toString());
		hashMap.put("infoReception.gisy", ShareUtil.getString(getApplicationContext(), "GPS",
                "latitude", ""));
		hashMap.put("infoReception.gisx", ShareUtil.getString(getApplicationContext(), "GPS",
                "longitude", ""));
		postdata(hashMap);
	}

	/**
	 * 
	 * 描述 postdata
	 * @author Mars zhang
	 * @created 2015-11-10 下午4:13:51
	 * @param entity
	 */
	private void postdata(final HashMap<String, String> entity) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = new Message();
				try {
					saveResult = connServerForResultPost(
							"jfs/ecssp/mobile/eventCtr/EventAdd", entity);
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
						BaseModel eventInfo = getObjInfo(saveResult);
						if (null != eventInfo) {
							eventId = eventInfo.get("id");
						}
					} catch (JSONException e) {
						Log.e("mars", e.getMessage());
					}
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("eventId", eventId);
					postImage(map,fileList,"jfs/ecssp/mobile/eventCtr/eventFileSave");
				} else {
					message.what = MESSAGETYPE_02;
				}
				Log.v("mars", saveResult);
				saveHandler.sendMessage(message);
			}
		}).start();
	}

	



	/**
	 * 消息处理机制
	 */
	private Handler saveHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGETYPE_01:// 文本保存成功 跳转至事件列表 并提交图片资源
				Intent intent = new Intent(getApplicationContext(),
						EventListActivity.class); 
				startActivity(intent);
				finish();
				break;
			case MESSAGETYPE_02:
				toast("请重新上报事件！", 1);
				break;
			default:
				break;
			}
		};
	};
 
	public void sjjb(View view) {
		Dialog dialog = new AlertDialog.Builder(EventAddActivity.this)
				.setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择级别")
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
				.setIcon(R.drawable.qq_dialog_default_icon).setTitle("请选择级别")
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
	 * 校验
	 * @return
	 */
	private Boolean checkifstop(){  
		if (nameeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请输入事件名称！");
			return true;
		}
		if (addresseditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请输入事发地址！");
			return true;
		}
		if (gpsView.getText().toString().trim().length() <16 ) {
			DialogToastNoCall("请输打开GPS重新定位！");
//			return true;
		}
		if (leveView.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请选择事件级别！");
			return true;
		}
		if (stateView.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请选择处理状态！");
			return true;
		}
		if (personeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请输入报告人！");
			return true;
		}
		if (phoneeditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请输入报告人电话！");
			return true;
		}
		if (contenteditText.getText().toString().trim().length() == 0) {
			DialogToastNoCall("请输入事件描述！");
			return true;
		}
		return false; 
		
		
	}
}
