package com.subzero.shares.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
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
 * 问诊提问
 * Created by The_p on 2016/4/15.
 */
public class OptionalExperDetailAsk extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OptionalExper";
    private ImageView ivBack;
    private TextView tvCommit;
    private EditText etTitle;
    private EditText etContent;
    private ImageView ivPhoto;
    private GridView mImgs;
    private final int REQUEST_CODE_GALLERY = 1001;
    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;
    private String adviserid;
    private String price;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_exper_ask);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        etTitle = (EditText) findViewById(R.id.et_title_quest);
        etContent = (EditText) findViewById(R.id.et_content);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        mImgs = (GridView) findViewById(R.id.reward_ac_op_ask);
    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        adviserid = bundle.getString("adviserid");
        price = bundle.getString("price");

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
            case R.id.tv_commit:


                if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
                    Toast.makeText(OptionalExperDetailAsk.this, "标题不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    Toast.makeText(OptionalExperDetailAsk.this, "内容不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogShow();
                break;
            //打开相册
            case R.id.iv_photo:
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


    private void dialogShow() {
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setTitle("注意!").setMessage("您需要向顾问支付" + price + "虚拟币才能提问").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                payVirtualMoney(SharedPreferencesUtils.getTokenId(OptionalExperDetailAsk.this), adviserid, price, "3");
            }
        }).create().show();
    }

    //虚拟币支付
    private void payVirtualMoney(String token, String postid, String money, String type) {

        // showLoadingDialogForNotCancel("正在支付....");
        showLoadingDialog("正在上传数据……");
        RequestParams params = new RequestParams(WebUrl.VMONEYPAY);
        params.addBodyParameter("token", token);
        params.addBodyParameter("postid", postid);
        params.addBodyParameter("money", money);
        params.addBodyParameter("type", type);
        Log.e(TAG, "支付参数:" + token + ";" + postid + ";" + money + ";" + type);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "支付返回的结果:" + result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        hideLoaddingDialog();
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (retCode.equals("-2")) {
                        hideLoaddingDialog();
                        Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    } else if (retCode.equals("-100")) {
                        hideLoaddingDialog();
                        Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if (retCode.equals("-3")) {
                        hideLoaddingDialog();
                        Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    advisorAsk();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "老子报错了");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {


            }
        });
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {


            //并发线程池（超过最大数量排队）
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

            imagePathList.clear();

            for (int i = 0; i < resultList.size(); i++) {

                final String filePath_ = resultList.get(i).getPhotoPath();

                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        Log.e(TAG, "图片的路径是" + filePath_);

                        String filePath = ImageUtils.saveMyBitmap(OptionalExperDetailAsk.this, ImageUtils.getimage(filePath_));

                        imagePathList.add(filePath);

                        Log.e(TAG, "压缩之后的图片路径" + filePath);
                    }
                });
            }

            adapter.notifyDataSetChanged(imagePathList);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(OptionalExperDetailAsk.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 像顾问提问
     */
    public void advisorAsk() {

        String token = SharedPreferencesUtils.getTokenId(OptionalExperDetailAsk.this);
        String title = etTitle.getText().toString().trim();
        String msg = etContent.getText().toString().trim();
        RequestParams params = new RequestParams(WebUrl.ADVISORASK);

        params.addBodyParameter("token", token);
        params.addBodyParameter("title", title);
        params.addBodyParameter("advisorid", adviserid);
        //params.addBodyParameter("price", price);
        params.addBodyParameter("message", msg);

        for (int i = 0; i < imagePathList.size(); i++) {
            params.addBodyParameter("image" + i, new File(imagePathList.get(i)));
            Log.e(TAG, imagePathList.get(i));
        }

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "提交问题返回的结果：" + result);
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
                Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(OptionalExperDetailAsk.this, message, Toast.LENGTH_SHORT).show();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
