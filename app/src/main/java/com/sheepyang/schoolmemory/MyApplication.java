package com.sheepyang.schoolmemory;

import android.app.Application;

import com.sheepyang.schoolmemory.util.Constant;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, Constant.APP_KEY);
    }
}
