package com.subzero.shares.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.MyCommitActivity;
import com.subzero.shares.activity.newsdetail.OpenAccountActivity;
import com.subzero.shares.adapter.CommunicationAdapter;
import com.subzero.shares.bean.UserGroup;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.db.UserDB;
import com.subzero.shares.utils.SharedPreferencesUtils;

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
 * 交流
 * Created by xzf on 2016/4/5.
 */
public class CommunicationFragment extends BaseFragment {

    private RadioGroup headerRadioGroup;
    private View view;
    private ViewPager viewPager;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private ImageView searchBar;
    private TextView kaiHuView;

    private ArrayList<Fragment> mFragments;

    private String[] title = {"聊天室", "悬赏令"};

    private String TAG = "CommunicationFragment";
    private ChatRoomFragment chatRoomFragment;


    private static Succes mSucces;

    /**
     * 记录是都是第几次显示
     */
    private int i = 0;

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
        view = View.inflate(getActivity(), R.layout.main_communicate, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);


        //实例化顶部导航对象
        headerRadioGroup = (RadioGroup) view.findViewById(R.id.rg_activtiy_index_head_navi);
        mRadioButton1 = (RadioButton) headerRadioGroup.findViewById(R.id.rg_activtiy_index_head_navi_1);
        mRadioButton2 = (RadioButton) headerRadioGroup.findViewById(R.id.rg_activtiy_index_head_navi_2);
        searchBar = (ImageView) view.findViewById(R.id.search_bar);
        kaiHuView = (TextView) view.findViewById(R.id.textView);

        //为title设置背景，字体颜色
        searchBar.setVisibility(View.INVISIBLE);
        mRadioButton1.setText(title[0]);
        mRadioButton2.setText(title[1]);

        mRadioButton1.setChecked(true);
        mRadioButton2.setChecked(false);


    }

    //加载数据
    private void initData() {

        mFragments = new ArrayList<>();
        chatRoomFragment = new ChatRoomFragment();
        mFragments.add(chatRoomFragment);
        mFragments.add(new OrdeRrewardFragment());

        viewPager.setAdapter(new CommunicationAdapter(getChildFragmentManager(), mFragments));

        getUserGroup(SharedPreferencesUtils.getTokenId(getActivity()));

    }

    //初始化监听器
    protected void initListener() {
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
                String text = kaiHuView.getText().toString();
                switch (text) {
                    case "开户":
                        startActivity(new Intent(getActivity(), OpenAccountActivity.class));
                        break;
                    case "我的发布":
                        startActivity(new Intent(getActivity(), MyCommitActivity.class));
                        break;
                }
            }
        });
    }


    public static void setSucces(Succes succes) {
        mSucces = succes;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden && mSucces != null && ++i > 1) {
            mSucces.onSucces(2222);
            getUserGroup(SharedPreferencesUtils.getTokenId(getActivity()));
        }
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
                kaiHuView.setText("开户");

            } else {
                mRadioButton1.setTextColor(getResources().getColor(subzero_head_unchecked_text_color));
                mRadioButton2.setTextColor(getResources().getColor(subzero_head_checked_text_color));
                mRadioButton1.setChecked(false);
                mRadioButton2.setChecked(true);
                kaiHuView.setText("我的发布");

            }

        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 获取群员的信息
     */
    private void getUserGroup(String token) {
        RequestParams params = new RequestParams(WebUrl.USERGROUP);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取群员信息返回的结果：" + result);
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

            }
        });
    }

    /**
     * 解析json
     *
     * @param result
     */
    private void analysisJson(String result) {

        try {
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

            if (ja.length() <= 0) {
                return;
            }


            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                String userid = jo.getString("id");
                String avatar = jo.getString("avatar");
                avatar = WebUrl.FILEHOST + avatar;
                String username = jo.getString("name");
                String groupid = jo.getString("groupid");

                UserGroup bean = new UserGroup(userid, avatar, username, groupid);

                if (UserDB.getInstance(getActivity()).checkUserGroup(userid, groupid)) {
                    UserDB.getInstance(getActivity()).updateUserGroup(bean);
                } else {
                    //把数据插入数据库
                    UserDB.getInstance(getActivity()).saveUserGroup(bean);
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
