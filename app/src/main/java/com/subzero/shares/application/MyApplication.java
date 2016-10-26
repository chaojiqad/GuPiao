package com.subzero.shares.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.subzero.shares.R;
import com.subzero.shares.activity.MainActivity;
import com.subzero.shares.bean.User;
import com.subzero.shares.github.activity.ErrorActivity;
import com.subzero.shares.github.ereza.CustomActivityOnCrash;
import com.subzero.shares.utils.PicassoImageLoader;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;


//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
public class MyApplication extends Application {

    private User u;

    /**
     * 记录用户的头像
     */
    public String imagePath;

    /**
     * 存放用户充值的金额对象
     */
    public ArrayList<HashMap<String,String>> mPayList;

    /**
     * 当前直播室顾问的userid
     */
    public String userId;

    public ArrayList<String> levelId=new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);


        }

        initCrash();

        initTheme();
    }

    private void initTheme() {
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.title_bg_color))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(getResources().getColor(R.color.title_bg_color))
                .setFabPressedColor(getResources().getColor(R.color.title_bg_color))
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(getResources().getColor(R.color.title_bg_color))
                .setIconBack(R.mipmap.nav_ico_back)
                .setIconRotate(R.mipmap.ic_action_repeat)
                .setIconCrop(R.mipmap.ic_action_crop)
                .setIconCamera(R.mipmap.ic_action_camera)
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder().build();

        //配置imageloader
        ImageLoader imageloader = new PicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 保存用户的信息
     *
     * @param u
     */
    public void setUser(User u) {
        this.u = u;
    }

    public User getUser() {
        return u;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 版本要求 Android 4.0  以上  api 14
     */
    private void initCrash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            CustomActivityOnCrash.install(this);
            CustomActivityOnCrash.setEnableAppRestart(true);
            CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(true);
            CustomActivityOnCrash.setShowErrorDetails(true);
            CustomActivityOnCrash.setErrorActivityClass(ErrorActivity.class);
            CustomActivityOnCrash.setRestartActivityClass(MainActivity.class);
        }


    }


}
