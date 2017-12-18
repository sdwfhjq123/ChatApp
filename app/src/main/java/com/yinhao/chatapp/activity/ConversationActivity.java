package com.yinhao.chatapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yinhao.chatapp.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        //String title = getIntent().getData().getQueryParameter("title");

        /**
         * <p>启动会话界面。</p>
         * <p>使用时，可以传入多种会话类型 {@link io.rong.imlib.model.Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
         * 如果传入的是 {@link io.rong.imlib.model.Conversation.ConversationType#CHATROOM}，sdk 会默认调用
         * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
         * 如果你的逻辑是，只允许加入已存在的聊天室，请使用接口 {@link #startChatRoomChat(Context, String, boolean)} 并且第三个参数为 true</p>
         *
         * @param context          应用上下文。
         * @param conversationType 会话类型。
         * @param targetId         根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
         * @param title            聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
         */

        RongIM.getInstance().startConversation(this, Conversation.ConversationType.CHATROOM, "", "123");

    }
}
