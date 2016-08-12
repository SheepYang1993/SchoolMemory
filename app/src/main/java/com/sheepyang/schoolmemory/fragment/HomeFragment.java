package com.sheepyang.schoolmemory.fragment;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by SheepYang on 2016/8/11.
 */
public class HomeFragment extends BaseFragment {
    private static HomeFragment mHomeFragment;
    private static final int TO_ISSUE_TOPIC = 0;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView mAbPullToRefresh;
    @BindView(R.id.lvPost)
    ListView mLvPost;
    @BindView(R.id.fab1)
    FloatingActionButton mFab1;
    @BindView(R.id.fab2)
    FloatingActionButton mFab2;
    @BindView(R.id.fab3)
    FloatingActionButton mFab3;
    @BindView(R.id.fabMenu)
    public FloatingActionMenu mFabMenu;
    private List<Post> mPostList;
    private PostAdapter mPostAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 5;//页数大小
    private int mPreviousVisibleItem;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        mFabMenu.setClosedOnTouchOutside(true);
        mFabMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
        mFabMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
        mLvPost.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    mFabMenu.hideMenu(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    mFabMenu.showMenu(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });
        //下拉刷新
        mAbPullToRefresh.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                initData();
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

    @Override
    public void initData() {
        mCurrentPage = 0;
        mPostList = getDataFromBmob(mCurrentPage, mSize);
        mPostAdapter = new PostAdapter(getActivity(), mPostList);
        mPostAdapter.setPageSize(5);
        mLvPost.setAdapter(mPostAdapter);
        mAbPullToRefresh.onHeaderRefreshFinish();
    }

    private void getMoreData() {
        mCurrentPage++;
        mPostList.addAll(getDataFromBmob(mCurrentPage, mSize));
        mPostAdapter.upDataList(mPostList);
        mAbPullToRefresh.onFooterLoadFinish();
    }

    private List<Post> getDataFromBmob(int currentPage, int size) {
        mLoadingPD.show();
        List<Post> postList = new ArrayList<Post>();
        for (int i = 0; i < size; i++) {
            Post post = new Post();
            post.setAuthor(mCurrentUser);
            post.setContent("这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页这是首页");
            post.setContentImg("http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76b17dd4781ed21b0ef43bf8.jpg");
            postList.add(post);
        }
        mLoadingPD.dismiss();
        return postList;
    }

    /**
     * 获取HomeFragment单例
     *
     * @return
     */
    public static HomeFragment getInstance() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        return mHomeFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_ISSUE_TOPIC && resultCode == 1) {

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
//            case R.id.fab:// 发表话题
//                mIntent = new Intent();
//                startActivityForResult(mIntent, TO_ISSUE_TOPIC);
//                break;
            default:
                break;
        }
    }
}
