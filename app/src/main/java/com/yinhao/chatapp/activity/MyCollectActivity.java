package com.yinhao.chatapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.adapter.MyCollectAdapter;
import com.yinhao.chatapp.model.Collect;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhao on 2017/12/24.
 */

public class MyCollectActivity extends AppCompatActivity {

    private static final String TAG = "MyCollectActivity";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private MyCollectAdapter mAdapter;

    private List<Collect> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycler_view);
        initToolbar();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = DataSupport.findAll(Collect.class);
        mAdapter = new MyCollectAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        //查询数据库
        selectDatabase();

        //长按点击删除
        mAdapter.setOnLongItemClickListener(new MyCollectAdapter.OnLongItemClickListener() {
            @Override
            public void onLongClickDelete(View v, final int position) {
                final int id = mList.get(position).getId();
                new AlertDialog.Builder(MyCollectActivity.this)
                        .setTitle("删除")
                        .setMessage("您确定要删除此信息吗")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //TODO 删除单个条目
                                DataSupport.delete(Collect.class, id);
                                List<Collect> collects = DataSupport.findAll(Collect.class);
                                mAdapter.setData(collects);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 从数据库中拿到全部数据
     */
    private void selectDatabase() {
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("我的收藏");//要在setSupportActionBar方法前设置
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mToolbar.findViewById(R.id.clear_all_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Collect.class);
                List<Collect> collects = DataSupport.findAll(Collect.class);
                mAdapter.setData(collects);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(MyCollectActivity.this, "清理成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
