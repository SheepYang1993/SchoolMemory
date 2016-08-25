package com.sheepyang.schoolmemory.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.BaseActivity;
import com.sheepyang.schoolmemory.activity.MainActivity;
import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.ErrorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        edtPhone.setText("15659988223");
        edtPassword.setText("123456");
    }

    @OnClick({R.id.btnLogin, R.id.tvForget, R.id.btnNext})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnLogin://登录
                toLogin();
                break;
            case R.id.tvForget://忘记密码
                mIntent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btnNext://注册账号
                mIntent = new Intent(this, RegistActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mCurrentTime < 2000) {
            mCurrentTime = 0;
            mIntent = new Intent();
            mIntent.setAction(Constant.EXIT_APP_ACTION);
            sendBroadcast(mIntent);
        } else {
            mCurrentTime = System.currentTimeMillis();
            showToast("再次点击退出APP");
        }
    }

    private void toLogin() {
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }
        if (!AppUtil.isMobileNO(phone)) {
            showToast("您输入的手机号有误,请重新输入!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        btnLogin.setEnabled(false);
        mLoadingPD.show();
        MyUser.loginByAccount(phone, password, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    showToast("登录成功!");
                    mIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    btnLogin.setEnabled(true);
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(LoginActivity.this, e);
                }
            }
        });
    }
}

