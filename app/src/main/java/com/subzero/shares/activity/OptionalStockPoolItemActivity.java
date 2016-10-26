package com.subzero.shares.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.newsdetail.PayActivity;
import com.subzero.shares.bean.OptionalStockPoolItemBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ShareSDKHelper;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 量化股票池Item界面
 * Created by The_p on 2016/4/29.
 */
public class OptionalStockPoolItemActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Optional";
    private TextView title;
    private TextView message;
    private TextView time;
    private TextView website;
    private ImageView image;
    private TextView summaryTitle;
    private TextView pay;
    String imgUrl = null;
    private OptionalStockPoolItemBean bean;

    @Override
    protected void initView() {
        setContentView(R.layout.news_detail_thrend);
        title = (TextView) findViewById(R.id.textTitle);
        summaryTitle = (TextView) findViewById(R.id.summary);
        message = (TextView) findViewById(R.id.maincontent);
        time = (TextView) findViewById(R.id.time);
        website = (TextView) findViewById(R.id.source);
        pay = (TextView) findViewById(R.id.pay_news_detail_thrend);
        image = (ImageView) findViewById(R.id.newsdetail_img);
    }

    @Override
    protected void initData() {
        title.setText("股票池详情");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (OptionalStockPoolItemBean) bundle.get("data");
            summaryTitle.setText(bean.getTitle());
            message.setText(bean.getContent());
            time.setText(bean.getTime());
            website.setText(bean.getCate());
            try {
                JSONObject jj = new JSONObject(bean.getImage());
                JSONArray jo = jj.getJSONArray("photo");
                JSONObject jb = (JSONObject) jo.get(0);
                imgUrl = jb.getString("url");
                imgUrl = WebUrl.FILEHOST1 + imgUrl;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            displayAvatar(image, imgUrl);

        }


    }

    @Override
    protected void initListener() {
        image.setOnClickListener(this);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(), PayActivity.class);
                intent.putExtra("postid",bean.getId());
                intent.addFlags(7);
                startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void share(View view) {


        String url = "http://123.56.82.112/gupiao/index.php?g=Api&m=Common&a=share&id=" + bean.getId() + "&type=1";

        new ShareSDKHelper(this, imgUrl, url, url, url, new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                sharePay(SharedPreferencesUtils.getTokenId(OptionalStockPoolItemActivity.this), "1", bean.getId());
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

        } finally {
            hideLoaddingDialog();
        }
    }

    @Override
    public void onClick(View v) {
        ImageUtils.startPhotoActivity(this, imgUrl, image);
    }


}
