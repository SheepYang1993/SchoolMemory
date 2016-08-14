package com.sheepyang.schoolmemory.fragment;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.TopicAdapter;
import com.sheepyang.schoolmemory.bean.Topic;
import com.sheepyang.schoolmemory.bean.TopicType;
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
    @BindView(R.id.lvTopic)
    ListView mLvPost;
    @BindView(R.id.fab1)
    FloatingActionButton mFab1;
    @BindView(R.id.fab2)
    FloatingActionButton mFab2;
    @BindView(R.id.fab3)
    FloatingActionButton mFab3;
    @BindView(R.id.fabMenu)
    public FloatingActionMenu mFabMenu;
    private List<Topic> mTopicList;
    private TopicAdapter mTopicAdapter;
    private int mCurrentPage = 0;//当前页数
    private int mSize = 8;//页数大小
    private int mPreviousVisibleItem;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        initFloatButton();// 初始化浮动按钮
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

    private void initFloatButton() {
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
    }

    @Override
    public void initData() {
        mCurrentPage = 0;
        mTopicList = getDataFromBmob(mCurrentPage, mSize);
        mTopicAdapter = new TopicAdapter(getActivity(), mTopicList);
        mLvPost.setAdapter(mTopicAdapter);
        mAbPullToRefresh.onHeaderRefreshFinish();
    }

    private void getMoreData() {
        mCurrentPage++;
        mTopicList.addAll(getDataFromBmob(mCurrentPage, mSize));
        mTopicAdapter.updataList(mTopicList);
        mAbPullToRefresh.onFooterLoadFinish();
    }

    private List<Topic> getDataFromBmob(int currentPage, int size) {
        mLoadingPD.show();
        List<Topic> topicList = new ArrayList<Topic>();
        for (int i = 0; i < size; i++) {
            Topic topic = new Topic();
            topic.setTitle("标题:" + "第" + (currentPage + 1) + "页" + "第" + (i + 1) + "条");
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
