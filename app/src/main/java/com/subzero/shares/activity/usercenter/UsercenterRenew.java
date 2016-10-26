package com.subzero.shares.activity.usercenter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 续费缴费
 * Created by The_p on 2016/4/20.
 */
public class UsercenterRenew extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private int[] ads = {R.id.tv_recharge1, R.id.tv_recharge2, R.id.tv_recharge3, R.id.tv_recharge4,
            R.id.tv_recharge5, R.id.tv_recharge6, R.id.tv_recharge7, R.id.tv_recharge8};
    private ArrayList<TextView> tvs;
    private TextView mUserGrade;
    private TextView mPay;
    private TextView mPay1;
    private TextView mDate;
    private CircleImageView mLogo;

    public static final String TAG = "UsercenterRenew";
    private LinearLayout mLpay1;
    private LinearLayout mLpay2;

    /**
     * 标识是否获取到了充值的金额
     */
    private boolean b;

    private ArrayList<HashMap<String, String>> mList;
    private TextView mMoney1;
    private TextView mMoney2;
    private TextView mMoney3;
    private TextView mMoney4;
    private TextView mMoney5;
    private TextView mMoney6;
    private TextView mMoney7;
    private TextView mMoney8;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_usercenter_renew);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvs = new ArrayList<>();
        for (int id : ads) {
            TextView tv = (TextView) findViewById(id);
            tvs.add(tv);
        }
        mUserGrade = (TextView) findViewById(R.id.tv_usercenter_grade);
        mPay = (TextView) findViewById(R.id.tv_ac_pay);
        mPay1 = (TextView) findViewById(R.id.tv_ac_pay1);
        mDate = (TextView) findViewById(R.id.tv_usercenter_date);
        mLogo = (CircleImageView) findViewById(R.id.iv_userlogo);
        mLpay1 = (LinearLayout) findViewById(R.id.ll_ac_usercenter_pay1);
        mLpay2 = (LinearLayout) findViewById(R.id.ll_ac_usercenter_pay2);
        mMoney1 = (TextView) findViewById(R.id.tv_user_ac_money1);
        mMoney2 = (TextView) findViewById(R.id.tv_user_ac_money2);
        mMoney3 = (TextView) findViewById(R.id.tv_user_ac_money3);
        mMoney4 = (TextView) findViewById(R.id.tv_user_ac_money4);
        mMoney5 = (TextView) findViewById(R.id.tv_user_ac_money5);
        mMoney6 = (TextView) findViewById(R.id.tv_user_ac_money6);
        mMoney7 = (TextView) findViewById(R.id.tv_user_ac_money7);
        mMoney8 = (TextView) findViewById(R.id.tv_user_ac_money8);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        getmembermoney(SharedPreferencesUtils.getTokenId(this));
        UsercenterOpenVipActivity.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                getmembermoney(SharedPreferencesUtils.getTokenId(UsercenterRenew.this));
            }
        });
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        for (TextView textView : tvs) {
            textView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = 4;

        if (!b) {
            Toast.makeText(UsercenterRenew.this, "正在获取充值的金额信息", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.tv_recharge1:
                i--;
            case R.id.tv_recharge2:
                i--;
            case R.id.tv_recharge3:
                i--;
            case R.id.tv_recharge4:
                i--;
                Intent intent = new Intent(this, UsercenterOpenVipActivity.class);
                intent.addFlags(1);
                intent.putExtra("flag", i);
                startActivity(intent);
                break;
            case R.id.tv_recharge5:
                i--;
            case R.id.tv_recharge6:
                i--;
            case R.id.tv_recharge7:
                i--;
            case R.id.tv_recharge8:
                i--;
                Intent intent1 = new Intent(this, UsercenterOpenVipActivity.class);
                intent1.addFlags(2);
                intent1.putExtra("flag", i);
                startActivity(intent1);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取续费缴费信息
     */
    private void getmembermoney(String token) {

        showLoadingDialog("正在加载……");


        RequestParams params = new RequestParams(WebUrl.MEMBERMONEY);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取续费缴费的返回的信息：" + result);
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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterRenew.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterRenew.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterRenew.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jo = js.getJSONObject("data");

            JSONObject ja_user = jo.getJSONObject("user");
            String avatar = WebUrl.FILEHOST + ja_user.getString("avatar");
            String levelname = ja_user.getString("levelname");
            String member_expire = ja_user.getString("member_expire");

            String levelid = ja_user.getString("levelid");

            Log.e(TAG, "当前的等级是" + levelid);

            displayAvatar(mLogo, avatar);
            mUserGrade.setText(levelname);
            mUserGrade.setBackground(getResources().getDrawable(R.drawable.shape_rect_usercenter));

            application.levelId.clear();

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

            mMoney1.setText(mList.get(1).get("onemonth"));
            mMoney2.setText(mList.get(1).get("threemonth"));
            mMoney3.setText(mList.get(1).get("halfyear"));
            mMoney4.setText(mList.get(1).get("oneyear"));

            mMoney5.setText(mList.get(2).get("onemonth"));
            mMoney6.setText(mList.get(2).get("threemonth"));
            mMoney7.setText(mList.get(2).get("halfyear"));
            mMoney8.setText(mList.get(2).get("oneyear"));


            if (levelid.equals("1")) {
                mLpay1.setVisibility(View.VISIBLE);
                mLpay2.setVisibility(View.VISIBLE);
                mPay.setText("升级为黄金会员");
                mPay1.setText("升级为铂金会员");
                mDate.setVisibility(View.INVISIBLE);
            } else if (levelid.equals("2")) {
                mLpay1.setVisibility(View.VISIBLE);
                mLpay2.setVisibility(View.VISIBLE);
                mPay.setText("续费");
                mPay1.setText("升级为铂金会员");
                mDate.setText(member_expire);
            } else if (levelid.equals("3")) {
                mLpay1.setVisibility(View.GONE);
                mLpay2.setVisibility(View.VISIBLE);
                mPay1.setText("续费");
                mDate.setText(member_expire);
            }

            b = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
