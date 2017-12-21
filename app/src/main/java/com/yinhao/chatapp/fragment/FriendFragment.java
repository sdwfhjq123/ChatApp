package com.yinhao.chatapp.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.FriendVO;
import com.yinhao.chatapp.activity.HomeActivity;
import com.yinhao.chatapp.adapter.HeaderAndFooterAdapter;
import com.yinhao.chatapp.utils.ConstantValue;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hp on 2017/12/18.
 */

public class FriendFragment extends Fragment {
    private static final String TAG = "FriendFragment";
    public static FriendFragment instance = null;//单例模式

    public static FriendFragment getInstance() {
        if (instance == null) {
            instance = new FriendFragment();
        }
        return instance;
    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private HeaderAndFooterAdapter mAdapter;

    private List<FriendVO.FriendData> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HeaderAndFooterAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HeaderAndFooterAdapter.OnItemClickListener() {
            @Override
            public void onStartConversationPrivateChat(View v, int position) {
                //开启单聊会话
                RongIM.getInstance().startPrivateChat(getActivity(),
                        mList.get(position).getId(),
                        mList.get(position).getNikeName());
            }

            @Override
            public void onStartGroupActivity(View v) {
                //开启群组列表
            }
        });

        //创建数据库表
        //SQLiteDatabase database = Connector.getWritableDatabase();

        initData();

        return view;

    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
//        map.put("userId", Prefs.getString(getActivity(), Prefs.PREF_KEY_LOGIN_ID));
        map.put("userId", "ffb809ca72ba4ae4a58e53103fcf7151");
        HttpUtils.handleInfoOnServer("/user/getFriendsList", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "获取的好友应用列表:" + result);
                Gson gson = new Gson();
                FriendVO friendVO = gson.fromJson(result, FriendVO.class);
                String resultCode = friendVO.getResultCode();
                if (resultCode.equals("200")) {
                    final List<FriendVO.FriendData> data = friendVO.getData();
                    for (int i = 0; i < data.size(); i++) {
                        mList.add(new FriendVO.FriendData(data.get(i).getId(),
                                data.get(i).getNikeName(),
                                data.get(i).getPortraitUri(),
                                data.get(i).getCommand()));
                        //保存用户信息到会话列表及界面
                        final int finalI = i;
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                            @Override
                            public UserInfo getUserInfo(String userId) {

                                return new UserInfo(data.get(finalI).getId(),
                                        data.get(finalI).getNikeName(),
                                        Uri.parse(ConstantValue.URL + data.get(finalI).getPortraitUri()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                            }

                        }, true);
                    }
                    if (getActivity() instanceof HomeActivity) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setData(mList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
            }
        });
    }
}
