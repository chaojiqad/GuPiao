package com.subzero.shares.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.newsdetail.PayActivity;
import com.subzero.shares.activity.usercenter.UsercenterVipActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.OptionalLiveBroadcastDetailAdapter;
import com.subzero.shares.bean.OptionalLiveBean;
import com.subzero.shares.bean.OptionalLiveCommentBean;
import com.subzero.shares.bean.UserGroup;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 盘中直播详情
 * Created by The_p on 2016/4/15.
 */
public class OptionalLiveBroadcastDetail extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private static final String TAG = "OptionalLive";
    private ImageView ivBack;
    private TextView tvComent;
    private TextView tvBrief;
    private TextView tvDate;
    private TextView tvGood;
    private TextView tvHistorylisten;
    private TextView tvHoster;
    private TextView tvListener;
    private TextView tvTitle;
    private TextView tvTag;
    private TextView tvTime;
    private TextView subscribe;
    private ImageView ivIamge;
    private ImageView ivGood;
    //    private String userId;
    private String tokenId;
    private String directSeedingID;
    private Button tvSend;
    private EditText edComment;
    private ImageView ivAwards;
    private ImageView ivLive;
    private ImageView ivComment;
    private XListView lv;
    private String subscri;
    private OptionalLiveBroadcastDetailAdapter adapter;
    private ArrayList<OptionalLiveCommentBean> beans;
    private OptionalLiveBean bean;
    private int page = 1;
//    private boolean isFirstGetData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.ui_item_live_broadcast_detail11111);
        lv = (XListView) findViewById(R.id.lv);
        View header = View.inflate(this, R.layout.ui_item_live_broadcast_detail_header, null);
        lv.addHeaderView(header);
        beans = new ArrayList<>();
        adapter = new OptionalLiveBroadcastDetailAdapter(this, beans);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        edComment = (EditText) findViewById(R.id.et_comment);
        tvComent = (TextView) header.findViewById(R.id.tv_comment);
        tvBrief = (TextView) header.findViewById(R.id.tv_brief);
        tvDate = (TextView) header.findViewById(R.id.tv_date);
        tvGood = (TextView) header.findViewById(R.id.tv_good);
        subscribe = (TextView) header.findViewById(R.id.tv_subscribe);
        tvHistorylisten = (TextView) header.findViewById(R.id.tv_historylisten);
        tvHoster = (TextView) header.findViewById(R.id.tv_hoster);
        tvListener = (TextView) header.findViewById(R.id.tv_listener);
        tvTitle = (TextView) header.findViewById(R.id.tv_live_detail);
        tvTag = (TextView) header.findViewById(R.id.tv_tag);
        tvTime = (TextView) header.findViewById(R.id.tv_clock);
        ivIamge = (ImageView) header.findViewById(R.id.iv_image);
        ivGood = (ImageView) findViewById(R.id.iv_good);
        tvSend = (Button) findViewById(R.id.tv_send);
        ivAwards = (ImageView) header.findViewById(R.id.iv_awards);
        ivLive = (ImageView) header.findViewById(R.id.iv_live);
        ivComment = (ImageView) header.findViewById(R.id.iv_comment);


    }

    @Override
    protected void initData() {
        bean = (OptionalLiveBean) getIntent().getSerializableExtra("bean");
        displayAvatar(ivIamge, bean.getAvatar());
        tvTitle.setText(bean.getTitle());
        tvTag.setText(bean.getTag());
        tvBrief.setText(bean.getDesc());
        tvHoster.setText("顾问——" + bean.getAdvisor());
        tvDate.setText(bean.getDate());
        tvTime.setText(bean.getTime());
        tokenId = SharedPreferencesUtils.getTokenId(this);
        directSeedingID = bean.getLiveid();
        getComments(page);
        lv.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        ivGood.setOnClickListener(this);
        tvSend.setOnClickListener(this);

        ivAwards.setOnClickListener(this);
        ivLive.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        lv.setXListViewListener(this);
        lv.setPullLoadEnable(true);

    }

    /**
     * 点击事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //点赞
            case R.id.iv_good:
                clickGood();
                break;
            //订阅
            case R.id.iv_comment:
//                listen直播收听
//                ivComment.setImageResource(R.mipmap.commentselect);

                String path = null;
                if ("1".equals(subscri)) {
                    path = WebUrl.UNLISTEN;
                } else {
                    path = WebUrl.LISTEN;
                }

                listen(path);
                break;
            case R.id.tv_send:
                //发表评论
                sendComment();
                break;
            case R.id.iv_awards:
//                ivAwards.setImageResource(R.mipmap.dashangselect);
//                ivLive.setImageResource(R.mipmap.zhibo);
//                ivComment.setImageResource(R.mipmap.comment);
                //跳到打赏
                Intent intent1 = new Intent(this, PayActivity.class);
                intent1.putExtra("postid",bean.getUid());
                intent1.addFlags(6);
                startActivity(intent1);
                break;
            case R.id.iv_live:
                valirung(bean.getRoomid());

                break;

        }
    }

    private void valirung(final String id) {
        showLoadingDialogForNotCancel("正在进入直播间....");
        String token = SharedPreferencesUtils.getTokenId(this);
        RequestParams params = new RequestParams(WebUrl.VALIRUN);
        params.addBodyParameter("token", token);
        params.addBodyParameter("roomid", id);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "顾问发布直播列表：" + result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-2".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    }
                    JSONObject jb = jo.getJSONObject("data");
                    String run = jb.getString("run");
                    final String info = jb.getString("info");
                    String groupid = jb.getString("groupid");
                    switch (run) {
                        //未订阅
                        case "-1":
                            Toast.makeText(OptionalLiveBroadcastDetail.this, info, Toast.LENGTH_SHORT).show();
                            break;
                        //未付款
                        case "-2":
                            final String shouldpay = jb.getString("shouldpay");
                            AlertDialog.Builder bulider = new AlertDialog.Builder(OptionalLiveBroadcastDetail.this);
                            bulider.setMessage(info).setNegativeButton("取消", null).setNeutralButton("支付虚拟币", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(OptionalLiveBroadcastDetail.this, PayPoolActivity.class);
                                    intent.putExtra("price", shouldpay);
                                    intent.addFlags(5);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            }).setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(OptionalLiveBroadcastDetail.this, UsercenterVipActivity.class);
                                    startActivity(intent);
                                }
                            }).create().show();
                            break;
                        //进入直播间
                        default:
                            intoLive(groupid);
                            break;
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
                hideLoaddingDialog();
            }
        });
    }


    private void intoLive(String grouid) {
        initAvatar();
        application.userId = bean.getUid();
        RongIM.getInstance().startGroupChat(OptionalLiveBroadcastDetail.this, grouid, bean.getTitle());
    }

    /**
     * 初始化信息
     */

    private void initAvatar() {
        /**
         * 加载用户头像
         */
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {


            @Override
            public UserInfo getUserInfo(String userID) {

                UserInfo userInfo;


                if (userID.equals(SharedPreferencesUtils.getUserId(OptionalLiveBroadcastDetail.this))) {

                    UserGroup userGroup = new UserGroup();

                    userGroup.setUsername(application.getUser().getName());
                    userGroup.setUserid(userID);
                    userGroup.setAvatar(WebUrl.FILEHOST + application.getUser().getAvatar());


                    userInfo = new UserInfo(userID, userGroup.getUsername(), Uri.parse(userGroup.getAvatar()));
                    Log.e(TAG, "盘中直播中自己的name" + userID + userGroup.getUsername() + Uri.parse(userGroup.getAvatar()));
                } else {


                    userInfo = new UserInfo(userID, bean.getAdvisor(), Uri.parse(bean.getAvatar()));
                    Log.e(TAG, "盘中直播中自顾问的name"+ bean.getAdvisor());
                }

                return userInfo;
            }

        }, true);
    }

    private void listen(final String path) {
        if (WebUrl.LISTEN.equals(path))
            showLoadingDialog("正在订阅....");
        else
            showLoadingDialog("正在取消订阅....");
        RequestParams params = new RequestParams(path);
        params.addBodyParameter("token", tokenId);
        params.addBodyParameter("liveid", directSeedingID);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                //服务器返回结果
                Log.e(TAG, "直播服务器订阅返回结果" + s);
                try {
                    JSONObject jo = new JSONObject(s);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    } else if ("-3".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                    if (path.equals(WebUrl.LISTEN)) {
                        subscribe.setText("已订阅");
                        ivComment.setImageResource(R.mipmap.commentselect);

                        subscri = "1";
                    } else {
                        subscribe.setText("未订阅");
                        subscri = "0";
                        ivComment.setImageResource(R.mipmap.comment);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
                lv.stopLoadMore();
                lv.stopRefresh();
            }
        });
    }


    /**
     * 链接服务器
     */
    private void getComments(int page) {
        //加载dialog
//        if (isFirstGetData)
//            showLoadingDialog("正在加载……");
        RequestParams params = new RequestParams(WebUrl.GETLIVEDETAIL);
        params.addBodyParameter("token", tokenId);
        params.addBodyParameter("liveid", directSeedingID);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                //服务器返回结果
                Log.e(TAG, "直播服务器返回结果" + s);
                analysisResult(s);

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                lv.stopLoadMore();
                lv.stopRefresh();
//                isFirstGetData=false;
//                hideLoaddingDialog();
            }
        });
    }

    /**
     * 服务器返回结果
     *
     * @param s
     */
    private void analysisResult(String s) {
        try {
            JSONObject jo = new JSONObject(s);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }
            if (page == 1)
                beans.clear();
            JSONObject ja = jo.getJSONObject("data");

            JSONObject js = ja.getJSONObject("detail");
            String goods = js.getString("goods");
            String listener = js.getString("listener");
            String listenerHistory = js.getString("listenerhistory");
            String commen = js.getString("commentcount");
            subscri = js.getString("subscri");

            tvGood.setText(goods);
            tvComent.setText(commen);
            tvListener.setText(listener);
            tvHistorylisten.setText(listenerHistory);
            if ("1".equals(subscri)) {
                subscribe.setText("已订阅");
                ivComment.setImageResource(R.mipmap.commentselect);

            } else {
                subscribe.setText("未订阅");
                ivComment.setImageResource(R.mipmap.comment);
            }

            JSONArray jsa = ja.getJSONArray("comments");
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jb = jsa.getJSONObject(i);
                String name = jb.getString("name");
                String time = jb.getString("create_time");
                String content = jb.getString("content");
                beans.add(new OptionalLiveCommentBean(name, time, content));
            }
            adapter.notifyDataSetChanged(beans);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * 发表评论
     */
    private void sendComment() {
        String text = edComment.getText().toString();
        if (text.equals("")) {
            Toast.makeText(this, "内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoadingDialog("正在提交评论....");
        RequestParams params1 = new RequestParams(WebUrl.COMMENT1);
        params1.addBodyParameter("token", tokenId);
        params1.addBodyParameter("liveid", directSeedingID);
        params1.addBodyParameter("content", text);
        x.http().post(params1, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "评论返回的内容:" + s);
                try {
                    JSONObject jo = new JSONObject(s);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    }
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                    edComment.setText("");
                    getComments(page = 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
            }
        });
    }

    /**
     * 点赞
     */
    public void clickGood() {

        RequestParams params = new RequestParams(WebUrl.LIVEGOOD);
        params.addBodyParameter("token", tokenId);
        params.addBodyParameter("liveid", directSeedingID);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "点赞返回的信息:" + s);
                try {
                    JSONObject jo = new JSONObject(s);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;
                    }
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                    int intGood = Integer.parseInt(tvGood.getText().toString()) + 1;
                    tvGood.setText(intGood + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onRefresh() {
        getComments(page = 1);
    }

    @Override
    public void onLoadMore() {
        getComments(++page);
    }
}
