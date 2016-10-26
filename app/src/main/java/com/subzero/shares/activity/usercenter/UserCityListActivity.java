package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.MainActivity;
import com.subzero.shares.bean.Region;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzf on 2016/4/29.
 */
public class UserCityListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "CityList";
    private ImageView mBack;

    private ListView mCityListView;

    private ArrayAdapter<String> mAdapter;

    private List<String> dataList;
    /**
     * 省列表
     */
    private ArrayList<Region> mProvinceList;

    /**
     * 城市列表
     */
    private ArrayList<Region> mCityList;

    /**
     * 县列表
     */
    private ArrayList<Region> mCountyList;

    /**
     * 当前选中的级别   0代表的是省份 1代表的是 城市  2代表的是地区
     */
    private int currentLevel = 0;

    //当前选择的省
    private String provinceName;
    //当前选中的市
    private String cityName;

    //当前选择的省
    private String provincePid;
    //当前选中的市
    private String cityPid;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_city);
        mBack = (ImageView) findViewById(R.id.iv_back);
        mCityListView = (ListView) findViewById(R.id.lv_user_city_select);
    }

    @Override
    protected void initData() {
        mProvinceList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mCountyList = new ArrayList<>();
        dataList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        mCityListView.setAdapter(mAdapter);

        getArea("");
    }

    @Override
    protected void initListener() {
        mBack.setOnClickListener(this);
        mCityListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    /**
     * 获取城市列表
     *
     * @param pid
     */
    public void getArea(String pid) {

        showLoadingDialog("正在加载……");

        RequestParams params = new RequestParams(WebUrl.GETAREA);
        params.addBodyParameter("pid", pid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取城市信息返回的结果：" + result);
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
                Toast.makeText(UserCityListActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UserCityListActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UserCityListActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            /*解析data部分信息*/
            JSONArray ja = js.getJSONArray("data");

            dataList.clear();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject JaRegion = ja.getJSONObject(i);
                String id = JaRegion.getString("id");
                String pid = JaRegion.getString("pid");
                String name = JaRegion.getString("name");

                Log.e(TAG, "id:" + id + "^^^^^^^pid:" + pid + "&&&&&&&name:" + name);

                Region region = new Region(name, pid, id);


                dataList.add(name);

                if (currentLevel == 0) {
                    mProvinceList.add(region);
                } else if (currentLevel == 1) {
                    mCityList.add(region);
                } else {
                    mCountyList.add(region);
                }

            }

            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if (currentLevel == 0) {

            Region province = mProvinceList.get(position);

            provincePid = province.getId();
            provinceName = province.getName();
            getArea(provincePid);
            currentLevel = 1;
        } else if (currentLevel == 1) {

            Region city = mCityList.get(position);

            cityPid = city.getId();
            cityName = city.getName();
            getArea(cityPid);
            currentLevel = 2;
        } else {
            String countyName = mCountyList.get(position).getName();

            Intent intent = new Intent(UserCityListActivity.this,
                    MainActivity.class);
            intent.putExtra("region", provinceName + "|" + cityName + "|" + countyName);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (currentLevel == 0) {
            finish();
        } else if (currentLevel == 1) {
            currentLevel=0;
            getArea("");
        } else {
            currentLevel=1;
            getArea(provincePid);
        }
    }
}
