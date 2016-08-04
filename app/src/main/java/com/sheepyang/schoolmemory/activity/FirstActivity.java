package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sheepyang.schoolmemory.R;


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

            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            intent.putExtra("fromFirst", true);// 我的页面判断是否从这个页面进去的
            startActivity(intent);
            finish();

        }
    };
}
