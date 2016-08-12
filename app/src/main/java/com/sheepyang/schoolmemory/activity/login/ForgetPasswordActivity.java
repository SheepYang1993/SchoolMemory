package com.sheepyang.schoolmemory.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.BaseActivity;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.ErrorUtil;
import com.sheepyang.schoolmemory.util.PLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * 忘记密码界面
 */
public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.btnNext)
    Button btnNext;

    private int intervals = 60;//验证码获取间隔时间
    private int mTime = intervals;
    private String mSendPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnGetCode, R.id.btnNext, R.id.tvLogin, R.id.ivBack})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivBack://返回
            case R.id.tvLogin:
                onBackPressed();
                break;
            case R.id.btnGetCode://获取验证码
                toGetCode();
                break;
            case R.id.btnNext:
                toNext();
                break;
        }
    }

    /**
     * 下一步，重置密码
     */
    private void toNext() {
        final String phone = edtPhone.getText().toString().trim();
        final String smsCode = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(smsCode) || TextUtils.isEmpty(mSendPhone)) {//手机号或者验证码为空，提示获取验证码
            showToast("请先获取短信验证码");
            return;
        }
        if (!phone.equals(mSendPhone)) {//对比2次手机号码，查看手机号是否被修改
            showToast("您修改了手机账号，请重新获取验证码");
            edtCode.setText("");
            return;
        }
        mIntent = new Intent(this, ResetPasswordActivity.class);
        mIntent.putExtra("phone", phone);
        mIntent.putExtra("smsCode", smsCode);
        startActivity(mIntent);
    }

    /**
     * 获取验证码
     */
    private void toGetCode() {
        mSendPhone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mSendPhone)) {
            showToast("您输入的手机号");
            return;
        }
        if (!AppUtil.isMobileNO(mSendPhone)) {
            showToast("您输入的手机号有误,请重新输入!");
            return;
        }
        btnGetCode.post(mRunnable);
        AppUtil.closeSoftInput(this);
        mLoadingPD.show();
        //链接服务器获取验证码
        BmobSMS.requestSMSCode(mSendPhone, "重置密码获取验证码", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    mLoadingPD.dismiss();
                    PLog.i("短信id：" + smsId);//用于后续的查询本次短信发送状态
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(ForgetPasswordActivity.this, ex);
                }
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            btnGetCode.setEnabled(false);
            edtPhone.setEnabled(false);
            btnGetCode.setText("重新获取(" + mTime + ")");
            mTime--;
            if (mTime == 0) {
                btnGetCode.setEnabled(true);
                edtPhone.setEnabled(true);
                btnGetCode.setText("获取验证码");
                mTime = intervals;
            } else {
                btnGetCode.postDelayed(mRunnable, 1000);
            }
        }
    };
}
