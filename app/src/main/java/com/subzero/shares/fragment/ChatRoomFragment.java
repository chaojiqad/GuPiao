package com.subzero.shares.fragment;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.GameActivity;
import com.subzero.shares.adapter.TalkGroupAdapter;
import com.subzero.shares.bean.ChatBean;
import com.subzero.shares.bean.UserGroup;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.db.UserDB;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * 聊天室
 * Created by xzf on 2016/5/9.
 */
public class ChatRoomFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "ChatRoomFragment";
    private View view;
    private ListView lv;
    private ImageView mHeaderIv;
    private ArrayList<ChatBean> mChatBeans;
    private TalkGroupAdapter mTalkGroupAdapter;
    private RelativeLayout mProgress;
    private LinearLayout ll_charroom_list;

    /**
     * 当前的群组id
     */
    private String groupid;


    @Override
    protected View initView() {
        view = View.inflate(getActivity(), R.layout.fragment_chatroom, null);
        lv = (ListView) view.findViewById(R.id.lv_grouplist);
        mProgress = (RelativeLayout) view.findViewById(R.id.rl_charroom);
        ll_charroom_list = (LinearLayout) view.findViewById(R.id.ll_charroom_list);

        View headerView = View.inflate(getActivity(), R.layout.item_chat_header, null);
        mHeaderIv = (ImageView) headerView.findViewById(R.id.iv_chatroom_header);
        lv.addHeaderView(headerView);

        initData();
        initListener();

        return view;
    }

    private void initData() {


        mChatBeans = new ArrayList<>();
        mTalkGroupAdapter = new TalkGroupAdapter(getActivity(), mChatBeans);
        lv.setAdapter(mTalkGroupAdapter);

        createGroup(SharedPreferencesUtils.getTokenId(getActivity()));

        //RongIM.setGroupUserInfoProvider(this, true);

        CommunicationFragment.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                createGroup(SharedPreferencesUtils.getTokenId(getActivity()));
            }
        });
    }


    private void loadingUserAvatar() {


        /**
         * 加载用户头像
         */
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {


            @Override
            public UserInfo getUserInfo(String userID) {

                UserInfo userInfo;

                Log.e(TAG, "融云返回的" + userID);

                if (userID.equals(SharedPreferencesUtils.getUserId(getActivity()))) {

                    UserGroup userGroup = new UserGroup();

                    if (application == null) {
                        Log.e(TAG, "application--------->null");
                        userGroup = UserDB.getInstance(getActivity()).loadUserGroup(userID, groupid);
                    } else {
                        Log.e(TAG, "application--------->设置自己的信息");
                        userGroup.setUsername(application.getUser().getName());
                        userGroup.setUserid(userID);
                        userGroup.setAvatar(WebUrl.FILEHOST + application.getUser().getAvatar());
                    }

                    userInfo = new UserInfo(userID, userGroup.getUsername(), Uri.parse(userGroup.getAvatar()));
                    Log.e(TAG, userID + userGroup.getUsername() + Uri.parse(userGroup.getAvatar()));
                } else {

                    Log.e(TAG, "别人的id是" + userID);

                    UserGroup userGroup = UserDB.getInstance(getActivity()).loadUserGroup(userID, groupid);
                    if (userGroup == null) {
                        userInfo = new UserInfo(userID, "该用户已经不在该群", Uri.parse(""));
                        Log.e(TAG, "获取" + userID + "这个用户从数据库获取的数据是null");
                    } else {
                        Log.e(TAG, "获取" + userID + "这个用户从数据库获取的数据是" + userGroup.getUsername() + "头像：" + Uri.parse(userGroup.getAvatar()).toString());
                        userInfo = new UserInfo(userID, userGroup.getUsername(), Uri.parse(userGroup.getAvatar()));
                    }

                }

                return userInfo;
            }

        }, true);
    }

    private void initListener() {
        lv.setOnItemClickListener(this);
        mHeaderIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 创建群组
     *
     * @param token
     */
    private void createGroup(String token) {

        RequestParams params = new RequestParams(WebUrl.GROUP);
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "获取融云群组返回的结果：" + result);
                        mProgress.setVisibility(View.GONE);
                        ll_charroom_list.setVisibility(View.VISIBLE);
                        analysisJsonForGroup(result);
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
                }

        );

    }

    private void analysisJsonForGroup(String result) {


        Log.e(TAG, "userid" + SharedPreferencesUtils.getUserId(getActivity()));


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

            JSONArray groups = js.getJSONArray("data");

            mChatBeans.clear();

            for (int i = 0; i < groups.length(); i++) {
                JSONObject jo_group = groups.getJSONObject(i);
                String id = jo_group.getString("groupid");
                String groupname = jo_group.getString("groupname");
                String membercount = jo_group.getString("membercount");
                String groupavatar = WebUrl.IMGGROUP + jo_group.getString("groupavatar");

                Log.e(TAG, jo_group.getString("groupavatar"));

                mChatBeans.add(new ChatBean(id, groupname, groupavatar, membercount));

                if (UserDB.getInstance(getActivity()).checkGroup(id)) {
                    UserDB.getInstance(getActivity()).updateGroup(new Group(id, groupname, Uri.parse(groupavatar)));
                } else {
                    UserDB.getInstance(getActivity()).saveGroup(new Group(id, groupname, Uri.parse(groupavatar)));
                }
            }


            mTalkGroupAdapter.notifyDataSetChanged(mChatBeans);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /**
         * 清除顾问所id信息
         */
        application.userId = "030303";


        loadingUserAvatar();

        //刷新群组用户信息
        // GroupUserInfo groupUserInfo = new GroupUserInfo(mChatBeans.get(position - 1).getId(), SharedPreferencesUtils.getUserId(getActivity()), application.getUser().getName());
        //  RongIM.getInstance().refreshGroupUserInfoCache(groupUserInfo);

        if (mChatBeans != null && mChatBeans.size() > 0) {

            ChatBean chatBean = mChatBeans.get(position - 1);
            Log.e(TAG, "群组的ID是" + chatBean.getId());
            groupid = chatBean.getId();
            RongIM.getInstance().startGroupChat(getActivity(), chatBean.getId(), chatBean.getGroupName());
        }
    }


//    @Override
//    public GroupUserInfo getGroupUserInfo(String groupId, String userId) {
//        String currentUserId = SharedPreferencesUtils.getUserId(getActivity());
//        Log.e(TAG,"groupld"+groupId+"^^^^^^^^^^^^^^^"+"userId"+userId);
//        if (userId.equals(currentUserId)) {
//            GroupUserInfo groupUserInfo = new GroupUserInfo(groupId, currentUserId, application.getUser().getName());
//            RongIM.getInstance().refreshGroupUserInfoCache(groupUserInfo);
//            Log.e(TAG,"我已经返回给融云啦");
//            return groupUserInfo;
//        }
//        return null;
//    }


}
