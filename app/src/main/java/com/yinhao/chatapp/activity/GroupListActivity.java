package com.yinhao.chatapp.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.GroupVO;
import com.yinhao.chatapp.adapter.GroupAdapter;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 群组列表
 */
public class GroupListActivity extends AppCompatActivity {
    private static final String TAG = "GroupListActivity";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GroupAdapter mAdapter;

    private List<GroupVO.GroupData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new GroupAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onStartConversationGroupChat(View v, int position) {
                RongIM.getInstance().startGroupChat(GroupListActivity.this,
                        mList.get(position).getId(),
                        mList.get(position).getName());
            }
        });

        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Prefs.getString(this, Prefs.PREF_KEY_LOGIN_ID));
        //map.put("userId", "ffb809ca72ba4ae4a58e53103fcf7151");
        HttpUtils.handleInfoOnServer("/user/getGroupList", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "获取的群组列表:" + result);
                Gson gson = new Gson();
                GroupVO groupVO = gson.fromJson(result, GroupVO.class);
                String resultCode = groupVO.getResultCode();
                if (resultCode.equals("200")) {
                    final List<GroupVO.GroupData> data = groupVO.getData();
                    for (int i = 0; i < data.size(); i++) {
                        mList.add(new GroupVO.GroupData(data.get(i).getId(),
                                data.get(i).getName(),
                                data.get(i).getPortraitUri()));
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
        mToolbar.setTitle("我的群组");//要在setSupportActionBar方法前设置
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
