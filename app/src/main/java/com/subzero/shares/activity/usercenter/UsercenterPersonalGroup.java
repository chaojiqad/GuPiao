package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.MainActivity;
import com.subzero.shares.adapter.GroupForListAdapter;
import com.subzero.shares.bean.WXGroup;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 群组选择
 * Created by The_p on 2016/4/21.
 */
public class UsercenterPersonalGroup extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "PersonalGroup";

    private ImageView ivBack;

    private ListView mGroupList;

    private ArrayList<WXGroup> mGroups;

    private View head;

    private GroupForListAdapter mAdapter;

    private TextView mNewUser;


    @Override
    protected void initView() {
        setContentView(R.layout.user_center_personal_data_group);
        mGroupList = (ListView) findViewById(R.id.lv_user_per_group);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        head = View.inflate(this, R.layout.item_group_head, null);
        head.findViewById(R.id.tv_item_group_head_id).setOnClickListener(this);
        mNewUser = (TextView) head.findViewById(R.id.tv_item_group_head_id);

    }


    @Override
    protected void initData() {

        mGroupList.addHeaderView(head);
        mGroups = new ArrayList<>();
        mAdapter = new GroupForListAdapter(this, mGroups);
        mGroupList.setAdapter(mAdapter);

        getWxGroups();
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        mNewUser.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_item_group_head_id:
                Intent intent = new Intent(this,
                        MainActivity.class);
                intent.putExtra("wxgroup", mNewUser.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }


    }


    /**
     * 获取通知信息
     */
    private void getWxGroups() {

        showLoadingDialog("正在加载……");


        RequestParams params = new RequestParams(WebUrl.GETWXGROUPS);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取微信群组的返回的信息：" + result);
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


    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterPersonalGroup.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterPersonalGroup.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterPersonalGroup.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray ja_data = js.getJSONArray("data");

            WXGroup wxGroup = null;

            for (int i = 0; i < ja_data.length(); i++) {
                JSONArray js_group = ja_data.getJSONArray(i);

                wxGroup = new WXGroup();


                String[] groups = new String[js_group.length()];
                for (int j = 0; j < js_group.length(); j++) {
                    String group = js_group.getString(j);
                    groups[j] = group;
                }
                wxGroup.setGroups(groups);
                mGroups.add(wxGroup);
            }


            mAdapter.notifyDataSetChanged(mGroups);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
