package com.yinhao.chatapp.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
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

        if (TextUtils.isEmpty(mName) && lastPathSegment.equals("private")) {
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

        // 如果是群组，就加载群组id里面的用户的数据进行更新头像，如果是单聊，直接return，因为前面已经请求过
        // 如果是群组，右上角就显示查看群成员列表，根据targetId，如果是单聊隐藏  private or group
        if (lastPathSegment.equals("private")) {
            mCheckGroupMember.setVisibility(View.GONE);
        } else if (lastPathSegment.equals("group")) {
            mCheckGroupMember.setVisibility(View.VISIBLE);
        }


        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            //长按点击收藏，删除,复制
            @Override
            public boolean onMessageLongClick(Context context, final View view, final Message message) {
                final String[] items = {"删除信息", "复制信息", "收藏信息"};
                new AlertDialog.Builder(ConversationActivity.this)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (items[0].equals("删除信息")) {
                                    int[] i = new int[]{message.getMessageId()};
                                    RongIM.getInstance().deleteMessages(i);
                                } else if (items[1].equals("复制信息")) {
                                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setText(((TextMessage) message.getContent()).getContent());
                                } else if (items[2].equals("收藏信息")) {
                                    //TODO 如果是图片类型，如果是文字类型,进行数据库保存，增，删除单个，删除全部
                                    MessageContent content = message.getContent();
                                    if (content instanceof ImageMessage) {
                                        String s = ((ImageMessage) content).getLocalUri().toString();
                                    } else if (content instanceof TextMessage) {
                                        String s = ((TextMessage) content).getContent();
                                        //fff
                                    }
                                }
                            }
                        })
                        .show();
                return true;
            }
        });

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

    /**
     * 长按消息时
     * @param context 上下文
     * @param view 触发点击的 View
     * @param message 被长按的消息的实体信息
     * @return
     */

}
