package com.yinhao.chatapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yinhao.chatapp.R;
import com.yinhao.chatapp.VO.LoginVO;
import com.yinhao.chatapp.global.ChatApplication;
import com.yinhao.chatapp.model.Friends;
import com.yinhao.chatapp.utils.HttpUtils;
import com.yinhao.chatapp.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RongIM.UserInfoProvider {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_PLUS = 1;
    private static final int REQUEST_MINUS = 2;
    private static final int REQUEST_RIDE = 3;
    private static final int REQUEST_DIVIDE = 4;
    private int flag;

    private TextView mNumTextView;
    private TextView mAcTextView;
    private ImageButton mBackspaceTextView;
    private LinearLayout mEqualLinearLayout;
    private TextView mDivideTextView;
    private TextView mRideTextView;
    private TextView mPlusTextView;
    private TextView mMinusTextView;
    private TextView mDotTextView;
    private TextView mPercentTextView;

    private TextView mZeroTextView;
    private TextView mOneTextView;
    private TextView mTwoTextView;
    private TextView mThreeTextView;
    private TextView mFourTextView;
    private TextView mFiveTextView;
    private TextView mSixTextView;
    private TextView mSevenTextView;
    private TextView mEightTextView;
    private TextView mNineTextView;

    private String mAnswer = "";

    private int mZero;
    private int mOne;
    private int mTwo;
    private int mThree;
    private int mFour;
    private int mFive;
    private int mSix;
    private int mSeven;
    private int mEight;
    private int mNine;

    private int[] mNumbers = new int[10];
    private List<Friends> mUserInfo = new ArrayList<>();
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        RongIM.setUserInfoProvider(this, true);
    }

    private void initUserInfo(String loginId, String nikeName, String headImageUrl) {
        mUserInfo.add(new Friends(loginId, nikeName, headImageUrl));
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 init 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        if (getApplicationInfo().packageName.equals(ChatApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.i("LoginActivity", "--onSuccess" + userid);
                    mUserId = userid;
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                    RongIM.setUserInfoProvider(LoginActivity.this, true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.d("LoginActivity", "--onError" + errorCode.getMessage());
                }
            });
        }
    }

    private void initView() {
        mNumTextView = (TextView) findViewById(R.id.num_textview);
        mAcTextView = (TextView) findViewById(R.id.ac_textview);
        mDivideTextView = (TextView) findViewById(R.id.divide_textview);
        mRideTextView = (TextView) findViewById(R.id.ride_textview);
        mPlusTextView = (TextView) findViewById(R.id.plus_textview);
        mMinusTextView = (TextView) findViewById(R.id.minus_textview);
        mDotTextView = (TextView) findViewById(R.id.dot_textview);
        mPercentTextView = (TextView) findViewById(R.id.percent_textview);

        mZeroTextView = (TextView) findViewById(R.id.zero_textview);
        mOneTextView = (TextView) findViewById(R.id.one_textview);
        mTwoTextView = (TextView) findViewById(R.id.two_textview);
        mThreeTextView = (TextView) findViewById(R.id.three_textview);
        mFourTextView = (TextView) findViewById(R.id.four_textview);
        mFiveTextView = (TextView) findViewById(R.id.five_textview);
        mSixTextView = (TextView) findViewById(R.id.six_textview);
        mSevenTextView = (TextView) findViewById(R.id.seven_textview);
        mEightTextView = (TextView) findViewById(R.id.eight_textview);
        mNineTextView = (TextView) findViewById(R.id.nine_textview);

        mBackspaceTextView = (ImageButton) findViewById(R.id.backspace_textview);
        mEqualLinearLayout = (LinearLayout) findViewById(R.id.equal_linearlayout);

        mDivideTextView.setOnClickListener(this);
        mRideTextView.setOnClickListener(this);
        mPlusTextView.setOnClickListener(this);
        mMinusTextView.setOnClickListener(this);
        mDotTextView.setOnClickListener(this);
        mAcTextView.setOnClickListener(this);

        mZeroTextView.setOnClickListener(this);
        mOneTextView.setOnClickListener(this);
        mTwoTextView.setOnClickListener(this);
        mThreeTextView.setOnClickListener(this);
        mFourTextView.setOnClickListener(this);
        mFiveTextView.setOnClickListener(this);
        mSixTextView.setOnClickListener(this);
        mSevenTextView.setOnClickListener(this);
        mEightTextView.setOnClickListener(this);
        mNineTextView.setOnClickListener(this);

        mEqualLinearLayout.setOnClickListener(this);

        //注册
        mBackspaceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                String info = mNumTextView.getText().toString();
                if (!(info.contains("\\.") && info.contains("/") && info.contains("\\+") && info.contains("×") && info.contains("-"))) {
                    map.put("command", info);
                    HttpUtils.handleInfoOnServer("user/register", map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "error net" + e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            try {
                                final JSONObject jsonObject = new JSONObject(result);
                                String resultCode = jsonObject.getString("resultCode");
                                if (resultCode.equals("500")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Toast.makeText(LoginActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else if (resultCode.equals("200")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, CompileInfoActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //登录
        mPercentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zero_textview:
                mAnswer += 0;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.one_textview:
                mAnswer += 1;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.two_textview:
                mAnswer += 2;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.three_textview:
                mAnswer += 3;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.four_textview:
                mAnswer += 4;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.five_textview:
                mAnswer += 5;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.six_textview:
                mAnswer += 6;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.seven_textview:
                mAnswer += 7;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.eight_textview:
                mAnswer += 8;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.nine_textview:
                mAnswer += 9;
                mNumTextView.setText(mAnswer);
                break;
            case R.id.ac_textview:
                mNumTextView.setText(String.valueOf(0));
                mAnswer = "";
                mNumTextView.setText(mAnswer);
                break;
            case R.id.dot_textview:
                mAnswer += ".";
                mNumTextView.setText(mAnswer);
                break;
            case R.id.plus_textview:
                mAnswer = mAnswer + "+";
                mNumTextView.setText(mAnswer);
                flag = REQUEST_PLUS;
                break;
            case R.id.minus_textview:
                mAnswer = mAnswer + "-";
                mNumTextView.setText(mAnswer);
                flag = REQUEST_MINUS;
                break;
            case R.id.ride_textview:
                mAnswer = mAnswer + "×";
                mNumTextView.setText(mAnswer);
                flag = REQUEST_RIDE;
                break;
            case R.id.divide_textview:
                mAnswer = mAnswer + "/";
                mNumTextView.setText(mAnswer);
                flag = REQUEST_DIVIDE;
                break;
            case R.id.equal_linearlayout:
                operation();
                break;
        }
    }

    /**
     *
     */
    private void login() {
        String info = mNumTextView.getText().toString();
        if (!(info.contains("\\.") && info.contains("/") && info.contains("\\+") && info.contains("×") && info.contains("-"))) {
            Map<String, String> map = new HashMap<>();
            map.put("command", info);
            HttpUtils.handleInfoOnServer("user/login", map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "error net" + e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    //Log.i(TAG, "登录" + result);
                    Gson gson = new Gson();
                    final LoginVO loginVO = gson.fromJson(result, LoginVO.class);
                    if (loginVO.getResultCode().equals("500")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "口令不对", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (loginVO.getResultCode().equals("200")) {
                        String token = loginVO.getData().getToken();
                        String loginId = loginVO.getData().getUser().getId();
                        String nikeName = loginVO.getData().getUser().getNikeName();
                        String headImageUrl = loginVO.getData().getUser().getPortraitUri();
                        Prefs.putString(LoginActivity.this, Prefs.PREF_KEY_LOGIN_ID, loginId);
                        if (TextUtils.isEmpty(token)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = CompileInfoActivity.newInstance(LoginActivity.this, true);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //将登录成功返回的userId保存
                            Prefs.putString(LoginActivity.this, Prefs.PREF_KEY_NIKE_NAME, nikeName);
                            Prefs.putString(LoginActivity.this, Prefs.PREF_KEY_HEAD_IMAGE_URL, headImageUrl);
                            Prefs.putString(LoginActivity.this, Prefs.PREF_KEY_TOKEN, token);
                            //保存用户信息到会话列表及界面
                            initUserInfo(loginId, nikeName, headImageUrl);
                            connect(token);
                        }

                    }

                }
            });

        } else {
            Toast.makeText(this, "格式不对", Toast.LENGTH_SHORT).show();
        }

    }

    private void operation() {
        String info = mNumTextView.getText().toString();
        if (flag == REQUEST_PLUS) {
            String[] split = info.split("\\+");
            Long add = Long.valueOf(Integer.parseInt(split[0]) + Integer.parseInt(split[1]));
            mNumTextView.setText(String.valueOf(add));
        } else if (flag == REQUEST_MINUS) {
            String[] split = info.split("-");
            Long add = Long.valueOf(Integer.parseInt(split[0]) - Integer.parseInt(split[1]));
            mNumTextView.setText(String.valueOf(add));
        } else if (flag == REQUEST_RIDE) {
            String[] split = info.split("×");
            Long add = Long.valueOf(Integer.parseInt(split[0]) * Integer.parseInt(split[1]));
            mNumTextView.setText(String.valueOf(add));
        } else if (flag == REQUEST_DIVIDE) {
            String[] split = info.split("/");
            Double add = Double.valueOf(Integer.parseInt(split[0]) / Integer.parseInt(split[1]));
            mNumTextView.setText(String.valueOf(add));
        }
    }

    @Override
    public UserInfo getUserInfo(String s) {
        for (Friends i : mUserInfo) {
            Log.e(TAG, i.getPortraitUri());
            return new UserInfo(i.getUserId(), i.getUserName(), Uri.parse(i.getPortraitUri()));
        }
        Log.e(TAG, "UserId is ：" + s);
        return null;
    }
}
