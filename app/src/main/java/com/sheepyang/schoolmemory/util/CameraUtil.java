/**
 *
 */
package com.sheepyang.schoolmemory.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;

import java.io.File;

/**
 * 相机类
 */
public class CameraUtil {
    /**
     * 根据Uri获取路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @SuppressLint("SimpleDateFormat")
    public static String openCamara(Context context, int type) {
        long time = System.currentTimeMillis();
        String st = String.valueOf(time);
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = "";
            String Path = FileUtil.getImageCAmearDir(context);
            filePath = Path + "/" + st + "icon.jpg";
            File out = new File(filePath);
            Uri mUri = Uri.fromFile(out);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, type);
            return filePath;
        } else {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String openCamara(Context context, int type, String name) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filePath = "";
            String Path = FileUtil.getImageCAmearDir(context);
            filePath = Path + "/" + name + ".jpg";
            File out = new File(filePath);
            Uri mUri = Uri.fromFile(out);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, type);
            return filePath;
        } else {
            return "";
        }
    }
}
