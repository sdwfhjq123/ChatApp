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

import com.bumptech.glide.Glide;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.activity.CompileInfoActivity;
import com.yinhao.chatapp.utils.ConstantValue;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private CircleImageView mHeadImage;//head_civ
    private TextView mNameText;//name_text

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //编辑资料
        mCompileInfo = (LinearLayout) view.findViewById(R.id.compile_info);
        mCompileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CompileInfoActivity.newInstance(getActivity());
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 初始化个人信息
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", Prefs.getString(getActivity(), Prefs.PREF_KEY_ACCOUNT));
        HttpUtils.handleInfoOnServer("/user/getUserById", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String id = jsonObject.getString("id");
                    String nikeName = jsonObject.getString("nikeName");
                    String portraitUri = jsonObject.getString("portraitUri");
                    //将登录成功返回的userId保存
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_NIKE_NAME, nikeName);
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_HEAD_IMAGE_URL, portraitUri);
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_LOGIN_ID, id);
                    mNameText.setText(nikeName);
                    Glide.with(getActivity()).load(ConstantValue.URL + portraitUri).into(mHeadImage);
                    //mUserIdText.setText(Prefs.getString(getActivity(), Prefs.PREF_KEY_ACCOUNT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
