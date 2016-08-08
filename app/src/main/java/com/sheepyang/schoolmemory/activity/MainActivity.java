package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.view.MyLinearLayout;
import com.sheepyang.schoolmemory.view.SlideMenu;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.lvPost)
    ListView lvPost;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView abPullToRefresh;
    @BindView(R.id.civAvatar)
    CircleImageView civAvatar;
    @BindView(R.id.lvMenu)
    ListView lvMenu;
    @BindView(R.id.mllMain)
    MyLinearLayout mllMain;
    @BindView(R.id.slideMenu)
    SlideMenu slideMenu;

    private List<Post> mPostList;
    private PostAdapter postAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 5;//页数大小
    private boolean isSlidingMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }

    private void initEvent() {
        mllMain.setSlideMenu(slideMenu);
        //下拉刷新
        abPullToRefresh.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initData();
            }
        });
        //下拉加在更多
        abPullToRefresh.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                getMoreData();
            }
        });
        slideMenu.setOnDragStateChangeListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
                isSlidingMenuOpen = true;
            }

            @Override
            public void onClose() {
                isSlidingMenuOpen = false;
            }

            @Override
            public void onDraging(float fraction) {

            }
        });
    }

    private void getMoreData() {
        mCurrentPage++;
        mPostList.addAll(getDataTest(mCurrentPage, mSize));
        postAdapter.upDataList(mPostList);
        abPullToRefresh.onFooterLoadFinish();
    }

    @Override
    public void onDoubleClick() {
        super.onDoubleClick();
        lvPost.smoothScrollToPositionFromTop(0, 0);
    }

    private void initData() {
        mCurrentPage = 0;
        mPostList = getDataTest(mCurrentPage, mSize);
        postAdapter = new PostAdapter(this, mPostList);
        lvPost.setAdapter(postAdapter);
        abPullToRefresh.onHeaderRefreshFinish();
    }

    private List<Post> getDataTest(int currentPage, int size) {
        List<Post> postList = new ArrayList<Post>();
        for (int i = 0; i < size; i++) {
            Post post = new Post();
            post.setAuthor(MyUser.getCurrentUser(MyUser.class));
            post.setContent("第" + (currentPage + 1) + "页，" + "第" + (i + 1) + "条数据");
            post.setContentImg("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
            postList.add(post);
        }
        return postList;
    }

    private void initView() {
        setNoLast();
        setNoBack();
        setBackIv(R.drawable.menu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack://显示侧滑菜单
                if (isSlidingMenuOpen) {
                    slideMenu.close();
                } else {
                    slideMenu.open();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mCurrentTime < 2000) {
            mCurrentTime = 0;
            mIntent = new Intent();
            mIntent.setAction(Constant.EXIT_APP_ACTION);
            mIntent.putExtra("isLogOut", true);
            sendBroadcast(mIntent);
        } else {
            mCurrentTime = System.currentTimeMillis();
            showToast("再次点击退出APP");
        }
    }
}
