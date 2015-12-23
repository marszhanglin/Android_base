/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.bean.FileManageBean;
import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * 2014-7-22下午4:13:38 类AfnailPictureActivity
 * 
 * @author Mars zhang
 * 
 */
public class AfnailPictureActivity extends BaseActivity {
    /** MemberVariables */
    private ImageView imageView;
    /** MemberVariables */
    private String URI;
    /** 文件File_Id */
    private int File_Id = -1;
    /** 数据库 */
    private FinalDb db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afnail_picture_activity);
        // 启动activity时不自动弹出软键盘
        imageView = (ImageView) findViewById(R.id.test1_picture);
        Intent intent = getIntent();
        URI = intent.getStringExtra("URI");
        File_Id = intent.getIntExtra("File_Id", -1);
        displayImage(imageView,URI);
    }

    /**
     * 返回按钮点击事件
     * 
     * @param v
     */
    public void fh(View v) { // 标题栏 返回按钮
        Intent intent = new Intent();
        // Intent intent = new Intent(this, EventAddActivity.class);
        setResult(4, intent);
        finish();
    }

    /** 删除 */
    public void delete(View v) {
        if (-1 == File_Id) {
            return;
        }
        db = FinalDb.create(AfnailPictureActivity.this, true);
        db.deleteById(FileManageBean.class, File_Id);
        // Intent intent = new Intent(this, EventAddActivity.class);
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Intent intent = new Intent(this, EventAddActivity.class);
            Intent intent = new Intent();
            setResult(4, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
