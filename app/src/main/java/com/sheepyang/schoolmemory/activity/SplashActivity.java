package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.login.LoginActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;


public class SplashActivity extends BaseActivity {

    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.rlMain)
    RelativeLayout rlMain;
    @BindView(R.id.ivHomeBackgroud)
    ImageView ivHomeBackgroud;

    private int[] mHomeBackground = new int[]{
            R.drawable.ic_splash1,
            R.drawable.ic_splash2,
            R.drawable.ic_splash3,
            R.drawable.ic_splash4,
    };
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
        addAnimation();
    }

    private void initView() {
        mRandom = new Random();
        int randomNum = mRandom.nextInt(mHomeBackground.length);
        ivHomeBackgroud.setBackgroundResource(mHomeBackground[randomNum]);// 设置随机背景图片
    }

    private void addAnimation() {
        RotateAnimation animRotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setDuration(1000);
        animRotate.setFillAfter(true);

        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);

        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(animRotate);
        animSet.addAnimation(animScale);
        animSet.addAnimation(animAlpha);
        rlMain.setAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(mRunnable, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private Handler mHandler = new Handler() {
    };

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (BmobUser.getCurrentUser() == null) {
                mIntent = new Intent(SplashActivity.this, LoginActivity.class);
                mIntent.putExtra("fromFirst", true);// 我的页面判断是否从这个页面进去的
            } else {
                mIntent = new Intent(SplashActivity.this, MainActivity.class);
                mIntent.putExtra("fromFirst", true);// 我的页面判断是否从这个页面进去的
            }
            startActivity(mIntent);
            finish();
        }
    };
}
