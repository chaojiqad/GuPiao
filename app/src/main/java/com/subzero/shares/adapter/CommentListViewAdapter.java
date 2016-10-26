package com.subzero.shares.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.ReportActivity;
import com.subzero.shares.bean.RewardDetailActivityBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 悬赏令详情页回复适配器
 * Created by zzy on 5/4/2016.
 */
public class CommentListViewAdapter extends BaseAdapter {
    private static final String TAG = "悬赏令详情页回复适配器";
    private Context context;
    private ArrayList<RewardDetailActivityBean> beans;


    public CommentListViewAdapter(Context context, ArrayList<RewardDetailActivityBean> beans) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_listview_rewarddetail_communicate, null);
        }
        //实例化控件
        TextView userNameText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_user_name_communicate);
        TextView timeText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_coment_time_communicate);
        TextView comentText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_coment_communicate);
        final TextView goodText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_good_num_communicate);
        final ImageView good = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_good);
        //举报按钮实例化，添加监听事件
        TextView reportView = (TextView) ViewHolderUtils.getView(convertView, R.id.report_commmunicate);
        reportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReportActivity.class);
                intent.putExtra("title", "举报");
                intent.putExtra("commentid", beans.get(position).getId());
                context.startActivity(intent);
            }
        });
        good.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!beans.get(position).isGoods()) {
                    beans.get(position).setIsGoods(true);
                    //改变点赞的数字
                    int good = Integer.valueOf(beans.get(position).getGoods()) + 1;
                    goodText.setText(good + "");
                    clikGood(beans.get(position).getId());
                    beans.get(position).setGoods((Integer.valueOf(beans.get(position).getGoods()) + 1 + ""));
                }

            }
        });

        //为控件添加数据
        userNameText.setText(beans.get(position).getReplyname());
        goodText.setText(beans.get(position).getGoods());
        timeText.setText(beans.get(position).getReplytime());
        comentText.setText(beans.get(position).getContent());

        return convertView;
    }


    private void clikGood(String id) {
        RequestParams params = new RequestParams(WebUrl.CLICKGOOD);
        params.addBodyParameter("token", SharedPreferencesUtils.getTokenId(context));
        params.addBodyParameter("id", id);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, result);
                analyResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, TAG + "错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void analyResult(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if (retCode.equals("1")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-1")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyDataSetChanged(ArrayList<RewardDetailActivityBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}

