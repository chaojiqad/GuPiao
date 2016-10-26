package com.subzero.shares.activity.usercenter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.UsercenterIncomeRecordAdapter;
import com.subzero.shares.bean.AmountOfWater;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * usercenter收支记录
 * Created by The_p on 2016/4/18.
 */
public class UsercenterIncomeRecord extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "Usercenter";

    private ImageView ivBack;
    private XListView lv;

    private ArrayList<AmountOfWater> mAmountOfWaters;


    /*记录是否是第一次加载数据*/
    private boolean isOnceLoadData = true;

    private int page = 1;

    private UsercenterIncomeRecordAdapter mAdapter;

    //标识  收支  收入 支出
    private int type;


    private TextView mtitle;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_income_expend_record);
        lv = (XListView) findViewById(R.id.lv);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mtitle = (TextView) findViewById(R.id.title);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

    }

    @Override
    protected void initData() {

        type = getIntent().getIntExtra(UsercenterWalletActivity.TYPENAME, -1);

        if (type == 0) {
            mtitle.setText("收支记录");
        } else if (type == 1) {
            mtitle.setText("我的支出");
        } else if (type == 2) {
            mtitle.setText("我的收入");
        } else {
            Toast.makeText(UsercenterIncomeRecord.this, "携带的页面出错", Toast.LENGTH_SHORT).show();
        }

        mAmountOfWaters = new ArrayList<>();
        mAdapter = new UsercenterIncomeRecordAdapter(UsercenterIncomeRecord.this, mAmountOfWaters);
        lv.setAdapter(mAdapter);

        getAmountOfWater(SharedPreferencesUtils.getTokenId(this), page, type);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @Override
    public void onRefresh() {
        getAmountOfWater(SharedPreferencesUtils.getTokenId(this), page = 1, type);
    }

    @Override
    public void onLoadMore() {
        getAmountOfWater(SharedPreferencesUtils.getTokenId(this), ++page, type);
    }


    /**
     * 获取通知信息
     */
    private void getAmountOfWater(String token, final int page, final int type) {

        if (isOnceLoadData) {
            showLoadingDialog("正在加载……");
        }

        RequestParams params = new RequestParams(WebUrl.GETAMOUNTOFWATER);

        if (type == 0) {
            params = new RequestParams(WebUrl.GETAMOUNTOFWATER);
        } else if (type == 1) {
            params = new RequestParams(WebUrl.GETAMOUNTOUT);
        } else if (type == 2) {
            params = new RequestParams(WebUrl.GETAMOUNTIN);
        } else {
            Toast.makeText(UsercenterIncomeRecord.this, "携带的页面出错", Toast.LENGTH_SHORT).show();
        }


        params.addBodyParameter("token", token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (type == 0) {
                    Log.e(TAG, "获取收支流水返回的结果：" + result);
                } else if (type == 1) {
                    Log.e(TAG, "获取支出流水返回的结果：" + result);
                } else if (type == 2) {
                    Log.e(TAG, "获取收入流水返回的结果：" + result);
                }


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
                lvStop(page);
            }
        });
    }

    /**
     * 停止刷新或者是加载的状态
     *
     * @param page
     */
    private void lvStop(int page) {

        isOnceLoadData = false;
        hideLoaddingDialog();

        if (page == 1) {
            lv.stopRefresh();
        } else {
            lv.stopLoadMore();
        }
    }

    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterIncomeRecord.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterIncomeRecord.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterIncomeRecord.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray ja = js.getJSONArray("data");

            if (ja.length() > 0 && page == 1) {
                mAmountOfWaters.clear();
            }

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb_notification = ja.getJSONObject(i);
                AmountOfWater amountOfWater = null;
                if (type == 0) {
                    String type = jb_notification.getString("type");
                    String time = jb_notification.getString("time");
                    String money = jb_notification.getString("money");
                    String from = jb_notification.getString("from");
                    amountOfWater = new AmountOfWater(from, time, type, money);
                } else {
                    String time = jb_notification.getString("time");
                    String money = jb_notification.getString("money");
                    String from = jb_notification.getString("from");
                    amountOfWater = new AmountOfWater(from, time, money);
                }

                mAmountOfWaters.add(amountOfWater);
            }

            mAdapter.notifyDataSetChanged(mAmountOfWaters);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
