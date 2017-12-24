package com.yinhao.chatapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.activity.CompileInfoActivity;
import com.yinhao.chatapp.activity.HomeActivity;
import com.yinhao.chatapp.activity.LoginActivity;
import com.yinhao.chatapp.activity.MyCollectActivity;
import com.yinhao.chatapp.model.Collect;
import com.yinhao.chatapp.utils.CleanMessageUtil;
import com.yinhao.chatapp.utils.ConstantValue;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by hp on 2017/12/18.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment instance = null;//单例模式
    private LinearLayout mCleanCache;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    private LinearLayout mCompileInfo;
    private CircleImageView mHeadImage;//head_civ
    private TextView mNameText;//name_text
    private LinearLayout mCollectLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
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

        mNameText = (TextView) view.findViewById(R.id.name_text);
        mHeadImage = (CircleImageView) view.findViewById(R.id.head_image);

        mCleanCache = view.findViewById(R.id.clean_cache_ll);
        mCleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().clearConversations(new RongIMClient.ResultCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //清空数据库表数据
                                DataSupport.deleteAll(Collect.class);
                                Toast.makeText(getActivity(), "清理成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                }, Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP);
            }
        });

        mCollectLinearLayout = (LinearLayout) view.findViewById(R.id.collect_linear_layout);
        //点击打开我的收藏
        mCollectLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCollectActivity.class);
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
        map.put("userId", Prefs.getString(getActivity(), Prefs.PREF_KEY_LOGIN_ID));
        HttpUtils.handleInfoOnServer("/user/getUserById", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "获取个人用户信息" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String id = data.getString("id");
                    final String nikeName = data.getString("nikeName");
                    final String portraitUri = data.getString("portraitUri");
                    //将登录成功返回的userId保存
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_NIKE_NAME, nikeName);
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_HEAD_IMAGE_URL, portraitUri);
                    Prefs.putString(getActivity(), Prefs.PREF_KEY_LOGIN_ID, id);
                    if (getActivity() instanceof HomeActivity) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNameText.setText(nikeName);
                                Glide.with(getActivity()).load(ConstantValue.URL + portraitUri).into(mHeadImage);
                                //mUserIdText.setText(Prefs.getString(getActivity(), Prefs.PREF_KEY_ACCOUNT));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
