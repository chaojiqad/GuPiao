package com.subzero.shares.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Optional盘中直播发布界面
 * Created by The_p on 2016/4/21.
 */
public class OptionalLiveBroadcastRelease extends BaseActivity implements View.OnClickListener,TextWatcher {
    public static final String TAG = "OptionalLiveBro";
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private EditText theme;
    private EditText desc;
    private TextView startTime;
    private TextView endTime;
    private TextView cls;
    private TextView commit;
    private TextView numWord;
    private TimePickerDialog tpd;
    private String title;
    private String st;
    private String et;
    private String tag;
    private DateFormat format;
    private String date;
    private String describe;


    @Override
    protected void initView() {

        setContentView(R.layout.activity_optional_live_broadcast_release);
        rl2 = (RelativeLayout) findViewById(R.id.rl_2);
        rl3 = (RelativeLayout) findViewById(R.id.rl_3);
        rl4 = (RelativeLayout) findViewById(R.id.rl_4);
        theme = (EditText) findViewById(R.id.et_edittheme);
        desc = (EditText) findViewById(R.id.et_desc);
        startTime = (TextView) findViewById(R.id.tv_starttime);
        endTime = (TextView) findViewById(R.id.tv_endtime);
        cls = (TextView) findViewById(R.id.tv_cls);
        numWord = (TextView) findViewById(R.id.tv_num_word);
        commit = (TextView) findViewById(R.id.tv_commit);

    }

    @Override
    protected void initData() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = format.format(new Date());
    }

    @Override
    protected void initListener() {
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        commit.setOnClickListener(this);
        desc.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_2:
                timeDialog(startTime);
                break;
            case R.id.rl_3:
                timeDialog(endTime);
                break;
            case R.id.rl_4:
                Intent intent4 = new Intent(this, OptionalLiveBroadcastReleaseLable.class);
                startActivityForResult(intent4, 100);
                break;
            case R.id.tv_commit:
                title = theme.getText().toString().trim();
                describe = desc.getText().toString().trim();
                st = startTime.getText().toString().trim();
                et = endTime.getText().toString().trim();
                tag = cls.getText().toString().trim();
                Log.e(TAG, "title:" + title + ";st:" + st + ";et:" + et + ";tag:" + tag);
//                ||describe.equals("")
                if (title.equals("") || st.equals("") || et.equals("") || tag.equals("")) {
                    Toast.makeText(this, "主题时间标签不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    releaseLive();
                }
                break;
        }

    }

    private void releaseLive() {
        showLoadingDialog("正在发布直播....");
        String token = SharedPreferencesUtils.getTokenId(this);
        RequestParams params = new RequestParams(WebUrl.RELEASELIVE);

//        2016-05-31 04:20 7:20:49
        st = date.replaceAll(" .+", " " + st + ":00");
        et = date.replaceAll(" .+", " " + et + ":00");
        System.out.println(st + et);
        params.addBodyParameter("token", token);
        params.addBodyParameter("title", title);
        params.addBodyParameter("start_time", st);
        params.addBodyParameter("end_time", et);
        params.addBodyParameter("tag", tag);
        params.addBodyParameter("desc", describe);
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "发布直播返回的结果:" + result);
                try {
                    JSONObject jo = new JSONObject(result);
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
                    setResult(RESULT_OK);
                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            cls.setText(data.getStringExtra("cls"));
        }
    }

    private void timeDialog(final TextView tv) {
        long time = System.currentTimeMillis();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String regx = "[0-9]{2}";
                String hour = hourOfDay + "";
                String min = minute + "";
                if (!hour.matches(regx)) {
                    hour = "0" + hour;
                }
                if (!min.matches(regx)) {
                    min = "0" + min;
                }
                tv.setText(hour + ":" + min);
                tpd.dismiss();
            }
        }, hour, min, true);

        tpd.show();
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        numWord.setText(s.length() + "/" + 150);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
