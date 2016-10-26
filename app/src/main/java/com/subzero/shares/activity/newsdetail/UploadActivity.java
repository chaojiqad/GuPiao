package com.subzero.shares.activity.newsdetail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.ClassifyAdapter;
import com.subzero.shares.adapter.SearchResultThrendAdapter;
import com.subzero.shares.bean.ClassifyBean;
import com.subzero.shares.bean.ThrenFragmentBean;
import com.subzero.shares.bean.ThrendItemPhotoUrlBean;
import com.subzero.shares.bean.ThrendlItemPhotoBean;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 搜索
 * Created by zzy on 2016/4/8.
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "UploadActivity";
    private ArrayList<TextView> textViews;
    private GridView gridView;
    private String[] time = {"1天内", "3天内", "7天内", "1个月", "3个月", "6个月", "1年内"};
    private int[] uptime={1*86400,3*86400,7*86400,30*86400,3*30*86400,6*30*86400,365*86400,};
    private EditText nameCorse;
    private TextView ensure;
    private LinearLayout ll;
    private XListView listView;
    private ListView lv;
    private boolean isVisibleListview = false;
    private int upLoadTimeId = 1;
    private int page = 1;
    private ArrayList<ThrenFragmentBean> beans;
    private ArrayList<ClassifyBean> classifyValues;
    private SearchResultThrendAdapter adapter;
    private ClassifyAdapter classifyAdapter;
    private TextView noResult;
    private boolean isFirstGetData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.upload_thrend);
        inintGridView();

        nameCorse = (EditText) findViewById(R.id.et_nameCourse);
        ensure = (TextView) findViewById(R.id.tv_ensure);
        ll = (LinearLayout) findViewById(R.id.ll_uptime_cate);
        noResult = (TextView) findViewById(R.id.tv_no_result);
        listView = (XListView) findViewById(R.id.listView);
        lv = (ListView) findViewById(R.id.lv_classify);
        beans = new ArrayList<ThrenFragmentBean>();
        classifyValues = new ArrayList<ClassifyBean>();
        adapter = new SearchResultThrendAdapter(this, beans);
        classifyAdapter = new ClassifyAdapter(this, classifyValues);
    }

    @Override
    protected void initData() {
        listView.setVisibility(View.GONE);
        noResult.setVisibility(View.INVISIBLE);
        getCoursecate();
    }

    @Override
    protected void initListener() {
        ensure.setOnClickListener(this);
        listView.setPullRefreshEnable(false);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        lv.setAdapter(classifyAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int x = 0; x < classifyValues.size(); x++) {
                    if (x == position) {
                        classifyValues.get(x).setSelect(true);
                    } else {
                        classifyValues.get(x).setSelect(false);
                    }
                }
                classifyAdapter.notifyDataSetChanged(classifyValues);
            }
        });

    }

    private void getCoursecate() {
        if (isFirstGetData)
            showLoadingDialog("正在获取分类列表....");
        RequestParams params = new RequestParams(WebUrl.GETCOURSECATE);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "课堂类别返回的结果：" + result);
                analysisClassify(result);
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
                isFirstGetData = false;
            }
        });
    }

    private void analysisClassify(String result) {
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
            }

            //解析数据
            JSONArray ja = jo.getJSONArray("data");
            for (int x = 0; x < ja.length(); x++) {
                JSONObject jb = ja.getJSONObject(x);
                String id = jb.getString("term_id");
                String name = jb.getString("name");
                if (x == 0) {
                    ClassifyBean bean = new ClassifyBean(name, id);
                    bean.setSelect(true);
                    classifyValues.add(bean);
                } else {
                    classifyValues.add(new ClassifyBean(name, id));
                }
            }
            classifyAdapter.notifyDataSetChanged(classifyValues);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        String keyWord = nameCorse.getText().toString().trim();
        String cateId = null;
        for (int i = 0; i < classifyValues.size(); i++) {
            ClassifyBean bean = classifyValues.get(i);
            if (bean.isSelect()) {
                cateId = bean.getId();
            }
        }
        if (keyWord.equals("") || cateId.equals("")) {
            Toast.makeText(this, "课程名称或者课堂类别不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, "upLoadTimeId：" + upLoadTimeId);
        Log.e(TAG, "cateId：" + cateId);
        Log.e(TAG, "page：" + page);
        Log.e(TAG, "keyWord：" + keyWord);

        getSearchResult(uptime[upLoadTimeId], cateId, keyWord, page);
        if (!isVisibleListview) {
            ll.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            isVisibleListview = !isVisibleListview;
        }
    }

    private void getSearchResult(int time, String cateId, String keyword, int page) {
        showLoadingDialog("正在加载数据.....");
        beans.clear();
        adapter.notifyDataSetChanged(beans);
        final RequestParams params = new RequestParams(WebUrl.SEARCH);
        params.addBodyParameter("time", time + "");
        params.addBodyParameter("cateid", cateId);
        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e("搜索成功返回的数据", result);
//                Log.e("搜索提交的URL", params.getUri());
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
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                noResult.setVisibility(View.VISIBLE);
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }

            //解析数据
            JSONArray ja = js.getJSONArray("data");

            for (int i = 0; i < ja.length(); i++) {

                JSONObject jo = ja.getJSONObject(i);

//                String id = jo.getString("id");
                String title = jo.getString("title");
                String website = jo.getString("website");
                String time = jo.getString("time");
                String msg = jo.getString("message");
                String[] times = time.split(" ");
                time = times[0];
                //解析图片url
                String imgString = jo.getString("image");
                Log.e(TAG, "图片URL：" + imgString);
                ThrendlItemPhotoBean threndlItemPhotoBean = new Gson().fromJson(imgString, ThrendlItemPhotoBean.class);
                ArrayList<ThrendItemPhotoUrlBean> photo = (ArrayList<ThrendItemPhotoUrlBean>) threndlItemPhotoBean.getPhoto();
                ThrendItemPhotoUrlBean photoUrlBean = photo.get(0);
                String imgUrl = null;
                String url = photoUrlBean.getUrl();
                imgUrl = WebUrl.FILEHOST1 + url;

                beans.add(new ThrenFragmentBean(null, title, website, time, imgUrl, msg));
            }

            adapter.notifyDataSetChanged(beans);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void inintGridView() {

        textViews = new ArrayList<TextView>();
        gridView = (GridView) findViewById(R.id.gridview);
        //设置gridview没有点击效果
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new MyGridViewAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (TextView t : textViews) {
                    t.setTextColor(getResources().getColor(R.color.upload_textcolor));
                    t.setBackgroundResource(R.drawable.uploadtime_unchecked_shape);
                }
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.item_live_broadcast));
                textView.setBackgroundResource(R.drawable.uploadtime_checked_shape);
                upLoadTimeId = position ;
            }
        });

    }

    //重写返回键
    @Override
    public void onBackPressed() {
        if (isVisibleListview) {
            ll.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.GONE);
            isVisibleListview = !isVisibleListview;
        } else {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ThrendClassDetail.class);
        intent.addFlags(2);
        intent.putExtra("data", beans.get(position - 1));
        startActivity(intent);
    }

    private class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            textView = new TextView(getApplicationContext());
            textView.setText(time[position]);
            textView.setTextColor(getResources().getColor(R.color.upload_textcolor));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//设置textView对象布局
            textViews.add(textView);
            if (position == 0) {
                textView.setTextColor(getResources().getColor(R.color.item_live_broadcast));
                textView.setBackgroundResource(R.drawable.uploadtime_checked_shape);
            } else {
                textView.setBackgroundResource(R.drawable.uploadtime_unchecked_shape);//为textView设置图片资源
            }
            return textView;
        }
    }

    public void back(View view) {
        if (isVisibleListview) {
            ll.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.GONE);
            isVisibleListview = !isVisibleListview;
        } else {
            finish();
        }
    }
}
