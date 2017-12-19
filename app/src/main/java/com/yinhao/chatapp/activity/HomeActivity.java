package com.yinhao.chatapp.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.fragment.FriendFragment;
import com.yinhao.chatapp.fragment.HomeFragment;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;//将tab页面持久在内存中
    private Fragment mConversationList;
    private Fragment mConversationFragment = null;
    private List<Fragment> mFragment = new ArrayList<>();

    private RadioButton mMsgButton;
    private RadioButton mMyButton;
    private RadioButton mContactButton;

    private Toolbar mToolbar;
    private PopupWindow mPopupWindow;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mConversationList = initConversationList();//获取融云会话列表的对象
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mFragment.add(mConversationList);//加入会话列表
        mFragment.add(FriendFragment.getInstance());//加入联系人列表
        mFragment.add(HomeFragment.getInstance());//加入我的

        //配置ViewPager的适配器
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mContactButton.setChecked(false);
                    mMsgButton.setChecked(true);
                    mMyButton.setChecked(false);
                } else if (position == 1) {
                    mContactButton.setChecked(true);
                    mMsgButton.setChecked(false);
                    mMyButton.setChecked(false);
                } else if (position == 2) {
                    mContactButton.setChecked(false);
                    mMsgButton.setChecked(false);
                    mMyButton.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMsgButton = (RadioButton) findViewById(R.id.rb_msg);
        mContactButton = (RadioButton) findViewById(R.id.rb_contact);
        mMyButton = (RadioButton) findViewById(R.id.rb_me);
        mMsgButton.setOnClickListener(this);
        mContactButton.setOnClickListener(this);
        mMyButton.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        //取代原本的actionBar
        setSupportActionBar(mToolbar);

        mToolbar.findViewById(R.id.add_friends_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpMyOverflow();
            }
        });
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度+toolbar的高度
        int yOffset = frame.top + mToolbar.getHeight();
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.list_item_pop_menu, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置Gravity，让它显示在右上角。
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 20, yOffset);
            //设置item的点击监听
            popView.findViewById(R.id.add_friends_linearlayout).setOnClickListener(this);
        } else {
            mPopupWindow.showAtLocation(mToolbar, Gravity.RIGHT | Gravity.TOP, 20, yOffset);
        }

    }

    private Fragment initConversationList() {
        /**
         * appendQueryParameter对具体的会话列表做展示
         */
        if (mConversationFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        } else {
            return mConversationFragment;
        }
    }

    @Override
    public void onClick(View v) {
        //button点击切换页卡事件
        switch (v.getId()) {
            case R.id.rb_msg:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_contact:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_me:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.add_friends_linearlayout:
                Toast.makeText(this, "添加好友", Toast.LENGTH_SHORT).show();
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}
