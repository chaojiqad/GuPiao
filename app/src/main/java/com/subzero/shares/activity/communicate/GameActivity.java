package com.subzero.shares.activity.communicate;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.RulesAdapter;
import com.subzero.shares.bean.Rules;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 有奖大赛
 * Created by zzy on 2016/4/26.
 */
public class GameActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private String title = "有奖大赛";
    private XListView xListView;
    private ImageView shareBar;
    private TextView titleText;
    private TextView gameText;
    private int page = 1;
    private boolean isFirstGetData = true;

    private RulesAdapter mRulesAdapter;
    private ArrayList<Rules> beans;

    public static final String TAG = "GameActivity";

    @Override
    protected void initView() {
        setContentView(R.layout.game_communicate);

        xListView = (XListView) findViewById(R.id.listView);
        xListView.setPullLoadEnable(true);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        titleText = (TextView) findViewById(R.id.textTitle);
        gameText = (TextView) findViewById(R.id.myCommit_text_rewardDetail);
        shareBar.setVisibility(View.INVISIBLE);
        titleText.setText(title);
        gameText.setText("我要参赛");
    }

    @Override
    protected void initData() {
        showLoadingDialog("正在加载数据.....");
        getDataFromServer(page);
        beans=new ArrayList<>();
        mRulesAdapter = new RulesAdapter(this, beans);
        xListView.setAdapter(mRulesAdapter);

        JoinGameActivity.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                getDataFromServer(page=1);
            }
        });
    }

    @Override
    protected void initListener() {
        gameText.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(GameActivity.this,GameDetalActivity.class);
                intent.putExtra("rules",beans.get(position-1));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplication(), JoinGameActivity.class));
    }

    private void getDataFromServer(final int page) {
        RequestParams params = new RequestParams(WebUrl.RULES);
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "牛栏山：" + result);
                analyzeResult(result, page);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "交流页错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                xListView.stopRefresh();
                xListView.stopLoadMore();
                if (isFirstGetData) {
                    isFirstGetData = false;
                    hideLoaddingDialog();
                }
                Log.e(TAG, "执行onFinished（）");
            }
        });
    }

    private void analyzeResult(String result, int page) {
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
            if (page == 1)
                beans.clear();
            //解析数据
            JSONArray ja = js.getJSONArray("data");
            Rules bean = null;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);

                String id = jo.getString("id");
                String title = jo.getString("title");
                String time = jo.getString("time");
                String image = jo.getString("images");
                String content = jo.getString("message");
                String website = jo.getString("website");

                String[] imgs = new String[0];

                if (!image.equals(null)) {

                    imgs = image.split(",");

                    for (int j = 0; j < imgs.length; j++) {
                        imgs[j] = WebUrl.FILEHOST + imgs[j];
                        Log.e(TAG,imgs[j]);
                    }
                }


                bean = new Rules(time, content, title, imgs,id,website);
                beans.add(bean);

            }

            mRulesAdapter.notifyDataSetChanged(beans);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getDataFromServer(page = 1);
    }

    @Override
    public void onLoadMore() {
        getDataFromServer(page++);
    }


    public void back(View view) {
        finish();
    }
}
