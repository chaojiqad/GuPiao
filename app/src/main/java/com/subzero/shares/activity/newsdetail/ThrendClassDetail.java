package com.subzero.shares.activity.newsdetail;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.bean.ThrenFragmentBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ShareSDKHelper;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 趋势课堂详情
 * Created by zzy on 2016/4/7.
 */
public class ThrendClassDetail extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ThrendClass";
    protected String TEXT_TITLE = "趋势课堂详情";

    protected TextView textTitle;
    protected TextView summary;
    protected TextView source;
    protected TextView time;
    protected ImageView newsdetail_img;
    protected TextView maincontent;
    private ThrenFragmentBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.threndclass_detail_thrend);

        textTitle = (TextView) findViewById(R.id.textTitle);
        summary = (TextView) findViewById(R.id.summary);
        source = (TextView) findViewById(R.id.source);
        time = (TextView) findViewById(R.id.time);
        newsdetail_img = (ImageView) findViewById(R.id.newsdetail_img);
        maincontent = (TextView) findViewById(R.id.maincontent);
    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (ThrenFragmentBean) bundle.get("data");
        }
        if (getIntent().getFlags() == 1) {
            textTitle.setText(TEXT_TITLE);
        } else {
            textTitle.setText("课程详情");
        }
        summary.setText(bean.getTitle());
        source.setText(bean.getWebsite());
        time.setText(bean.getTime());
        ImageUtils.displayAvatar(this, newsdetail_img, bean.getImage());
        maincontent.setText(bean.getMessage());


    }

    @Override
    protected void initListener() {
        newsdetail_img.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    public void share(View view) {
        String shareImg = bean.getImage();
        String url = "http://123.56.82.112/gupiao/index.php?g=Api&m=Common&a=share&id=" + bean.getId() + "&type=1";
        new ShareSDKHelper(this, shareImg, url, url, url, new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                sharePay(SharedPreferencesUtils.getTokenId(ThrendClassDetail.this), "3", bean.getId());
            }
        }).showShare();
    }

    /**
     * 分享之后 获取奖励
     *
     * @param token
     * @param type
     * @param postid
     */
    private void sharePay(String token, String type, String postid) {
        RequestParams params = new RequestParams(WebUrl.Sharepay);
        params.addBodyParameter("token", token);
        params.addBodyParameter("type", type);
        params.addBodyParameter("postid", postid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, "分享之后获取奖励返回的参数" + result);
                analyzeResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //解析从服务器返回的数据
    private void analyzeResult(String result) {
        try {
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {
        ImageUtils.startPhotoActivity(this,bean.getImage(),newsdetail_img);
    }
}
