package com.subzero.shares.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.OptionalExperAnalysis;
import com.subzero.shares.activity.OptionalExperAnalysisNoRecord;
import com.subzero.shares.activity.OptionalLiveBroadcast;
import com.subzero.shares.activity.OptionalStockPoolActivity;
import com.subzero.shares.activity.OptionalStockQuizActivity;
import com.subzero.shares.activity.newsdetail.OpenAccountActivity;
import com.subzero.shares.bean.StockQuizResultDataBean;
import com.subzero.shares.config.Filed;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 择机 Fragment
 * Created by xzf on 2016/4/5.
 */
public class OptionalMachineFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MachineFragment";
    private TextView tvRed;
    private TextView tvGreen;
    private ProgressBar progressBarGreen;
    private ProgressBar progressBarRed;
    private StockQuizResultDataBean data = null;
    public static final int STOCK_QUIZ_RESUL = 0;

    @Override
    protected View initView() {
        View viewOptional = View.inflate(getActivity(), R.layout.fragment_optional, null);
        TextView openCount = (TextView) viewOptional.findViewById(R.id.tv_open_count);
        ImageView stock_quiz = (ImageView) viewOptional.findViewById(R.id.optional_stock_quiz);
        ImageView quantitative_stock_pool = (ImageView) viewOptional.findViewById(R.id.optional_quantitative_stock_pool);
        ImageView live_broadcast = (ImageView) viewOptional.findViewById(R.id.optional_live_broadcast);
        ImageView expert_analysis = (ImageView) viewOptional.findViewById(R.id.optional_expert_analysis);
        tvRed = (TextView) viewOptional.findViewById(R.id.tv_red);
        tvGreen = (TextView) viewOptional.findViewById(R.id.tv_green);
        progressBarGreen = (ProgressBar) viewOptional.findViewById(R.id.received_green_progressbar);
        progressBarRed = (ProgressBar) viewOptional.findViewById(R.id.received_red_progressbar);
        initData();

        stock_quiz.setOnClickListener(this);
        quantitative_stock_pool.setOnClickListener(this);
        live_broadcast.setOnClickListener(this);
        expert_analysis.setOnClickListener(this);
        openCount.setOnClickListener(this);
        return viewOptional;
    }


    private void initData() {
//        connectService();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            connectService();
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
        String tokenId = SharedPreferencesUtils.getTokenId(getActivity());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if ("-100".equals(retCode)) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        String userLevel = SharedPreferencesUtils.getUserType(getActivity());
        switch (v.getId()) {
            case R.id.optional_stock_quiz:
                //进入股指竞猜
                Intent intent0 = new Intent(getActivity(), OptionalStockQuizActivity.class);
                if (data != null) {
                    //传递StockQuizResultDataBean到OptionalStockQuizActivity
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("quizData", data);
                    intent0.putExtras(bundle);
                }
                startActivity(intent0);
                break;
            case R.id.optional_quantitative_stock_pool:
                Intent intent1 = new Intent(getActivity(), OptionalStockPoolActivity.class);
                startActivity(intent1);
                break;
            case R.id.optional_live_broadcast:
                Log.e(TAG, "用户类型：" + userLevel);
                Intent intent = new Intent(getActivity(), OptionalLiveBroadcast.class);
                if (userLevel.equals(Filed.GUWENLEVEL)) {
                    //顾问进入的Activity
                    intent.addFlags(1);

                } else if (userLevel.equals(Filed.USERLEVEL)) {
                    //普通用户进入ACtivity
                    intent.addFlags(2);
                }

                startActivity(intent);
                break;
            case R.id.optional_expert_analysis:
                Log.e(TAG, "用户类型：" + userLevel);
                if (userLevel.equals(Filed.GUWENLEVEL)) {
                    //顾问进入的Activity
                    Intent intent4 = new Intent(getActivity(), OptionalExperAnalysisNoRecord.class);
                    startActivity(intent4);
                } else if (userLevel.equals(Filed.USERLEVEL)) {
                    //普通用户进入ACtivity
                    Intent intent3 = new Intent(getActivity(), OptionalExperAnalysis.class);
                    startActivity(intent3);
                }
                break;
            case R.id.tv_open_count:
                startActivity(new Intent(getActivity(), OpenAccountActivity.class));
                break;
            default:
                break;
        }
    }


}
