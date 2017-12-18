package com.yinhao.chatapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yinhao.chatapp.R;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;


/**
 * Created by hp on 2017/12/15.
 */

public class ConversationListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        Map<String, Boolean> supportedConversation = new HashMap<>();
        supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
        /**
         * 启动会话列表界面。
         *
         * @param context               应用上下文。
         * @param supportedConversation 定义会话列表支持显示的会话类型，及对应的会话类型是否聚合显示。
         *                              例如：supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false) 非聚合式显示 private 类型的会话。
         */
        RongIM.getInstance().startConversationList(this, supportedConversation);
    }
}
