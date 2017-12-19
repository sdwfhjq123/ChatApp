package com.yinhao.chatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yinhao.chatapp.R;

public class AddFriendActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setSupportActionBar(mToolbar);
        initToolbar();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.inflateMenu(R.menu.menu_search);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item_search) {
                    // do search
                    Toast.makeText(AddFriendActivity.this, "search", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        // 获取ToolBar 上的编辑框
        EditText searchEdit = (EditText) mToolbar.findViewById(R.id.edit_search);
        // 获取内容
        String content = searchEdit.getText().toString();
    }

}
