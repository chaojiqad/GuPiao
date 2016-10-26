package com.subzero.shares.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.subzero.shares.R;
import com.subzero.shares.activity.ui.DialogExit;
import com.subzero.shares.activity.ui.DialogUserLogo;
import com.subzero.shares.activity.usercenter.UsercenterDataEditActivity;
import com.subzero.shares.activity.usercenter.UsercenterNewsActivity;
import com.subzero.shares.activity.usercenter.UsercenterRenew;
import com.subzero.shares.activity.usercenter.UsercenterSettingActivity;
import com.subzero.shares.activity.usercenter.UsercenterVipActivity;
import com.subzero.shares.activity.usercenter.UsercenterWalletActivity;
import com.subzero.shares.bean.User;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 我的
 * Created by xzf on 2016/4/5.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MyFragment";

    private RelativeLayout userData;
    private RelativeLayout userNews;
    private RelativeLayout userWallet;
    private RelativeLayout userRenew;
    private RelativeLayout userVip;
    private ImageView setting;
    private CircleImageView logo;
    private RelativeLayout exit;

    /**
     * 文件名
     */
    private static final String IMAGE_FILE_NAME = "headicon";

    /**
     * 请求拍照的请求码
     */
    private static final int REQUESTCODE_TAKE = 100;

    /**
     * 请求相册的请求码
     */
    private static final int REQUESTCODE_PICK = 101;

    /**
     * 请求剪裁的请求码
     */
    private static final int REQUESTCODE_CUTTING = 102;

    //会员级别
    private TextView level;

    //签到
    private TextView mSign;

    /**
     * 本地头像的地址
     */
    private String avatarUrl = "/sdcard/avatar.png";


    /**
     * 用来存储我们在fragment请求的个人信息
     */
    private User u;

    private final int REQUEST_CODE_GALLERY = 1001;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_user_center, null);
        userData = (RelativeLayout) view.findViewById(R.id.rl_1);
        userNews = (RelativeLayout) view.findViewById(R.id.rl_user_news);
        level = (TextView) view.findViewById(R.id.tv_usercenter_grade);
        mSign = (TextView) view.findViewById(R.id.tv_usercenter_sign);
        userWallet = (RelativeLayout) view.findViewById(R.id.rl_user_wallet);
        userRenew = (RelativeLayout) view.findViewById(R.id.rl_user_renew);
        userVip = (RelativeLayout) view.findViewById(R.id.rl_user_vip);
        setting = (ImageView) view.findViewById(R.id.iv_setting);
        logo = (CircleImageView) view.findViewById(R.id.iv_userlogo);
        exit = (RelativeLayout) view.findViewById(R.id.rl_exi);


        initData();

        userData.setOnClickListener(this);
        userNews.setOnClickListener(this);
        userWallet.setOnClickListener(this);
        userRenew.setOnClickListener(this);
        userVip.setOnClickListener(this);
        setting.setOnClickListener(this);
        exit.setOnClickListener(this);
        logo.setOnClickListener(this);
        mSign.setOnClickListener(this);

        return view;
    }

    private void initData() {

        getUserInfo(SharedPreferencesUtils.getTokenId(getActivity()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_1: {
                Intent intent = new Intent(getActivity(), UsercenterDataEditActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.rl_user_news: {
                Intent intent = new Intent(getActivity(), UsercenterNewsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.rl_user_wallet: {

                Intent intent = new Intent(getActivity(), UsercenterWalletActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.rl_user_renew: {
                if (SharedPreferencesUtils.getUserType(getActivity()).equals("3")) {
                    Toast.makeText(getActivity(), "顾问不可以进入", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), UsercenterRenew.class);
                startActivity(intent);
                break;
            }

            case R.id.rl_user_vip: {

                if (SharedPreferencesUtils.getUserType(getActivity()).equals("3")) {
                    Toast.makeText(getActivity(), "顾问不可以进入", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), UsercenterVipActivity.class);
                startActivity(intent);
                break;

            }


            case R.id.iv_setting: {
                Intent intent = new Intent(getActivity(), UsercenterSettingActivity.class);
                startActivity(intent);
                break;
            }


            case R.id.iv_userlogo:
                final DialogUserLogo dialogLogo = new DialogUserLogo(getActivity(), R.style.dialog);
                setDialogPosition(dialogLogo);
                dialogLogo.show();
                //dialog中各按钮的监听
                Button bt_1 = (Button) dialogLogo.findViewById(R.id.bt_cancel);
                Button bt_2 = (Button) dialogLogo.findViewById(R.id.bt_photograph);
                Button bt_3 = (Button) dialogLogo.findViewById(R.id.bt_photo);
                bt_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLogo.dismiss();
                    }
                });


                bt_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用系统相机程序

                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //指定调用相机拍照后的照片存储的路径
                        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                        startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                        dialogLogo.dismiss();
                    }
                });

                bt_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);

                        //调用系统打开外部图库程序
                        //Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        // pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        // startActivityForResult(pickIntent, REQUESTCODE_PICK);
                        dialogLogo.dismiss();
                    }
                });

                break;
            case R.id.rl_exi:
                final DialogExit dialogExit = new DialogExit(getActivity(), R.style.dialog);
                setDialogPosition(dialogExit);
                dialogExit.show();

                Button cancel = (Button) dialogExit.findViewById(R.id.bt_dialog_cancel);
                Button exit = (Button) dialogExit.findViewById(R.id.bt_determine);
                //button监听
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogExit.dismiss();
                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startLoginActivity();
                    }
                });

                break;

            case R.id.tv_usercenter_sign: {
                sign(SharedPreferencesUtils.getTokenId(getActivity()));
                break;

            }


        }
    }

    /**
     * 签到
     */
    private void sign(String token) {

        Toast.makeText(getActivity(), "签到中……，请勿重复签到", Toast.LENGTH_SHORT).show();


        RequestParams params = new RequestParams(WebUrl.SIGN);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取签到返回的结果：" + result);
                analysisJsonForSign(result);

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
            }
        });
    }

    /**
     * 解析返回的json数据
     *
     * @param result
     */
    private void analysisJsonForSign(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            }
            String virtualCurrency = js.getString("data");
            showAlertDialog(virtualCurrency);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取用户信息
     */
    private void getUserInfo(String token) {

        RequestParams params = new RequestParams(WebUrl.GETUSERINFO);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取信息返回的结果：" + result);
                analysisJson(result);
                level.setText(application.getUser().getLevel());
                Log.e(TAG, "会员等级：" + application.getUser().getLevel());

                displayAvatar(logo, WebUrl.FILEHOST + application.getUser().getAvatar());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

            application.imagePath = WebUrl.FILEHOST + avatar;

            this.u = u;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置dialog位置
     *
     * @param dialogExit
     */
    private void setDialogPosition(Dialog dialogExit) {
        Window dialogWindow = dialogExit.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            //这一步把bitmap保存到本地
            saveMyBitmap(photo);
            Drawable drawable = new BitmapDrawable(null, photo);
            logo.setImageDrawable(drawable);
            // 在这里把图片上传至服务器
            modifyAvatar(SharedPreferencesUtils.getTokenId(getActivity()), avatarUrl);
        }
    }


    /**
     * 将图像保存到SD卡中
     */

    public void saveMyBitmap(Bitmap mBitmap) {
        File f = new File(avatarUrl);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (u == null) {
            getUserInfo(SharedPreferencesUtils.getTokenId(getActivity()));
        //}
    }

    /**
     * 修改头像
     *
     * @param token
     * @param fileUrl
     */
    public void modifyAvatar(String token, String fileUrl) {


        RequestParams params = new RequestParams(WebUrl.MODIFYAVATAR);
        params.addBodyParameter("token", token);
        params.addBodyParameter("avatar", new File(fileUrl));

        Log.e(TAG, "选择的头像的路径是：" + fileUrl);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "修改头像返回的结果：" + result);
                analysisJsonForAvatar(result);

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


            }
        });

    }


    /**
     * 解析返回的json数据
     *
     * @param result
     */
    private void analysisJsonForAvatar(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            }

            application.imagePath = WebUrl.FILEHOST + js.getString("data");

            /**
             * 刷新用户缓存数据。  融云
             *
             * @param userInfo 需要更新的用户缓存数据。
             */

            RongIM.getInstance().refreshUserInfoCache(new UserInfo(SharedPreferencesUtils.getUserId(getActivity()), application.getUser().getName(), Uri.parse(application.imagePath)));

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String imgPath = resultList.get(0).getPhotoPath();
                Log.e(TAG, "头像的路径是" + imgPath);
                String filePath = ImageUtils.saveMyBitmap(getActivity(), ImageUtils.getimage(imgPath));
                Picasso.with(getActivity()).load(new File(filePath)).into(logo);
                modifyAvatar(SharedPreferencesUtils.getTokenId(getActivity()), filePath);
            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

}
