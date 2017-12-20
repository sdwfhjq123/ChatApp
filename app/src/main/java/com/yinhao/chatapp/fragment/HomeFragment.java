package com.yinhao.chatapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.activity.CompileInfoActivity;

/**
 * Created by hp on 2017/12/18.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment instance = null;//单例模式

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    private LinearLayout mCompileInfo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //编辑资料
        mCompileInfo = (LinearLayout) view.findViewById(R.id.compile_info);
        mCompileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CompileInfoActivity.newInstance(getActivity(), false);
                startActivity(intent);
            }
        });

        return view;

    }
}
