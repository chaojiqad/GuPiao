package com.subzero.shares.activity.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.subzero.shares.R;

/**
 * 退出dialog
 * Created by The_p on 2016/4/20.
 */
public class DialogExit extends Dialog  {

    Context context;

    public DialogExit(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_dialog_exit);

    }


}
