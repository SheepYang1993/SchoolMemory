package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.receiver.ExitAppReceiver;
import com.sheepyang.schoolmemory.receiver.ExitUIReceiver;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.MyToast;
import com.sheepyang.schoolmemory.view.dialog.CustomProgressDialog;


/**
 * 基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ExitAppReceiver exitAppReceiver = new ExitAppReceiver();
    private ExitUIReceiver exitUIReceiver = new ExitUIReceiver();
    public CustomProgressDialog mLoadingPD;

    public Intent mIntent;

    public long mCurrentTime = 0;
    public MyUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();//注册广播
        mLoadingPD = new CustomProgressDialog(this, "loading");
        mCurrentUser = MyUser.getCurrentUser(MyUser.class);
    }

    private void registerReceiver() {
        IntentFilter exitUIFilter = new IntentFilter();
        exitUIFilter.addAction(Constant.EXIT_UI_ACTION);
        registerReceiver(exitUIReceiver, exitUIFilter);

        IntentFilter exitAppFilter = new IntentFilter();
        exitAppFilter.addAction(Constant.EXIT_APP_ACTION);
        registerReceiver(exitAppReceiver, exitAppFilter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(exitUIReceiver);
        unregisterReceiver(exitAppReceiver);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();//注销广播
        super.onDestroy();
    }

    public void showToast(int resId) {
        if (!this.isFinishing()) {
            MyToast.showMessage(this, resId);
        }
    }

    public void showToast(String msg) {
        if (!this.isFinishing()) {
            MyToast.showMessage(this, msg);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
