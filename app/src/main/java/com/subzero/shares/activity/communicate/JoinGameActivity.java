package com.subzero.shares.activity.communicate;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.adapter.MainGVAdapter;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 参加比赛
 * Created by xzf on 2016/5/19
 */
public class JoinGameActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OptionalExper";
    private ImageView ivBack;
    private TextView tvCommit;
    private EditText etTitle;
    private EditText etContent;
    private ImageView ivPhoto;


    private GridView mImgs;

    private final int REQUEST_CODE_GALLERY = 1001;

    public static Succes mSucces;

    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;


    @Override
    protected void initView() {
        setContentView(R.layout.ui_stack_pool_detail);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvCommit = (TextView) findViewById(R.id.tv_commit_game);
        etTitle = (EditText) findViewById(R.id.et_title_game);
        etContent = (EditText) findViewById(R.id.et_content_game);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo_game);
        mImgs = (GridView) findViewById(R.id.reward_ac_game);

    }

    @Override
    protected void initData() {
        /*初始化显示选择照片的grid*/
        imagePathList = new ArrayList<>();
        adapter = new MainGVAdapter(this, imagePathList);
        mImgs.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
    }

    public static void setSucces(Succes succes){
        mSucces=succes;
    }

    /**
     * 返回上一界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_commit_game:


                if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
                    Toast.makeText(JoinGameActivity.this, "标题不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    Toast.makeText(JoinGameActivity.this, "内容不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                enterpost(SharedPreferencesUtils.getTokenId(this), etTitle.getText().toString().trim(), etContent.getText().toString().trim(), imagePathList);
                break;
            //打开相册
            case R.id.iv_photo_game:
                //配置功能
                FunctionConfig functionConfig = new FunctionConfig.Builder()
                        .setEnableCamera(true)
                        .setEnableCrop(true)
                        .setEnableRotate(true)
                        .setCropSquare(true)
                        .setEnablePreview(true)
                        .setMutiSelectMaxSize(3).build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                break;

        }

    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {


                //并发线程池（超过最大数量排队）
                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

                imagePathList.clear();

                for (int i = 0; i < resultList.size(); i++) {

                    final String filePath_ = resultList.get(i).getPhotoPath();

                    fixedThreadPool.execute(new Runnable() {
                        public void run() {
                            Log.e(TAG, "图片的路径是" + filePath_);

                            String filePath = ImageUtils.saveMyBitmap(JoinGameActivity.this, ImageUtils.getimage(filePath_));

                            imagePathList.add(filePath);

                            Log.e(TAG, "压缩之后的图片路径" + filePath);
                        }
                    });
                }

                adapter.notifyDataSetChanged(imagePathList);
            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(JoinGameActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    public void enterpost(String token, String title, String content, ArrayList<String> imagePathList) {


        showLoadingDialog("正在上传数据……");

        RequestParams params = new RequestParams(WebUrl.ENTERPOST);
        params.addBodyParameter("token", token);
        params.addBodyParameter("title", title);
        params.addBodyParameter("content", content);

        for (int i = 0; i < imagePathList.size(); i++) {
            params.addBodyParameter("image" + i, new File(imagePathList.get(i)));
            Log.e(TAG, imagePathList.get(i));
        }

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "参赛返回的结果：" + result);
                analysisJsonForAvatar(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

                hideLoaddingDialog();

            }
        });

    }


    /**
     * 解析返回的json数据
     *
     * @param result
     */
    private void analysisJsonForAvatar(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(JoinGameActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(JoinGameActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(JoinGameActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(JoinGameActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(JoinGameActivity.this, message, Toast.LENGTH_SHORT).show();
            if(mSucces!=null){
                mSucces.onSucces(6262);
            }
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
