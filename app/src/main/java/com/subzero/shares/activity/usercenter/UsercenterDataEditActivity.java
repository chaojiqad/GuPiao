package com.subzero.shares.activity.usercenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.bean.User;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 个人中心
 * Created by xzf on 2016/4/18.
 */
public class UsercenterDataEditActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "UsercenterData";

    private EditText mPhone;
    private EditText mNikeName;
    private EditText mEmail;
    private EditText mSharesOld;
    private EditText et_wx_id;

    private TextView tv_wx_group;
    private TextView tv_sex;
    private TextView tv_birthday;
    private TextView tv_city;
    private TextView tv_industry;

    private TextView mCancel;
    private TextView mOk;
    private TextView mEdit;

    private RelativeLayout mWxGroup;
    private RelativeLayout mSex;
    private RelativeLayout mBirthday;
    private RelativeLayout mCity;
    private RelativeLayout mIndustry;

    private ImageButton iBack;


    //是否是可以编辑的状态
    private boolean isEdit = false;

    /**
     * 微信标志
     */
    public final static int WEIXIN = 100;

    /**
     * 性别标志
     */
    public final static int SEX = 101;

    /**
     * 城市标志
     */
    public final static int CITY = 102;

    /**
     * 行业标志
     */
    public final static int INDUSTRY = 103;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_personal_data_edit);

        mPhone = (EditText) findViewById(R.id.et_ac_user_phone);
        mNikeName = (EditText) findViewById(R.id.et_ac_user_nikename);
        mEdit = (TextView) findViewById(R.id.tv_ac_user_edit);
        mEmail = (EditText) findViewById(R.id.et_ac_user_email);
        mSharesOld = (EditText) findViewById(R.id.et_ac_user_shares_old);
        mCancel = (TextView) findViewById(R.id.tv_ac_user_cancel);
        tv_wx_group = (TextView) findViewById(R.id.tv_wx_group);
        et_wx_id = (EditText) findViewById(R.id.et_wx_id);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_industry = (TextView) findViewById(R.id.tv_industry);
        mOk = (TextView) findViewById(R.id.tv_ac_user_ok);
        mWxGroup = (RelativeLayout) findViewById(R.id.rl_wx_group);
        mSex = (RelativeLayout) findViewById(R.id.rl_sex);
        mBirthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        mCity = (RelativeLayout) findViewById(R.id.rl_city);
        mIndustry = (RelativeLayout) findViewById(R.id.rl_industry);
        iBack = (ImageButton) findViewById(R.id.img_back);


    }

    @Override
    protected void initData() {

        /*如果在首页没有保存user的信息  那么在这个页面就再次调用*/
        if (application.getUser() != null) {
            setUserInfo(application.getUser());
        } else {
            getUserInfo(SharedPreferencesUtils.getTokenId(this));
        }


    }

    @Override
    protected void initListener() {
        mPhone.setOnClickListener(this);
        mNikeName.setOnClickListener(this);
        mEmail.setOnClickListener(this);
        mEdit.setOnClickListener(this);
        mSharesOld.setOnClickListener(this);
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mWxGroup.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mCity.setOnClickListener(this);
        mIndustry.setOnClickListener(this);
        mBirthday.setOnClickListener(this);
        iBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_ac_user_phone:
                break;
            case R.id.et_ac_user_nikename:
                break;
            case R.id.et_ac_user_email:
                break;
            case R.id.et_ac_user_shares_old:
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_ac_user_cancel:

                mEdit.setVisibility(View.VISIBLE);
                mOk.setVisibility(View.GONE);
                isEdit = false;
                setViewEditStatus();
                hideInputMethold();
                setUserInfo(application.getUser());
                mCancel.setVisibility(View.GONE);
                iBack.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ac_user_ok:
                mEdit.setVisibility(View.VISIBLE);
                mOk.setVisibility(View.GONE);
                isEdit = false;
                //显示隐藏键盘
                hideInputMethold();
                //设置控件的状态
                setViewEditStatus();
                mCancel.setVisibility(View.GONE);
                iBack.setVisibility(View.VISIBLE);
                //更新用户信息
                userInfoUpdate(SharedPreferencesUtils.getTokenId(this), getUpdateUserInfo());

                break;
            case R.id.tv_ac_user_edit:
                mEdit.setVisibility(View.GONE);
                mOk.setVisibility(View.VISIBLE);
                isEdit = true;
                setViewEditStatus();
                mCancel.setVisibility(View.VISIBLE);
                iBack.setVisibility(View.GONE);
                break;

            case R.id.rl_sex:

                if (isEdit) {
                    startActivityForResult(new Intent(this, UsercenterChangeSex.class), SEX);
                }
                break;

            case R.id.rl_birthday:

                if (isEdit) {

                    showDateAlertDialog(StringUtils.split(tv_birthday.getText().toString().trim()));
                }

                break;

            case R.id.rl_city:

                if (isEdit) {
                    startActivityForResult(new Intent(this, UserCityListActivity.class), CITY);
                }

                break;

            case R.id.rl_industry:

                if (isEdit) {
                    startActivityForResult(new Intent(this, UsercenterProfessionChoseActivity.class), INDUSTRY);
                }

                break;


            case R.id.rl_wx_group:

                if (isEdit) {
                    startActivityForResult(new Intent(this, UsercenterPersonalGroup.class), WEIXIN);
                }

                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(String token) {

        showLoadingDialog("正在加载用户信息……");

        RequestParams params = new RequestParams(WebUrl.GETUSERINFO);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取信息返回的结果：" + result);
                analysisJson(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

                hideLoaddingDialog();
            }
        });
    }

    /**
     * 解析返回的json数据
     *
     * @param result
     */
    private void analysisJson(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            /*解析data部分信息*/
            JSONObject user = js.getJSONObject("data");
            String avatar = user.getString("avatar");
            String level = user.getString("level");
            String phone = user.getString("phone");
            String name = user.getString("name");
            String email = user.getString("email");
            String wxgroup = user.getString("wxgroup");
            String wxid = user.getString("wxid");
            String sex = user.getString("sex");
            String birthday = user.getString("birthday");
            String shareage = user.getString("shareage");
            String city = user.getString("city");
            String industry = user.getString("industry");


            User u = new User(avatar, level, phone, name, email, wxgroup, wxid, sex, birthday, shareage, city, industry);

            application.setUser(u);

            setUserInfo(u);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case WEIXIN:

                if (resultCode == RESULT_OK) {
                    String wxgroup = data.getStringExtra("wxgroup");
                    tv_wx_group.setText(wxgroup);
                }


                break;
            case SEX:

                if (resultCode == RESULT_OK) {
                    String sex = data.getStringExtra("sex");
                    tv_sex.setText(sex);
                }

                break;
            case CITY:
                if (resultCode == RESULT_OK) {
                    String region = data.getStringExtra("region");
                    tv_city.setText(region);
                }
                break;
            case INDUSTRY:
                if (resultCode == RESULT_OK) {
                    String profession = data.getStringExtra("profession");
                    tv_industry.setText(profession);
                }
                break;
        }
    }

    /**
     * 获取编辑之后的用户信息用户
     *
     * @return
     */
    private User getUpdateUserInfo() {

        User u = new User();

        u.setPhone(mPhone.getText().toString().trim());
        u.setName(mNikeName.getText().toString().trim());
        u.setEmail(mEmail.getText().toString().trim());
        u.setShareage(mSharesOld.getText().toString().trim());
        u.setWxgroup(tv_wx_group.getText().toString().trim());
        u.setWxid(et_wx_id.getText().toString().trim());
        u.setSex(tv_sex.getText().toString().trim());
        u.setBirthday(tv_birthday.getText().toString().trim());
        u.setCity(tv_city.getText().toString().trim());
        u.setIndustry(tv_industry.getText().toString().trim());


        return u;
    }

    /**
     * 设置用户信息
     *
     * @param u
     */
    private void setUserInfo(User u) {

        if (u != null) {
            mPhone.setText(u.getPhone());
            mNikeName.setText(u.getName());
            mEmail.setText(u.getEmail());
            mSharesOld.setText(u.getShareage());
            tv_wx_group.setText(u.getWxgroup());
            et_wx_id.setText(u.getWxid());
            tv_sex.setText(u.getSex());
            tv_birthday.setText(u.getBirthday());
            tv_city.setText(u.getCity());
            tv_industry.setText(u.getIndustry());
        }
    }

    /**
     * 显示生日对话框
     */
    private void showDateAlertDialog(int[] data) {
        /* 设置alertDialog背景透明，无边框 */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View
                .inflate(this, R.layout.alertdialog_date, null);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker_birthday);
        datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        datePicker.init(data[0], data[1], data[2], new DatePicker.OnDateChangedListener() {
            //监听选择的出生日期
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                tv_birthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            }
        });
        TextView cancel = (TextView) view
                .findViewById(R.id.alertdialog_date_cancel);
        TextView ok = (TextView) view.findViewById(R.id.alertdialog_date_ok);

        final AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

    }


    /**
     * 修改用户信息
     *
     * @param token
     * @param u
     */
    private void userInfoUpdate(String token, final User u) {

        showLoadingDialog("正在上传数据……");

        RequestParams params = new RequestParams(WebUrl.INfOUPDATE);
        params.addBodyParameter("token", token);
        params.addBodyParameter("phone", u.getPhone());
        params.addBodyParameter("name", u.getName());
        params.addBodyParameter("email", u.getEmail());
        params.addBodyParameter("weixingroup", u.getWxgroup());
        params.addBodyParameter("weixinid", u.getWxid());
        params.addBodyParameter("sex", u.getSex());
        params.addBodyParameter("birthday", u.getBirthday());
        params.addBodyParameter("shareage", u.getShareage());
        params.addBodyParameter("city", u.getCity());
        params.addBodyParameter("industry", u.getIndustry());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, "修改信息返回的结果：" + result);

                analysisJsonForUpdate(result);

                application.setUser(u);

                setUserInfo(application.getUser());

                /**
                 * 刷新用户缓存数据。  融云
                 *
                 * @param userInfo 需要更新的用户缓存数据。
                 */
                UserInfo userInfo = new UserInfo(SharedPreferencesUtils.getUserId(UsercenterDataEditActivity.this), application.getUser().getName(), Uri.parse(application.imagePath));
                Log.e(TAG, application.getUser().getName());
                Log.e(TAG, application.imagePath);
                RongIM.getInstance().refreshUserInfoCache(userInfo);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
            }
        });
    }

    private void analysisJsonForUpdate(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(UsercenterDataEditActivity.this, message, Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 改变
     */
    private void setViewEditStatus() {
        setEditTextEditable(mNikeName, false);
        setEditTextEditable(mSharesOld, false);
        setEditTextEditable(mEmail, false);
        setEditTextEditable(et_wx_id, false);
    }

    /**
     * 设置控件是否可以编辑
     *
     * @param editText
     * @param isRequestFocus
     */
    private void setEditTextEditable(EditText editText, boolean isRequestFocus) {
        if (isEdit) {
            editText.setFocusableInTouchMode(isEdit);
            if (isRequestFocus) {
                editText.requestFocus();
            }

        } else {
            editText.setFocusableInTouchMode(isEdit);
            editText.clearFocus();
        }
    }

    /**
     * 隐藏显示键盘
     */
    private void hideInputMethold() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (isEdit) {
            imm.showSoftInput(mPhone, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(mPhone.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {

        //判断当前是否是编辑状态  如果是的话  恢复为未编辑转态
        if (isEdit) {
            mEdit.setVisibility(View.VISIBLE);
            mOk.setVisibility(View.GONE);
            isEdit = false;
            setViewEditStatus();
            hideInputMethold();
            setUserInfo(application.getUser());
            mCancel.setVisibility(View.GONE);

            return;
        }

        super.onBackPressed();

    }

}
