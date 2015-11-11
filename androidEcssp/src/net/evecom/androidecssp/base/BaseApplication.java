
package net.evecom.androidecssp.base;

import net.tsz.afinal.FinalBitmap;
import android.app.Application;
/**
 * 
 * ÃèÊö BaseApplication
 * @author Mars zhang
 * @created 2015-11-9 ÏÂÎç3:11:24
 */
public class BaseApplication extends Application {

    public static FinalBitmap finalbitmap;
    public static BaseApplication instence;

    @Override
    public void onCreate() {
        finalbitmap = FinalBitmap.create(this);
        super.onCreate();
        instence = this;
        
    }
}
