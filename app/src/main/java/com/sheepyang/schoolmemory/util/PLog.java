package com.sheepyang.schoolmemory.util;

import android.content.Context;
import android.util.Log;

public class PLog {
    // 开发的时候这3个位true。可以查看打印信息，
    /**
     * debug开关.
     */
    public static boolean D = true;

    /**
     * info开关.
     */
    public static boolean I = true;

    /**
     * error开关.
     */
    public static boolean E = true;

    private static String defaultTag = "SheepYang";

    /**
     * debug日志
     *
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (tag == null) {
            tag = defaultTag;
        }
        if (D)
            Log.d(tag, message);
    }

    /**
     * debug日志
     *
     * @param message
     */
    public static void d(String message) {
        Log.d(defaultTag, message);
    }

    /**
     * debug日志
     *
     * @param context
     * @param message
     */
    public static void d(Context context, String message) {
        String tag = null;
        if (context == null) {
            tag = defaultTag;
        } else {
            tag = context.getClass().getSimpleName();
        }
        d(tag, message);
    }

    /**
     * debug日志
     *
     * @param clazz
     * @param message
     */
    public static void d(Class<?> clazz, String message) {
        String tag = null;
        if (clazz == null) {
            tag = defaultTag;
        } else {
            tag = clazz.getSimpleName();
        }
        d(tag, message);
    }

    /**
     * info日志
     *
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (message == null) {
            message = "";
        }
        if (tag == null) {
            tag = defaultTag;
        }
        if (I)
            Log.i(tag, message);
    }

    /**
     * info日志
     *
     * @param context
     * @param message
     */
    public static void i(Context context, String message) {
        if (context == null) {
            i(defaultTag, message);
        } else {
            String tag = context.getClass().getSimpleName();
            i(tag, message);
        }

    }

    /**
     * info日志
     *
     * @param clazz
     * @param message
     */
    public static void i(Class<?> clazz, String message) {
        if (clazz == null) {
            i(defaultTag, message);
            return;
        }
        String tag = clazz.getSimpleName();
        i(tag, message);
    }

    /**
     * error日志
     *
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        if (tag == null) {
            tag = defaultTag;
        }
        if (message == null) {
            return;
        }
        if (E)
            Log.e(tag, message);
    }

    /**
     * error日志
     *
     * @param context
     * @param message
     */
    public static void e(Context context, String message) {
        if (context == null) {
            e(defaultTag, message);
            return;
        }
        String tag = context.getClass().getSimpleName();
        e(tag, message);
    }

    /**
     * error日志
     *
     * @param clazz
     * @param message
     */
    public static void e(Class<?> clazz, String message) {
        if (clazz == null) {
            e(defaultTag, message);
            return;
        }
        String tag = clazz.getSimpleName();
        e(tag, message);
    }

}
