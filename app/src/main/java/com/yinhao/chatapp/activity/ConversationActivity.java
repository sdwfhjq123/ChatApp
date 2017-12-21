package com.yinhao.chatapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.yinhao.chatapp.R;

import java.util.Locale;

import javax.security.auth.login.LoginException;

import io.rong.imlib.model.Conversation;

/**
 * Created by hp on 2017/12/18.
 */

public class ConversationActivity extends AppCompatActivity {
    private static final String TAG = "ConversationActivity";
    private String mName;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        initToolbar();

        //Log.e("type", "type is:" + getIntent().getData().getPath());
        //会话界面 对方id
        String targetId = getIntent().getData().getQueryParameter("targetId");
        //对方 昵称
        mName = getIntent().getData().getQueryParameter("title");
        //会话类型
        String lastPathSegment = getIntent().getData().getLastPathSegment();//private or group
        //Conversation.ConversationType type = Conversation.ConversationType.valueOf(getIntent().getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        Log.i(TAG, "会话类型" + lastPathSegment);
        Log.i(TAG, "开启会话后得到的昵称" + mName);
        Log.i(TAG, "开启群组或者会话后得到的id" + targetId);

        if (!TextUtils.isEmpty(mName)) {
            mToolbar.setTitle(mName);//要在setSupportActionBar方法前设置
        } else {
            //TODO 如果昵称没拿到,根据id重新请求服务器进行保存

        }
        //TODO 如果是群组，就加载群组id里面的用户的数据进行更新头像，如果是单聊，直接return，因为前面已经请求过
        //TODO 如果是群组，右上角就显示查看群成员列表，根据targetId，如果是单聊隐藏  private or group


    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
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
