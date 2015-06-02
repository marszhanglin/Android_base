package net.evecom.androidecssp.activity;

import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
/**
 * 事件列表
 * @author EVECOM-PC
 *
 */
public class EventListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_list_at);
		
		
	}
	
	
	
	/**
     * 匿名适ListView配器类
     * 
     * @author Mars zhang
     */
    public class DangerAdapter extends BaseAdapter implements ListAdapter {
        /** MemberVariables */
        private Context context;
        /** MemberVariables */
        private LayoutInflater inflater;
        /** MemberVariables */
        private List<MianPerson> list;

        public DangerAdapter(Context context, List<MianPerson> list) {
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
            /*if (null == view) {
                view = inflater.inflate(R.layout.search_for_main_person_list_item2, null);
            }
            TextView textViewName = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_name);
            TextView textViewBirth = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_birth);
            TextView textViewSex = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_sex);
            TextView textViewAddress = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_address);
            textViewName.setText("姓名：" + list.get(i).getPERSONNAME());
            textViewBirth.setText("出身年月：" + list.get(i).getBIRTH());
            textViewSex.setText("性别：" + list.get(i).getMALEDICTID());
            textViewAddress.setText("家庭住址：" + list.get(i).getHOUSEADDR());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 点击删除该人员
                    View view = inflater.inflate(R.layout.main_person_dialog_info, null);
                    TextView textViewName = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_name);
                    TextView textViewBirth = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_birth);
                    TextView textViewSex = (TextView) view.findViewById(R.id.search_for_main_person_list_item2_sex);
                    TextView textViewAddress = (TextView) view
                            .findViewById(R.id.search_for_main_person_list_item2_address);
                    textViewName.setText("姓名：" + list.get(i).getPERSONNAME());
                    textViewBirth.setText("出身年月：" + list.get(i).getBIRTH());
                    textViewSex.setText("性别：" + list.get(i).getMALEDICTID());
                    textViewAddress.setText("家庭住址：" + list.get(i).getHOUSEADDR());

                    Dialog delDia = new AlertDialog.Builder(MainPersonListActivity.this)
                            .setIcon(R.drawable.qq_dialog_default_icon).setTitle("人员信息").setView(view)
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    db = FinalDb.create(MainPersonListActivity.this, true);
                                    db.deleteById(MianPerson.class, list.get(i).getPERSONID());
                                    updateListView();
                                    dia.dismiss();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    delDia.show();
                }
            });*/
            return view;
        }
    }
	

}
