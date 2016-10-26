package com.subzero.shares.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.subzero.shares.R;

/**
 * 充值自定义dialog
 * Created by The_p on 2016/4/20.
 */
public class DialogUserLogo extends Dialog  {
//    private static final int CAMERA = 100;
//    private static final int PICTURE = 200;
    Context context;
    private float density;

    public DialogUserLogo(Context context) {
        super(context);
        this.context=context;
    }

    public DialogUserLogo(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.ui_dialog_user_logo, null);

        this.setContentView(layout);
    }



}
