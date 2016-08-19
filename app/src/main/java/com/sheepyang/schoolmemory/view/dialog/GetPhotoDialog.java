package com.sheepyang.schoolmemory.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.util.CameraUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.PLog;

/**
 * 选择拍照，还是从相册获取图片
 */
public class GetPhotoDialog extends Dialog {

    private Context context;
    private final String IMAGE_TYPE = "image/*";
    private String cameraPath;
    private String name;//没设置的话，照片名字根据时间戳来命名，设置了可以避免重复创建照片
    private onPositiveClickListener mListener;


    public GetPhotoDialog(Context context) {
        super(context, R.style.dateDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_photo);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        window.setWindowAnimations(R.style.date_dialog);

    }

    @Override
    public void show() {
        super.show();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        this.getWindow().setAttributes(lp);
        findViewById(R.id.photo_album).setOnClickListener(clickListener);
        findViewById(R.id.photo_call).setOnClickListener(clickListener);
        findViewById(R.id.photo_camera).setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photo_album:// 相册
                    if (mListener != null) {
                        mListener.onPositiveClick(1);
                    }
                    Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                    getAlbum.setType(IMAGE_TYPE);
                    ((Activity) context).startActivityForResult(getAlbum, Constant.TO_ALUBM);
                    break;
                case R.id.photo_camera:// 相机
                    if (mListener != null) {
                        mListener.onPositiveClick(2);
                    }
                    if (name == null) {
                        cameraPath = CameraUtil.openCamara(
                                context, Constant.TO_CAMARA);
                    } else {
                        cameraPath = CameraUtil.openCamara(
                                context, Constant.TO_CAMARA, name);
                    }
                    PLog.d("cameraPath------------->" + cameraPath);
                    break;
                case R.id.photo_call:// 取消
                    if (mListener != null) {
                        mListener.onPositiveClick(3);
                    }
                    break;
            }
            dismiss();
        }
    };

    public interface onPositiveClickListener {
        void onPositiveClick(int position);
    }

    public onPositiveClickListener getListener() {
        return mListener;
    }

    public void setonPositiveClick(onPositiveClickListener mListener) {
        this.mListener = mListener;
    }

    public String getCameraPath() {
        return cameraPath;
    }

    public void setCameraPath(String cameraPath) {
        this.cameraPath = cameraPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
