package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.ui.DialogRecharge;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * usercenter钱包
 * Created by The_p on 2016/4/18.
 */
public class UsercenterWalletActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Usercenter";
    private TextView record;
    private RelativeLayout expend;
    private RelativeLayout incomes;
    private TextView tvRecharge;
    private ImageView ivBack;

    private TextView mMoney;

    private ArrayList<HashMap<String, String>> mList;
    /**
     * 0  代表的是  收支流水  1  代表的是  支出流水  2.代表的是   收入流水
     */
    private final static int AMOUNTOFWATER = 0;
    private final static int AMOUNTOUT = 1;
    private final static int AMOUNTIN = 2;


    public final static String TYPENAME = "UsercenterWalletActivity";
    private CircleImageView mLogo;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_wallet);
        record = (TextView) findViewById(R.id.tv_income_record);
        expend = (RelativeLayout) findViewById(R.id.rl_expend);
        incomes = (RelativeLayout) findViewById(R.id.rl_incomes);
        tvRecharge = (TextView) findViewById(R.id.tv_recharge);
        mMoney = (TextView) findViewById(R.id.tv_usercenter_grade);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mLogo = (CircleImageView) findViewById(R.id.iv_userlogo);
    }

    @Override
    protected void initData() {
        mList=new ArrayList<>();
        getWallet(SharedPreferencesUtils.getTokenId(this));
        UsercenterPayMoney.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                getWallet(SharedPreferencesUtils.getTokenId(UsercenterWalletActivity.this));
            }
        });

    }

    @Override
    protected void initListener() {

        record.setOnClickListener(this);
        expend.setOnClickListener(this);
        incomes.setOnClickListener(this);
        tvRecharge.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tv_income_record: {
                Intent intent = new Intent(this, UsercenterIncomeRecord.class);
                intent.putExtra(TYPENAME, AMOUNTOFWATER);
                startActivity(intent);
            }

            break;
            case R.id.rl_expend: {
                Intent intent = new Intent(this, UsercenterIncomeRecord.class);
                intent.putExtra(TYPENAME, AMOUNTOUT);
                startActivity(intent);
            }

            break;
            case R.id.rl_incomes: {
                Intent intent = new Intent(this, UsercenterIncomeRecord.class);
                intent.putExtra(TYPENAME, AMOUNTIN);
                startActivity(intent);
            }

            break;
            //充值
            case R.id.tv_recharge:
                DialogRecharge mDialog = new DialogRecharge(this, R.style.dialog);
                mDialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    public void getWallet(final String token) {

        showLoadingDialog("正在加载……");

        RequestParams params = new RequestParams(WebUrl.GETWEALTH);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取钱包返回的结果：" + result);
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
                Toast.makeText(UsercenterWalletActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterWalletActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterWalletActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            /*解析data部分信息*/
            JSONObject jo = js.getJSONObject("data");
            String money = jo.getString("money");
            String avatar = jo.getString("avatar");
            mMoney.setText(money);
            Log.e(TAG,WebUrl.FILEHOST+avatar);
            displayAvatar(mLogo, WebUrl.FILEHOST + avatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
