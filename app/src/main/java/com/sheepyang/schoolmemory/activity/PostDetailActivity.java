package com.sheepyang.schoolmemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.TopicAdapter;
import com.sheepyang.schoolmemory.bean.Topic;
import com.sheepyang.schoolmemory.util.ErrorUtil;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 帖子列表
 * Created by SheepYang on 2016/8/11.
 */
public class PostDetailActivity extends BaseActivity {
    private static final int TYPE_INIT_DATA = 0;
    private static final int TYPE_GET_MORE_DATA = 1;
    @BindView(R.id.lvPost)
    ListView mLvPost;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView mAbPullToRefresh;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 8;//页数大小
    private List<Topic> mTopicList;
    private TopicAdapter mTopicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
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

    private void initListData() {
        mCurrentPage = 0;
        getDataFromBmob(TYPE_INIT_DATA, mCurrentPage, mSize);
    }

    public void getMoreData() {
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

    }
}
