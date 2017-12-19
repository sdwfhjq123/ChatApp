package com.yinhao.chatapp.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.MainActivity;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.fragment.FriendFragment;
import com.yinhao.chatapp.fragment.HomeFragment;
import com.yinhao.chatapp.view.MenuPopwindow;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;//将tab页面持久在内存中
    private Fragment mConversationList;
    private Fragment mConversationFragment = null;
    private List<Fragment> mFragment = new ArrayList<>();

    private RadioButton mMsgButton;
    private RadioButton mMyButton;
    private RadioButton mContactButton;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_title_menu, menu);
        getSupportActionBar().setTitle("微信");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_friends:
                View itemActionView = item.getActionView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPopup(View view) {
        int[] icons = {R.mipmap.ic_add_friend};
        final String[] texts = {"添加好友"};
        List<MenuPopwindow.MenuPopwindowBean> list = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            list.add(new MenuPopwindow.MenuPopwindowBean(icons[i], texts[i]));
        }

        MenuPopwindow pw = new MenuPopwindow(HomeActivity.this, list);
        pw.setOnItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HomeActivity.this, texts[position], Toast.LENGTH_SHORT).show();
            }
        });
        pw.showPopupWindow(view);//点击右上角的那个button
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
        }
    }
}
