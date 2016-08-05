package com.sheepyang.schoolmemory.activity.login;

import com.sheepyang.schoolmemory.activity.BaseActivity;

/**
 * 注册
 */
public class RegistActivity extends BaseActivity {
//
//    @BindView(R.id.regist_phone)
//    EditText registPhone;
//    @BindView(R.id.regist_code)
//    EditText registCode;
//    @BindView(R.id.regist_pass1)
//    EditText registPass1;
//    @BindView(R.id.regist_pass2)
//    EditText registPass2;
//    @BindView(R.id.regist_agree)
//    TextView registAgree;
//    @BindView(R.id.regist_getcode)
//    Button getCodeBtn;
//
//    private int time = 60;
//    private String sendPhone;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_regist);
//        ButterKnife.bind(this);
//        actionBar.hide();
//        String strAgree = "<html>同意  <font color=#33abff>《鹰博服务协议》</font></html>";
//        registAgree.setText(Html.fromHtml(strAgree));
//    }
//
//    @OnClick({R.id.regist_getcode, R.id.regist_regist, R.id.regist_agree, R.id.regist_login, R.id.regist_back})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.regist_back://返回
//            case R.id.regist_login:
//                onBackPressed();
//                break;
//            case R.id.regist_getcode:
//                toGetCode();
//                break;
//            case R.id.regist_regist:
//
//                if (registAgree.isSelected()) {
//                    toRegist();
//                } else {
//                    showToast("请阅读并同意服务协议");
//                }
//                break;
//            case R.id.regist_agree:
//                registAgree.setSelected(!registAgree.isSelected());
//                break;
//        }
//    }
//
//    /**
//     * 去注册
//     */
//    private void toRegist() {
//        String phoe2 = registPhone.getText().toString();
//        String smsCode = registCode.getText().toString();
//
//        if (TextUtils.isEmpty(phoe2) || TextUtils.isEmpty(smsCode)) {//手机号或者验证码为空，提示获取验证码
//            showToast("请先获取短信验证码");
//            return;
//        }
//        if (!phoe2.equals(sendPhone)) {//对比2次手机号码，查看手机号是否被修改
//            showToast("您修改了手机账号，请重新获取验证码");
//            registCode.setText("");
//            return;
//        }
//        String pass1 = registPass1.getText().toString();
//        String pass2 = registPass2.getText().toString();
//        if (!pass1.equals(pass2)) {
//            showToast("2次输入的密码不一致");
//            return;
//        }
//
//        if (pass1.equals("")) {
//            MyToast.showMessage(this, "请输入密码");
//            return;
//        }
//        if (pass1.length() < 6) {
//            MyToast.showMessage(this, "密码不足6位");
//            return;
//        }
//
//        startActivity(new Intent(this, SelectIdentityActivity.class));
//    }
//
//    /**
//     * 获取验证码
//     */
//    private void toGetCode() {
//        sendPhone = registPhone.getText().toString();
//        if (TextUtils.isEmpty(sendPhone)) {
//            showToast("您输入的手机号");
//            return;
//        }
//        if (!AppUtil.isMobileNO(sendPhone)) {
//            showToast("您输入的手机号有误,请重新输入!");
//            return;
//        }
//        //TODO 链接服务器获取验证码
//        getCodeBtn.post(mRunnable);
//        AppUtil.closeSoftInput(this);
//    }
//
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            getCodeBtn.setEnabled(false);
//            registPhone.setEnabled(false);
//            getCodeBtn.setText("重新获取(" + time + ")");
//            time--;
//            if (time == 0) {
//                getCodeBtn.setEnabled(true);
//                registPhone.setEnabled(true);
//                getCodeBtn.setText("获取验证码");
//                time = 60;
//            } else {
//                getCodeBtn.postDelayed(mRunnable, 1000);
//            }
//        }
//    };

}
