package com.subzero.shares.github.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.github.ereza.CustomActivityOnCrash;


public class ErrorActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		initView();
	}
	private void initView()
	{
		((TextView)findViewById(R.id.tv_error_details)).setText(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
		Button btRestartApp = (Button) findViewById(R.id.bt_restartApp);
		Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
		btRestartApp.setOnClickListener(new MyOnClickListener(restartActivityClass));
		if(restartActivityClass != null){
			btRestartApp.setText("重启App");
		}
	}
	private final class MyOnClickListener implements OnClickListener
	{
		private Class<? extends Activity> cls;
		public MyOnClickListener(Class<? extends Activity> cls) {
			this.cls = cls;
		}
		@Override
		public void onClick(View v)
		{
			if (R.id.bt_restartApp == v.getId())
			{
				if (cls != null) {
					Intent intent = new Intent(ErrorActivity.this, cls);
					CustomActivityOnCrash.restartApplicationWithIntent(ErrorActivity.this, intent);
				}
				else {
					CustomActivityOnCrash.closeApplication(ErrorActivity.this);
				}
			}
		}
	}
}
