/*
 * Copyright (c) 2005, 2014, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package net.evecom.androidecssp.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.evecom.androidecssp.R;
import net.evecom.androidecssp.util.FileUtils;
import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * 2014-7-22下午6:04:23 类UploadPictureActivity
 * 
 * @author Mars zhang
 * 
 */
public class UploadPictureActivity extends Activity {
    /** 成员变量 */
    private String strImgPath = "";// 照片文件绝对路径
    /** 成员变量 */
    private String strImgName = "";
    /** 成员变量 */
    private Uri photoUri;
    /** 成员变量 */
    private static final int MESSAGETYPE_01 = 0x0001;// 用于判断是否发送成功
    /** 成员变量 */
    private static final int MESSAGETYPE_02 = 0x0002;
    /** 成员变量 */
    private ProgressDialog progressDialog = null;
    /** 拍照按钮 */
    private Button takePicture;
    /** 本地选择按钮 */
    private Button pickPicture;
    /** 上传图片按钮 */
    private Button uploadPictrue;
    /** 成员变量 */
    private ImageView imageView;
    /** 事件id */
    private String id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadpictrue);
        // 启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 获取事件列表传来的数据
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        initView();
    }

    public void initView() {
        // 返回按钮
//        mBtnBack = (Button) findViewById(R.id.uploadpictrue_btn_back);
//        mBtnBack.setOnClickListener(this);
        // 图片视图
        imageView = (ImageView) findViewById(R.id.uploadpicture_cameraImage);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        // 存入数据
        Editor editor = sp.edit();
        editor.putString("fileName", "");
        editor.commit();
    }

    /**
     * 获取文件路径并上传至服务器
     * 
     * @param v
     */
    public void uploadpicture_up_btn(View v) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
        String path = sp.getString("fileName", "none");
        // Toast.makeText(getApplicationContext(), path, 0).show();
        // Intent intent = new Intent(UploadPictureActivity.this,
        // EventAddActivity.class);
        Intent intent = new Intent();
        intent.putExtra("filePath", path);
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }

    /**
     * 相机按钮监听
     * 
     * @param v
     */
    public void uploadpicture_take_btn(View v) {
        cameraMethod();// 拍照
    }

    /**
     * 本地选择按钮监听
     * 
     * @param v
     */
    public void uploadpicture_pick_btn(View v) {
        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
        intent2.setType("*/*");
        intent2.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent2, "请选择一个文件"), 2);
    }

    /**
     * fh
     * 
     * @param v
     */
    public void fh(View v) {
        Intent intent = new Intent(); 
        intent.putExtra("filePath", "none");
        setResult(R.layout.uploadpictrue, intent);
        finish();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // 点击手机返回按钮时操作
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	Intent intent = new Intent(); 
            intent.putExtra("filePath", "none");
            setResult(R.layout.uploadpictrue, intent);
            finish();
            return true;
        }
        return false;
    }

    /**
     * 照相功能
     */
    private void cameraMethod() {
        Intent Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(Media.TITLE, filename);
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(Intent, 1);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: // 从拍照跳转回来
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    String fPath = uri2filePath(uri); // 转化为路径
                    // System.out.println("4   " + fPath);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
                    // 存入数据
                    Editor editor = sp.edit();
                    editor.putString("fileName", fPath);
                    editor.commit();
                    /*FileOutputStream b = null;
                    try {
                        b = new FileOutputStream(fPath);
                        // 把数据写入文件
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                    imageView.setImageURI(uri);
                }
                break;

            case 2: // 从本地选择器跳转回来
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    if (!isImageFile(path)) {
                        Toast.makeText(this, "您选择的不是图片，请选择一个图片", 1).show();
                        return;
                    } else {
                        int xm = 10;
                        File file = new File(path);
                        long fileLength = file.length();
                        if (null != file && fileLength / (1024 * 1024) > xm) {
                            Toast.makeText(this, "您选择的图片文件超过10M,请重新选择图片文件", 1).show();
                            file = null;
                            return;
                        }
                        file = null;
                    }
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Picture", MODE_PRIVATE);
                    // 存入数据
                    Editor editor = sp.edit();
                    editor.putString("fileName", path);
                    editor.commit();
                    // imageView.setImageURI(uri); //1
                    FinalBitmap fb = FinalBitmap.create(this);
                    fb.display(imageView, path);
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 2014-4-1 判断文件路径是否为"jpg","png","gif","jpeg"后缀
     * 
     * @param filePath
     *            文件路径
     * @return Boolean 是否为图片路径
     */
    private Boolean isImageFile(String filePath) {
        String[] imageFormatSet = new String[] { "jpg", "png", "gif", "peg" };
        String endPath = filePath.substring(filePath.length() - 3, filePath.length());
        // System.out.println(endPath);
        for (String item : imageFormatSet) {
            if (endPath.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /** 把Uri转化成文件路径 */
    private String uri2filePath(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        // System.out.println("uri2filePath    "+path);
        return path;
    }

    /** 将绝对路径转换成URI */
    private Uri path2Uri(String picPath) {
        Uri mUri = Uri.parse("content://media/external/images/media");
        Uri mImageUri = null;
        Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            if (picPath.equals(data)) {
                int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
                // System.out.println("找到了");
                break;
            }
            cursor.moveToNext();
        }
        return mImageUri;
    }

    /** computeSampleSize */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /** computeInitialSampleSize */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 错误填报提示信息
     * 
     * @param errorMsg
     */
    private void DialogToast(String errorMsg) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UploadPictureActivity.this);
        builder1.setTitle("提示信息");
        builder1.setIcon(R.drawable.qq_dialog_default_icon);// 图标
        builder1.setMessage("" + errorMsg);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            // @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder1.show();
    }
}
