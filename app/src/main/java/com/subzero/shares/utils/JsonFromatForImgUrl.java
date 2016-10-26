package com.subzero.shares.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xzf on 2016/6/8.
 */
public class JsonFromatForImgUrl {


    /**
     * image : {"thumb":"","photo":[{"url":"20160524\/5744227b09b31.jpg","alt":""}]}
     */
    public static String formatImgUrl(String json) {

        try {
            JSONArray photo_arr = new JSONObject(json).getJSONArray("photo");
            JSONObject jo = (JSONObject) photo_arr.get(0);
            return jo.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();

        }

        return "";
    }

}
