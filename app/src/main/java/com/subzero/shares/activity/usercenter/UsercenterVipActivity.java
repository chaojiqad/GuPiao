package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 开通会员
 * Created by The_p on 2016/4/18.
 */
public class UsercenterVipActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UsercenterVip";
    private ImageView ivBack;
    private TextView godVip;
    private TextView ptVip;
    private ImageView mImg;
    private TextView mContent;

    /**
     * 客户等级信息  0代表的是  普通用户    1代表的是  黄金用户  2代表的是 铂金用户
     */
    private int userLevel = -1;


    private ArrayList<HashMap<String, String>> mList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_vip);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        godVip = (TextView) findViewById(R.id.tv_openvip_god_usercenter);
        ptVip = (TextView) findViewById(R.id.tv_openvip_ptgod_usercenter);
        mImg = (ImageView) findViewById(R.id.iv_user_ac_img);
        mContent = (TextView) findViewById(R.id.tv_user_ac_content);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        getmembermoney(SharedPreferencesUtils.getTokenId(this));
        UsercenterOpenVipActivity.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                //  getmembermoney(SharedPreferencesUtils.getTokenId(UsercenterVipActivity.this));
            }
        });
    }

    /**
     * 获取开通会员详情信息
     */
    public void getleveldesc(String token) {

        RequestParams params = new RequestParams(WebUrl.GETLEVELDESC);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "开通会员详情返回的结果：" + result);

                analysisJson(result);

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

    private void analysisJson(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject data = js.getJSONObject("data");

            JSONObject js_leveldesc = data.getJSONObject("leveldesc");
            String content = data.getString("content");

            String imgUrl = WebUrl.FILEHOST + js_leveldesc.getString("image");

            String levelname = data.getString("lvname");

            if (levelname.equals("黄金会员")) {
                userLevel = 1;
            } else if (levelname.equals("铂金会员")) {
                userLevel = 2;
            } else if (levelname.equals("普通会员")) {
                userLevel = 0;
            }

            displayAvatar(mImg, imgUrl);

            mContent.setText(content);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        godVip.setOnClickListener(this);
        ptVip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_openvip_god_usercenter: {
                if (userLevel == 1) {
                    showAlertDialog("您已经是黄金会员");
                    return;
                }
                if (userLevel == 2) {
                    showAlertDialog("您已经是铂金会员");
                    return;
                }

                if (userLevel == -1) {
                    Toast.makeText(UsercenterVipActivity.this, "正在检测您当前的等级", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getApplication(), UsercenterOpenVipActivity.class);
                intent.addFlags(1);
                startActivity(intent);
                break;
            }

            case R.id.tv_openvip_ptgod_usercenter: {


                if (userLevel == 2) {
                    showAlertDialog("您已经是铂金会员");
                    return;
                }

                if (userLevel == -1) {
                    Toast.makeText(UsercenterVipActivity.this, "正在检测您当前的等级", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(getApplication(), UsercenterOpenVipActivity.class);
                intent.addFlags(2);
                startActivity(intent);
                break;

            }
        }

    }


    /**
     * 获取续费缴费信息
     */
    private void getmembermoney(final String token) {

        showLoadingDialog("正在加载……");


        RequestParams params = new RequestParams(WebUrl.MEMBERMONEY);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取续费缴费的返回的信息：" + result);
                analysisJsonForMoney(result);
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

            }
        });
    }


    private void analysisJsonForMoney(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterVipActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            application.levelId.clear();

            JSONObject jo = js.getJSONObject("data");
            JSONArray ja = jo.getJSONArray("levels");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String onemonth = jsonObject.getString("onemonth");
                String threemonth = jsonObject.getString("threemonth");
                String halfyear = jsonObject.getString("halfyear");
                String oneyear = jsonObject.getString("oneyear");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", id);
                hashMap.put("name", name);
                hashMap.put("onemonth", onemonth);
                hashMap.put("threemonth", threemonth);
                hashMap.put("halfyear", halfyear);
                hashMap.put("oneyear", oneyear);
                mList.add(hashMap);


                application.levelId.add(id);

            }

            application.mPayList = mList;

            getleveldesc(SharedPreferencesUtils.getTokenId(UsercenterVipActivity.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
