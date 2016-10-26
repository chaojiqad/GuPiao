package com.subzero.shares.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.application.MyApplication;
import com.subzero.shares.bean.OptionalLiveBean;
import com.subzero.shares.bean.UserGroup;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ActivityCollector;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 顾问发布的直播 Adapter
 * Created by zzy on 2016/4/8.
 */
public class LiveBroadcastGuWenAdapter extends BaseAdapter {
    private static final String TAG = "LiveGuWenAdapter";
    private Activity context;
    private ArrayList<OptionalLiveBean> beans;


    public LiveBroadcastGuWenAdapter(Activity context, ArrayList<OptionalLiveBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size();
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ui_item_live_broadcast_guwen, null);
        }
        TextView title = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_title);
        TextView cls = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_cls);
        TextView date = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_date);
        TextView time = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
        TextView openLive = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_state);

        final OptionalLiveBean bean = beans.get(position);
        title.setText(bean.getTitle());
        cls.setText(bean.getTag());
        String hm = bean.getTime().substring(5, 16);
        String[] times = hm.split(" ");
        String[] dates = times[0].split("-");
        date.setText(dates[0] + "月" + dates[1] + "号");
        time.setText(times[1]);
        openLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"正在进入直播间,请勿重复开启!",Toast.LENGTH_SHORT).show();
                valirung(bean.getRoomid(), bean.getTitle());
            }
        });


        return convertView;
    }

    private void valirung(String id, final String title) {
        String token = SharedPreferencesUtils.getTokenId(context);
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        ActivityCollector.finishAll();
                        return;
                    }
                    JSONObject jb = jo.getJSONObject("data");
                    String run = jb.getString("run");
                    String info = jb.getString("info");
                    final String groupid = jb.getString("groupid");
                    if ("-1".equals(run)) {
                        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
                    } else if ("-2".equals(run)) {

                    } else if ("1".equals(run)) {
                        initAvatar();
                        RongIM.getInstance().startGroupChat(context, groupid, title);

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

            }
        });
    }


    private void initAvatar() {
        /**
         * 加载用户头像
         */
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {


            @Override
            public UserInfo getUserInfo(String userID) {


                UserInfo userInfo = null;


                if (userID.equals(SharedPreferencesUtils.getUserId(context))) {

                    UserGroup userGroup = new UserGroup();

                    MyApplication application = (MyApplication) context.getApplication();
                    userGroup.setUsername(application.getUser().getName());
                    userGroup.setUserid(userID);
                    userGroup.setAvatar(WebUrl.FILEHOST + application.getUser().getAvatar());


                    userInfo = new UserInfo(userID, userGroup.getUsername(), Uri.parse(userGroup.getAvatar()));
                    Log.e(TAG, "只显示顾问的信息"+userID + userGroup.getUsername() + Uri.parse(userGroup.getAvatar()));
                }

                return userInfo;
            }

        }, true);
    }

    public void notifyDataSetChanged(ArrayList<OptionalLiveBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
