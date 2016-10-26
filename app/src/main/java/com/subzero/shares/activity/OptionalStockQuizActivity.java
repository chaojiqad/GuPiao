package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.bean.StockQuizResultDataBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 股指竞猜
 * <p/>
 * Created by The_p on 2016/4/6.
 */
public class OptionalStockQuizActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "Optional";
    private ImageView ivBack;
    private ImageView betRed;
    private ImageView betGreen;
    private TextView tvGreen;
    private TextView tvRed;
    private TextView tvTodayodds;
    private TextView tvIncomes;
    private TextView tvTomorrowodds;
    private TextView rule;
    private ProgressBar progressBarGreen;
    private ProgressBar progressBarRed;
    private StockQuizResultDataBean data = null;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_stock_quiz);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        betRed = (ImageView) findViewById(R.id.bg_icon_red);
        betGreen = (ImageView) findViewById(R.id.bg_icon_green);
        tvGreen = (TextView) findViewById(R.id.tv_green);
        tvRed = (TextView) findViewById(R.id.tv_red);
        progressBarGreen = (ProgressBar) findViewById(R.id.received_green_progressbar);
        progressBarRed = (ProgressBar) findViewById(R.id.received_red_progressbar);
        tvTodayodds = (TextView) findViewById(R.id.tv_todayodds);
        tvIncomes = (TextView) findViewById(R.id.tv_incomes);
        rule = (TextView) findViewById(R.id.tv_rule);
        tvTomorrowodds = (TextView) findViewById(R.id.tv_tomorrowodds);
    }

    @Override
    protected void initData() {
        data = (StockQuizResultDataBean) getIntent().getSerializableExtra("quizData");
        if (data != null) {
            int green = data.getGreens();
            int red = data.getReds();
            String income = data.getIncome();
            String rule1 = data.getRule();
            String todayodds = data.getTodayodds();
            String tomorrowodds = data.getTomorrowodds();

            rule.setText(rule1);
            tvGreen.setText(green + "%");
            tvRed.setText(red + "%");
            tvIncomes.setText(income);
            tvTodayodds.setText(todayodds);
            tvTomorrowodds.setText(tomorrowodds);

            progressBarGreen.setProgress(green);
            progressBarRed.setProgress(red);
        }
//        connectService();
    }

    @Override
    protected void initListener() {
        //后退
        ivBack.setOnClickListener(this);
        betGreen.setOnClickListener(this);
        betRed.setOnClickListener(this);

    }

    /**
     * 返回上一页面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = 0;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bg_icon_green:
                i++;
            case R.id.bg_icon_red:
                i++;
                Intent intent = new Intent(this, OptionalBetActivity.class);
//                intent.putExtra("data",data);
                intent.addFlags(i);
                startActivity(intent);
                break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        connectService();
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
                Log.e(TAG, "择机股指竞猜返回的数据：" + result);
                serviceRerult(result);
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

    /**
     * 服务器返回结果
     *
     * @param arg0
     */
    private void serviceRerult(String arg0) {

        try {
            JSONObject jo = new JSONObject(arg0);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if ("-100".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            String jb=jo.getString("data");
            data = new Gson().fromJson(jb, StockQuizResultDataBean.class);
            setResult();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setResult() {
        //设置首页顶部股指竞猜结果
        int sum = data.getReds() + data.getGreens();
        int red, green;
        if (sum == 0) {
            red = data.getReds();
            green = data.getGreens();
        } else {
            red = data.getReds() * 100 / sum;
            green = data.getGreens() * 100 / sum;
        }
        data.setReds(red);
        data.setGreens(green);
        String odds = data.getOdds();

        data.setTodayodds(odds);
        data.setTomorrowodds(odds);
        tvRed.setText(red + "%");
        tvGreen.setText(green + "%");
        progressBarGreen.setProgress(green);
        progressBarRed.setProgress(red);
    }

}
