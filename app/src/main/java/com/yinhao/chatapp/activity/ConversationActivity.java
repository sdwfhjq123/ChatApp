package com.yinhao.chatapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.GroupMember;
import com.yinhao.chatapp.VO.User;
import com.yinhao.chatapp.utils.ConstantValue;
import com.yinhao.chatapp.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.login.LoginException;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hp on 2017/12/18.
 */

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = "ConversationActivity";
    private String mName;
    private Toolbar mToolbar;
    private ImageButton mCheckGroupMember;
    private String mTargetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        initToolbar();

        //Log.e("type", "type is:" + getIntent().getData().getPath());
        //会话界面 对方或者群组id
        mTargetId = getIntent().getData().getQueryParameter("targetId");
        //对方 昵称
        mName = getIntent().getData().getQueryParameter("title");
        //会话类型
        String lastPathSegment = getIntent().getData().getLastPathSegment();//private or group
        //Conversation.ConversationType type = Conversation.ConversationType.valueOf(getIntent().getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        Log.i(TAG, "会话类型" + lastPathSegment);
        Log.i(TAG, "开启会话后得到的昵称" + mName);
        Log.i(TAG, "开启群组或者会话后得到的id" + mTargetId);

        if (TextUtils.isEmpty(mName)) {
            //如果昵称没拿到,根据id重新请求服务器进行保存
            Map<String, String> map = new HashMap<>();
            map.put("userId", mTargetId);
            HttpUtils.handleInfoOnServer("/user/getUserById", map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    User user = gson.fromJson(result, User.class);
                    final User.UserData data = user.getData();
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                        @Override
                        public UserInfo getUserInfo(String userId) {

                            return new UserInfo(data.getId(),
                                    data.getNikeName(),
                                    Uri.parse(ConstantValue.URL + data.getPortraitUri()));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                        }

                    }, true);
                }
            });
        }

        //TODO 如果是群组，就加载群组id里面的用户的数据进行更新头像，如果是单聊，直接return，因为前面已经请求过
        //TODO 如果是群组，右上角就显示查看群成员列表，根据targetId，如果是单聊隐藏  private or group
        if (lastPathSegment.equals("private")) {
            mCheckGroupMember.setVisibility(View.GONE);
        } else if (lastPathSegment.equals("group")) {
            mCheckGroupMember.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle(mName);//要在setSupportActionBar方法前设置
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        //getSupportActionBar().setTitle(mName);
        mToolbar.setTitle(mName);//要在setSupportActionBar方法前设置
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mCheckGroupMember = (ImageButton) mToolbar.findViewById(R.id.check_grouop_member);
        mCheckGroupMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GroupMemberListActivity.newInstance(ConversationActivity.this, mTargetId);
                startActivity(intent);
            }
        });
    }

}
