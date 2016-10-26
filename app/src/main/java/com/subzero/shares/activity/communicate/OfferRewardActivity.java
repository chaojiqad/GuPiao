package com.subzero.shares.activity.communicate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.adapter.MainGVAdapter;
import com.subzero.shares.bean.Classification;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ScreenUtils;
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
 * 发布悬赏令
 * Created by xzf on 2016/4/15.
 */
public class OfferRewardActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "OfferReward";
    private String title = "发布悬赏令";
    private TextView classifyText;
    private TextView titleText;
    private TextView myHint;
    private TextView mMsgLength;
    private ImageView shareBar;
    private ImageView mSelectIcon;
    private GridView mPhonto;
    private EditText mMsg;
    private EditText mTitle;
    private EditText mAmount;
    private TextView mRelease;
    private LinearLayout llClassify;

    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;

    private String mCategoryId;

    private final int REQUEST_CODE_GALLERY = 1001;

    //回调需要的
    private static Succes succesInterFace;
    private static int requestCode;


    @Override
    protected void initView() {
        setContentView(R.layout.reward_listview1_communicate);
        //获取屏幕像素
        ScreenUtils.initScreen(this);

        classifyText = (TextView) findViewById(R.id.myCommit);
        mMsgLength = (TextView) findViewById(R.id.reward_ac_tv_mes_length);
        myHint = (TextView) findViewById(R.id.myHint);
        titleText = (TextView) findViewById(R.id.textTitle);
        mRelease = (TextView) findViewById(R.id.tv_reward_ac_release);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        mSelectIcon = (ImageView) findViewById(R.id.reward_ac_iv_select_icon);
        mPhonto = (GridView) findViewById(R.id.reward_ac_gv);
        mMsg = (EditText) findViewById(R.id.reward_ac_et_mes);
        mTitle = (EditText) findViewById(R.id.et_titlehint_communicate);
        mAmount = (EditText) findViewById(R.id.et_ac_amount_reward);
        llClassify = (LinearLayout) findViewById(R.id.more);

    }

    @Override
    protected void initData() {
        titleText.setText(title);
        classifyText.setText("分类");
        myHint.setHint("请选择分类");
        shareBar.setVisibility(View.INVISIBLE);
        /*初始化显示选择照片的grid*/
        imagePathList = new ArrayList<>();
        adapter = new MainGVAdapter(this, imagePathList);
        mPhonto.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        mSelectIcon.setOnClickListener(this);
        mMsg.addTextChangedListener(this);
        mRelease.setOnClickListener(this);
        llClassify.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                      case R.id.reward_ac_iv_select_icon:

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

            case R.id.tv_reward_ac_release:


                if (TextUtils.isEmpty(mTitle.getText().toString().trim())) {
                    Toast.makeText(OfferRewardActivity.this, "标题不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mAmount.getText().toString().trim())) {
                    Toast.makeText(OfferRewardActivity.this, "悬赏金额不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mMsg.getText().toString().trim())) {
                    Toast.makeText(OfferRewardActivity.this, "内容不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(myHint.getText().toString().trim())) {
                    Toast.makeText(OfferRewardActivity.this, "分类不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                releaseReward(SharedPreferencesUtils.getTokenId(this), mTitle.getText().toString().trim(), mCategoryId, mAmount.getText().toString().trim(), mMsg.getText().toString().trim(), imagePathList);

                break;
            case R.id.more:
                startActivityForResult(new Intent(this, ClassifyActivity.class), 200);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Classification classification = (Classification) bundle.get("classify");
            myHint.setText(classification.getCatename());
            mCategoryId = classification.getId();
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mMsgLength.setText(s.length() + "/" + 300);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * http://123.56.82.112/gupiao/index.php/Api/Apidiscuss/ReleaseReward/userid/43/token/fasdlkfj14234/title/%E6%A0%87%E9%A2%981/category/%E5%88%86%E7%B1%BB%E4%B8%80/moneyreward/100/message/%E6%B3%95%E5%B8%88%E6%89%93%E5%8F%91%E6%96%AF%E8%92%82%E8%8A%AC/images/fasdfasdf
     * <p>
     * <p>
     * <p>
     * <p>
     * 发布悬赏令
     *
     * @param token
     * @param title
     * @param category
     * @param msg
     * @param imagePathList
     */
    public void releaseReward(String token, String title, String category, String moneyreward, String msg, ArrayList<String> imagePathList) {


        showLoadingDialog("正在上传数据……");

        RequestParams params = new RequestParams(WebUrl.RELEASEREWARD);
        params.addBodyParameter("token", token);
        params.addBodyParameter("title", title);
        params.addBodyParameter("cateid", category);
        params.addBodyParameter("moneyreward", moneyreward);
        params.addBodyParameter("message", msg);

        for (int i = 0; i < imagePathList.size(); i++) {
            params.addBodyParameter("image" + i, new File(imagePathList.get(i)));
            Log.e(TAG, imagePathList.get(i));
        }

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "提交悬赏返回的结果：" + result);
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
                Toast.makeText(OfferRewardActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(OfferRewardActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(OfferRewardActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(OfferRewardActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(OfferRewardActivity.this, message, Toast.LENGTH_SHORT).show();
            if(succesInterFace!=null) {
                succesInterFace.onSucces(requestCode);
            }
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听悬赏令发布成功的通知
     *
     * @param succes
     * @param requestcode
     */
    public static void setSuccesNotification(Succes succes, int requestcode) {

        succesInterFace = succes;
        requestCode = requestcode;

    }




    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
            if (resultList != null) {


                //并发线程池（超过最大数量排队）
                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

                imagePathList.clear();

                for (int i = 0; i < resultList.size(); i++) {

                    final String filePath_ = resultList.get(i).getPhotoPath();

                    fixedThreadPool.execute(new Runnable() {
                        public void run() {
                            Log.e(TAG, "图片的路径是" + filePath_);

                            String filePath = ImageUtils.saveMyBitmap(OfferRewardActivity.this, ImageUtils.getimage(filePath_));

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
            Toast.makeText(OfferRewardActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
