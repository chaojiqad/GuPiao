package com.subzero.shares.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.OfferRewardActivity;
import com.subzero.shares.activity.communicate.RewardDetailActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.CommunicateAdapter2;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 悬赏令
 * Created by xzf on 2016/5/9.
 */
public class OrdeRrewardFragment extends BaseFragment implements XListView.IXListViewListener {

    private static final String TAG = "OrdeRreward";
    private XListView lv;
    private CommunicateAdapter2 mListViewAdapter2;
    private ArrayList<CommunicateBean> beans;
    private int page = 1;
    private TextView mCommite;

    @Override
    protected View initView() {


        View v = View.inflate(getActivity(), R.layout.fragment_orde_rerward, null);
        lv = (XListView) v.findViewById(R.id.xlv);
        mCommite = (TextView) v.findViewById(R.id.tv_orde_mycommit);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

        initData();
        initListener();

        return v;

    }

    private void initData() {
        beans = new ArrayList<>();
        mListViewAdapter2 = new CommunicateAdapter2(getActivity(), beans);
        lv.setAdapter(mListViewAdapter2);
        getOfferList(page);



        OfferRewardActivity.setSuccesNotification(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                Log.e(TAG,"我被回调啦");
                getOfferList(page = 1);
            }
        },0);
    }

    private void initListener() {
        mCommite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OfferRewardActivity.class));
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RewardDetailActivity.class);
                CommunicateBean bean = beans.get(position - 1);

                intent.putExtra("data", bean);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取悬赏令
     *
     * @param page
     */
    private void getOfferList(final int page) {

        RequestParams params = new RequestParams(WebUrl.GETOFFERREWARD);
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "悬赏令成功信息：" + result);
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
                lvStop(page);
            }
        });
    }

    private void analyzeResult(String result, int page) {
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
            if (page == 1)
                beans.clear();
            //解析数据
            JSONArray ja = js.getJSONArray("data");
            CommunicateBean bean = null;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                String id = jo.getString("id");
                String useravatar = jo.getString("useravatar");
                String useravatarUrl = WebUrl.FILEHOST + useravatar;
                String uname = jo.getString("user_nicename");
                String catename = jo.getString("catename");
                String time = jo.getString("time");
                String content = jo.getString("content");
                String rewardmoney = jo.getString("rewardmoney");
                String replycount = jo.getString("replycount");
                String title = jo.getString("title");
                String images = jo.getString("images");
                String userid = jo.getString("userid");


                String[] imgs = new String[0];

                if (!images.equals(null)) {

                    imgs = images.split(",");

                    for (int j = 0; j < imgs.length; j++) {
                        imgs[j] = WebUrl.FILEHOST + imgs[j];
                    }
                }



                bean = new CommunicateBean(id, useravatarUrl, uname, catename, time, content, replycount, rewardmoney, title, imgs, userid);
                beans.add(bean);
            }

            mListViewAdapter2.notifyDataSetChanged(beans);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 停止刷新或者是加载的状态
     *
     * @param page
     */
    private void lvStop(int page) {

        if (page == 1) {
            lv.stopRefresh();
        } else {
            lv.stopLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        getOfferList(page = 1);
    }

    @Override
    public void onLoadMore() {
        getOfferList(++page);
    }

}
