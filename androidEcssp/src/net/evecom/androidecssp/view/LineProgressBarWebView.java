package net.evecom.androidecssp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
/**
 * 
 * 描述  直线进度条webview
 * @author Mars zhang
 * @created 2015-11-6 上午9:10:38
 */
public class LineProgressBarWebView extends WebView {
    
    
    

public LineProgressBarWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public LineProgressBarWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    //    public ProgressBar lineProgressBar;
    public LineProgressBarWebView(Context context ,AttributeSet attrs) {//
        super(context);
//        lineProgressBar =new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
//        lineProgressBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3,0,0));
//        this.addView(lineProgressBar);//加到webView中
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        LayoutParams progressLayoutParams = (LayoutParams) lineProgressBar.getLayoutParams();
//        progressLayoutParams.x=l;//x位置为横向偏移大小
//        progressLayoutParams.y=t;//y位置为纵向偏移大小
//        lineProgressBar.setLayoutParams(progressLayoutParams);
        super.onScrollChanged(l, t, oldl, oldt);//super执行在后
    }

}
