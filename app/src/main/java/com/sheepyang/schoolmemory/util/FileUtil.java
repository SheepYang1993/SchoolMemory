package com.sheepyang.schoolmemory.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 文件存储路径
 */
public class FileUtil {
    /**
     * 默认APP根目录.
     */
    private static String downloadRootDir = null;
    /**
     * 默认下载图片文件目录.
     */
    private static String imageDownloadDir = null;
    /**
     * 默认本app拍照文件目录.
     */
    private static String imageCamearDir = null;
    /**
     * 默认裁剪后涂抹文件目录.
     */
    private static String imageCutDir = null;
    /**
     * 默认下载文件目录.
     */
    private static String fileDownloadDir = null;

    public static String getDownloadRootDir(Context context) {
        if (downloadRootDir == null)
            initFileDir(context);
        return downloadRootDir;
    }

    public static String getImageDownloadDir(Context context) {
        if (imageDownloadDir == null)
            initFileDir(context);
        return imageDownloadDir;
    }

    public static String getImageCAmearDir(Context context) {
        if (imageCamearDir == null)
            initFileDir(context);
        return imageCamearDir;
    }

    public static String getImageCutDir(Context context) {
        if (imageCutDir == null)
            initFileDir(context);
        return imageCutDir;
    }

    public static String getFileDownloadDir(Context context) {
        if (fileDownloadDir == null)
            initFileDir(context);
        return fileDownloadDir;
    }

    /**
     * 描述：初始化存储目录.
     *
     * @param context the context
     */
    public static void initFileDir(Context context) {

        // PackageInfo info = AppUtil.getPackageInfo(context);

        // 默认下载文件根目录.
        String downloadRootPath = File.separator
                + AppConfig.DOWNLOAD_ROOT_DIR
                + File.separator;
        // 默认下载图片文件目录.
        String imageDownloadPath = downloadRootPath
                + AppConfig.DOWNLOAD_IMAGE_DIR + File.separator;
        // 默认相机拍照文件目录.
        String imageCameraPath = imageDownloadPath
                + AppConfig.DOWNLOAD_IMAGE_CAMARA_DIR+ File.separator;
        // 默认图片裁剪文件目录.
        String imageCutPath = imageDownloadPath
                + AppConfig.DOWNLOAD_IMAGE_CUT_DIR + File.separator;
        // 默认下载文件目录.
        String fileDownloadPath = downloadRootPath
                + AppConfig.DOWNLOAD_FILE_DIR + File.separator;


        try {
            if (!isCanUseSD()) {
                return;
            } else {

                File root = Environment.getExternalStorageDirectory();
                File downloadDir = new File(root.getAbsolutePath()
                        + downloadRootPath);
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();
                }
                downloadRootDir = downloadDir.getPath();


                File imageDownloadDirFile = new File(root.getAbsolutePath()
                        + imageDownloadPath);
                if (!imageDownloadDirFile.exists()) {
                    imageDownloadDirFile.mkdirs();
                }
                imageDownloadDir = imageDownloadDirFile.getPath();


                File imageCamearDirFile = new File(root.getAbsolutePath()
                        + imageCameraPath);
                if (!imageCamearDirFile.exists()) {
                    imageCamearDirFile.mkdirs();
                }
                imageCamearDir = imageCamearDirFile.getPath();

                File imageCutDirFile = new File(root.getAbsolutePath()
                        + imageCutPath);
                if (!imageCutDirFile.exists()) {
                    imageCutDirFile.mkdirs();
                }
                imageCutDir = imageCutDirFile.getPath();



                File fileDownloadDirFile = new File(root.getAbsolutePath()
                        + fileDownloadPath);
                if (!fileDownloadDirFile.exists()) {
                    fileDownloadDirFile.mkdirs();
                }
                fileDownloadDir = fileDownloadDirFile.getPath();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算sdcard上的剩余空间.
     *
     * @return the int
     */
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / 1024 * 1024;
        return (int) sdFreeMB;
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
