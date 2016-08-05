package com.sheepyang.schoolmemory.receiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import cn.bmob.v3.BmobUser;

/**
 * 退出APP
 * <p>
 * 要退出App的方法中添加以下发送广播代码即可
 * Intent intent = new Intent();
 * intent.setAction(Constant.EXIT_APP_ACTION);
 * intent.putExtra("isLogOut", true);
 * sendBroadcast(intent);
 * <p>
 * Created by Administrator on 2016/8/5.
 */
public class ExitAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null) {
            if (!intent.getBooleanExtra("isLogOut", false)) {
                BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(); // 现在的currentUser是null了
            }
            if (context instanceof Activity) {
                ((Activity) context).finish();
            } else if (context instanceof FragmentActivity) {
                ((FragmentActivity) context).finish();
            } else if (context instanceof Service) {
                ((Service) context).stopSelf();
            }
        }
    }
}
