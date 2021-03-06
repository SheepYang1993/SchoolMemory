package com.sheepyang.schoolmemory.fragment;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.activity.PostListActivity;
import com.sheepyang.schoolmemory.adapter.TopicAdapter;
import com.sheepyang.schoolmemory.bean.Topic;
import com.sheepyang.schoolmemory.bean.TopicType;
import com.sheepyang.schoolmemory.util.AppUtil;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.util.ErrorUtil;
import com.sheepyang.schoolmemory.util.FileUtil;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;
import com.sheepyang.schoolmemory.view.dialog.GetPhotoDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by SheepYang on 2016/8/11.
 */
public class TopicListFragment extends BaseFragment {
    private static final int TYPE_INIT_DATA = 0;
    private static final int TYPE_GET_MORE_DATA = 1;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView mAbPullToRefresh;
    @BindView(R.id.lvTopic)
    ListView mLvTopic;
    @BindView(R.id.fabMenu)
    public FloatingActionMenu mFabMenu;
    private EditText edtTitleText;
    private EditText edtContentText;
    private EditText edtTitleImage;
    private EditText edtContentImage;
    private EditText edtTitleQuestion;
    private EditText edtContentQuestion;
    private ImageView mIvAddImage;

    private static TopicListFragment mTopicListFragment;
    private List<Topic> mTopicList;
    private TopicAdapter mTopicAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 8;//页数大小
    private int mPreviousVisibleItem;
    private MaterialDialog dialogText;
    private MaterialDialog dialogImage;
    private MaterialDialog dialogQuestion;
    private GetPhotoDialog getPhotoDialog;
    private Bitmap mTopicImgBitmap;
    public boolean mIsOnActivityResult = false;
    private Uri mImageCropUri;
    private int mLastItem;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    public void initView() {
        initFloatButton();// 初始化浮动按钮
        //下拉刷新
        mAbPullToRefresh.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initListData();
            }
        });
        //下拉加在更多
        mAbPullToRefresh.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                getMoreData();
            }
        });
    }

    private void initFloatButton() {
        mFabMenu.setClosedOnTouchOutside(true);
        mFabMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
        mFabMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
        mLvTopic.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        getMoreData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
//                    mFabMenu.hideMenu(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
//                    mFabMenu.showMenu(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
                mLastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
    }

    @Override
    public void initData() {
        mTopicList = new ArrayList<>();
        mTopicAdapter = new TopicAdapter(getActivity(), mTopicList);
        mLvTopic.setAdapter(mTopicAdapter);
        mAbPullToRefresh.setNodata("暂无数据,点击刷新");
        mAbPullToRefresh.setLoadMoreEnable(false);
        mLvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("点击了" + i);
                mIntent = new Intent(getActivity(), PostListActivity.class);
                mIntent.putExtra("Topic", mTopicList.get(i));
                startActivity(mIntent);
            }
        });
        initListData();
    }

    private void initListData() {
        mCurrentPage = 0;
        getDataFromBmob(TYPE_INIT_DATA, mCurrentPage, mSize);
    }

    private void getMoreData() {
        mCurrentPage++;
        getDataFromBmob(TYPE_GET_MORE_DATA, mCurrentPage, mSize);
    }

    /**
     * 正式用的数据
     *
     * @param currentPage
     * @param size
     */
    private void getDataFromBmob(final int type, int currentPage, int size) {
        mLoadingPD.show();
        BmobQuery<Topic> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.include("creator[nick|avatar]");// 查询出作者昵称/头像
        //返回size条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(size);
        if (type == TYPE_GET_MORE_DATA) {// 加载更多
            query.setSkip(mTopicList.size()); // 忽略前mTopicList.size()条数据
        }
        //执行查询方法
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> topicList, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    if (type == TYPE_INIT_DATA) {
                        if (topicList != null && topicList.size() > 0) {
                            mTopicList = topicList;
                            mTopicAdapter.updataList(mTopicList);
                            mAbPullToRefresh.setHaveData();
                            mAbPullToRefresh.setLoadMoreEnable(true);
                        } else {
                            mTopicList.clear();
                            mTopicAdapter.updataList(mTopicList);
                            mAbPullToRefresh.setNodata("暂无数据,点击刷新");
                            mAbPullToRefresh.setLoadMoreEnable(false);
                        }
                    } else if (type == TYPE_GET_MORE_DATA) {
                        if (topicList != null && topicList.size() > 0) {
                            mTopicList.addAll(topicList);
                            mTopicAdapter.updataList(mTopicList);
                            mAbPullToRefresh.setHaveData();
                            mAbPullToRefresh.setLoadMoreEnable(true);
                        } else {
                            mCurrentPage--;
                            showToast("没有更多内容啦~");
                        }
                    }
                } else {
                    mLoadingPD.dismiss();
                    if (type == TYPE_GET_MORE_DATA) {
                        mCurrentPage--;
                        mAbPullToRefresh.setHaveData();
                        mAbPullToRefresh.setLoadMoreEnable(true);
                    }
                    ErrorUtil.showErrorCode(getActivity(), e);
                }
                mAbPullToRefresh.onHeaderRefreshFinish();
                mAbPullToRefresh.onFooterLoadFinish();
            }
        });
    }

    /**
     * 测试用的数据
     *
     * @param currentPage
     * @param size
     * @return
     */
    private List<Topic> getDataFromTest(int currentPage, int size) {
        mLoadingPD.show();
        List<Topic> topicList = new ArrayList<Topic>();
        for (int i = 0; i < size; i++) {
            Topic topic = new Topic();
            topic.setTitle("标题:" + "第" + (currentPage + 1) + "页" + "第" + (i + 1) + "条 (这是测试数据)");
            topic.setContent("第" + (currentPage + 1) + "页" + "第" + (i + 1) + "条" + "这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容");
            topic.setCreator(mCurrentUser);
            topic.setPostNum(i);
            ArrayList<String> imageList = new ArrayList<>();
            if (i % 6 == 0) {
                topic.setType(TopicType.TEXT);
            } else if (i % 8 == 1) {
                topic.setType(TopicType.IMAGE);
                imageList.add("http://img.52fuqing.com/upload/news/2015-4-22/201542212257252y52ih.jpg");
            } else if (i % 8 == 2) {
                topic.setType(TopicType.IMAGE);
                imageList.add("http://y3.ifengimg.com/cmpp/2014/04/09/15/eb9b263c-487a-4b3d-b110-23b4b0a183e7.jpg");
                imageList.add("http://ww2.sinaimg.cn/mw690/6a9ef3d1jw1f51rr9gz1bj20qo0svtjj.jpg");
            } else if (i % 8 == 3) {
                topic.setType(TopicType.TEXT);
            } else if (i % 8 == 4) {
                topic.setType(TopicType.IMAGE);
                imageList.add("http://y3.ifengimg.com/cmpp/2014/04/09/15/eb9b263c-487a-4b3d-b110-23b4b0a183e7.jpg");
                imageList.add("http://ww2.sinaimg.cn/mw690/6a9ef3d1jw1f51rr9gz1bj20qo0svtjj.jpg");
                imageList.add("http://img.52fuqing.com/upload/news/2015-4-22/201542212257252y52ih.jpg");
            } else if (i % 8 == 5) {
                topic.setType(TopicType.QUESTION);
            } else if (i % 8 == 6) {
                topic.setType(TopicType.QUESTION);
            } else if (i % 8 == 7) {
                topic.setType(TopicType.TEXT);
            }
            topic.setImageList(imageList);
            topicList.add(topic);
        }
        mLoadingPD.dismiss();
        return topicList;
    }

    /**
     * 获取TopicFragment单例
     *
     * @return
     */
    public static TopicListFragment getInstance() {
        if (mTopicListFragment == null) {
            mTopicListFragment = new TopicListFragment();
        }
        return mTopicListFragment;
    }

    @Override
    @OnClick({R.id.fab1, R.id.fab2, R.id.fab3})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.fab1:// 文字
                createTopicDialog(TopicType.TEXT);
                dialogText.show();
                break;
            case R.id.fab2:// 图片
                createTopicDialog(TopicType.IMAGE);
                dialogImage.show();
                break;
            case R.id.fab3:// 提问
                createTopicDialog(TopicType.QUESTION);
                dialogQuestion.show();
                break;
            case R.id.ivAddImage:// 添加图片
                if (getPhotoDialog == null)
                    getPhotoDialog = new GetPhotoDialog(getActivity());
                getPhotoDialog.setName("mIvAddImage");
                getPhotoDialog.setOnPositiveClickListener(new GetPhotoDialog.onPositiveClickListener() {
                    @Override
                    public void onPositiveClick(int position) {
                        // position: 1相册 2相机 3取消
                        if (position == 1 || position == 2) {
                            mIsOnActivityResult = true;
                        }
                    }
                });
                getPhotoDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 新建话题对话框
     *
     * @param type
     */
    private void createTopicDialog(TopicType type) {
        boolean wrapInScrollView = true;
        mFabMenu.close(true);
        switch (type) {
            case TEXT:
                View viewDialogText = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_text, null);
                edtTitleText = ButterKnife.findById(viewDialogText, R.id.edtTitle);
                edtContentText = ButterKnife.findById(viewDialogText, R.id.edtContent);
                dialogText = new MaterialDialog.Builder(getActivity())
                        .title("文字")
                        .customView(viewDialogText, wrapInScrollView)
                        .positiveText("创建")
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                createTextTopic(edtTitleText.getText().toString().trim(), edtContentText.getText().toString().trim());
                            }
                        }).build();
                break;
            case IMAGE:
                mTopicImgBitmap = null;
                View viewDialogImage = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_image, null);
                edtTitleImage = ButterKnife.findById(viewDialogImage, R.id.edtTitle);
                edtContentImage = ButterKnife.findById(viewDialogImage, R.id.edtContent);
                mIvAddImage = ButterKnife.findById(viewDialogImage, R.id.ivAddImage);
                mIvAddImage.setOnClickListener(this);
                dialogImage = new MaterialDialog.Builder(getActivity())
                        .title("图片")
                        .customView(viewDialogImage, wrapInScrollView)
                        .positiveText("创建")
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                createImageTopic(edtTitleImage.getText().toString().trim(), edtContentImage.getText().toString().trim());
                            }
                        }).build();
                break;
            case QUESTION:
                View viewDialogQuestion = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_question, null);
                edtTitleQuestion = ButterKnife.findById(viewDialogQuestion, R.id.edtTitle);
                edtContentQuestion = ButterKnife.findById(viewDialogQuestion, R.id.edtContent);
                dialogQuestion = new MaterialDialog.Builder(getActivity())
                        .title("提问")
                        .customView(viewDialogQuestion, wrapInScrollView)
                        .positiveText("创建")
                        .autoDismiss(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                createQuestionTopic(edtTitleQuestion.getText().toString().trim(), edtContentQuestion.getText().toString().trim());
                            }
                        }).build();
                break;
            default:
                break;
        }
    }

    /**
     * 创建文字话题
     *
     * @param title   标题
     * @param content 内容
     */
    private void createTextTopic(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容");
            return;
        }
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setType(TopicType.TEXT);
        topic.setCreator(mCurrentUser);
        topic.setPostNum(0);

        dialogText.dismiss();
        mLoadingPD.show();
        topic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    showToast("创建成功!");
                    initListData();
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(getActivity(), e);
                }
            }
        });
    }

    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

    /**
     * 创建图片话题
     *
     * @param title   标题
     * @param content 内容
     */
    private void createImageTopic(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容");
            return;
        }
        if (mTopicImgBitmap == null) {
            mIvAddImage.setImageResource(R.drawable.btn_add_image_wrong);
            tada(mIvAddImage).start();
            showToast("请添加一张图片");
            return;
        }
        final String picPath = AppUtil.getRealFilePath(getActivity(), mImageCropUri);
        if (picPath == null) {
            showToast("图片损毁，请重新添加图片");
            return;
        }

        final Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setType(TopicType.IMAGE);
        topic.setCreator(mCurrentUser);
        topic.setPostNum(0);

        dialogImage.dismiss();
        mLoadingPD.show();
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    List<String> imageList = new ArrayList<String>();
                    imageList.add(bmobFile.getFileUrl());
                    topic.setImageList(imageList);
                    topic.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                mLoadingPD.dismiss();
                                showToast("创建成功!");
                                initListData();
                            } else {
                                mLoadingPD.dismiss();
                                ErrorUtil.showErrorCode(getActivity(), e);
                            }
                        }
                    });
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(getActivity(), e);
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /**
     * 创建提问话题
     *
     * @param title   标题
     * @param content 内容
     */
    private void createQuestionTopic(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            showToast("请输入问题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容");
            return;
        }
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setType(TopicType.QUESTION);
        topic.setCreator(mCurrentUser);
        topic.setPostNum(0);

        dialogQuestion.dismiss();
        mLoadingPD.show();
        topic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    showToast("创建成功!");
                    initListData();
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(getActivity(), e);
                }
            }
        });
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
            String Path = FileUtil.getImageCAmearDir(getActivity());
            filePath = Path + "/" + st + "icon.jpg";
            File out = new File(filePath);
            mImageCropUri = Uri.fromFile(out);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            mIsOnActivityResult = true;
            startActivityForResult(intent, Constant.TO_CUT);
            return filePath;
        } else {
            return "";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    getImageToView(mIvAddImage, data);
                }
                break;
            default:
                break;
        }
        mIsOnActivityResult = false;
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
                mTopicImgBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mImageCropUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(mTopicImgBitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }
}
