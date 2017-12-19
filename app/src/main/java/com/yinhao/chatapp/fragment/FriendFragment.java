package com.yinhao.chatapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.adapter.HeaderAndFooterAdapter;

import io.rong.imkit.RongIM;

/**
 * Created by hp on 2017/12/18.
 */

public class FriendFragment extends Fragment {
    public static FriendFragment instance = null;//单例模式

    public static FriendFragment getInstance() {
        if (instance == null) {
            instance = new FriendFragment();
        }
        return instance;
    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private HeaderAndFooterAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new HeaderAndFooterAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return view;

    }
}
