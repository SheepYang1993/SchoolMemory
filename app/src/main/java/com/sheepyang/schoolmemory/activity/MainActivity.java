package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.MyUser;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.util.Constant;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.lvPost)
    ListView lvPost;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView abPullToRefresh;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private List<Post> mPostList;
    private PostAdapter postAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 5;//页数大小

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
        mBarIvBack.setOnClickListener(this);
    }

    private void getMoreData() {
        mCurrentPage++;
        mPostList.addAll(getDataTest(mCurrentPage, mSize));
        postAdapter.upDataList(mPostList);
        abPullToRefresh.onFooterLoadFinish();
    }

    private void initData() {
        mCurrentPage = 0;
        mPostList = getDataTest(mCurrentPage, mSize);
        postAdapter = new PostAdapter(this, mPostList);
        postAdapter.setPageSize(5);
        lvPost.setAdapter(postAdapter);
        abPullToRefresh.onHeaderRefreshFinish();
    }

    private List<Post> getDataTest(int currentPage, int size) {
//        mLoadingPD.show();
        List<Post> postList = new ArrayList<Post>();
        for (int i = 0; i < size; i++) {
            Post post = new Post();
            post.setAuthor(MyUser.getCurrentUser(MyUser.class));
            post.setContent("今天天气真好啊今天天气真好啊今天天气真好啊今天天气真好啊今天天气真好啊今天天气真好啊今天天气真好啊");
            post.setContentImg("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
            postList.add(post);
        }
//        mLoadingPD.dismiss();
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
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
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
