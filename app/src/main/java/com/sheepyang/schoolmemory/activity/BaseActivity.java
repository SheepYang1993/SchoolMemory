package com.sheepyang.schoolmemory.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.util.MyToast;
import com.sheepyang.schoolmemory.view.dialog.CustomProgressDialog;


/**
 * 基类
 */
public class BaseActivity extends AppCompatActivity {
    public CustomProgressDialog mProgressDialog;
    public ActionBar mActionBar;
    ImageView mBarIvBack;
    TextView mBarTvRight;
    ImageView mBarIvRight;
    TextView mBarTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new CustomProgressDialog(this, "loading");
        initActionbar();
    }

    /**
     * 初始化标签
     */
    private void initActionbar() {
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar_view, null);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        mBarIvBack = (ImageView) findViewById(R.id.ivBack);
        mBarIvRight = (ImageView) findViewById(R.id.ivRight);
        mBarTvRight = (TextView) findViewById(R.id.tvRight);
        mBarTvTitle = (TextView) findViewById(R.id.tvTitle);
        mBarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param resId 资源id
     */
    public void setmBarTvTitle(int resId) {
        mBarTvTitle.setText(resId);
    }

    /**
     * 设置标题
     *
     * @param title 标提内容
     */
    public void setBarTitle(String title) {
        mBarTvTitle.setText(title);
    }

    /**
     * 设置右边显示文字
     *
     * @param resId 文字资源id
     */
    public void setRigthTv(int resId) {
        mBarIvRight.setVisibility(View.GONE);
        mBarTvRight.setVisibility(View.VISIBLE);
        mBarTvRight.setText(resId);
    }

    /**
     * 设置右边显示文字
     *
     * @param rihgtName 文字内容
     */
    public void setRigthTv(String rihgtName) {
        mBarIvRight.setVisibility(View.GONE);
        mBarTvRight.setVisibility(View.VISIBLE);
        mBarTvRight.setText(rihgtName);
    }

    /**
     * s设置右边图标显示
     *
     * @param resId 图标资源id
     */
    public void setRightIv(int resId) {
        mBarTvRight.setVisibility(View.GONE);
        mBarIvRight.setVisibility(View.VISIBLE);
        mBarIvRight.setImageResource(resId);
    }

    public void setNoLast() {
        mBarIvRight.setVisibility(View.GONE);
        mBarTvRight.setVisibility(View.GONE);
    }

    public void setNoBack() {
        mBarIvBack.setVisibility(View.GONE);
    }

    public void showToast(int resId) {
        if (!this.isFinishing()) {
            MyToast.showMessage(this, resId);
        }
    }

    public void showToast(String msg) {
        if (!this.isFinishing()) {
            MyToast.showMessage(this, msg);
        }
    }
}