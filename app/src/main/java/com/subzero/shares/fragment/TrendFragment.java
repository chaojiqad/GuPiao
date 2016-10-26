package com.subzero.shares.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.newsdetail.NewsDetailActivity;
import com.subzero.shares.activity.newsdetail.OpenAccountActivity;
import com.subzero.shares.activity.newsdetail.ThrendClassDetail;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.ThrendFagmentListViewAdapter;
import com.subzero.shares.application.MyApplication;
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
import static com.subzero.shares.R.color.subzero_head_checked_text_color;
import static com.subzero.shares.R.color.subzero_head_unchecked_text_color;

/**
 * 趋势
 * Created by xzf on 2016/4/5.
 */
public class TrendFragment extends BaseFragment {

    private static final String TAG = "TrendFragment";
    private RadioGroup headerRadioGroup;
    private View view;
    private ViewPager viewPager;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private ImageView searchBar;
    private TextView kaiHuView;
    private XListView listView1;
    private XListView listView2;
    private ArrayList<ThrenFragmentBean> consultationBeans;
    private ArrayList<ThrenFragmentBean> courseBeans;
    private boolean isFirstLoadData = true;
    private ThrendFagmentListViewAdapter mListViewAdapter1;
    private ThrendFagmentListViewAdapter mListViewAdapter2;
    private String[] title = {"策略资讯", "趋势课堂"};

    private int page = 1;

    @Override
    protected View initView() {

        initViews();
        initData();
        initListener();
        return view;
    }

    //初始化自定义view
    protected void initViews() {
        //加载布局
        view = View.inflate(getActivity(), R.layout.main_thrend, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        //实例化对象
        headerRadioGroup = (RadioGroup) view.findViewById(R.id.rg_activtiy_index_head_navi);
        mRadioButton1 = (RadioButton) headerRadioGroup.findViewById(R.id.rg_activtiy_index_head_navi_1);
        mRadioButton2 = (RadioButton) headerRadioGroup.findViewById(R.id.rg_activtiy_index_head_navi_2);
        searchBar = (ImageView) view.findViewById(R.id.search_bar);
        kaiHuView = (TextView) view.findViewById(R.id.textView);

        consultationBeans = new ArrayList<ThrenFragmentBean>();
        courseBeans = new ArrayList<ThrenFragmentBean>();
        //为title设置背景，字体颜色
        searchBar.setVisibility(View.INVISIBLE);
        mRadioButton1.setText(title[0]);
        mRadioButton2.setText(title[1]);

        mRadioButton1.setChecked(true);
        mRadioButton2.setChecked(false);


    }

    private void initData() {

        //第一次进入首页请求数据
        getDataFromServer(WebUrl.GETCONSULTATION, page);
        getDataFromServer(WebUrl.GETCOURSE, page);

    }

    //初始化监听器
    private void initListener() {
        headerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rg_activtiy_index_head_navi_1:
                        switchViewPager(0);
                        break;
                    case R.id.rg_activtiy_index_head_navi_2:
                        switchViewPager(1);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new MyViewPagerListener());
        kaiHuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OpenAccountActivity.class));
            }
        });
        viewPager.setAdapter(new ViewPagerAdapter());
    }


    //顶部导航切换
    private void switchViewPager(int index) {
        //设置顶部导航selector状态
        if (index == 0) {
            mRadioButton1.setTextColor(getResources().getColor(subzero_head_checked_text_color));
            mRadioButton2.setTextColor(getResources().getColor(subzero_head_unchecked_text_color));
            viewPager.setCurrentItem(index);
        } else {
            mRadioButton1.setTextColor(getResources().getColor(subzero_head_unchecked_text_color));
            mRadioButton2.setTextColor(getResources().getColor(subzero_head_checked_text_color));
            viewPager.setCurrentItem(index);
        }


    }


    private void getDataFromServer(final String path, final int page) {
        if (isFirstLoadData)
            showLoadingDialog("正在加载……");

        RequestParams params = new RequestParams(path);
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "首页返回的数据" + result);
                analyzeResult(result, path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "首页错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                isFirstLoadData = false;
                if (viewPager.getCurrentItem() == 0) {
                    listView1.stopRefresh();
                    listView1.stopLoadMore();
                } else {
                    listView2.stopRefresh();
                    listView2.stopLoadMore();
                }
            }
        });
    }

    //解析从服务器返回的数据
    private void analyzeResult(String result, String path) {
        try {
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            }
            //解析数据
            JSONArray ja = js.getJSONArray("data");

            if (page == 1) {
                if (consultationBeans != null && WebUrl.GETCONSULTATION == path)
                    consultationBeans.clear();
                else if (courseBeans != null && WebUrl.GETCOURSE == path)
                    courseBeans.clear();
            }

            for (int i = 0; i < ja.length(); i++) {

                JSONObject jo = ja.getJSONObject(i);
                String id = jo.getString("id");
                String title = jo.getString("title");
                String website = jo.getString("website");
                String time = jo.getString("time");
                String msg = jo.getString("message");
                String[] times = time.split(" ");
                time = times[0];
                //解析图片url
                String imgString=jo.getString("image");
//                String regx="[url\":\"0-9+]";
//                Pattern pattern=Pattern.compile(regx);
//                Matcher matcher=pattern.matcher(imgString);
//                matcher.
//                Log.e(TAG,"imgString"+imgString);
                ThrendlItemPhotoBean threndlItemPhotoBean=new Gson().fromJson(imgString,ThrendlItemPhotoBean.class);
                ArrayList<ThrendItemPhotoUrlBean> photo = (ArrayList<ThrendItemPhotoUrlBean>) threndlItemPhotoBean.getPhoto();
                ThrendItemPhotoUrlBean photoUrlBean = photo.get(0);

                String imgUrl=null;
                String url = photoUrlBean.getUrl();
                imgUrl=WebUrl.FILEHOST1+url;
                if (path == WebUrl.GETCONSULTATION) {
                    consultationBeans.add(new ThrenFragmentBean(id,title, website, time, imgUrl, msg));
                } else {
                    courseBeans.add(new ThrenFragmentBean(id,title, website, time, imgUrl, msg));
                }

            }

            if (viewPager.getCurrentItem() == 0) {
                mListViewAdapter1.notifyDataSetChanged(consultationBeans);
            } else {
                mListViewAdapter2.notifyDataSetChanged(courseBeans);
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }finally {
            hideLoaddingDialog();
        }
    }


    protected class ViewPagerAdapter extends PagerAdapter {
        private XListView listView;
        private ThrendFagmentListViewAdapter adapter;

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(getActivity(), R.layout.listview_viewpager, null);
            if (position == 0) {
                listView = listView1 = (XListView) view.findViewById(R.id.listView);
                adapter = mListViewAdapter1 = new ThrendFagmentListViewAdapter(getActivity(), consultationBeans);
            } else {
                listView = listView2 = (XListView) view.findViewById(R.id.listView);
                adapter = mListViewAdapter2 = new ThrendFagmentListViewAdapter(getActivity(), courseBeans);
            }

            listView.setAdapter(adapter);
            listView.setPullLoadEnable(true);
            listView.setXListViewListener(new MyXlistViewListener(position));
            listView.setOnItemClickListener(new MyListViewItemClickListener());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    protected class MyXlistViewListener implements XListView.IXListViewListener {
        private int TYPE;

        public MyXlistViewListener(int TYPE) {
            this.TYPE = TYPE;
        }

        //下拉刷新
        @Override
        public void onRefresh() {
            if (TYPE == 0)
                getDataFromServer(WebUrl.GETCONSULTATION, page = 1);
            else
                getDataFromServer(WebUrl.GETCOURSE, page = 1);
        }

        //上拉加载
        @Override
        public void onLoadMore() {
            if (TYPE == 0)
                getDataFromServer(WebUrl.GETCONSULTATION, ++page);
            else
                getDataFromServer(WebUrl.GETCOURSE, ++page);
        }
    }

    protected class MyListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (viewPager.getCurrentItem() == 0) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("data", consultationBeans.get(position - 1));
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), ThrendClassDetail.class);
                intent.putExtra("data", courseBeans.get(position - 1));
                startActivity(intent);
            }
        }
    }

    //ViewPager变化监听器
    protected class MyViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //根ViewPager变化据设置顶部导航selector状态
            if (position == 0) {
                mRadioButton1.setTextColor(getResources().getColor(subzero_head_checked_text_color));
                mRadioButton2.setTextColor(getResources().getColor(subzero_head_unchecked_text_color));
                mRadioButton1.setChecked(true);
                mRadioButton2.setChecked(false);
                searchBar.setVisibility(View.INVISIBLE);
                kaiHuView.setVisibility(View.VISIBLE);
            } else {
                mRadioButton1.setTextColor(getResources().getColor(subzero_head_unchecked_text_color));
                mRadioButton2.setTextColor(getResources().getColor(subzero_head_checked_text_color));
                mRadioButton1.setChecked(false);
                mRadioButton2.setChecked(true);
                searchBar.setVisibility(View.VISIBLE);
                kaiHuView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    /*被继承的成员变量只能在方法里面 初始化 */
    protected MyApplication application;
    private ProgressDialog progressDialog;

    /**
     * 显示加载的对话框
     *
     * @param content
     */
    public void showLoadingDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog
                    (getActivity());
        }
        progressDialog.setMessage(content);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 隐藏对话框
     */
    public void hideLoaddingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }



}
