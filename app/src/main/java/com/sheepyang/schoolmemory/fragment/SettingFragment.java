package com.sheepyang.schoolmemory.fragment;

import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.view.abView.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 设置
 * Created by SheepYang on 2016/8/11.
 */
public class SettingFragment extends BaseFragment {
    private static SettingFragment mHomeFragment;
    @BindView(R.id.abPullToRefresh)
    AbPullToRefreshView mAbPullToRefresh;
    @BindView(R.id.lvTopic)
    ListView mLvPost;
    private List<Post> mPostList;
    private PostAdapter mPostAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 5;//页数大小

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
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
            post.setContent("这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面这是设置界面");
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
    public static SettingFragment getInstance() {
        if (mHomeFragment == null) {
            mHomeFragment = new SettingFragment();
        }
        return mHomeFragment;
    }
}
