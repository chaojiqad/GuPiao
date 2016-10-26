package com.subzero.shares.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.usercenter.UsercenterVipActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.StockPoolAdapter;
import com.subzero.shares.bean.OptionalStockPoolItemBean;
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
 * 量化股票池
 * Created by The_p on 2016/4/7.
 */
public class OptionalStockPoolActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private static final String TAG = "量化股票池";
    private ImageView ivBack;
    private XListView lv;
    private ArrayList<OptionalStockPoolItemBean> beans;
    private StockPoolAdapter adapter;
    private int page = 1;
    private boolean isFirstGetData = true;
    private String token;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_stock_pool);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        lv = (XListView) findViewById(R.id.lv);
        beans = new ArrayList<>();
        adapter = new StockPoolAdapter(OptionalStockPoolActivity.this, beans);
        token = SharedPreferencesUtils.getTokenId(this);
    }

    @Override
    protected void initData() {
        lv.setAdapter(adapter);
        connectService(page);
        //ivBack.layout();
    }

    @Override
    protected void initListener() {
        //返回
        ivBack.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

    }

    /**
     * 服务器返回结果
     *
     * @param s
     */
    private void serviceResult(String s) {
        try {
            JSONObject jo = new JSONObject(s);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;

            }
            if (page == 1) {
                beans.clear();
            }
            JSONArray ja = jo.getJSONArray("data");
            Gson gson = new Gson();
            for (int i = 0; i < ja.length(); i++) {
                String jb = ja.getString(i);
                OptionalStockPoolItemBean bean = gson.fromJson(jb, OptionalStockPoolItemBean.class);
                beans.add(bean);
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 返回上一界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        finish();

    }

    private void connectService(int page) {
        //加载dialog
        if (isFirstGetData)
            showLoadingDialog("正在加载……");
        RequestParams params = new RequestParams(WebUrl.GETSTOCKPOOL);
        params.addBodyParameter("token", token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("股票量化池返回的信息：", s);
                //服务器返回结果
                serviceResult(s);

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                isFirstGetData = false;
                hideLoaddingDialog();
                lv.stopRefresh();
                lv.stopLoadMore();
            }
        });

    }

    @Override
    public void onRefresh() {
        connectService(page = 1);
    }

    @Override
    public void onLoadMore() {
        connectService(++page);

    }

    /**
     * item监听
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionalStockPoolItemBean osptb = beans.get(position - 1);
        String userType = SharedPreferencesUtils.getUserType(this);
        if ("3".equals(userType)) {
            Intent intent = new Intent(this, OptionalStockPoolItemActivity.class);
            intent.putExtra("data", osptb);
            startActivity(intent);
        } else {
            paypost(osptb);

        }


    }

    private void paypost(final OptionalStockPoolItemBean osptb) {
        lv.setEnabled(false);
        showLoadingDialogForNotCancel("正在加载数据....");
        RequestParams params = new RequestParams(WebUrl.VALIREAD);
        params.addBodyParameter("postid", osptb.getId());
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "请求阅读权限返回的数据:" + result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        JSONObject jb = jo.getJSONObject("data");
                        final String price = jb.getString("price");
                        AlertDialog.Builder bulider = new AlertDialog.Builder(OptionalStockPoolActivity.this);
                        bulider.setMessage(message).setNegativeButton("取消", null).setNeutralButton("支付虚拟币", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(OptionalStockPoolActivity.this, PayPoolActivity.class);
                                intent.putExtra("price", price);
                                intent.putExtra("id", osptb.getId());
                                intent.addFlags(4);
                                startActivity(intent);
                            }
                        }).setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(OptionalStockPoolActivity.this, UsercenterVipActivity.class);
                                startActivity(intent);
                            }
                        }).create().show();
                    } else {
                        Intent intent = new Intent(getApplication(), OptionalStockPoolItemActivity.class);
                        intent.putExtra("data", osptb);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                lv.setEnabled(true);
                hideLoaddingDialog();
            }
        });

    }


}
