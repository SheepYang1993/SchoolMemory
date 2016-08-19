package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.login.LoginActivity;
import com.sheepyang.schoolmemory.fragment.TopicFragment;
import com.sheepyang.schoolmemory.fragment.PersonFragment;
import com.sheepyang.schoolmemory.fragment.SettingFragment;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.view.dialog.GetPhotoDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 帖子概览列表界面
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navView)
    NavigationView mNavView;
    private View mHeaderView;
    private CircleImageView mCivHeadAvatar;
    private Fragment[] mFragments;
    private int currentIndex;//当前Fragment的位置
    private GetPhotoDialog getPhotoDialog;
    private TopicFragment mTopicFragment;
    private PersonFragment mPersonFragment;
    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (TopicFragment.getInstance().mFabMenu.isOpened()) {
                    TopicFragment.getInstance().mFabMenu.close(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        initFragment();
        initNavigationView();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        mTopicFragment = TopicFragment.getInstance();
        mPersonFragment = PersonFragment.getInstance();
        mSettingFragment = SettingFragment.getInstance();
        mFragments = new Fragment[]{
                mTopicFragment,
                mPersonFragment,
                mSettingFragment
        };
    }

    /**
     * 切换Fragment
     *
     * @param index 位置
     * @param title 标题
     * @param item  菜单点击的item
     */
    public void switchFragment(int index, String title, MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mFragments[currentIndex]);
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.content, mFragments[index]);
        }
        ft.show(mFragments[index]).commit();
        currentIndex = index;

        item.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    /**
     * 初始化菜单栏
     */
    private void initNavigationView() {
        mNavView.getMenu().clear();
        mNavView.inflateMenu(R.menu.menu_nav_view);
        mNavView.setNavigationItemSelectedListener(this);
        if (mHeaderView == null) {
            mHeaderView = mNavView.inflateHeaderView(R.layout.layout_header_main);
            mCivHeadAvatar = (CircleImageView) mHeaderView.findViewById(R.id.civHeadAvatar);
            if (mCurrentUser.getAvatar() == null || TextUtils.isEmpty(mCurrentUser.getAvatar())) {
                Glide.with(getApplicationContext())
                        .load(AppUtil.getRadomHeadView(null))
                        .fitCenter()
                        .crossFade()
                        .into(mCivHeadAvatar);
            } else {
                Glide.with(getApplicationContext())
                        .load(mCurrentUser.getAvatar())
                        .fitCenter()
                        .crossFade()
                        .into(mCivHeadAvatar);
            }
        }
        mCivHeadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPhotoDialog == null)
                    getPhotoDialog = new GetPhotoDialog(MainActivity.this);
                getPhotoDialog.setName("mCivHeadAvatar");
                getPhotoDialog.show();
            }
        });
        switchFragment(0, "首页", mNavView.getMenu().getItem(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTopicFragment != null && mTopicFragment.mIsOnActivityResult) {
            mTopicFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (TopicFragment.getInstance().mFabMenu.isOpened()) {
            TopicFragment.getInstance().mFabMenu.close(true);
        } else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (System.currentTimeMillis() - mCurrentTime < 2000) {
            mCurrentTime = 0;
            mIntent = new Intent();
            mIntent.setAction(Constant.EXIT_APP_ACTION);
            sendBroadcast(mIntent);
        } else {
            mCurrentTime = System.currentTimeMillis();
            showToast("再次点击退出APP");
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemHome:
                switchFragment(0, "首页", item);
                break;
            case R.id.itemPersonCenter:
                switchFragment(1, "个人中心", item);
                break;
            case R.id.itemSetting:
                switchFragment(2, "设置", item);
                break;
            case R.id.itemShare:
                showToast("分享");
                break;
            case R.id.itemAbout:
                showToast("关于");
                break;
            case R.id.itemLogout:
                if (System.currentTimeMillis() - mCurrentTime < 2000) {
                    mCurrentTime = 0;
                    mIntent = new Intent();
                    mIntent.setAction(Constant.EXIT_APP_ACTION);
                    mIntent.putExtra("isLogOut", true);
                    sendBroadcast(mIntent);
                    mIntent = new Intent(this, LoginActivity.class);
                    startActivity(mIntent);
                } else {
                    mCurrentTime = System.currentTimeMillis();
                    showToast("再次点击切换账号");
                }
                break;
            default:
                break;
        }
        return true;
    }
}
