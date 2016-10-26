package com.subzero.shares.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.subzero.shares.R;
import com.subzero.shares.activity.usercenter.UsercenterPayMoney;

/**
 * 充值自定义dialog
 * Created by The_p on 2016/4/20.
 */
public class DialogRecharge extends Dialog implements View.OnClickListener {
    Context context;
    public DialogRecharge(Context context) {
        super(context);
        this.context=context;
    }

    public DialogRecharge(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.ui_dialog_recharge, null);

        this.setContentView(layout);
        Button bt_1 = (Button) findViewById(R.id.bt_cancel);
        Button bt_2 = (Button) findViewById(R.id.bt_determine);
        //button监听
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancel:
                DialogRecharge.this.dismiss();
                break;
            case R.id.bt_determine:
                DialogRecharge.this.dismiss();
                context.startActivity(new Intent(getContext(), UsercenterPayMoney.class));
                break;
            default:
                break;
        }
    }
}
