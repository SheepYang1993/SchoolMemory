package com.sheepyang.schoolmemory.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.lvPost)
    ListView lvPost;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView abPullToRefresh;

    private List<Post> mPostList;
    private PostAdapter postAdapter;

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
    }

    private void getMoreData() {

    }

    private void initData() {
        mPostList = getData();
        postAdapter = new PostAdapter(this, mPostList);
        lvPost.setAdapter(postAdapter);
    }

    private List<Post> getData() {

        return null;
    }

    private void initView() {
        setNoLast();
        setNoBack();
    }


}
