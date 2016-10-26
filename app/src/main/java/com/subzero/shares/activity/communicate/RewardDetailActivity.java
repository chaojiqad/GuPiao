package com.subzero.shares.activity.communicate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.subzero.shares.adapter.CommentListViewAdapter;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.bean.RewardDetailActivityBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ShareSDKHelper;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;


/**
 * 悬赏令详情
 * Created by zzy on 2016/4/15.
 */
public class RewardDetailActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {

    String TITLE = "悬赏令详情";
    private XListView xListView;
    private TextView textTitle;
    private TextView title;
    private TextView coin;
    private TextView time;
    private TextView content;
    private TextView comentNum;
    private View headrView;
    private CommunicateBean bean;
    private ArrayList<RewardDetailActivityBean> beans;
    private CommentListViewAdapter mListViewAdapter;
    private String TAG = "RewardDetailActivity";
    private TextView answer;
    private boolean isReal = true;
    private int page = 1;
    private int[] ads = {R.id.iv_1, R.id.iv_2, R.id.iv_3};
    private ArrayList<ImageView> imgs;
    private String mUserId;
    private ImageView mShare;


    @Override
    protected void initView() {
        setContentView(R.layout.reward_detail_communicate);
        answer = (TextView) findViewById(R.id.tv_answerquestion);
        headrView = View.inflate(getApplication(), R.layout.headview_rewarddetail_communicate, null);
        textTitle = (TextView) findViewById(R.id.textTitle);
        mShare = (ImageView) findViewById(R.id.share_communicate);
        xListView = (XListView) findViewById(R.id.listView);
        xListView.setPullLoadEnable(true);

        title = (TextView) headrView.findViewById(R.id.tv_title);
        coin = (TextView) headrView.findViewById(R.id.tv_virtual_coin);
        time = (TextView) headrView.findViewById(R.id.tv_time);
        content = (TextView) headrView.findViewById(R.id.tv_content);
        comentNum = (TextView) headrView.findViewById(R.id.tv_coment_num);
        imgs = new ArrayList<>();
        beans = new ArrayList<>();


    }


    @Override
    protected void initData() {
        textTitle.setText(TITLE);
        xListView.addHeaderView(headrView);
        Bundle bundle = getIntent().getExtras();
        bean = (CommunicateBean) bundle.get("data");
        mUserId = bean.getUserid();
        Log.e(TAG, "悬赏令的userID是：" + mUserId);
        Log.e(TAG, "当前用户的userID是：" + SharedPreferencesUtils.getUserId(RewardDetailActivity.this));
        title.setText(bean.getTitle());
        coin.setText(bean.getRewardmoney());
        time.setText(bean.getTime());
        comentNum.setText(bean.getReplycount());
        content.setText(bean.getContent());

        for (int i = 0; i < bean.getImages().length; i++) {
            ImageView img = (ImageView) headrView.findViewById(ads[i]);
            imgs.add(img);
        }

        for (int i = 0; i < bean.getImages().length; i++) {
            ImageUtils.displayAvatar(this, imgs.get(i), bean.getImages()[i]);
        }

        getDataFromServer(WebUrl.GETREWARDREPLYS, bean.getId(), page);

        if (getIntent().getFlags() == 2) {
            answer.setVisibility(View.GONE);
            isReal = false;
        }

        mListViewAdapter = new CommentListViewAdapter(this, beans);
        xListView.setAdapter(mListViewAdapter);


        ReportActivity.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                getDataFromServer(WebUrl.GETREWARDREPLYS, bean.getId(), page = 1);
            }
        });
    }

    @Override
    protected void initListener() {
        if (isReal)
            answer.setOnClickListener(this);
        mShare.setOnClickListener(this);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前的悬赏令发布者是否是当前的用户
                if (mUserId.equals(SharedPreferencesUtils.getUserId(RewardDetailActivity.this))) {

                    if (position >= 2) {
                        String currentRewardreplysId = beans.get(position - 2).getId();
                        showMoneyRewardDialog("您确定要把悬赏金额给当前这个用户吗", currentRewardreplysId);
                    }

                }
            }
        });

        for (int i = 0; i < imgs.size(); i++) {
            imgs.get(i).setOnClickListener(this);
        }
    }

    private void showMoneyRewardDialog(String content, final String replyid) {

        AlertDialog.Builder dialog = new AlertDialog.Builder
                (this);
        dialog.setMessage(content);
        dialog.setCancelable(false);
        dialog.setNegativeButton("取消", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectReply(WebUrl.SELECTREPLY, bean.getId(), replyid);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRefresh() {
        getDataFromServer(WebUrl.GETREWARDREPLYS, bean.getId(), page = 1);
    }

    @Override
    public void onLoadMore() {
        getDataFromServer(WebUrl.GETREWARDREPLYS, bean.getId(), ++page);
    }


    /**
     * 打发赏金
     *
     * @param rewardid
     * @param replyid
     */
    private void selectReply(String url, String rewardid, String replyid) {
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("rewardid", rewardid);
        params.addBodyParameter("replyid", replyid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "打发金额信息：" + result);
                analyzeResultForReply(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "打发金额错误信息：" + ex.getMessage());
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

    /**
     * 解析打发金额返回的信息
     *
     * @param result
     */
    private void analyzeResultForReply(String result) {
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
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取悬赏令的回复
     *
     * @param path
     * @param id
     * @param page
     */
    private void getDataFromServer(String path, String id, final int page) {
        RequestParams params = new RequestParams(path);
        params.addBodyParameter("rewardid", id);
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "悬赏令回复页信息：" + result);
                analyzeResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "悬赏令回复页错误信息：" + ex.getMessage());
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


    /**
     * 解析获取悬赏令回复json
     *
     * @param result
     */
    private void analyzeResult(String result) {
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
            JSONObject ja = js.getJSONObject("data");
            String replycount = ja.getString("replycount");
            //设置悬赏令的回复总条数
            comentNum.setText(replycount);
            JSONArray jo = ja.getJSONArray("replys");

            if (jo.length() > 0 && page == 1) {
                beans.clear();
            }

            for (int i = 0; i < jo.length(); i++) {
                JSONObject jj = (JSONObject) jo.get(i);
                String id = jj.getString("id");
                String replyname = jj.getString("replyname");
                String goods = jj.getString("goods");
                String replytime = jj.getString("replytime");
                String content = jj.getString("content");
                beans.add(new RewardDetailActivityBean(id, replyname, goods, replytime, content));
            }
            mListViewAdapter.notifyDataSetChanged(beans);


        } catch (JSONException e) {
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
            xListView.stopRefresh();
        } else {
            xListView.stopLoadMore();
        }
    }


    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.share_communicate:

                String shareImg = bean.getImages()[0] == null ? WebUrl.LOGO : bean.getImages()[0];
                String url = "http://123.56.82.112/gupiao/index.php?g=Api&m=Common&a=share&id=" + bean.getId() + "&type=2";
                Log.e(TAG,"分享的链接"+url);
                new ShareSDKHelper(this, shareImg, url, url, url, new Succes() {
                    @Override
                    public void onSucces(int reResultCode) {
                        sharePay(SharedPreferencesUtils.getTokenId(RewardDetailActivity.this), "2", bean.getId());
                    }
                }).showShare();
                break;

            case R.id.tv_answerquestion:
                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra("title", "我来回答");
                intent.putExtra("commentid", bean.getId());
                startActivity(intent);
                break;


            case R.id.iv_1:
                if (!bean.getImages()[0].equals(WebUrl.FILEHOST + "null")) {

                    Log.e(TAG, "跳转到相册页面1");
                    Log.e(TAG, bean.getImages()[0]);

                    ImageUtils.startPhotoActivity(this, bean.getImages()[0], imgs.get(0));
                }

                break;

            case R.id.iv_2:
                if (!bean.getImages()[1].equals(WebUrl.FILEHOST + "null")) {
                    ImageUtils.startPhotoActivity(this, bean.getImages()[1], imgs.get(1));
                }

                break;

            case R.id.iv_3:
                if (!bean.getImages()[2].equals(WebUrl.FILEHOST + "null")) {
                    ImageUtils.startPhotoActivity(this, bean.getImages()[2], imgs.get(2));
                }

                break;


        }


    }

    /**
     * 分享之后 获取奖励
     *
     * @param token
     * @param type
     * @param postid
     */
    private void sharePay(String token, String type, String postid) {
        RequestParams params = new RequestParams(WebUrl.Sharepay);
        params.addBodyParameter("token", token);
        params.addBodyParameter("type", type);
        params.addBodyParameter("postid", postid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, "分享之后获取奖励返回的参数" + result);
                analyzeResultForSharePay(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //解析从服务器返回的数据
    private void analyzeResultForSharePay(String result) {
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


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
