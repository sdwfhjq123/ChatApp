package com.yinhao.chatapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.yinhao.chatapp.R;

import javax.security.auth.login.LoginException;

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
        Log.e("type", "type is:" + getIntent().getData().getPath());
        //会话界面 对方id
        String targetId = getIntent().getData().getQueryParameter("targetId");
        //对方 昵称
        mName = getIntent().getData().getQueryParameter("title");
        Log.i(TAG, "开启会话后得到的昵称" + mName);

        initToolbar();


    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle(mName);//要在setSupportActionBar方法前设置
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
