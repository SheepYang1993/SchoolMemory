package com.sheepyang.schoolmemory.activity;

import android.content.IntentFilter;
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
import com.sheepyang.schoolmemory.receiver.ExitAppReceiver;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.MyToast;
import com.sheepyang.schoolmemory.view.dialog.CustomProgressDialog;


/**
 * 基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private ExitAppReceiver exitReceiver = new ExitAppReceiver();
    //自定义退出应用Action,实际应用中应该放到整个应用的Constant类中.
    public CustomProgressDialog mProgressDialog;
    public ActionBar mActionBar;
    ImageView mBarIvBack;
    TextView mBarTvRight;
    ImageView mBarIvRight;
    TextView mBarTvTitle;
    private long mCurrentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerExitReceiver();//注册退出登录广播
        mProgressDialog = new CustomProgressDialog(this, "loading");
        initActionbar();
    }

    private void registerExitReceiver() {
        IntentFilter exitFilter = new IntentFilter();
        exitFilter.addAction(Constant.EXIT_APP_ACTION);
        registerReceiver(exitReceiver, exitFilter);
    }

    private void unregisterExitReceiver() {
        unregisterReceiver(exitReceiver);
    }

    @Override
    protected void onDestroy() {
        unregisterExitReceiver();//注销退出登录广播
        super.onDestroy();
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

    /**
     * 双击标题栏
     */
    public void onDoubleClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvTitle:
                if (System.currentTimeMillis() - mCurrentTime < 500) {
                    mCurrentTime = 0;
                    onDoubleClick();
                }
                mCurrentTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }
}
