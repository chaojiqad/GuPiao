package com.subzero.shares.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by xzf on 2016/4/19.
 */
public class StringUtils {

    /**
     * 检查是否是正确的手机号码格式
     *
     * @param mobile
     * @return
     */
    public static boolean isPhoneNum(String mobile) {
        if (TextUtils.isEmpty(mobile)) return false;
        return Pattern.compile("^1[0-9]{10}$").matcher(mobile).matches();
    }

    /**
     * 根据-分割字符串   得到转化好的  年月日  int
     *
     * @param str
     * @return
     */
    public static int[] split(String str) {
        if (TextUtils.isEmpty(str)) return new int[0];

        String[] strs = str.split("-");

        if (strs.length > 0) {

            int[] data = new int[strs.length];

            for (int i = 0; i < strs.length; i++) {
                data[i] = Integer.parseInt(strs[i]);
            }

            return data;
        } else {
            return new int[0];
        }

    }
}
