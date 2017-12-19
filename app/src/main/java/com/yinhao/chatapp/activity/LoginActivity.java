package com.yinhao.chatapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinhao.chatapp.R;
import com.yinhao.chatapp.global.ChatApplication;
import com.yinhao.chatapp.model.Friends;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RongIM.UserInfoProvider {

    private static final String TAG = "LoginActivity";
    private static final String token = "SOdFE77zr1BcFzozNM3Rf6wP/8LjdbHb241WAFqykOcjyFm/jQVVVupQ1ztzdSXgpco6AJ/+8Rqz51sTnXdCWg==";
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
        getSupportActionBar().hide();
        initView();
        initUserInfo();
        RongIM.setUserInfoProvider(this, true);
    }

    private void initUserInfo() {
        mUserInfo.add(new Friends("123", "联通22", "http://i5.hexunimg.cn/2012-11-07/147694350.jpg"));
        mUserInfo.add(new Friends("1213", "移动2222", "http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg"));
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
        mPercentTextView.setOnClickListener(this);
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
            case R.id.percent_textview:
                //登陆方法
                login();
                break;
        }
    }

    private void login() {
        String info = mNumTextView.getText().toString();
        if (info.contains(".")) {
            final String[] split = info.split("\\.");
            Log.i(TAG, "login: " + split[0] + ":" + split[1]);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    connect(token);
                }
            }.start();
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
