package net.evecom.androidecssp.base;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 
 * √Ë ˆ 
 * @author Mars zhang
 * @created 2015-11-5 œ¬ŒÁ6:04:04
 */
public class BaseWebViewClient extends WebViewClient{
    private BaseWebActivity context;
    
    public BaseWebViewClient(BaseWebActivity context) {
        super();
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
    
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.setVisibility(View.INVISIBLE);
        super.onPageStarted(view, url, favicon);

    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        view.setVisibility(View.VISIBLE);
//        if (context.dialog != null)
//            context.dialog.dismiss();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        context.toast("onPageFinished" + failingUrl, 1);
//        if (context.dialog != null)
//            context.dialog.dismiss();
        view.clearView();
        view.setVisibility(View.GONE);
        context.imageView.setVisibility(View.VISIBLE);
        System.out.println(errorCode + "                errorCode ");
        if (errorCode == -6) {
            try {
                view.stopLoading();
            } catch (Exception e) {
            }
            try {
                view.clearView();
            } catch (Exception e) {
            }
            if (view.canGoBack()) {
                view.goBack();
            }
        } else if (errorCode == -2) {
            try {
                view.stopLoading();
                view.destroy();
            } catch (Exception e) {
            }
            try {
                view.clearView();
            } catch (Exception e) {
            }
            if (view.canGoBack()) {
                view.goBack();
            }
        }
        context.toast( "Õ¯¬Á—”≥Ÿ£¨«Î÷ÿ–¬≥¢ ‘£°", 0);
    }
}
