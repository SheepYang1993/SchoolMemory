package com.sheepyang.schoolmemory.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edtPhone)
    EditText loginPhone;
    @BindView(R.id.edtPassword)
    EditText loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btnLogin, R.id.tvForget, R.id.btnRegist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                showToast("登录");
                break;
            case R.id.tvForget:
                showToast("忘记密码");
                break;
            case R.id.btnRegist:
                showToast("注册");
                break;
        }
    }
}

