package com.subzero.shares.activity.communicate;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.adapter.ClassificationAdapter;
import com.subzero.shares.bean.Classification;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 发布悬赏令分类标签
 * Created by zzy on 5/6/2016.
 */
public class ClassifyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ClassifyActivity";
    private String[] items = {"沪深大盘", "美股", "港股", "新三板", "题材", "个股", "技术分析", "操作", "理财", "其他"};
    private TextView titleText;
    private ImageView shareBar;
    private ListView listView;
    private ArrayList<Classification> mClassifications;
    private ClassificationAdapter mAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.classify_reward_communicate);
        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        listView = (ListView) findViewById(R.id.lv_classify);


    }

    @Override
    protected void initData() {
        titleText.setText("选择分类");
        shareBar.setVisibility(View.INVISIBLE);
        mClassifications = new ArrayList<>();
        mAdapter = new ClassificationAdapter(this, mClassifications);
        listView.setAdapter(mAdapter);

        getRewardcate();
    }

    @Override
    protected void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ClassifyActivity.this,
                        OfferRewardActivity.class);
                intent.putExtra("classify", mClassifications.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ClassifyActivity.this,
                OfferRewardActivity.class);
        TextView tv = (TextView) v.findViewById(R.id.tv_calssify);
        intent.putExtra("classify", tv.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 获取分类
     */
    private void getRewardcate() {
        showLoadingDialog("正在加载数据……");
        RequestParams params = new RequestParams(WebUrl.GETREWARDCATE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取分类返回的结果：" + result);
                analysisJsonForAvatar(result);

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
    private void analysisJsonForAvatar(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(ClassifyActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(ClassifyActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(ClassifyActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(ClassifyActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            JSONArray ja_data = js.getJSONArray("data");
            for (int i = 0; i < ja_data.length(); i++) {
                JSONObject jo = ja_data.getJSONObject(i);
                String id = jo.getString("id");
                String catename = jo.getString("catename");
                Classification c = new Classification(id, catename);
                mClassifications.add(c);
            }

            mAdapter.notifyDataSetChanged(mClassifications);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void back(View view) {
        finish();
    }
}
