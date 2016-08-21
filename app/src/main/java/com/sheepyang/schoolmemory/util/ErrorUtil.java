package com.sheepyang.schoolmemory.util;

import android.app.Activity;
import android.content.Context;

import com.sheepyang.schoolmemory.activity.login.LoginActivity;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by SheepYang on 2016/8/6 01:16.
 */
public class ErrorUtil {
    public static void showErrorCode(Context context, BmobException e) {
        PLog.i(e.toString());
        switch (e.getErrorCode()) {
            case 101:
                if ((Activity) context instanceof LoginActivity) {
                    MyToast.showMessage(context, "用户名或密码不正确!");
                } else {
                    MyToast.showMessage(context, "没有查询到数据!");
                }
                break;
            case 207:
                MyToast.showMessage(context, "验证码错误!");
                break;
            case 209:
                MyToast.showMessage(context, "该手机号码已经存在!");
                break;
            case 9015:
                MyToast.showMessage(context, "错误码:9015 未知错误!");
                break;
            case 9016:
                MyToast.showMessage(context, "无网络连接，请检查您的手机网络!");
                break;
            case 10010:
                MyToast.showMessage(context, "该手机号发送短信达到限制条数!");
                break;
            case 10011:
                MyToast.showMessage(context, "开发者没钱给你发短信了!");
                break;
            default:
                MyToast.showMessage(context, "错误码:" + e.getErrorCode() + ", " + e.getMessage());
                break;
        }
    }
}
