package com.sheepyang.schoolmemory.receiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import cn.bmob.v3.BmobUser;

/**
 * 关闭指定aUI
 * <p>
 * 要关闭UI的方法中添加以下发送广播代码即可
 * Intent intent = new Intent();
 * intent.setAction(Constant.EXIT_UI_ACTION);
 * intent.putExtra("ClassName", "");
 * sendBroadcast(intent);
 * <p>
 * Created by Administrator on 2016/8/5.
 */
public class ExitUIReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String className = intent.getStringExtra("ClassName");
        if (context != null) {
            if (context instanceof Activity) {
                if (className.contains(((Activity) context).getClass().getSimpleName())) {
                    ((Activity) context).finish();
                }
            } else if (context instanceof FragmentActivity) {
                if (className.contains(((FragmentActivity) context).getClass().getSimpleName())) {
                    ((FragmentActivity) context).finish();
                }
            } else if (context instanceof Service) {
                if (className.contains(((Service) context).getClass().getSimpleName())) {
                    ((Service) context).stopSelf();
                }
            }
        }
    }
}
