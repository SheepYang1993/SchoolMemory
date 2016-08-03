package com.sheepyang.schoolmemory;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.sheepyang.schoolmemory.util.Constant;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // LeanCloud初始化参数
        AVOSCloud.initialize(this, Constant.APP_ID, Constant.APP_KEY);
    }
}
