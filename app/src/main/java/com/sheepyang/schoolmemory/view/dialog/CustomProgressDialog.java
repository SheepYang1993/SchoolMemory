package com.sheepyang.schoolmemory.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sheepyang.schoolmemory.R;


public class CustomProgressDialog extends Dialog {
    private TextView tvMsg;
    private boolean mCancelable;

    public CustomProgressDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    public CustomProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.dialog_loading);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg = (TextView) this.findViewById(R.id.tvLoadingMsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
            tvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus && mCancelable) {
            dismiss();
        }
    }

    public void setDialogCancelable(boolean cancelable) {
        this.setCancelable(cancelable);
        this.mCancelable = cancelable;
    }

    public void setMessage(String msg) {
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
    }

    public void setMessage(int resId) {
        if (tvMsg != null) {
            tvMsg.setText(resId);
        }
    }
}
