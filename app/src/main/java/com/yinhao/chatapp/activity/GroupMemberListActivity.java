package com.yinhao.chatapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.FriendVO;
import com.yinhao.chatapp.VO.GroupVO;
import com.yinhao.chatapp.adapter.GroupAdapter;
import com.yinhao.chatapp.adapter.GroupMemberAdapter;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 群组成员列表
 */
public class GroupMemberListActivity extends AppCompatActivity {
    //TODO 目前是copy的群组列表，需要修改以及适配器的修改
    private static final String TAG = "GroupMemberListActivity";

    private static final String GROUP_ID = "group_id";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GroupMemberAdapter mAdapter;

    private List<FriendVO.FriendData> mList = new ArrayList<>();
    private String mGroupId;

    public static Intent newInstance(Context context, String groupId) {
        Intent intent = new Intent(context, GroupMemberListActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mGroupId = getIntent().getStringExtra(GROUP_ID);

        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new GroupMemberAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GroupMemberAdapter.OnItemClickListener() {
            @Override
            public void onStartConversationPrivateChat(View v, int position) {

            }
        });

        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", mGroupId);
        HttpUtils.handleInfoOnServer("/user/getGroupUser", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "获取的群组列表:" + result);
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
                        final int finalI = i;
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                            @Override
                            public UserInfo getUserInfo(String s) {
                                return new UserInfo(data.get(finalI).getId(),
                                        data.get(finalI).getNikeName(),
                                        Uri.parse(data.get(finalI).getPortraitUri()));
                            }
                        }, true);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(mList);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("群成员");//要在setSupportActionBar方法前设置
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
