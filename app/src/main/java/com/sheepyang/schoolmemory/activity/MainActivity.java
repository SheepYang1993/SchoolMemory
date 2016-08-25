package com.sheepyang.schoolmemory.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.login.LoginActivity;
import com.sheepyang.schoolmemory.fragment.TopicListFragment;
import com.sheepyang.schoolmemory.fragment.PersonFragment;
import com.sheepyang.schoolmemory.fragment.SettingFragment;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.ErrorUtil;
import com.sheepyang.schoolmemory.util.FileUtil;
import com.sheepyang.schoolmemory.view.dialog.GetPhotoDialog;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
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
    private TextView mTvNick;
    private Fragment[] mFragments;
    private int currentIndex;//当前Fragment的位置
    private GetPhotoDialog getPhotoDialog;
    private TopicListFragment mTopicListFragment;
    private PersonFragment mPersonFragment;
    private SettingFragment mSettingFragment;
    private Uri mImageCropUri;
    private Bitmap mTopicImgBitmap;

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
                if (TopicListFragment.getInstance().mFabMenu.isOpened()) {
                    TopicListFragment.getInstance().mFabMenu.close(true);
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
        mTopicListFragment = TopicListFragment.getInstance();
        mPersonFragment = PersonFragment.getInstance();
        mSettingFragment = SettingFragment.getInstance();
        mFragments = new Fragment[]{
                mTopicListFragment,
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
            mTvNick = (TextView) mHeaderView.findViewById(R.id.tvNick);
            String nick = mCurrentUser.getNick();
            if (nick != null && AppUtil.isMobileNO(nick)) {
                nick = nick.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            }
            if (nick != null) {
                mTvNick.setText(nick);
            }
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
                getPhotoDialog.setOnPositiveClickListener(new GetPhotoDialog.onPositiveClickListener() {
                    @Override
                    public void onPositiveClick(int position) {
                        // position: 1相册 2相机 3取消
                        mTopicImgBitmap = null;
                        mImageCropUri = null;
                    }
                });
                getPhotoDialog.show();
            }
        });
        switchFragment(0, "首页", mNavView.getMenu().getItem(0));
    }

    /**
     * 切图
     *
     * @param uri
     */
    public String startPhotoZoom(Uri uri) {
        long time = System.currentTimeMillis();
        String st = String.valueOf(time);
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            // 裁剪图片过小，放大，去除黑边
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            String filePath = "";
            String Path = FileUtil.getImageCAmearDir(this);
            filePath = Path + "/" + st + "icon.jpg";
            File out = new File(filePath);
            mImageCropUri = Uri.fromFile(out);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, Constant.TO_CUT);
            return filePath;
        } else {
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTopicListFragment != null && mTopicListFragment.mIsOnActivityResult) {
            mTopicListFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            switch (requestCode) {
                case Constant.TO_ALUBM:
                    if (data != null) {
                        Uri originalUri = data.getData(); // 获得图片的uri
                        startPhotoZoom(originalUri);
                    }
                    break;
                case Constant.TO_CAMARA:
                    if (resultCode != Activity.RESULT_CANCELED) {
                        String path = getPhotoDialog.getCameraPath();
                        if (TextUtils.isEmpty(path)) {
                            showToast("相机异常，请重新拍照！");
                            return;
                        }
                        Uri originalUri = Uri.fromFile(new File(path));
                        startPhotoZoom(originalUri);
                    }
                    break;
                case Constant.TO_CUT:
                    if (data != null) {
                        getImageToView(mCivHeadAvatar, data);
                        saveUserAvatar(mImageCropUri);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void saveUserAvatar(Uri uri) {
        final String picPath = AppUtil.getRealFilePath(this, mImageCropUri);
        if (picPath == null) {
            showToast("图片损毁，请重新添加图片");
            return;
        }
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    mCurrentUser.setAvatar(bmobFile.getFileUrl());
                    mCurrentUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mLoadingPD.dismiss();
                                showToast("头像修改成功!");
                            } else {
                                mLoadingPD.dismiss();
                                ErrorUtil.showErrorCode(MainActivity.this, e);
                            }
                        }
                    });
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(MainActivity.this, e);
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /**
     * 显示图片
     *
     * @param data
     */
    private void getImageToView(ImageView image, Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            try {
                mTopicImgBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageCropUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(mTopicImgBitmap);
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
        if (TopicListFragment.getInstance().mFabMenu.isOpened()) {
            TopicListFragment.getInstance().mFabMenu.close(true);
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
