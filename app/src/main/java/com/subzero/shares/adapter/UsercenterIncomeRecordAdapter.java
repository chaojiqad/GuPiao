package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.AmountOfWater;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 收支记录Adapter
 * Created by The_p on 2016/4/18.
 */
public class UsercenterIncomeRecordAdapter extends BaseAdapter {


    private Context mContext;

    private ArrayList<AmountOfWater> mAmountOfWaters;

    public UsercenterIncomeRecordAdapter(Context context, ArrayList<AmountOfWater> amountOfWaters) {
        this.mContext = context;
        this.mAmountOfWaters = amountOfWaters;
    }

    @Override
    public int getCount() {
        return mAmountOfWaters.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_user_center_income_expend_record, null);
        }

        TextView money = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_income_money);
        TextView time = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_income_time);
        TextView from = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_income_from);


        /*因为是复用  所以在  收入  支出  流水接口服务器不会返回type*/
//        try {
//
//            String type = mAmountOfWaters.get(position).getType();
//            if (type.equals("收入")) {
//                money.setText("+" + mAmountOfWaters.get(position).getMoney());
//            } else {
//                money.setText(mAmountOfWaters.get(position).getMoney());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            //设置money
//            money.setText(mAmountOfWaters.get(position).getMoney());
//
//        }
        //设置money
        money.setText(mAmountOfWaters.get(position).getMoney());
        time.setText(mAmountOfWaters.get(position).getTime());
        from.setText(mAmountOfWaters.get(position).getFrom());

        return convertView;
    }


    /**
     * 刷新
     *
     * @param amountOfWaters
     */
    public void notifyDataSetChanged(ArrayList<AmountOfWater> amountOfWaters) {
        this.mAmountOfWaters = amountOfWaters;
        super.notifyDataSetChanged();
    }
}
