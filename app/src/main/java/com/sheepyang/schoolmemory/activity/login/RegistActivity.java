package com.sheepyang.schoolmemory.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.BaseActivity;
import com.sheepyang.schoolmemory.activity.MainActivity;
import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.ErrorUtil;
import com.sheepyang.schoolmemory.util.PLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面
 */
public class RegistActivity extends BaseActivity {

    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.edtPassword1)
    EditText edtPassword1;
    @BindView(R.id.edtPassword2)
    EditText edtPassword2;
    @BindView(R.id.tvAgree)
    TextView tvAgree;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.btnNext)
    Button btnRegist;

    private int intervals = 60;//验证码获取间隔时间
    private int mTime = intervals;
    private String mSendPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        String strAgree = "<html>同意  <font color=#33abff>《校园纪念册服务协议》</font></html>";
        tvAgree.setText(Html.fromHtml(strAgree));
    }

    @OnClick({R.id.btnGetCode, R.id.btnNext, R.id.tvAgree, R.id.tvLogin, R.id.ivBack})
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
                toRegist();
                break;
            case R.id.tvAgree:
                tvAgree.setSelected(!tvAgree.isSelected());
                break;
        }
    }

    /**
     * 去注册
     */
    private void toRegist() {
        String phone2 = edtPhone.getText().toString().trim();
        String smsCode = edtCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone2) || TextUtils.isEmpty(smsCode) || TextUtils.isEmpty(mSendPhone)) {//手机号或者验证码为空，提示获取验证码
            showToast("请先获取短信验证码");
            return;
        }
        if (!phone2.equals(mSendPhone)) {//对比2次手机号码，查看手机号是否被修改
            showToast("您修改了手机账号，请重新获取验证码");
            edtCode.setText("");
            return;
        }
        String pass1 = edtPassword1.getText().toString();
        String pass2 = edtPassword2.getText().toString();
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
        if (!tvAgree.isSelected()) {
            showToast("请阅读并同意服务协议");
            return;
        }
        MyUser user = new MyUser();
        user.setMobilePhoneNumber(phone2);//设置手机号码（必填）
        user.setPassword(pass2);                  //设置用户密码
        user.setNick(phone2);
        btnRegist.setEnabled(false);
        mLoadingPD.show();
        user.signOrLogin(smsCode, new SaveListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    showToast("注册成功");
                    Intent intent = new Intent();
                    intent.setAction(Constant.EXIT_UI_ACTION);
                    intent.putExtra("ClassName", LoginActivity.class.getSimpleName());
                    sendBroadcast(intent);
                    mIntent = new Intent(RegistActivity.this, MainActivity.class);
                    startActivity(mIntent);
                    finish();
                } else {
                    btnRegist.setEnabled(true);
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(RegistActivity.this, e);
                }
            }
        });
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
        BmobSMS.requestSMSCode(mSendPhone, "注册获取验证码", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                if (ex == null) {//验证码发送成功
                    mLoadingPD.dismiss();
                    PLog.i("短信id：" + smsId);//用于后续的查询本次短信发送状态
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(RegistActivity.this, ex);
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
