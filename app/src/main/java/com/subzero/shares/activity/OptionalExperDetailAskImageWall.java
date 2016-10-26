package com.subzero.shares.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.OfferRewardActivity;
import com.subzero.shares.adapter.ImageAdapter;

import java.util.ArrayList;

/**
 * 自定义本地图片库多选
 * Created by The_p on 2016/5/3.
 */
public class OptionalExperDetailAskImageWall extends BaseActivity {
    private TextView titleTV;
    private GridView mPhotoWall;
    private ArrayList<String> imageUrls;
    private ImageAdapter adapter;
    private Button confirmBtn;

    @Override
    protected void initView() {
        setContentView(R.layout.photo_wall);
        titleTV = (TextView) findViewById(R.id.topbar_title_tv);
        titleTV.setText(R.string.latest_image);

        Button backBtn = (Button) findViewById(R.id.topbar_left_btn);
        confirmBtn = (Button) findViewById(R.id.topbar_right_btn);
        backBtn.setText("返回");
        backBtn.setVisibility(View.VISIBLE);
        confirmBtn.setText("确定");
        confirmBtn.setVisibility(View.VISIBLE);

        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
//        adapter = new ImageWallAdapter(this, list);
//        mPhotoWall.setAdapter(adapter);


    }

    @Override
    protected void initData() {
        imageUrls=getImagePath();
        if(imageUrls!=null){

            adapter = new ImageAdapter(this,imageUrls,mPhotoWall);
            mPhotoWall.setAdapter(adapter);
        }else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void initListener() {
        //选择照片完成
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片完成,回到起始页面
                ArrayList<String> paths = getSelectImagePaths();
                Intent intent = new Intent(OptionalExperDetailAskImageWall.this, OptionalExperDetailAsk.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("code", paths != null ? 100 : 101);
                intent.putStringArrayListExtra("paths", paths);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 图片地址
     *
     * @return
     */
    public ArrayList<String> getImagePath() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> imagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            imagePaths = new ArrayList<String>();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA);
                imagePaths.add(cursor.getString(dataColumnIndex));
//                cursor.close();
            }
            cursor.close();
        }
        return imagePaths;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时结束所有的下载任务
        adapter.cancelAllTasks();
    }

    //获取已选择的图片路径
    private ArrayList<String> getSelectImagePaths() {
        SparseBooleanArray map = adapter.getSelectionMap();
        if (map.size() == 0) {
            return null;
        }

//        Log.i("1111111111",map.size()+"");
        ArrayList<String> selectedImageList = new ArrayList<String>();

        for (int i = 0; i <imageUrls.size() ; i++) {
            if(map.get(i)){
                selectedImageList.add(imageUrls.get(i));
            }
        }

        return selectedImageList;
    }
}