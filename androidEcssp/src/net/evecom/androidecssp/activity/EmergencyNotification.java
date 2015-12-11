/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.activity;

import java.util.ArrayList;
import java.util.List;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.viewpager.RotateDownTransformer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**
 * 
 * 描述 EmergencyNotification
 * @author Mars zhang
 * @created 2015-11-12 上午10:13:00
 */
public class EmergencyNotification extends BaseActivity {
	private ViewPager viewpager;
	private int[] imagesId={R.drawable.emergency_info_3,
			R.drawable.emergency_info_33,
			R.drawable.emergency_info_34};
	
	private List<ImageView> imageViews =new ArrayList<ImageView>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_info_activity);
		
		//1、
		viewpager=(ViewPager) findViewById(R.id.my_viewpager_id);
		//4、属性动画DepthPageTransformer
//		viewpager.setPageTransformer(true, new DepthPageTransformer());
		viewpager.setPageTransformer(true, new RotateDownTransformer());
		//2、设置适配器
		viewpager.setAdapter(new PagerAdapter() {
			@Override//初始化item   在这里布置每个page的布局在这里添加控件
			public Object instantiateItem(ViewGroup container, int position) {
				//打算在每个页面上添加一个图片    也可以不是图片switch判断下每个page要放什么
				ImageView imageView=new ImageView(EmergencyNotification.this);
				imageView.setImageResource(imagesId[position]);
				//不让图片变形
				imageView.setScaleType(ScaleType.CENTER_CROP);
				
				//把imageView加到container中  ？？如果一个page不止一个控件怎么办
				container.addView(imageView);
				//把imageView加到list中
				imageViews.add(imageView);
				//返回imageView
				return imageView;
			}
			@Override//destroyItem
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(imageViews.get(position));
			}
			
			@Override //判断  每个adapter都有这个
			public boolean isViewFromObject(View view, Object object) {
				return view==object;
			}
			
			@Override  //返回有多少页
			public int getCount() {
				return imagesId.length;
			}
		});
		
	}

	
	public void  call(View view){
//		 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:13850157372"));
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:18259154949"));
		startActivity(intent);
	}
}
