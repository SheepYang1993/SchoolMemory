package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.login.LoginActivity;

import cn.bmob.v3.BmobUser;


public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mActionBar.hide();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private Handler mHandler = new Handler() {
    };

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (BmobUser.getCurrentUser() == null) {
                mIntent = new Intent(FirstActivity.this, LoginActivity.class);
                mIntent.putExtra("fromFirst", true);// 我的页面判断是否从这个页面进去的
            } else {
                mIntent = new Intent(FirstActivity.this, MainActivity.class);
                mIntent.putExtra("fromFirst", true);// 我的页面判断是否从这个页面进去的
            }
            startActivity(mIntent);
            finish();
        }
    };
}
