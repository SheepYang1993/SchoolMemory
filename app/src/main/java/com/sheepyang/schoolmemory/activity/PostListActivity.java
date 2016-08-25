package com.sheepyang.schoolmemory.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sheepyang.schoolmemory.R;
import com.sheepyang.schoolmemory.adapter.PostAdapter;
import com.sheepyang.schoolmemory.bean.Post;
import com.sheepyang.schoolmemory.bean.Topic;
import com.sheepyang.schoolmemory.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 帖子列表
 * Created by SheepYang on 2016/8/11.
 */
public class PostListActivity extends BaseActivity {
    private static final int TYPE_INIT_DATA = 0;
    private static final int TYPE_GET_MORE_DATA = 1;
    @BindView(R.id.lvPost)
    ListView mLvPost;
    @BindView(R.id.edtContext)
    EditText edtContext;

    private int mCurrentPage = 0;//当前页数
    private int mSize = 8;//页数大小
    private List<Post> mPostList;
    private PostAdapter mPostAdapter;
    private Topic mTopic;
    private int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        mTopic = (Topic) getIntent().getSerializableExtra("Topic");
        mPosition = getIntent().getIntExtra("mPosition", -1);
        if (mTopic == null) {
            showToast("没有找到相关话题");
            finish();
        }
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        mPostList = new ArrayList<>();
        mPostAdapter = new PostAdapter(this, mPostList);
        mLvPost.setAdapter(mPostAdapter);
        mLvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showToast("点击了" + i);
            }
        });
        initListData();
    }

    private void sendPost(Topic topic) {
        String context = edtContext.getText().toString().trim();
        if (TextUtils.isEmpty(context)) {
            showToast("请输入内容");
            return;
        }
        Post post = new Post();
        post.setAuthor(mCurrentUser);
        post.setTopic(topic);
        post.setContent(context);
        mLoadingPD.show();
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    showToast("发布成功!");
                    initListData();
                } else {
                    mLoadingPD.dismiss();
                    ErrorUtil.showErrorCode(PostListActivity.this, e);
                }
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
        mLoadingPD.show();
        BmobQuery<Post> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.include("creator[nick|avatar]");// 查询出作者昵称/头像
        //返回size条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(size);
        if (type == TYPE_GET_MORE_DATA) {// 加载更多
            query.setSkip(mPostList.size()); // 忽略前mTopicList.size()条数据
        }
        //执行查询方法
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> postList, BmobException e) {
                if (e == null) {
                    mLoadingPD.dismiss();
                    if (type == TYPE_INIT_DATA) {
                        if (postList != null && postList.size() > 0) {
                            mPostList = postList;
                            mPostAdapter.updataList(mPostList);
//                            mAbPullToRefresh.setHaveData();
//                            mAbPullToRefresh.setLoadMoreEnable(true);
                        } else {
                            mPostList.clear();
                            mPostAdapter.updataList(mPostList);
//                            mAbPullToRefresh.setNodata("暂无帖子,点击刷新");
//                            mAbPullToRefresh.setLoadMoreEnable(false);
                        }
                    } else if (type == TYPE_GET_MORE_DATA) {
                        if (postList != null && postList.size() > 0) {
                            mPostList.addAll(postList);
                            mPostAdapter.updataList(mPostList);
//                            mAbPullToRefresh.setHaveData();
//                            mAbPullToRefresh.setLoadMoreEnable(true);
                        } else {
                            mCurrentPage--;
                            showToast("没有更多内容啦~");
                        }
                    }
                } else {
                    mLoadingPD.dismiss();
                    if (type == TYPE_GET_MORE_DATA) {
                        mCurrentPage--;
//                        mAbPullToRefresh.setHaveData();
//                        mAbPullToRefresh.setLoadMoreEnable(true);
                    }
                    ErrorUtil.showErrorCode(PostListActivity.this, e);
                }
//                mAbPullToRefresh.onHeaderRefreshFinish();
//                mAbPullToRefresh.onFooterLoadFinish();
            }
        });
    }

    @OnClick(R.id.btnSendPost)
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnSendPost:
                sendPost(mTopic);
                break;
            default:
                break;
        }
    }
}
