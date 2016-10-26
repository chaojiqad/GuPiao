package com.subzero.shares.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.subzero.shares.bean.UserGroup;
import com.subzero.shares.utils.SharedPreferencesUtils;

import io.rong.imlib.model.Group;

/**
 * Created by xzf on 2016/5/17.
 */
public class UserDB {

    private static final String TAG = "UserDB";
    /**
     * 数据库名
     */
    public String db_name;
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static UserDB userDB;
    private SQLiteDatabase db;


    /**
     * 将构造方法私有化
     */
    private UserDB(Context context) {
        UserIdOpenHelper dbHelper = new UserIdOpenHelper(context,
                db_name, null, VERSION);
        db_name = SharedPreferencesUtils.getUserId(context);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例。
     */
    public synchronized static UserDB getInstance(Context context) {
        if (userDB == null) {
            userDB = new UserDB(context);
        }

        return userDB;
    }

    /**
     * 将UserGroup实例存储到数据库。
     */
    public void saveUserGroup(UserGroup userGroup) {

        if (userGroup != null) {

            Log.e(TAG,"插入");

            Log.e(TAG, "id" + userGroup.getUserid());
            Log.e(TAG, "name" + userGroup.getUsername());
            Log.e(TAG, "avatar" + userGroup.getAvatar());
            Log.e(TAG, "groupid" + userGroup.getGroupId());
            db.execSQL(
                    "insert into UserGroup(userid,username,avatar,groupid) values(?,?,?,?)",
                    new String[]{userGroup.getUserid(), userGroup.getUsername(), userGroup.getAvatar(),userGroup.getGroupId()});


        }
    }



    /**
     * 更新表中UserGroup数据
     *
     * @param userGroup
     */
    public void updateUserGroup(UserGroup userGroup) {

        if (userGroup != null) {
            Log.e(TAG,"更新");
            Log.e(TAG, "id" + userGroup.getUserid());
            Log.e(TAG, "name" + userGroup.getUsername());
            Log.e(TAG, "avatar" + userGroup.getAvatar());
            Log.e(TAG, "groupid" + userGroup.getGroupId());
            db.execSQL(
                    "update UserGroup set userid=?,username=?,avatar=?,groupid=? where userid=? and groupid=?",
                    new String[]{userGroup.getUserid(), userGroup.getUsername(), userGroup.getAvatar(),userGroup.getGroupId(), userGroup.getUserid()});
        }
    }



    /**
     * 从数据库读取群友的信息。
     */
    public UserGroup loadUserGroup(String userid,String groupid) {

        Log.e(TAG,"读取");

        Cursor cursor = db.rawQuery("select * from UserGroup where userid=? and groupid=?",
                new String[]{userid, groupid});
        if (cursor.moveToFirst()) {

            UserGroup userGroup = new UserGroup();
            userGroup.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
            userGroup.setUsername(cursor.getString(cursor
                    .getColumnIndex("username")));
            userGroup.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            userGroup.setGroupId(cursor.getString(cursor.getColumnIndex("groupid")));
            return userGroup;

        }
        return null;
    }

    /**
     * 查询数据库是否存在该记录forUserGroup
     *
     * @param userID
     * @return
     */
    public boolean checkUserGroup(String userID,String groupid) {
        Cursor cursor = db.rawQuery("select * from UserGroup where userid=? and groupid=?",
                new String[]{userID, groupid});
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }


    /**
     * 将Group实例存储到数据库。
     */
    public void saveGroup(Group group) {


        if (group != null) {

            Log.e(TAG, "id" + group.getId());
            Log.e(TAG, "name" + group.getName());
            Log.e(TAG, "avatar" + group.getPortraitUri());
            db.execSQL(
                    "insert into Groups(groupid,grouptitle,groupavatar) values(?,?,?)",
                    new String[]{group.getId(), group.getName(), group.getPortraitUri().toString()});


        }
    }


    /**
     * 更新表中Group数据
     *
     * @param group
     */
    public void updateGroup(Group group) {

        if (group != null) {

            Log.e(TAG, "id" + group.getId());
            Log.e(TAG, "name" + group.getName());
            Log.e(TAG, "avatar" + group.getPortraitUri());

            db.execSQL(
                    "update Groups set groupid=?,grouptitle=?,groupavatar=? where groupid=?",
                    new String[]{group.getId(), group.getName(), group.getPortraitUri().toString(), group.getId()});
        }
    }

    /**
     * 从数据库读取所有的群组的信息。
     */
    public Group loadGroup(String groupid) {

        Cursor cursor = db
                .query("Groups", null, "groupid=?", new String[]{groupid}, null, null, null);
        if (cursor.moveToFirst()) {

            Group group = new Group(cursor.getString(cursor.getColumnIndex("groupid")), cursor.getString(cursor
                    .getColumnIndex("grouptitle")), Uri.parse(cursor.getString(cursor.getColumnIndex("groupavatar"))));

            return group;

        }
        return null;
    }




    /**
     * 查询数据库是否存在该记录forGroup
     *
     * @param groupid
     * @return
     */
    public boolean checkGroup(String groupid) {
        Cursor cursor = db.rawQuery("select * from Groups where groupid=?",
                new String[]{groupid});
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }


}
