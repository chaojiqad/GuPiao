package com.subzero.shares.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 下注
 * Created by zzy on 5/13/2016.
 */
public class OptionalBetActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OptionalBetActivity";
    private TextView title;
    private TextView ensure;
    private TextView redGreen;
    private TextView countMoney;
    private ImageView share;
    private EditText getMoney;
    private String select;
    private String money;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_bet);

        title = (TextView) findViewById(R.id.textTitle);
        redGreen = (TextView) findViewById(R.id.tv_red_green);
        ensure = (TextView) findViewById(R.id.tv_ensure);
        countMoney = (TextView) findViewById(R.id.tv_conunt_money);
        share = (ImageView) findViewById(R.id.share_communicate);
        getMoney = (EditText) findViewById(R.id.et_bet_money);
    }

    @Override
    protected void initData() {
//        StockQuizResultDataBean bean= (StockQuizResultDataBean) getIntent().getSerializableExtra("data");
        title.setText("下注");
        connectService();
        share.setVisibility(View.INVISIBLE);
//        countMoney.setText("您的账户余额:"+bean.getWealth());
        int flag = getIntent().getFlags();
        if (flag == 1) {
            select = "红";
        } else {
            select = "绿";
        }
        redGreen.setText("你认为明日上证收"+select);
    }

    /**
     * 获取股指竞猜数据
     */
    private void connectService() {
        RequestParams params = new RequestParams(WebUrl.GETQUIZRESULTS1);
        String tokenId = SharedPreferencesUtils.getTokenId(this);
        params.addBodyParameter("token", tokenId);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "择机股指竞猜下注返回的数据：" + result);
                try {
                    JSONObject jo=new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    } else if ("-100".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONObject jb=jo.getJSONObject("data");
                    String wealth=jb.getString("wealth");
                    countMoney.setText("您的账户余额:"+wealth);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
            }

            @Override
            public void onFinished() {
            }

        });
    }
    @Override
    protected void initListener() {
        ensure.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        money = getMoney.getText().toString().trim();
        if (money.equals("")) {
            Toast.makeText(this, "投注不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        bet();

    }

    private void bet() {
        showLoadingDialog("正在下注....");
        RequestParams params = new RequestParams(WebUrl.BET);
        params.addBodyParameter("token", SharedPreferencesUtils.getTokenId(this));
        params.addBodyParameter("select", select);
        params.addBodyParameter("money", money);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "投注返回的信息：" + result);
                analysisResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
            }
        });
    }

    private void analysisResult(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
          if (retCode.equals("-1")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }else if (retCode.equals("-4")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            //投注成功
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
