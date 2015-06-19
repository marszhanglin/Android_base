package net.evecom.androidecssp.viewpager;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
/**
 * Viewpager的动画 滚动   
 * 2015-3-1下午7:34:15 类DepthPageTransformer
 * @author Mars zhang
 *
 */
public class RotateDownTransformer implements  PageTransformer {
	//最大旋转角度
	private static final int  MAX_ROTATE=0;
	//角度变化0~20   -20~0
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        //[-Infinity,-1) 左看不见  [-1,0] 左看的见    (0,1]有看的见       (1,+Infinity]看不见
        if (position < -1) { // [-Infinity,-1)
        	ViewHelper.setAlpha(view, 0);
        } else if (position <= 0) { // [-1,0]  
        	//设置旋转中心为底部中心
             ViewHelper.setPivotX(view, pageWidth/2);
             ViewHelper.setPivotY(view, pageHeight);
             //设置旋转角度
             ViewHelper.setRotation(view, position*MAX_ROTATE); //-20~0
        } else if (position <= 1) { // (0,1]   在右侧可见时改变缩放及透明
        	//设置旋转中心为底部中心
            ViewHelper.setPivotX(view, pageWidth/2);
            ViewHelper.setPivotY(view, pageHeight);
            //设置旋转角度
            ViewHelper.setRotation(view, position*MAX_ROTATE); //0~20
        } else { // (1,+Infinity]
        	ViewHelper.setAlpha(view, 0);
        }
    }
}

