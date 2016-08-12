package com.sheepyang.schoolmemory.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.BaseActivity;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.ErrorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 重置密码界面
 */
public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.edtPassword1)
    EditText edtPassword1;
    @BindView(R.id.edtPassword2)
    EditText edtPassword2;
    @BindView(R.id.btnRestPassword)
    Button btnRestPassword;

    private String phone;
    private String smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        phone = getIntent().getStringExtra("phone");
        smsCode = getIntent().getStringExtra("smsCode");
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号码为空！");
            finish();
        }
        if (TextUtils.isEmpty(smsCode)) {
            showToast("验证码为空！");
            finish();
        }
    }

    @OnClick(R.id.btnRestPassword)
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnRestPassword:
                toResetPassword();
                break;
            default:
                break;
        }
    }

    private void toResetPassword() {
        String pass1 = edtPassword1.getText().toString();
        String pass2 = edtPassword2.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            showToast("验证码为空!");
            return;
        }
        if (pass1.equals("")) {
            showToast("请输入密码");
            return;
        }
        if (pass1.length() < 6) {
            showToast("密码不足6位");
            return;
        }
        if (!pass1.equals(pass2)) {
            showToast("2次输入的密码不一致");
            return;
        }
        btnRestPassword.setEnabled(false);
        mLoadingPD.show();
        BmobUser.resetPasswordBySMSCode(smsCode, pass2, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                if (ex == null) {
                    mLoadingPD.dismiss();
                    showToast("密码修改成功，可以用新密码进行登录啦!");
                    Intent intent = new Intent();
                    intent.setAction(Constant.EXIT_UI_ACTION);
                    intent.putExtra("ClassName", ForgetPasswordActivity.class.getSimpleName());
                    sendBroadcast(intent);
                    finish();
                } else {
                    btnRestPassword.setEnabled(true);
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(ResetPasswordActivity.this, ex);
                }
            }
        });
    }
}
