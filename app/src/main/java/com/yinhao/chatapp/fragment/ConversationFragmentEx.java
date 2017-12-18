package com.yinhao.chatapp.fragment;

import android.content.Context;

import com.yinhao.chatapp.adapter.ConversationExAdapter;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.adapter.MessageListAdapter;

/**
 * Created by hp on 2017/12/15.
 */

public class ConversationFragmentEx extends ConversationFragment {

    @Override
    public MessageListAdapter onResolveAdapter(Context context) {
        return new ConversationExAdapter(context);
    }
}
